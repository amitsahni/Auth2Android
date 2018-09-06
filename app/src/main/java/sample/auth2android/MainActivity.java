package sample.auth2android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.auth.AuthManager;


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
                        .loginCustom("af1cc8b0-b739-47ac-8939-8d9a0285a0d3", "sample.auth2android",
                                "https://login.microsoftonline.com/common/oauth2/v2.0/authorize",
                                "https://login.microsoftonline.com/common/oauth2/v2.0/token",
                                new String[]{"openid", "email", "profile", "offline_access"});
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
                            .handleAuth(data);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    AuthManager.with(this)
                            .profileCustom("https://graph.microsoft.com/v1.0/me/")
                            .build();
                    break;
            }
        }
    }
}
