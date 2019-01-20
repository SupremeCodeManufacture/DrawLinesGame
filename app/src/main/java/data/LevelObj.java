package data;

import com.google.gson.annotations.SerializedName;

public class LevelObj {

    @SerializedName("lvl_id")
    private String levelId;

    @SerializedName("lvl_name")
    private String levelName;

     @SerializedName("p_cdns")
    private String[] parentCoordonates;

    @SerializedName("lvl_routes")
    private RouteObj[] routeObjs;

    @SerializedName("sort_o")
    private int sortOrder;

    private String tabName;

    private int tableSize;

    private boolean passed;


    public String getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String[] getParentCoordonates() {
        return parentCoordonates;
    }

    public void setParentCoordonates(String[] parentCoordonates) {
        this.parentCoordonates = parentCoordonates;
    }

    public RouteObj[] getRouteObjs() {
        return routeObjs;
    }

    public void setRouteObjs(RouteObj[] routeObjs) {
        this.routeObjs = routeObjs;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public int getTableSize() {
        return tableSize;
    }

    public void setTableSize(int tableSize) {
        this.tableSize = tableSize;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }
}