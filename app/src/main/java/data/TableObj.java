package data;

import com.google.gson.annotations.SerializedName;

public class TableObj {

    @SerializedName("t_id")
    private String tabId;

    @SerializedName("t_name")
    private String tabName;

    @SerializedName("t_descr")
    private String tabDescr;

     @SerializedName("t_lvls")
    private LevelObj[] tabLevels;

    @SerializedName("t_size")
    private int tabSize;

    @SerializedName("sort_o")
    private int sortOrder;


    public String getTabId() {
        return tabId;
    }

    public void setTabId(String tabId) {
        this.tabId = tabId;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public String getTabDescr() {
        return tabDescr;
    }

    public void setTabDescr(String tabDescr) {
        this.tabDescr = tabDescr;
    }

    public LevelObj[] getTabLevels() {
        return tabLevels;
    }

    public void setTabLevels(LevelObj[] tabLevels) {
        this.tabLevels = tabLevels;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public int getTabSize() {
        return tabSize;
    }

    public void setTabSize(int tabSize) {
        this.tabSize = tabSize;
    }
}