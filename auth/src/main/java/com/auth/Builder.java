package com.auth;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * Created by clickapps on 28/2/18.
 */

public class Builder {
    Param param;

    Builder(@NonNull Context context) {
        param = new Param();
        param.context = context;
    }

    @Deprecated
    public Intent login(@NonNull String clientId, @NonNull String redirectUri) {
        param.clientId = clientId;
        param.redirectUri = redirectUri;
        return new RequestBuilder.Login(param).getAuthIntent();
    }

    public Intent loginGoogle(@NonNull String clientId, @NonNull String redirectUri) {
        param.clientId = clientId;
        param.redirectUri = redirectUri;
        param.authEndPoint = "https://accounts.google.com/o/oauth2/v2/auth";
        param.tokenEndPoint = "https://www.googleapis.com/oauth2/v4/token";
        param.scope = new String[]{"address", "email", "profile", "phone"};
        return new RequestBuilder.Login(param).getAuthIntent();
    }

    public Intent loginCustom(@NonNull String clientId,
                              @NonNull String redirectUri,
                              @NonNull String authEndPoint,
                              @NonNull String tokenEndPoint,
                              @NonNull String scope[]) {
        param.clientId = clientId;
        param.redirectUri = redirectUri;
        param.authEndPoint = authEndPoint;
        param.tokenEndPoint = tokenEndPoint;
        param.scope = scope;
        return new RequestBuilder.Login(param).getAuthIntent();
    }

    public void handleAuth(@NonNull Intent intent) {
        new RequestBuilder.Login(param).handleAuth(intent);
    }

    public void logout() {
        new RequestBuilder.Logout(param).build();
    }

    @Deprecated
    public RequestBuilder.UserInfo profile() {
        return new RequestBuilder.UserInfo(param);
    }

    public RequestBuilder.UserInfo profileGoogle() {
        param.url = "https://www.googleapis.com/oauth2/v3/userinfo";
        return new RequestBuilder.UserInfo(param);
    }

    public RequestBuilder.UserInfo profileCustom(@NonNull String url) {
        param.url = url;
        return new RequestBuilder.UserInfo(param);
    }
}
