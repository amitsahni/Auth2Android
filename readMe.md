Login

```aidl
Intent intent = AuthManager.with(MainActivity.this)
                        .login(clientId, redictUri);
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

#### Profile
```aidl
AuthManager.with(this)
           .profile()
           .listener(callback)
           .build();
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