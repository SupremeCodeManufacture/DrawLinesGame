package logic.helpers;

import android.net.Uri;

import com.google.gson.Gson;

import java.net.URLDecoder;

import logic.push_notification.CloudDataObj;

public class DataFormatConverter {


    public static CloudDataObj getObjFromJson(String json) {
        try {
            MyLogs.LOG("DataFormatConverter", "getObjFromJson", "json: " + json);
            return new Gson().fromJson(json, CloudDataObj.class);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static Uri safeConvertUrlToUri(String url) {
        try {
            return Uri.parse(URLDecoder.decode(url, "UTF-8"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }


}