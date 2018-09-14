package com.auth;

import android.support.annotation.Nullable;


public interface AuthListener {
    void onTokenRequestCompleted(@Nullable final String accessToken, @Nullable String idToken);
}
