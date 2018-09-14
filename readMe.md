Login

```aidl
@Depricated
Intent intent = AuthManager.with(MainActivity.this)
                        .login(clientId, redictUri);
startActivityForResult(intent, 100);
```
`Google Login`
```aidl
Intent intent = AuthManager.with(MainActivity.this)
                        .loginGoogle(clientId, redictUri);
startActivityForResult(intent, 100);
```

`Custom Login`
```aidl
Intent intent = AuthManager.with(MainActivity.this)
                        .loginCustom(clientId,
                                redictUri,
                                authEndPoint,
                                tokenEndPoint,
                                new String[]{"scope1", "scope2"});
startActivityForResult(intent, 100);
```

#### OnActivityResult

```aidl
@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 100:
                    AuthManager.with(this)
                            .handleAuth(data);
                    break;
            }
        }
    }

```

```aidl
@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 100:
                    AuthManager.with(this)
                          .handleAuth(data, new AuthListener() {
                          @Override
                          public void onTokenRequestCompleted(@Nullable String accessToken, @Nullable String idToken) {

                          });
                    break;
            }
        }
    }

```

#### Profile
```aidl
@Depricated
AuthManager.with(this)
           .profile()
           .listener(callback)
           .build();
```


```aidl
AuthManager.with(this)
           .profileGoogle()
           .build();
```


```aidl
AuthManager.with(MainActivity.this)
           .profileCustom(url)
           .listener(new ProfileListener() {
           @Override
           public void profile(Object user) {
                 Log.i(getLocalClassName(), "response = " + user.toString());
           }
           })
           .build();
           }
```

#### Check Login Status
```aidl
boolean isAuth = AuthManager.get().checkAuthState(this);
```

##### Add following in defaultConfig in gradle
```groovy
manifestPlaceholders = [
                'appAuthRedirectScheme': 'redirectUri'
        ]
```