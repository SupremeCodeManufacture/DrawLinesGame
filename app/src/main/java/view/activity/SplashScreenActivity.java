package view.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;

import java.util.concurrent.Callable;

import data.App;
import io.fabric.sdk.android.Fabric;
import logic.async_await.AsyncTaskWorker;
import logic.async_await.OnAsyncDoneNoRsListener;
import logic.helpers.MyLogs;
import logic.helpers.Utils;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        App.setAppBuilds(App.getAppBuilds() + 1);

        if (App.getFirstLaunchMilis() == 0)
            App.setFirstLaunchMilis(System.currentTimeMillis());

        onAsyncAutoDetectLockedLevels();
    }

    public void onAsyncAutoDetectLockedLevels() {
        //MyLogs.LOG("SplashScreenActivity", "onAsyncAutoDetectLockedLevels", "start: " + System.currentTimeMillis());

        new AsyncTaskWorker(
                new Callable<Void>() {
                    public Void call() {
                        Utils.autodetectLockedLevels();
                        //MyLogs.LOG("SplashScreenActivity", "onAsyncAutoDetectLockedLevels", "resultat: " + Utils.getLockedLevelsList().toString());
                        return null;
                    }
                },
                new OnAsyncDoneNoRsListener() {
                    @Override
                    public void onDone() {
                        SplashScreenActivity.this.startActivity(new Intent(SplashScreenActivity.this, TablesActivity.class));
                        SplashScreenActivity.this.finish();

                        //MyLogs.LOG("SplashScreenActivity", "onAsyncAutoDetectLockedLevels", "end: " + System.currentTimeMillis());
                    }
                }
        ).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}