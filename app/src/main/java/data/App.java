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
    public static boolean OLD_PAID_ADS;
    public static boolean PAID_ADS;
    public static boolean PAID_UNLOCK_LVLS;
    public static boolean PAID_FULL;
    public static IabHelper PAYMENT_HELPER;
    public static int SELECTED_THEME;
    public static List<String> GAME_LEVELS = new ArrayList<>();
    public static long FIRST_LAUNCH_MILIS;


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

    public static boolean isOldPaidAds() {
        return OLD_PAID_ADS || SharedPrefs.getSharedPreferencesBool(SharedPrefs.KEY_SP_IS_PAID_OLD_ADS, false);
    }

    public static void setOldPaidAds(boolean oldPaidAds) {
        OLD_PAID_ADS = oldPaidAds;
        SharedPrefs.setSharedPreferencesBool(SharedPrefs.KEY_SP_IS_PAID_OLD_ADS, oldPaidAds);
    }

    public static boolean isPaidAds() {
        return PAID_ADS || SharedPrefs.getSharedPreferencesBool(SharedPrefs.KEY_SP_IS_PAID_ADS, false);

    }

    public static void setPaidAds(boolean paidAds) {
        PAID_ADS = paidAds;
        SharedPrefs.setSharedPreferencesBool(SharedPrefs.KEY_SP_IS_PAID_ADS, paidAds);
    }

    public static boolean isPaidUnlockLvls() {
        return PAID_UNLOCK_LVLS || SharedPrefs.getSharedPreferencesBool(SharedPrefs.KEY_SP_IS_PAID_UNLOCK_LVLS, false);
    }

    public static void setPaidUnlockLvls(boolean paidUnlockLvls) {
        PAID_UNLOCK_LVLS = paidUnlockLvls;
        SharedPrefs.setSharedPreferencesBool(SharedPrefs.KEY_SP_IS_PAID_UNLOCK_LVLS, paidUnlockLvls);
    }

    public static boolean isPaidFull() {
        return PAID_FULL || SharedPrefs.getSharedPreferencesBool(SharedPrefs.KEY_SP_IS_PAID_FULL, false);
    }

    public static void setPaidFull(boolean paidFull) {
        PAID_FULL = paidFull;
        SharedPrefs.setSharedPreferencesBool(SharedPrefs.KEY_SP_IS_PAID_FULL, paidFull);
    }

    public static long getFirstLaunchMilis() {
        return FIRST_LAUNCH_MILIS != 0 ? FIRST_LAUNCH_MILIS : SharedPrefs.getSharedPreferencesLong(SharedPrefs.KEY_FIRST_LAUNCH, 0);
    }

    public static void setFirstLaunchMilis(long firstLaunchMilis) {
        FIRST_LAUNCH_MILIS = firstLaunchMilis;
        SharedPrefs.setSharedPreferencesLong(SharedPrefs.KEY_FIRST_LAUNCH, firstLaunchMilis);
    }
}