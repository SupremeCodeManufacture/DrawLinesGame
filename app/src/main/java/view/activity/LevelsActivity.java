package view.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.SupremeManufacture.DrawLines.R;

import data.App;
import data.DataWrapperObj;
import logic.adapter.OnTableLevelSelectedListener;
import logic.adapter.TableLevelsAdapter;
import logic.async_await.AsyncTaskWorker;
import logic.async_await.CallableObj;
import logic.async_await.OnAsyncDoneRsObjListener;
import logic.helpers.MyLogs;
import logic.helpers.Utils;
import view.custom.WrapLayoutManager;

public class LevelsActivity extends BaseActivity implements
        OnTableLevelSelectedListener,
        View.OnClickListener {

    private RecyclerView rvItms;
    public static final String ARG_TABLE_LEVEL_ID = "ARG_TABLE_LEVEL_ID";
    private String mTableTypeId;

    private TableLevelsAdapter mTableLevelsAdapter;


    @Override
    protected void onResume() {
        super.onResume();

        asyncLoadAllRecent(mTableTypeId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_types);

        initView();

        mTableTypeId = getIntent().getStringExtra(TablesActivity.ARG_TABLE_ID);
    }

    private void initView() {
        TextView tvTitle = (TextView) findViewById(R.id.tv_type);
        tvTitle.setText(App.getAppCtx().getResources().getString(R.string.txt_levels));

        rvItms = (RecyclerView) findViewById(R.id.rv_itms);
    }

    private void loadItems(DataWrapperObj dataWrapperObj) {
        if (dataWrapperObj != null) {
            mTableLevelsAdapter = new TableLevelsAdapter(dataWrapperObj, mTableTypeId, LevelsActivity.this);
            rvItms.setAdapter(mTableLevelsAdapter);
            rvItms.setLayoutManager(new WrapLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
            rvItms.setHasFixedSize(true);

        } else {
            Toast.makeText(LevelsActivity.this, App.getAppCtx().getResources().getString(R.string.app_err), Toast.LENGTH_SHORT).show();
        }
    }

    private void asyncLoadAllRecent(final String tableId) {
        //MyLogs.LOG("LevelsActivity", "asyncLoadAllRecent", "tableId: " + tableId);

        new AsyncTaskWorker(
                new CallableObj<DataWrapperObj>() {
                    public DataWrapperObj call() {
                        DataWrapperObj dataWrapperObj = new DataWrapperObj();
                        dataWrapperObj.setLevelObjs(Utils.getLevelObjs(tableId));
                        dataWrapperObj.setPassedLevels(Utils.getFinishedLevelsList());
                        dataWrapperObj.setLockedLevels(Utils.getLockedLevelsList());

                        return dataWrapperObj;
                    }
                },
                new OnAsyncDoneRsObjListener() {
                    @Override
                    public <T> void onDone(T t) {
                        if (t != null) {
                            loadItems((DataWrapperObj) t);
                        }
                    }
                }
        ).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void oTableLevelSelected(String levelId) {
        Intent intent = new Intent(LevelsActivity.this, GameActivity.class);
        intent.putExtra(ARG_TABLE_LEVEL_ID, Utils.getGameLevelsPos(mTableTypeId, levelId));
        startActivity(intent);
    }

    @Override
    public void onLockedLevelSelected() {
        showUpgradeDialog();
    }

    @Override
    void decideDemoOrPro() {
        if (App.isPaidFull() || App.isPaidUnlockLvls() && mTableLevelsAdapter != null) {
            mTableLevelsAdapter.updateViews();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
           case  R.id.btn_settings:
            startActivity(new Intent(LevelsActivity.this, SettingsActivity.class));
            break;
        }
    }
}