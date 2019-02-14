package view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.SupremeManufacture.DrawLines.R;

import data.App;
import data.GenericConstants;
import logic.helpers.MyLogs;
import logic.payment.PaymentHelper;
import logic.payment.util.IabHelper;
import logic.payment.util.IabResult;
import logic.payment.util.Inventory;
import logic.payment.util.Purchase;
import view.custom.OnPayListener;
import view.custom.UpgradeDialog;

public abstract class BaseActivity extends AppCompatActivity implements
        IabHelper.OnIabSetupFinishedListener,
        IabHelper.OnIabPurchaseFinishedListener,
        IabHelper.QueryInventoryFinishedListener {

    //needed to update views status for payments
    abstract void decideDemoOrPro();

    protected void showUpgradeDialog() {
        new UpgradeDialog(BaseActivity.this, new OnPayListener() {
            @Override
            public void onPayNoAds() {
                PaymentHelper.doLifePayment(BaseActivity.this, App.getPaymentHelper(), GenericConstants.KEY_IN_APP_NO_ADS_SKU_ID, BaseActivity.this);
            }

            @Override
            public void onPayUnlockLevels() {
                PaymentHelper.doLifePayment(BaseActivity.this, App.getPaymentHelper(), GenericConstants.KEY_IN_APP_NO_LOCK_SKU_ID, BaseActivity.this);
            }

            @Override
            public void onPayFull() {
                PaymentHelper.doLifePayment(BaseActivity.this, App.getPaymentHelper(), GenericConstants.KEY_IN_APP_FULL_ID, BaseActivity.this);
            }
        }).show();
    }


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
        if (result.isSuccess()) {
            boolean isPaidOldAds = inventory.hasPurchase(GenericConstants.KEY_IN_APP_NO_ADS_OLD_SKU_ID);
            boolean isPaidAds = inventory.hasPurchase(GenericConstants.KEY_IN_APP_NO_ADS_SKU_ID);
            boolean isPaidLvls = inventory.hasPurchase(GenericConstants.KEY_IN_APP_NO_LOCK_SKU_ID);
            boolean isPaidFull = inventory.hasPurchase(GenericConstants.KEY_IN_APP_FULL_ID);
            //MyLogs.LOG("TablesActivity", "onQueryInventoryFinished", "isPaidOldAds: " + isPaidOldAds + " isPaidAds: " + isPaidAds + " isPaidLvls: " + isPaidLvls + " isPaidFull: " + isPaidFull);

            App.setOldPaidAds(isPaidOldAds);
            App.setPaidAds(isPaidAds);
            App.setPaidUnlockLvls(isPaidLvls);
            App.setPaidFull(isPaidFull);

            //reinitialise repo levels (available/locked)
            App.asyncSetupGameLevels();

            decideDemoOrPro();

        } else {
            //MyLogs.LOG("TablesActivity", "onQueryInventoryFinished", "Error query inventory: " + result);
        }
    }

    @Override
    public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
        if (result.isSuccess()) {
            //MyLogs.LOG("SettingsActivity", "onIabPurchaseFinished", "purchese SKU: " + purchase.getSku());

            switch (purchase.getSku()) {
                case GenericConstants.KEY_IN_APP_NO_ADS_SKU_ID:
                    App.setPaidAds(true);
                    break;

                case GenericConstants.KEY_IN_APP_NO_LOCK_SKU_ID:
                    App.setPaidUnlockLvls(true);
                    break;

                case GenericConstants.KEY_IN_APP_FULL_ID:
                    App.setPaidFull(true);
                    break;
            }

            //reinitialise repo levels (available/locked)
            App.asyncSetupGameLevels();

            decideDemoOrPro();
            Toast.makeText(BaseActivity.this, App.getAppCtx().getResources().getString(R.string.txt_worning_pro_done), Toast.LENGTH_LONG).show();

        } else {
            //MyLogs.LOG("SettingsActivity", "onIabPurchaseFinished", "Error purchasing: " + result);
        }
    }
}