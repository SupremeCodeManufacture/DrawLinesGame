package view.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.SupremeManufacture.DrawLines.BuildConfig;
import com.SupremeManufacture.DrawLines.R;

import data.App;
import data.GenericConstants;
import logic.helpers.MyLogs;
import logic.payment.PaymentHelper;
import logic.payment.util.IabResult;
import logic.payment.util.Purchase;

public class SettingsActivity extends BaseActivity implements
        View.OnClickListener {

    private TextView tvUpgrade;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initViews();
        decideDemoOrPro();
    }

    private void initViews() {
        tvUpgrade = (TextView) findViewById(R.id.tv_remove_ads);
        tvUpgrade.setOnClickListener(this);

        TextView tvTheme = (TextView) findViewById(R.id.tv_sel_theme);
        tvTheme.setOnClickListener(this);

        TextView tvRate = (TextView) findViewById(R.id.tv_rate);
        tvRate.setOnClickListener(this);

        TextView tvSupport = (TextView) findViewById(R.id.tv_support);
        tvSupport.setOnClickListener(this);

        TextView tvAbout = (TextView) findViewById(R.id.tv_about);
        tvAbout.setOnClickListener(this);
    }

    private void decideDemoOrPro() {
        if (App.isUserPro()) {
            tvUpgrade.setVisibility(View.GONE);

        } else {
            tvUpgrade.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_remove_ads:
                //init here and payment is done when it's initializsed in listener
                PaymentHelper.setUpPayments(SettingsActivity.this, SettingsActivity.this);
                break;

            case R.id.tv_sel_theme:
                Toast.makeText(SettingsActivity.this, "aaaa....", Toast.LENGTH_LONG).show();
                break;

            case R.id.tv_rate:
                try {
                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + App.getAppCtx().getPackageName());
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));

                } catch (ActivityNotFoundException e) {
                    Toast.makeText(SettingsActivity.this, App.getAppCtx().getResources().getString(R.string.error_open_google_play), Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.tv_support:
                try {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", GenericConstants.SUPPORT_EMAIL, null));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, App.getAppCtx().getResources().getString(R.string.txt_name));
                    startActivity(emailIntent);

                } catch (ActivityNotFoundException e) {
                    Toast.makeText(SettingsActivity.this, App.getAppCtx().getResources().getString(R.string.error_txt_no_mail), Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.tv_about:
                String str = App.getAppCtx().getResources().getString(R.string.txt_name) + " v. " + BuildConfig.VERSION_NAME;
                Toast.makeText(SettingsActivity.this, str, Toast.LENGTH_LONG).show();
                break;
        }
    }


    //=============================== PAYMENTS FUNCTIONAL ========================================//
    @Override
    public void onIabSetupFinished(IabResult result) {
        if (result.isSuccess()) {
            //MyLogs.LOG("SettingsActivity", "onIabSetupFinished", "Setting up In-app Billing succesfull");
            PaymentHelper.doLifePayment(SettingsActivity.this, App.getPaymentHelper(), SettingsActivity.this);

        } else {
            //MyLogs.LOG("SettingsActivity", "onIabSetupFinished", "Problem setting up In-app Billing: " + result);
        }
    }

    @Override
    public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
        if (result.isSuccess()) {
            //MyLogs.LOG("SettingsActivity", "onIabPurchaseFinished", "purchese SKU: " + purchase.getSku());
            App.setUserPro(true);
            decideDemoOrPro();
            Toast.makeText(SettingsActivity.this, App.getAppCtx().getResources().getString(R.string.txt_worning_pro_done), Toast.LENGTH_LONG).show();

        } else {
            //MyLogs.LOG("SettingsActivity", "onIabPurchaseFinished", "Error purchasing: " + result);
        }
    }
}