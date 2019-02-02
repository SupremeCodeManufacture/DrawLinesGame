package view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import data.App;
import logic.helpers.MyLogs;
import logic.payment.util.IabHelper;
import logic.payment.util.IabResult;
import logic.payment.util.Inventory;
import logic.payment.util.Purchase;

public class BaseActivity extends AppCompatActivity implements
        IabHelper.OnIabSetupFinishedListener,
        IabHelper.OnIabPurchaseFinishedListener,
        IabHelper.QueryInventoryFinishedListener {


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Pass on the activity result to the helper for handling
        if (!App.getPaymentHelper().handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where we perform any handling of activity results not related to in-app billing...
            super.onActivityResult(requestCode, resultCode, data);

        } else {
            //MyLogs.LOG("BaseActivity", "onActivityResult", "handled by IABUtil");
        }
    }


    //=============================== PAYMENTS FUNCTIONAL ========================================//
    @Override
    public void onIabSetupFinished(IabResult result) {
        //implemented in TablesActivity
    }

    @Override
    public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
        //implemented in TablesActivity
    }

    @Override
    public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
        //implemented in SettingsActivity
    }

}