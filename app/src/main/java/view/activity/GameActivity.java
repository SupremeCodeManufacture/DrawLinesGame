package view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.SupremeManufacture.DrawLines.R;
import com.google.android.gms.ads.AdSize;
import com.soloviof.easyads.AdsRepo;
import com.soloviof.easyads.CustomizeAds;
import com.soloviof.easyads.InitApp;
import com.soloviof.easyads.InterstitialAddsHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import data.App;
import data.LevelObj;
import data.RouteObj;
import logic.async_await.AsyncTaskWorker;
import logic.async_await.CallableObj;
import logic.async_await.OnAsyncDoneRsObjListener;
import logic.helpers.DataFormatConverter;
import logic.helpers.ThemeColorsHelper;
import logic.helpers.Utils;
import view.custom.CellView;
import view.custom.Dialogs;
import view.custom.OnGoNextLevelListener;

public class GameActivity extends AppCompatActivity implements
        View.OnTouchListener,
        View.OnClickListener {


    //views
    private TableLayout mTableView;
    private TextView tvType, tvLevel, tvMovements, tvBest, tvPipe;
    private ImageButton btnRestartPrev, btnRestartNext;

    //vars
    private CellView[][] mGameCellsArray;


    private int CUR_LEVEL_POS;
    private LevelObj mGameLevelData;
    private RouteObj mCurrentRoute;
    private int mTabSize;
    private String mCompleteLevelId;


    private LinkedList<CellView> mDrawTempCellList = new LinkedList<>();
    private String mTempRouteId;
    private int mTempColorLine;
    private boolean mIsDrawAllowed, mIsDifferentParentLock, mIsRouteCompletedLock;

    //game data
    private int mGameMovements;


    @Override
    protected void onResume() {
        super.onResume();
        setupAdBanner();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Window win = getWindow();
        win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_game);

        initViews();

        InitApp.doFirebaseInit(GameActivity.this, AdsRepo.getAppId(App.getAppCtx(), App.getAppBuilds(), App.getAppCtx().getResources().getString(R.string.ads_app_id)));

        InterstitialAddsHelper.prepareInterstitialAds(
                GameActivity.this,
                App.getAppBuilds(),
                App.getAppCtx().getResources().getString(R.string.banner_id_interstitial));

        CUR_LEVEL_POS = getIntent().getIntExtra(LevelsActivity.ARG_TABLE_LEVEL_ID, 0);
        asyncLoadGame();
    }

    private void initViews() {
        mTableView = (TableLayout) findViewById(R.id.table_game);
        mTableView.setOnTouchListener(this);

        tvType = (TextView) findViewById(R.id.tv_type);
        tvLevel = (TextView) findViewById(R.id.tv_level);
        tvMovements = (TextView) findViewById(R.id.tv_moves);
        tvBest = (TextView) findViewById(R.id.tv_best);
        tvPipe = (TextView) findViewById(R.id.tv_pipe);

        ImageButton btnBack = (ImageButton) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);

        ImageButton btnSettings = (ImageButton) findViewById(R.id.btn_settings);
        btnSettings.setOnClickListener(this);

        btnRestartPrev = (ImageButton) findViewById(R.id.btn_prev);
        btnRestartPrev.setOnClickListener(this);

        ImageButton btnRestartGame = (ImageButton) findViewById(R.id.btn_restart);
        btnRestartGame.setOnClickListener(this);

        btnRestartNext = (ImageButton) findViewById(R.id.btn_next);
        btnRestartNext.setOnClickListener(this);
    }

    private void setupAdBanner() {
        LinearLayout bannerHolder = (LinearLayout) findViewById(R.id.ll_banner);
        if (DataFormatConverter.isPassedAdsFree() && !App.isPaidFull() && !App.isOldPaidAds() && !App.isPaidAds()) {
            bannerHolder.setVisibility(View.VISIBLE);

            CustomizeAds.setupAddBanner(
                    GameActivity.this,
                    bannerHolder,
                    AdSize.SMART_BANNER,
                    AdsRepo.getBannerId1(App.getAppCtx(), App.getAppBuilds(), App.getAppCtx().getResources().getString(R.string.banner_id)),
                    "game screen");

        } else {
            bannerHolder.setVisibility(View.GONE);
        }
    }

    //async get data from json
    private void asyncLoadGame() {
        Pair<String, String> pairIds = Utils.getGameIds(CUR_LEVEL_POS);

        if (pairIds != null) {
            final String tabId = pairIds.first;
            final String labelId = pairIds.second;
            //MyLogs.LOG("GameActivity", "asyncLoadGame", "CUR_LEVEL_POS: " + CUR_LEVEL_POS + " tabId: " + tabId + " labelId: " + labelId);

            new AsyncTaskWorker(
                    new CallableObj<LevelObj>() {
                        public LevelObj call() {
                            mCompleteLevelId = tabId + "," + labelId;
                            return Utils.getGameLevel(tabId, labelId);
                        }
                    },
                    new OnAsyncDoneRsObjListener() {
                        @Override
                        public <T> void onDone(T t) {
                            if (t != null) {
                                mGameLevelData = (LevelObj) t;
                                mTabSize = mGameLevelData.getTableSize();

                                tvType.setText(mGameLevelData.getTabName());
                                tvLevel.setText(App.getAppCtx().getResources().getString(R.string.app_level) + " " + mGameLevelData.getLevelName());

                                getRoutes();

                                fillUpGameTable(mTabSize);
                            }
                        }
                    }
            ).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } else {
            Toast.makeText(GameActivity.this, App.getAppCtx().getResources().getString(R.string.app_err), Toast.LENGTH_SHORT).show();
        }

        //setup buttons prev/next
        if (CUR_LEVEL_POS > 0) {
            btnRestartPrev.setEnabled(true);

        } else {
            btnRestartPrev.setEnabled(false);
        }

        if (CUR_LEVEL_POS < App.getGameLevels().size() - 1) {
            btnRestartNext.setEnabled(true);

        } else {
            btnRestartNext.setEnabled(false);
        }
    }

    //get game level route
    private void getRoutes() {

        mGameMovements = 0;
        tvMovements.setText(App.getAppCtx().getResources().getString(R.string.app_moves) + " 0");
        tvPipe.setText(App.getAppCtx().getResources().getString(R.string.app_pipe) + " 0%");
        tvBest.setText(App.getAppCtx().getResources().getString(R.string.app_best) + " " + mTabSize);

        //generate local cells matrix
        mGameCellsArray = new CellView[mTabSize][mTabSize];

        //make all board color firstly
        for (int row = 0; row < mTabSize; row++) {
            for (int col = 0; col < mTabSize; col++) {
                mGameCellsArray[row][col] = new CellView(GameActivity.this, CellView.ShapeType.NONE, null, ThemeColorsHelper.getBoardColor(), row, col, false);
            }
        }

        //get data from server or locally
        int lenParentCells = mGameLevelData.getParentCoordonates().length;

        for (int i = 0; i < lenParentCells; i++) {
            String cellData = mGameLevelData.getParentCoordonates()[i];//'pair has structure: id.row.col,id.row.col'
            String[] arrayPairs = cellData.split(",");
            int pairColor = Utils.getRandomColor(i);

            String[] arrayCell1 = arrayPairs[0].split("\\.");
            int row1 = Utils.safeParseToInt(arrayCell1[1]);
            int col1 = Utils.safeParseToInt(arrayCell1[2]);
            mGameCellsArray[row1][col1] = new CellView(GameActivity.this, CellView.ShapeType.CIRCLE, arrayCell1[0], pairColor, row1, col1, true);

            String[] arrayCell2 = arrayPairs[1].split("\\.");
            int row2 = Utils.safeParseToInt(arrayCell2[1]);
            int col2 = Utils.safeParseToInt(arrayCell2[2]);
            mGameCellsArray[row2][col2] = new CellView(GameActivity.this, CellView.ShapeType.CIRCLE, arrayCell2[0], pairColor, row2, col2, true);
        }
    }

    //add all created array to table
    private void fillUpGameTable(int size) {

        //reset all views
        clearGameTable();

        for (int i = 0; i < size; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            for (int col = 0; col < size; col++) {
                row.addView(mGameCellsArray[i][col]);
            }

            mTableView.addView(row);
        }
    }

    // remove all itms
    private void clearGameTable() {
        int count = mTableView.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = mTableView.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }
        mTableView.removeAllViewsInLayout();
    }


    // add cells to be drawn to temp game obj and temp cells to draw
    private void addCellToDraw(CellView newCell) {
        mDrawTempCellList.add(newCell);

        //curent route is stored as temp to ignore loop
        if (mCurrentRoute != null) {
            String newCellTempData = newCell.getUniqueId() + "." + newCell.getIndexRow() + "." + newCell.getIndexCol();

            if (mCurrentRoute.getTempRouteCoordonates() == null)
                mCurrentRoute.setTempRouteCoordonates(new LinkedList<String>());

            //add unique cells to route logs - used for detections
            if (!mCurrentRoute.getTempRouteCoordonates().contains(newCellTempData))
                mCurrentRoute.getTempRouteCoordonates().add(newCellTempData);
        }
    }


    //delete all route - pressed on parent cell
    private void deleteEntireRoute(String id) {
        for (RouteObj routeObj : mGameLevelData.getRouteObjs()) {
            if (routeObj.getId().equals(id) && routeObj.getTempRouteCoordonates() != null) {
                //MyLogs.LOG("GameActivity", "094353465 - deleteEntireRoute", "routes: " + routeObj.getTempRouteCoordonates().toString());

                for (String cellData : routeObj.getTempRouteCoordonates()) {
                    String[] array = cellData.split("\\.");
                    int row = Utils.safeParseToInt(array[1]);
                    int col = Utils.safeParseToInt(array[2]);

                    if (mGameCellsArray[row][col].isParent()) {
                        mGameCellsArray[row][col] = new CellView(GameActivity.this, CellView.ShapeType.CIRCLE, mGameCellsArray[row][col].getUniqueId(), mGameCellsArray[row][col].getColor(), row, col, true);

                    } else {
                        mGameCellsArray[row][col] = new CellView(GameActivity.this, CellView.ShapeType.NONE, null, ThemeColorsHelper.getBoardColor(), row, col, false);
                    }
                }

                routeObj.getTempRouteCoordonates().clear();
                fillUpGameTable(mTabSize);

                //MyLogs.LOG("GameActivity", "094353465 - deleteEntireRoute", "routes after: " + routeObj.getTempRouteCoordonates().toString());
                //MyLogs.LOG("GameActivity", "094353465 - deleteEntireRoute", "srte 4.0: " + mGameCellsArray[4][0].getShapeType().name());

                //no need to loop all list
                return;
            }
        }
    }

    private void deleteOtherRoutePart(String id, int rowPos, int colPos, boolean keepHittedCell) {
        for (RouteObj routeObj : mGameLevelData.getRouteObjs()) {
            //MyLogs.LOG("GameActivity", "094353465 - deleteOtherRoutePart", "looping: " + routeObj.getId() + " need: " + id);

            if (routeObj.getId().equals(id) && routeObj.getTempRouteCoordonates() != null) {
                //MyLogs.LOG("GameActivity", "094353465 - deleteOtherRoutePart", "route: " + routeObj.getTempRouteCoordonates().toString());

                int tempSize = routeObj.getTempRouteCoordonates().size();
                int posToStartDelete = tempSize;

                for (int pos = 0; pos < tempSize; pos++) {
                    String[] curCellArray = routeObj.getTempRouteCoordonates().get(pos).split("\\.");
                    int curRow = Utils.safeParseToInt(curCellArray[1]);
                    int curCol = Utils.safeParseToInt(curCellArray[2]);

                    if (curRow == rowPos && curCol == colPos) {
                        //MyLogs.LOG("GameActivity", "094353465 - deleteOtherRoutePart", "detect delete start curRow: " + curRow + " curCol: " + curCol);
                        posToStartDelete = pos;

                        if (keepHittedCell) {
                            String[] prevCellArray = routeObj.getTempRouteCoordonates().get(pos - 1).split("\\.");
                            int prevRow = Utils.safeParseToInt(prevCellArray[1]);
                            int prevCol = Utils.safeParseToInt(prevCellArray[2]);

                            posToStartDelete++;

                            CellView.ShapeType newState = Utils.getMiddleShapeState(mGameCellsArray[curRow][curCol].getShapeType(), curRow, curCol, prevRow, prevCol);
                            mGameCellsArray[curRow][curCol].setShapeType(newState);
                        }
                    }

                    if (pos >= posToStartDelete) {
                        //just draw transparent all unused cells
                        if (mGameCellsArray[curRow][curCol].isParent()) {
                            mGameCellsArray[curRow][curCol] = new CellView(GameActivity.this, CellView.ShapeType.CIRCLE, mGameCellsArray[curRow][curCol].getUniqueId(), mGameCellsArray[curRow][curCol].getColor(), curRow, curCol, true);

                        } else {
                            mGameCellsArray[curRow][curCol] = new CellView(GameActivity.this, CellView.ShapeType.NONE, null, ThemeColorsHelper.getBoardColor(), curRow, curCol, false);
                        }
                    }
                }

                //delete from temp list
                routeObj.getTempRouteCoordonates().subList(posToStartDelete, tempSize).clear();

                //if single pair remains in list, for ex [A.1.1] - it's the start point and need to draw it a simple circle
                if (routeObj.getTempRouteCoordonates().size() == 1) {
                    String[] array = routeObj.getTempRouteCoordonates().get(0).split("\\.");
                    int row = Utils.safeParseToInt(array[1]);
                    int col = Utils.safeParseToInt(array[2]);

                    mGameCellsArray[row][col] = new CellView(GameActivity.this, CellView.ShapeType.CIRCLE, mGameCellsArray[row][col].getUniqueId(), mGameCellsArray[row][col].getColor(), row, col, true);

                    //clear it all - no items inside - last one was just redrawn in initial state
                    routeObj.getTempRouteCoordonates().clear();
                }

                if (keepHittedCell) fillUpGameTable(mTabSize);

                //MyLogs.LOG("GameActivity", "094353465 - deleteOtherRoutePart", "route after: " + routeObj.getTempRouteCoordonates().toString());

                //no need to loop all routes - it;s needed only current one
                return;
            }
        }
    }


    private int getPipe() {
        int total = 0;
        int done = 0;

        //make all board color firstly
        for (int row = 0; row < mTabSize; row++) {
            for (int col = 0; col < mTabSize; col++) {
                total++;

                //no board color cells and no simple circles
                if (mGameCellsArray[row][col].getColor() != ThemeColorsHelper.getBoardColor() && mGameCellsArray[row][col].getShapeType() != CellView.ShapeType.CIRCLE)
                    done++;
            }
        }

        //MyLogs.LOG("GameActivity", "getPipe", "total: " + total + " done: " + done + " %: " + (done * 100 / total));

        return done * 100 / total;
    }

    private void onOpenNext() {
        if (CUR_LEVEL_POS < App.getGameLevels().size() - 1) {
            CUR_LEVEL_POS++;

        } else {
            CUR_LEVEL_POS = 0;
        }

        Utils.storeCompletedLevel(mCompleteLevelId);

        asyncLoadGame();

    }


    public boolean isRouteCompleted() {
        if (mCurrentRoute.getTempRouteCoordonates() != null) {

            //MyLogs.LOG("GameActivity", "isRouteCompleted", "temp: " + mCurrentRoute.getTempRouteCoordonates().toString());

            for (String parentsPair : mGameLevelData.getParentCoordonates()) {
                if (mCurrentRoute.getTempRouteCoordonates().containsAll(new ArrayList<String>(Arrays.asList(parentsPair.split(","))))) {
                    //MyLogs.LOG("GameActivity", "isRouteCompleted", "aaaaaaaaaaaac:  " + parentsPair);
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isNewCellConectedToRoute(int indexRow, int indexCol) {
        //MyLogs.LOG("GameActivity", "isNewCellConectedToRoute", "indexRow: " + indexRow + " indexCol: " + indexCol);

        if (mGameCellsArray[indexRow][indexCol].isParent()) {
            //MyLogs.LOG("GameActivity", "", "parent");
            return true;
        }

        //prev cell up
        if (indexRow > 0 && mGameCellsArray[indexRow - 1][indexCol] != null && mGameCellsArray[indexRow - 1][indexCol].getUniqueId() != null && mGameCellsArray[indexRow - 1][indexCol].getUniqueId().equals(mTempRouteId)) {
            //MyLogs.LOG("GameActivity", "", "down");
            return true;
        }

        //prev cell down
        if (indexRow < mTabSize - 1 && mGameCellsArray[indexRow + 1][indexCol] != null && mGameCellsArray[indexRow + 1][indexCol].getUniqueId() != null && mGameCellsArray[indexRow + 1][indexCol].getUniqueId().equals(mTempRouteId)) {
            //MyLogs.LOG("GameActivity", "isNewCellConectedToRoute", "down");
            return true;
        }

        //prev cell left
        if (indexCol > 0 && mGameCellsArray[indexRow][indexCol - 1] != null && mGameCellsArray[indexRow][indexCol - 1].getUniqueId() != null && mGameCellsArray[indexRow][indexCol - 1].getUniqueId().equals(mTempRouteId)) {
            //MyLogs.LOG("GameActivity", "isNewCellConectedToRoute", "left");
            return true;
        }

        //prev cell right
        if (indexCol < mTabSize - 1 && mGameCellsArray[indexRow][indexCol + 1] != null && mGameCellsArray[indexRow][indexCol + 1].getUniqueId() != null && mGameCellsArray[indexRow][indexCol + 1].getUniqueId().equals(mTempRouteId)) {
            //MyLogs.LOG("GameActivity", "isNewCellConectedToRoute", "right");
            return true;
        }

        //MyLogs.LOG("GameActivity", "isNewCellConectedToRoute", "oh no!!!!!!!!");
        return false;
    }


    private void onDrawMovement() {

        // temp data to draw - last 2 cells
        CellView prevCell = mDrawTempCellList.get(mDrawTempCellList.size() - 2);
        CellView currentCell = mDrawTempCellList.get(mDrawTempCellList.size() - 1);

        int indexPreviousCellRow = prevCell.getIndexRow();
        int indexPreviousCellCol = prevCell.getIndexCol();
        int indexCurrentCellRow = currentCell.getIndexRow();
        int indexCurrentCellCol = currentCell.getIndexCol();
        CellView.ShapeType prevousState = prevCell.getShapeType();

        //MyLogs.LOG("GameActivity", "onDrawMovement", "BEFOR prevousState: " + prevousState.name() + " cur state: " + currentCell.getShapeType().name() + "  T: " + System.currentTimeMillis());

        // From Right to Left
        if (indexPreviousCellRow == indexCurrentCellRow && indexPreviousCellCol > indexCurrentCellCol) {
            prevCell.setShapeType(Utils.getPrevCellShapeType(prevousState, CellView.ShapeType.LEFT));
            mGameCellsArray[indexPreviousCellRow][indexPreviousCellCol] = prevCell;

            currentCell.setShapeType(Utils.getCurCellShapeType(currentCell.isParent(), CellView.ShapeType.RIGHT));
            mGameCellsArray[indexCurrentCellRow][indexCurrentCellCol] = currentCell;

            //MyLogs.LOG("GameActivity", "onDrawMovement", "Right to Left");
        } else

            // From Left to Right
            if (indexPreviousCellRow == indexCurrentCellRow && indexPreviousCellCol < indexCurrentCellCol) {
                prevCell.setShapeType(Utils.getPrevCellShapeType(prevousState, CellView.ShapeType.RIGHT));
                mGameCellsArray[indexPreviousCellRow][indexPreviousCellCol] = prevCell;

                currentCell.setShapeType(Utils.getCurCellShapeType(currentCell.isParent(), CellView.ShapeType.LEFT));
                mGameCellsArray[indexCurrentCellRow][indexCurrentCellCol] = currentCell;
                //MyLogs.LOG("GameActivity", "onDrawMovement", "Left to Right");
            } else

                // From Down to Up
                if (indexPreviousCellRow > indexCurrentCellRow && indexPreviousCellCol == indexCurrentCellCol) {
                    prevCell.setShapeType(Utils.getPrevCellShapeType(prevousState, CellView.ShapeType.UP));
                    mGameCellsArray[indexPreviousCellRow][indexPreviousCellCol] = prevCell;

                    currentCell.setShapeType(Utils.getCurCellShapeType(currentCell.isParent(), CellView.ShapeType.DOWN));
                    mGameCellsArray[indexCurrentCellRow][indexCurrentCellCol] = currentCell;
                    //MyLogs.LOG("GameActivity", "onDrawMovement", "Down to Up");
                } else

                    // From Up to Down
                    if (indexPreviousCellRow < indexCurrentCellRow && indexPreviousCellCol == indexCurrentCellCol) {
                        prevCell.setShapeType(Utils.getPrevCellShapeType(prevousState, CellView.ShapeType.DOWN));
                        mGameCellsArray[indexPreviousCellRow][indexPreviousCellCol] = prevCell;

                        currentCell.setShapeType(Utils.getCurCellShapeType(currentCell.isParent(), CellView.ShapeType.UP));
                        mGameCellsArray[indexCurrentCellRow][indexCurrentCellCol] = currentCell;
                        //MyLogs.LOG("GameActivity", "onDrawMovement", "Up to Down");
                    } else {
                        //MyLogs.LOG("GameActivity", "onDrawMovement", "wazaaaaaaaaap stop mo route");
                        mIsDrawAllowed = false;
                    }

        //MyLogs.LOG("GameActivity", "onDrawMovement", prevCell.getIndexRow() + "," + prevCell.getIndexCol() + " " + prevCell.getShapeType().name() + "  ==> " + currentCell.getIndexRow() + "," + currentCell.getIndexCol() + " " + currentCell.getShapeType().name());

        fillUpGameTable(mTabSize);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        // intercept game table params
        mTableView.setMeasureWithLargestChildEnabled(true);
        int cellWidth = mTableView.getMeasuredWidth() / mTabSize;
        int cellHeight = mTableView.getMeasuredHeight() / mTabSize;

        // pressed positions
        int indexRow = (int) event.getY() / cellWidth;
        int indexCol = (int) event.getX() / cellHeight;

        //check if not pressed out of bounds
        indexRow = Utils.getIndexInBounds(indexRow, mTabSize);
        indexCol = Utils.getIndexInBounds(indexCol, mTabSize);

        CellView currentCell = mGameCellsArray[indexRow][indexCol];

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //MyLogs.LOG("GameActivity", "onTouch", "ACTION_DOWN - indexRow: " + indexRow + " indexCol: " + indexCol);

            //if board color cell - do nothing
            if (currentCell.getColor() != ThemeColorsHelper.getBoardColor()) {

                //if press on parent - delete it's route
                if (currentCell.isParent()) {
                    deleteEntireRoute(currentCell.getUniqueId());
                    currentCell = mGameCellsArray[indexRow][indexCol];

                } else if (Utils.isInMiddleOfRoute(currentCell)) {
                    deleteOtherRoutePart(currentCell.getUniqueId(), currentCell.getIndexRow(), currentCell.getIndexCol(), true);
                }

                mTempColorLine = currentCell.getColor();
                mTempRouteId = currentCell.getUniqueId();
                mCurrentRoute = Utils.getCurrentRoute(mGameLevelData.getRouteObjs(), currentCell.getUniqueId());
                mIsDrawAllowed = true;

                mGameMovements++;
                tvMovements.setText(App.getAppCtx().getResources().getString(R.string.app_moves) + " " + mGameMovements);
            }
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            //MyLogs.LOG("GameActivity", "onTouch", "ACTION_UP - indexRow: " + indexRow + " indexCol: " + indexCol);

            //reset temp values
            mDrawTempCellList.clear();
            mTempColorLine = ThemeColorsHelper.getBoardColor();
            mTempRouteId = null;
            mIsDrawAllowed = false;
            mIsRouteCompletedLock = false;
            mIsDifferentParentLock = false;

            int pipe = getPipe();
            tvPipe.setText(App.getAppCtx().getResources().getString(R.string.app_pipe) + " " + pipe + "%");

            if (pipe == 100) {
                Dialogs.showShareToUserDialog(GameActivity.this, Utils.getLevelFinishSuccess(mGameLevelData.getParentCoordonates().length, mGameMovements), new OnGoNextLevelListener() {
                    @Override
                    public void onOpenNextLevel() {
                        boolean isAllowedInterstitial = CUR_LEVEL_POS != 0 && CUR_LEVEL_POS % 3 == 0 && DataFormatConverter.isPassedAdsFree() && !App.isPaidFull() && !App.isOldPaidAds() && !App.isPaidAds();
                        InterstitialAddsHelper.tryShowInterstitialAdNow(isAllowedInterstitial);
                        onOpenNext();
                    }
                });
            }
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {

            //check if current cell is not a diferent parent - just lock all further draw
            if (currentCell.isParent()) {
                if (!mIsDifferentParentLock)//if once is locked need to keep lock till user takes up finger
                    mIsDifferentParentLock = !Utils.isAllowedToHitParent(currentCell, mCurrentRoute, mTempRouteId);

            } else if (currentCell.getColor() == mTempColorLine && Utils.isInMiddleOfRoute(currentCell)) { //delete part of route if the same route is hitted
                deleteOtherRoutePart(currentCell.getUniqueId(), currentCell.getIndexRow(), currentCell.getIndexCol(), true);
                mIsDrawAllowed = false;
            }

            if (mIsDrawAllowed && !mIsDifferentParentLock && !mIsRouteCompletedLock && !Utils.isAlreadyDrawn(mDrawTempCellList, currentCell) && isNewCellConectedToRoute(indexRow, indexCol)) {
                //this cell is already drawn by a different id - need to distroy part of that route
                if (!mTempRouteId.equals(currentCell.getUniqueId())) {
//                    deleteOtherRoutePart(currentCell.getUniqueId(), indexRow, indexCol, false);
                    deleteEntireRoute(currentCell.getUniqueId());
                }

                //assign values to cell
                if (!currentCell.isParent()) {
                    currentCell.setUniqueId(mTempRouteId);
                    currentCell.setColor(mTempColorLine);
                }

                //add cell to temp & draw line
                addCellToDraw(currentCell);
                if (mDrawTempCellList.size() > 1) {
                    onDrawMovement();
                }

                //detect if it's route completed - disable draw
                if (currentCell.isParent() && isRouteCompleted()) {
                    //Utils.isRouteCompleted(mCurrentRoute, mGameLevelData.getParentCoordonates(), mTempRouteId)) {
                    //MyLogs.LOG("GameActivity", "onTouch", "ACTION_MOVE - dooooooooooone");
                    mIsRouteCompletedLock = true;
                }
            }

            return true;
        }

        return false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                GameActivity.this.onBackPressed();
                break;

            case R.id.btn_settings:
                Intent intent = new Intent(GameActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_prev:
                CUR_LEVEL_POS--;
                asyncLoadGame();
                break;

            case R.id.btn_restart:
                asyncLoadGame();
                break;

            case R.id.btn_next:
                CUR_LEVEL_POS++;
                asyncLoadGame();
                break;
        }
    }
}