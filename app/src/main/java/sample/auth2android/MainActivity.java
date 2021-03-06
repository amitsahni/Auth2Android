package sample.auth2android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.auth.AuthListener;
import com.auth.AuthManager;
import com.auth.ProfileListener;


public class MainActivity extends AppCompatActivity {
    private boolean isAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(android.R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = AuthManager.with(MainActivity.this)
//                        .login("1023462665588-eb3ndglm3gfagmltjq0mc70obucjfn2o.apps.googleusercontent.com", "sample.auth2android");
//                startActivityForResult(intent, 100);

                Intent intent = AuthManager.with(MainActivity.this)
                        .loginCustom("ea9c87a5-2dfd-4e10-911c-f3058ce831d0",
                                "sample.auth2android://oauth2callback",
                                "https://login.microsoftonline.com/common/oauth2/v2.0/authorize",
                                "https://login.microsoftonline.com/common/oauth2/v2.0/token",
                                new String[]{"openid", "email", "profile", "https://outlook.office.com/contacts.read"});
                startActivityForResult(intent, 100);
            }
        });

        findViewById(android.R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthManager.with(MainActivity.this)
                        .logout();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        isAuth = AuthManager.get().checkAuthState(this);
    }

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
                                    AuthManager.with(MainActivity.this)
                                            .profileCustom("https://outlook.office.com/api/v2.0/me/")
                                            .listener(new ProfileListener() {
                                                @Override
                                                public void profile(Object user) {
                                                    Log.i(getLocalClassName(), "response = " + user.toString());
                                                }
                                            })
                                            .build();
                                }
                            });
                    break;
            }
        }
    }
}
