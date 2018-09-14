package com.auth;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;
import net.openid.appauth.TokenResponse;

import java.util.LinkedHashMap;
import java.util.Map;

import webconnect.com.webconnect.WebConnect;
import webconnect.com.webconnect.listener.OnWebCallback;

/**
 * Created by clickapps on 28/2/18.
 */

public class RequestBuilder {

    public static class Login {
        Param param;

        Login(Param param) {
            this.param = param;
        }

        public Login scope(String... scope) {
            param.scope = scope;
            return this;
        }

        public Intent getAuthIntent() {
            AuthorizationServiceConfiguration serviceConfiguration = new AuthorizationServiceConfiguration(
                    Uri.parse(param.authEndPoint) /* auth endpoint */,
                    Uri.parse(param.tokenEndPoint) /* token endpoint */
            );
            AuthorizationService authorizationService = new AuthorizationService(param.context);
            String clientId = param.clientId;
            Uri redirectUri = Uri.parse(param.redirectUri);
            AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(
                    serviceConfiguration,
                    clientId,
                    ResponseTypeValues.CODE,
                    redirectUri
            );
            builder.setScopes(param.scope);
            AuthorizationRequest request = builder.build();
            return authorizationService.getAuthorizationRequestIntent(request);
        }

        public void handleAuth(Intent intent, final AuthListener authListener) {
            AuthorizationResponse response = AuthorizationResponse.fromIntent(intent);
            AuthorizationException error = AuthorizationException.fromIntent(intent);
            final AuthState authState = new AuthState(response, error);
            if (response != null) {
                final AuthorizationService service = new AuthorizationService(param.context.getApplicationContext());
                service.performTokenRequest(response.createTokenExchangeRequest(), new AuthorizationService.TokenResponseCallback() {
                    @Override
                    public void onTokenRequestCompleted(@Nullable final TokenResponse tokenResponse, @Nullable AuthorizationException exception) {
                        if (tokenResponse != null) {
                            authState.update(tokenResponse, exception);
                            Log.i(getClass().getName(), String.format("Token Response [ Access Token: %s, ID Token: %s ]", tokenResponse.accessToken, tokenResponse.idToken));
                            if (authListener != null) {
                                authListener.onTokenRequestCompleted(tokenResponse.accessToken, tokenResponse.idToken);
                            }
                            param.context.getSharedPreferences(AuthManager.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
                                    .putString(AuthManager.AUTH_STATE, authState.jsonSerializeString())
                                    .apply();
                        } else {
                            if (authListener != null) {
                                authListener.onTokenRequestCompleted(null, null);
                            }
                        }
                    }
                });
            }
        }
    }

    public static class Logout {
        Param param;

        Logout(Param param) {
            this.param = param;
        }

        public void build() {
            param.context.getSharedPreferences(AuthManager.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                    .edit()
                    .remove(AuthManager.AUTH_STATE)
                    .apply();

        }
    }

    public static class UserInfo {
        Param param;
        ProfileListener callback;

        UserInfo(Param param) {
            this.param = param;
        }

        public UserInfo listener(ProfileListener l) {
            callback = l;
            return this;
        }

        public void build() {
            AuthState authState = AuthManager.get().restoreAuthState(param.context);
            if (authState != null) {
                authState.performActionWithFreshTokens(new AuthorizationService(param.context), new AuthState.AuthStateAction() {
                    @Override
                    public void execute(@Nullable String s, @Nullable String s1, @Nullable AuthorizationException e) {
                        Map<String, String> map = new LinkedHashMap<>();
                        map.put("Authorization", String.format("Bearer %s", s));
                        WebConnect.with(param.context, "")
                                .get()
                                .baseUrl(param.url)
                                .headerParam(map)
                                .callback(new OnWebCallback() {
                                    @Override
                                    public <T> void onSuccess(@Nullable T object, int taskId) {
                                        if (object instanceof User) {
                                            User user = (User) object;
                                            if (callback != null) {
                                                callback.profile(user);
                                            }
                                        }
                                    }

                                    @Override
                                    public <T> void onError(@Nullable T object, String error, int taskId) {
                                        if (callback != null) {
                                            callback.profile(new User());
                                        }
                                    }
                                }, Object.class, Object.class)
                                .connect();

                    }
                });
            }
        }
    }

    public static class CustomInfo {
        Param param;
        ProfileListener callback;
        private Class<?> success = Object.class;
        private Class<?> error = Object.class;

        CustomInfo(Param param) {
            this.param = param;
        }

        public CustomInfo listener(ProfileListener l) {
            callback = l;
            return this;
        }

        public CustomInfo success(Class<?> Klass) {
            this.success = Klass;
            return this;
        }

        public CustomInfo error(Class<?> Klass) {
            this.error = Klass;
            return this;
        }

        public void build() {
            AuthState authState = AuthManager.get().restoreAuthState(param.context);
            if (authState != null) {
                authState.performActionWithFreshTokens(new AuthorizationService(param.context), new AuthState.AuthStateAction() {
                    @Override
                    public void execute(@Nullable String s, @Nullable String s1, @Nullable AuthorizationException e) {
                        Map<String, String> map = new LinkedHashMap<>();
                        map.put("Authorization", String.format("Bearer %s", s));
                        WebConnect.with(param.context, "")
                                .get()
                                .baseUrl(param.url)
                                .headerParam(map)
                                .callback(new OnWebCallback() {
                                    @Override
                                    public <T> void onSuccess(@Nullable T object, int taskId) {
                                        if (callback != null) {
                                            callback.profile(object);
                                        }
                                    }

                                    @Override
                                    public <T> void onError(@Nullable T object, String error, int taskId) {
                                        if (callback != null) {
                                            callback.profile(object);
                                        }
                                    }
                                }, success, error)
                                .connect();

                    }
                });
            }
        }
    }
}
