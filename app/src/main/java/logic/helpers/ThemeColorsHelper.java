package logic.helpers;

import com.SupremeManufacture.DrawLines.R;

import data.App;

public class ThemeColorsHelper {


    public static int getBoardColor() {
        return App.getAppCtx().getResources().getColor(R.color.primary_light);
    }
}