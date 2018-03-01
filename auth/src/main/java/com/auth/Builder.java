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

    public Intent login(@NonNull String clientId, @NonNull String redirectUri) {
        param.clientId = clientId;
        param.redirectUri = redirectUri;
        return new RequestBuilder.Login(param).getAuthIntent();
    }

    public void handleAuth(@NonNull Intent intent) {
        new RequestBuilder.Login(param).handleAuth(intent);
    }

    public void logout() {
        new RequestBuilder.Logout(param).build();
    }

    public RequestBuilder.UserInfo profile() {
        return new RequestBuilder.UserInfo(param);
    }
}
