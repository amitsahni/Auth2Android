package com.auth;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import net.openid.appauth.AuthState;

import org.json.JSONException;

import webconnect.com.webconnect.ApiConfiguration;
import webconnect.com.webconnect.WebConnect;

/**
 * Created by clickapps on 28/2/18.
 */

public class AuthManager {
    static final String SHARED_PREFERENCES_NAME = "AuthStatePreference";
    static final String AUTH_STATE = "AUTH_STATE";
    private static volatile AuthManager sAuthManager;

    public static AuthManager get() {
        if (sAuthManager == null) {
            synchronized (AuthManager.class) {
                if (sAuthManager == null) {
                    sAuthManager = new AuthManager();

                }
            }
        }
        return sAuthManager;
    }

    public static Builder with(@NonNull Context context) {
        return new Builder(context);
    }

    @Nullable
    public AuthState restoreAuthState(@NonNull Context context) {
        String jsonString = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                .getString(AUTH_STATE, null);
        if (!TextUtils.isEmpty(jsonString)) {
            try {
                return AuthState.jsonDeserialize(jsonString);
            } catch (JSONException jsonException) {
                // should never happen
                jsonException.printStackTrace();
            }
        }
        return null;
    }

    public boolean checkAuthState(@NonNull Context context) {
        AuthState state = restoreAuthState(context);
        if (state != null && state.isAuthorized()) {
            return true;
        }
        return false;
    }
}
