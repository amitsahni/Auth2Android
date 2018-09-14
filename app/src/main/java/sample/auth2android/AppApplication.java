package sample.auth2android;

import android.support.multidex.MultiDexApplication;

import com.auth.Configuration;

/**
 * Created by clickapps on 28/2/18.
 */

public class AppApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        new Configuration(this)
                .debug(true)
                .config();
    }
}
