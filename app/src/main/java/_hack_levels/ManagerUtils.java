package _hack_levels;

import android.os.Environment;
import android.util.Pair;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import data.App;
import data.LevelObj;
import data.RouteObj;
import logic.helpers.MyLogs;

public class ManagerUtils {

    public static String[] IDS_REPO = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N"};

    public static void writeContentInFile(String src) {
        String filePath = Environment.getExternalStorageDirectory() + App.getAppCtx().getFilesDir().getPath() + "/logs/";

        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        if (src != null) {
            try {
                File path = new File(filePath);
                File file = new File(path, "logs.txt");

                FileWriter out = new FileWriter(file);
                out.write(src);
                out.close();
                MyLogs.LOG("MyLogs", "writeContentInFile", "New content is written in file: " + file.getAbsolutePath());

            } catch (IOException e) {
                MyLogs.LOG("FileWorker", "writeContentInFile", "Exception: " + e);
            }
        }
    }


    public static void convert() {
        List<LevelObj> levelObjs = new ArrayList<>();

        ObjHcked[] array = new Gson().fromJson(Mock.TEST, ObjHcked[].class);

        for (int pos = 0; pos < array.length; pos++) {

            if (pos == 4) {
                LevelObj levelObj = new LevelObj();
                levelObj.setLevelId("9x9_" + String.valueOf(pos + 1));
                levelObj.setLevelName(String.valueOf(pos + 1));
                levelObj.setSortOrder(pos);

                Pair<RouteObj[], String[]> pair = getObj(array[pos].getFlows());
                levelObj.setParentCoordonates(pair.second);
                levelObj.setRouteObjs(pair.first);

                levelObjs.add(levelObj);
            }
        }

        MyLogs.LOG("ManagerUtils", "convert", "hioak: " + levelObjs.size());
        writeContentInFile(new Gson().toJson(levelObjs));
    }

    private static Pair<RouteObj[], String[]> getObj(String input) {
        input = input.replaceAll("\\s+", "");
        MyLogs.LOG("ManagerUtils", "getObj", "iobanavrot: " + input);

        List<String> list = new ArrayList<>();
        List<RouteObj> listRoutes = new ArrayList<>();

        String[] arrayPair = input.split(",");

        for (int pos = 0; pos < arrayPair.length; pos++) {
            String id = IDS_REPO[pos];

            MyLogs.LOG("ManagerUtils", "getObj", "id: " + id + " input: " + arrayPair[pos]);
            String points = arrayPair[pos].substring(1, arrayPair[pos].length() - 1);

            char[] chars = points.toCharArray();
            MyLogs.LOG("ManagerUtils", "getObj", "points: " + points + " chars: " + chars.length);

            //need pair  structure: 'id.row.col,id.row.col'
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(id).append(".").append(chars[0]).append(".").append(chars[1]).append(",").append(id).append(".").append(chars[2]).append(".").append(chars[3]);
            MyLogs.LOG("ManagerUtils", "getObj", "stringBuilder: " + stringBuilder.toString());

            listRoutes.add(new RouteObj(id));
            list.add(stringBuilder.toString());
        }

        MyLogs.LOG("ManagerUtils", "getObj", "list: " + list.toString());
        return new Pair<>(listRoutes.toArray(new RouteObj[0]), list.toArray(new String[0]));
    }
}
