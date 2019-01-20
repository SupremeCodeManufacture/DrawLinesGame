package data;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import logic.async_await.AsyncTaskWorker;
import logic.helpers.Utils;
import logic.payment.util.IabHelper;

public class App extends Application {

    private static Context mContext;
    public static int APP_BUILDS;
    public static boolean USER_PRO;
    public static IabHelper PAYMENT_HELPER;
    public static int SELECTED_THEME;
    public static List<String> GAME_LEVELS = new ArrayList<>();


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        asyncSetupGameLevels();
    }


    public static Context getAppCtx() {
        return mContext;
    }

    public static IabHelper getPaymentHelper() {
        return PAYMENT_HELPER;
    }

    public static void setPaymentHelper(IabHelper paymentHelper) {
        PAYMENT_HELPER = paymentHelper;
    }

    public static void asyncSetupGameLevels() {
        new AsyncTaskWorker(
                new Callable<Void>() {
                    public Void call() {
                        GAME_LEVELS = Utils.getGameLevels();
                        return null;
                    }
                },
                null
        ).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static List<String> getGameLevels() {
        if (GAME_LEVELS.size() == 0) asyncSetupGameLevels();
        return GAME_LEVELS;
    }

    public static int getAppBuilds() {
        return APP_BUILDS != 0 ? APP_BUILDS : SharedPrefs.getSharedPreferencesInt(SharedPrefs.KEY_SP_APP_BUILDS, 0);
    }

    public static void setAppBuilds(int appBuilds) {
        APP_BUILDS = appBuilds;
        SharedPrefs.setSharedPreferencesInt(SharedPrefs.KEY_SP_APP_BUILDS, appBuilds);
    }

    public static boolean isUserPro() {
        return USER_PRO || SharedPrefs.getSharedPreferencesBool(SharedPrefs.KEY_SP_IS_USER_PRO,false);
    }

    public static void setUserPro(boolean userPro) {
        USER_PRO = userPro;
        SharedPrefs.setSharedPreferencesBool(SharedPrefs.KEY_SP_IS_USER_PRO, userPro);
    }


}