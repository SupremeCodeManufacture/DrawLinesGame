package view.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.SupremeManufacture.DrawLines.R;

import data.App;


public class UpgradeDialog extends Dialog implements View.OnClickListener {

    private LinearLayout zoneNoAds, zoneLvls;
    private OnPayListener mOnPayListener;


    public UpgradeDialog(@NonNull Context context, OnPayListener listener) {
        super(context);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (this.getWindow() != null)
            this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setContentView(R.layout.dialog_upgrades);
        this.mOnPayListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeViews();
    }

    private void initializeViews() {
        zoneNoAds = (LinearLayout) findViewById(R.id.zone_no_ads);
        if (App.isOldPaidAds() || App.isPaidAds()) zoneNoAds.setVisibility(View.GONE);

        zoneLvls = (LinearLayout) findViewById(R.id.zone_lvls);
        if(App.isPaidUnlockLvls()) zoneLvls.setVisibility(View.GONE);


        RelativeLayout rvPackNoAds = (RelativeLayout) findViewById(R.id.zone_buy_no_ads);
        rvPackNoAds.setOnClickListener(this);

        RelativeLayout rvPackUnlockedLvls = (RelativeLayout) findViewById(R.id.zone_buy_unlocked);
        rvPackUnlockedLvls.setOnClickListener(this);

        RelativeLayout rvPackFull = (RelativeLayout) findViewById(R.id.zone_buy_all);
        rvPackFull.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.zone_buy_no_ads:
                if (mOnPayListener != null) mOnPayListener.onPayNoAds();
                UpgradeDialog.this.dismiss();
                break;

            case R.id.zone_buy_unlocked:
                if (mOnPayListener != null) mOnPayListener.onPayUnlockLevels();
                UpgradeDialog.this.dismiss();
                break;

            case R.id.zone_buy_all:
                if (mOnPayListener != null) mOnPayListener.onPayFull();
                UpgradeDialog.this.dismiss();
                break;
        }
    }
}