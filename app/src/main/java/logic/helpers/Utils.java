package logic.helpers;

import android.support.v4.content.ContextCompat;
import android.util.Pair;

import com.SupremeManufacture.DrawLines.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import data.App;
import data.LevelObj;
import data.RouteObj;
import data.SharedPrefs;
import data.TableObj;
import view.custom.CellView;

public class Utils {

    public static String loadJSONFromAsset() {
        String json = null;

        try {
            InputStream is = App.getAppCtx().getAssets().open("mydata.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static List<String> getGameLevels() {
        List<String> list = new ArrayList<>();

        try {
            TableObj[] tableObjs = new Gson().fromJson(loadJSONFromAsset(), TableObj[].class);

            for (TableObj tableObj : tableObjs) {

                //all levels
                if (App.isPaidFull() || App.isPaidUnlockLvls()) {
                    for (LevelObj levelObj : tableObj.getTabLevels()) {
                        list.add(tableObj.getTabId() + "," + levelObj.getLevelId());
                    }

                    //only allowed levels
                } else {
                    LevelObj[] levelObjs = tableObj.getTabLevels();
                    int levelsCount = levelObjs.length;
                    int endLockPos = levelsCount / 2;

                    for (int pos = 0; pos < endLockPos; pos++) {
                        list.add(tableObj.getTabId() + "," + levelObjs[pos].getLevelId());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static void autodetectLockedLevels() {
        try {
            TableObj[] tableObjs = new Gson().fromJson(loadJSONFromAsset(), TableObj[].class);

            for (TableObj tableObj : tableObjs) {

                LevelObj[] levelObjs = tableObj.getTabLevels();
                int levelsCount = levelObjs.length;
                int startLockPos = levelsCount / 2;

                for (int pos = startLockPos; pos < levelsCount; pos++) {
                    storeLockedLevels(tableObj.getTabId() + "," + levelObjs[pos].getLevelId());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static TableObj[] getTableObjs() {
        try {
            return new Gson().fromJson(loadJSONFromAsset(), TableObj[].class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static LevelObj[] getLevelObjs(String tableId) {
        try {
            TableObj[] tableObjs = new Gson().fromJson(loadJSONFromAsset(), TableObj[].class);

            for (TableObj tableObj : tableObjs) {
                if (tableObj.getTabId().equals(tableId)) {
                    return tableObj.getTabLevels();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static LevelObj getGameLevel(String tableId, String levelId) {
        try {
            TableObj[] tableObjs = new Gson().fromJson(loadJSONFromAsset(), TableObj[].class);

            for (TableObj tableObj : tableObjs) {

                if (tableObj.getTabId().equals(tableId)) {
                    for (LevelObj levelObj : tableObj.getTabLevels()) {

                        if (levelObj.getLevelId().equals(levelId)) {
                            levelObj.setTableSize(tableObj.getTabSize());
                            levelObj.setTabName(tableObj.getTabDescr());
                            return levelObj;
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static int getGameLevelsPos(String tableTypeId, String levelId) {
        List<String> list = App.getGameLevels();
        int size = list.size();

        for (int pos = 0; pos < size; pos++) {
            String strToCompare = tableTypeId + "," + levelId;
            if (strToCompare.equals(list.get(pos))) {
                return pos;
            }
        }

        return 0;
    }

    public static Pair<String, String> getGameIds(int curPos) {
        List<String> list = App.getGameLevels();
        int size = list.size();

        if (size != 0 && curPos < size) {
            String[] array = list.get(curPos).split(",");
            return new Pair<>(array[0], array[1]);
        }

        return null;
    }

    public static List<String> getFinishedLevelsList() {
        List<String> valuesList = new ArrayList<>();
        String storedContent = SharedPrefs.getSharedPreferencesString(SharedPrefs.KEY_FINISHED, "");

        if (storedContent != null) {
            valuesList = new ArrayList<String>(Arrays.asList(storedContent.split("\\|")));
        }

        return valuesList;
    }

    public static void storeCompletedLevel(String completedLvlData) {
        String storedContent = SharedPrefs.getSharedPreferencesString(SharedPrefs.KEY_FINISHED, "");

        boolean alreadyPassed = false;

        if (storedContent != null) {
            List<String> valuesList = new ArrayList<String>(Arrays.asList(storedContent.split("\\|")));

            alreadyPassed = valuesList.contains(completedLvlData);
        }

        if (!alreadyPassed) {
            storedContent = storedContent + completedLvlData + "|";
            SharedPrefs.setSharedPreferencesString(SharedPrefs.KEY_FINISHED, storedContent);
        }
    }

    public static List<String> getLockedLevelsList() {
        List<String> valuesList = new ArrayList<>();
        String storedContent = SharedPrefs.getSharedPreferencesString(SharedPrefs.KEY_LOCKED, "");

        if (storedContent != null) {
            valuesList = new ArrayList<String>(Arrays.asList(storedContent.split("\\|")));
        }

        return valuesList;
    }

    private static void storeLockedLevels(String completedLvlData) {
        String loackedContent = SharedPrefs.getSharedPreferencesString(SharedPrefs.KEY_LOCKED, "");

        boolean alreadyLocked = false;

        if (loackedContent != null) {
            List<String> valuesList = new ArrayList<String>(Arrays.asList(loackedContent.split("\\|")));

            alreadyLocked = valuesList.contains(completedLvlData);
        }

        if (!alreadyLocked) {
            loackedContent = loackedContent + completedLvlData + "|";
            SharedPrefs.setSharedPreferencesString(SharedPrefs.KEY_LOCKED, loackedContent);
        }
    }

    public static int safeParseToInt(String number) {
        try {
            return Integer.parseInt(number);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return 0;
    }

    public static int getRandomColor(int pos) {
        switch (pos) {
            case 0:
                return ContextCompat.getColor(App.getAppCtx(), R.color.color1);

            case 1:
                return ContextCompat.getColor(App.getAppCtx(), R.color.color2);

            case 2:
                return ContextCompat.getColor(App.getAppCtx(), R.color.color3);

            case 3:
                return ContextCompat.getColor(App.getAppCtx(), R.color.color4);

            case 4:
                return ContextCompat.getColor(App.getAppCtx(), R.color.color5);

            case 5:
                return ContextCompat.getColor(App.getAppCtx(), R.color.color6);

            case 6:
                return ContextCompat.getColor(App.getAppCtx(), R.color.color7);

            case 7:
                return ContextCompat.getColor(App.getAppCtx(), R.color.color8);

            case 8:
                return ContextCompat.getColor(App.getAppCtx(), R.color.color9);

            case 9:
                return ContextCompat.getColor(App.getAppCtx(), R.color.color10);

            case 10:
                return ContextCompat.getColor(App.getAppCtx(), R.color.color11);

            case 11:
                return ContextCompat.getColor(App.getAppCtx(), R.color.color12);

            case 12:
                return ContextCompat.getColor(App.getAppCtx(), R.color.Yellow);

            case 13:
                return ContextCompat.getColor(App.getAppCtx(), R.color.Violet);

            case 14:
                return ContextCompat.getColor(App.getAppCtx(), R.color.Navy);

            case 15:
                return ContextCompat.getColor(App.getAppCtx(), R.color.DarkGoldenrod);
        }

        return ContextCompat.getColor(App.getAppCtx(), R.color.SandyBrown);
    }

    public static int getIndexInBounds(int index, int matrixSize) {
        if (index > (matrixSize - 1))
            return (matrixSize - 1);

        if (index < 0)
            return 0;

        return index;
    }

    public static CellView.ShapeType getPrevCellShapeType(CellView.ShapeType prevState, CellView.ShapeType newState) {
        switch (newState) {
            case UP: {
                switch (prevState) { // case LEFT_RIGHT: ?? need prev of prev
                    case CIRCLE:
                        return CellView.ShapeType.CIRCLE_UP;

                    case UP:
                        return CellView.ShapeType.UP;

                    case DOWN:
                    case UP_DOWN:
                    case DOWN_RIGHT:
                    case DOWN_LEFT:
                        return CellView.ShapeType.UP_DOWN;

                    case LEFT:
                    case UP_LEFT:
                        return CellView.ShapeType.UP_LEFT;

                    case RIGHT:
                    case UP_RIGHT:
                        return CellView.ShapeType.UP_RIGHT;
                }

                break;
            }

            case DOWN: {// case LEFT_RIGHT: ?? need prev of prev
                switch (prevState) {
                    case CIRCLE:
                        return CellView.ShapeType.CIRCLE_DOWN;

                    case UP:
                    case UP_DOWN:
                    case UP_RIGHT:
                    case UP_LEFT:
                        return CellView.ShapeType.UP_DOWN;

                    case DOWN:
                        return CellView.ShapeType.DOWN;

                    case LEFT:
//                    case LEFT_RIGHT:
                    case DOWN_LEFT:
                        return CellView.ShapeType.DOWN_LEFT;

                    case RIGHT:
                    case DOWN_RIGHT:
                        return CellView.ShapeType.DOWN_RIGHT;
                }
                break;
            }

            case LEFT: {
                switch (prevState) {
                    case CIRCLE:
                        return CellView.ShapeType.CIRCLE_LEFT;

                    case UP:
                    case UP_DOWN:
                    case UP_LEFT:
                    case UP_RIGHT:
                        return CellView.ShapeType.UP_LEFT;

                    case DOWN:
                    case DOWN_RIGHT:
                    case DOWN_LEFT:
                        return CellView.ShapeType.DOWN_LEFT;

                    case LEFT:
                        return CellView.ShapeType.LEFT;

                    case RIGHT:
                    case LEFT_RIGHT:
                        return CellView.ShapeType.LEFT_RIGHT;
                }
                break;
            }

            case RIGHT: {
                switch (prevState) {
                    case CIRCLE:
                        return CellView.ShapeType.CIRCLE_RIGHT;

                    case UP:
                    case UP_DOWN:
                    case UP_LEFT:
                    case UP_RIGHT:
                        return CellView.ShapeType.UP_RIGHT;

                    case DOWN:
                    case DOWN_RIGHT:
                    case DOWN_LEFT:
                        return CellView.ShapeType.DOWN_RIGHT;

                    case LEFT:
                    case LEFT_RIGHT:
                        return CellView.ShapeType.LEFT_RIGHT;

                    case RIGHT:
                        return CellView.ShapeType.RIGHT;
                }
                break;
            }
        }

        return CellView.ShapeType.NONE;
    }

    public static CellView.ShapeType getCurCellShapeType(boolean isParent, CellView.ShapeType newState) {
        switch (newState) {
            case UP: {
                if (isParent) return CellView.ShapeType.CIRCLE_UP;

                return CellView.ShapeType.UP;
            }

            case DOWN: {
                if (isParent) return CellView.ShapeType.CIRCLE_DOWN;

                return CellView.ShapeType.DOWN;
            }

            case LEFT: {
                if (isParent) return CellView.ShapeType.CIRCLE_LEFT;

                return CellView.ShapeType.LEFT;
            }

            case RIGHT: {
                if (isParent) return CellView.ShapeType.CIRCLE_RIGHT;

                return CellView.ShapeType.RIGHT;
            }
        }

        return CellView.ShapeType.NONE;
    }

    public static CellView.ShapeType getMiddleShapeState(CellView.ShapeType shapeType, int curRow, int curCol, int prevRow, int prevCol) {
        switch (shapeType) {
            case LEFT_RIGHT:
                if (prevCol > curCol) {
                    return CellView.ShapeType.RIGHT;
                } else {
                    return CellView.ShapeType.LEFT;
                }

            case UP_DOWN:
                if (prevRow > curRow) {
                    return CellView.ShapeType.DOWN;
                } else {
                    return CellView.ShapeType.UP;
                }

            case DOWN_LEFT:
                if (prevRow > curRow) {
                    return CellView.ShapeType.DOWN;
                } else {
                    return CellView.ShapeType.LEFT;
                }

            case DOWN_RIGHT:
                if (prevRow > curRow) {
                    return CellView.ShapeType.DOWN;
                } else {
                    return CellView.ShapeType.RIGHT;
                }

            case UP_LEFT:
                if (prevRow < curRow) {
                    return CellView.ShapeType.UP;
                } else {
                    return CellView.ShapeType.LEFT;
                }

            case UP_RIGHT:
                if (prevRow < curRow) {
                    return CellView.ShapeType.UP;
                } else {
                    return CellView.ShapeType.RIGHT;
                }
        }

        MyLogs.LOG("Utils", "000111222 - getMiddleShapeState", "NONE");
        return CellView.ShapeType.NONE;
    }


    public static boolean isCurrCellNewDifferentParent(CellView currentCell, String curRouteId) {
        return currentCell.isParent() && !currentCell.getUniqueId().equals(curRouteId);
    }

    public static boolean isAllowedToHitParent(CellView currentCell, RouteObj routeObj, String curRouteId) {
        //different parent - other route
        if (!currentCell.getUniqueId().equals(curRouteId)) {
            MyLogs.LOG("Utils", "094353465 - isAllowedToHitParent", "oops different parent");
            return false;

            //same parent - check for a match
        } else if (isSameParent(routeObj, currentCell)) {
            MyLogs.LOG("Utils", "094353465 - isAllowedToHitParent", "oops same parent twice");
            return false;
        }

        MyLogs.LOG("Utils", "094353465 - isAllowedToHitParent", "zaebisi!");
        return true;
    }

    public static boolean isSameParent(RouteObj routeObj, CellView currentCell) {
        if (routeObj.getTempRouteCoordonates() != null && routeObj.getTempRouteCoordonates().size() > 1) {
            String firstCellData = routeObj.getTempRouteCoordonates().get(0);
            String curCellData = currentCell.getUniqueId() + "." + currentCell.getIndexRow() + "." + currentCell.getIndexCol();
            MyLogs.LOG("Utils", "094353465 - isSameParent", "firstCellData: " + firstCellData + " curCellData: " + curCellData);

            return firstCellData.equals(curCellData);
        }

        return false;
    }


    //uswd sincw listener is fired very fast couple times per cell
    public static boolean isAlreadyDrawn(LinkedList<CellView> linkedList, CellView newCell) {
        for (CellView existingCell : linkedList) {
            if ((existingCell.getIndexRow() == newCell.getIndexRow()) && (existingCell.getIndexCol() == newCell.getIndexCol())) {
                return true;
            }
        }

        return false;
    }

    public static boolean isRouteCompleted(RouteObj routeObj, String[] parentPairs, String curId) {
        if (routeObj.getId().equals(curId) && routeObj.getTempRouteCoordonates() != null) {
            String firstCellData = routeObj.getTempRouteCoordonates().get(0);
            MyLogs.LOG("Utils", "094353465 - isRouteCompleted", "routes: " + routeObj.getTempRouteCoordonates().toString() + " firstCellData: " + firstCellData);

            int listSize = routeObj.getTempRouteCoordonates().size();
            if (listSize > 1) {
                String lastCellData = routeObj.getTempRouteCoordonates().get(listSize - 1);
                MyLogs.LOG("Utils", "094353465 - isRouteCompleted", "lastCellData: " + lastCellData);

                return isRouteCompleted(parentPairs, firstCellData, lastCellData);
            }
        }

        return false;
    }

    public static boolean isRouteCompleted(String[] parentPairs, String cellData1, String cellData2) {
        for (String pairData : parentPairs) {
            List<String> arrayPairs = new ArrayList<String>(Arrays.asList(pairData.split(",")));
            MyLogs.LOG("Utils", "094353465 - isRouteCompleted", "arrayPairs: " + arrayPairs.toString());

            if (arrayPairs.contains(cellData1) && arrayPairs.contains(cellData2))
                return true;
        }

        return false;
    }


    public static RouteObj getCurrentRoute(RouteObj[] routes, String id) {
        for (RouteObj routeObj : routes) {
            if (routeObj.getId().equals(id)) {
                return routeObj;
            }
        }

        //warning no route found!
        return null;
    }

    public static boolean isInMiddleOfRoute(CellView cellView) {
        switch (cellView.getShapeType()) {
            case UP_DOWN:
            case LEFT_RIGHT:
            case UP_LEFT:
            case UP_RIGHT:
            case DOWN_LEFT:
            case DOWN_RIGHT:
                MyLogs.LOG("Utils", "094353465 - isInMiddleOfRoute", "true");
                return true;
        }

        MyLogs.LOG("Utils", "000111222 - isInMiddleOfRoute", "false");
        return false;
    }

    public static String getLevelFinishSuccess(int bestMovies, int curMovies) {
        if (bestMovies == curMovies)
            return App.getAppCtx().getResources().getString(R.string.txt_exc);

        if (bestMovies >= curMovies - 3)
            return App.getAppCtx().getResources().getString(R.string.txt_good);

        return App.getAppCtx().getResources().getString(R.string.txt_no_ok);
    }

    public static String getTableName(int pos) {
        switch (pos) {
            case 0:
                return App.getAppCtx().getResources().getString(R.string.txt_levels1);

            case 1:
                return App.getAppCtx().getResources().getString(R.string.txt_levels2);

            case 2:
                return App.getAppCtx().getResources().getString(R.string.txt_levels3);

            case 3:
                return App.getAppCtx().getResources().getString(R.string.txt_levels4);

            case 4:
                return App.getAppCtx().getResources().getString(R.string.txt_levels5);
        }

        return "unknown";
    }

}