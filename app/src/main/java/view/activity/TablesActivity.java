package view.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.SupremeManufacture.DrawLines.R;

import data.App;
import data.TableObj;
import logic.adapter.OnTableSelectedListener;
import logic.adapter.TablesAdapter;
import logic.async_await.AsyncTaskWorker;
import logic.async_await.CallableObj;
import logic.async_await.OnAsyncDoneRsObjListener;
import logic.helpers.MyLogs;
import logic.helpers.Utils;
import logic.payment.PaymentHelper;
import logic.payment.util.IabResult;
import view.custom.WrapLayoutManager;

public class TablesActivity extends BaseActivity implements
        OnTableSelectedListener,
        View.OnClickListener {

    private RecyclerView rvItms;
    public static final String ARG_TABLE_ID = "ARG_TABLE_ID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_types);

        initView();

        //init here to get status for further game activity
        PaymentHelper.setUpPayments(TablesActivity.this, TablesActivity.this);

        asyncLoadAllRecent();
    }

    private void initView() {
        rvItms = (RecyclerView) findViewById(R.id.rv_itms);
    }

    private void loadItems(TableObj[] tableObjs) {
        if (tableObjs != null && tableObjs.length > 0) {

            rvItms.setAdapter(new TablesAdapter(tableObjs, TablesActivity.this));
            rvItms.setLayoutManager(new WrapLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
            rvItms.setHasFixedSize(true);

        } else {
            Toast.makeText(TablesActivity.this, App.getAppCtx().getResources().getString(R.string.app_err), Toast.LENGTH_SHORT).show();
        }
    }

    private void asyncLoadAllRecent() {
        new AsyncTaskWorker(
                new CallableObj<TableObj[]>() {
                    public TableObj[] call() {
                        return Utils.getTableObjs();
                    }
                },
                new OnAsyncDoneRsObjListener() {
                    @Override
                    public <T> void onDone(T t) {
                        if (t != null) {
                            loadItems((TableObj[]) t);
                        }
                    }
                }
        ).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    @Override
    public void oTableSelected(String tabId) {
        Intent intent = new Intent(TablesActivity.this, LevelsActivity.class);
        intent.putExtra(ARG_TABLE_ID, tabId);
        startActivity(intent);
    }


    @Override
    public void decideDemoOrPro() {
        //no functional
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_settings:
                startActivity(new Intent(TablesActivity.this, SettingsActivity.class));
                break;
        }
    }

    //=============================== PAYMENTS FUNCTIONAL ========================================//
    @Override
    public void onIabSetupFinished(IabResult result) {
        if (result.isSuccess()) {
            //MyLogs.LOG("TablesActivity", "onIabSetupFinished", "Setting up In-app Billing succesfull");
            PaymentHelper.getLifePaymentStatus(App.getPaymentHelper(), TablesActivity.this);

        } else {
            //MyLogs.LOG("TablesActivity", "onIabSetupFinished", "Problem setting up In-app Billing: " + result);
        }
    }
}