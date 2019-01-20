package view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import data.App;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.setAppBuilds(App.getAppBuilds() + 1);

        SplashScreenActivity.this.startActivity(new Intent(this, TablesActivity.class));
        SplashScreenActivity.this.finish();
    }
}