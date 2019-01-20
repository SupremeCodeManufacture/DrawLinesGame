package data;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;

public class RouteObj {

    @SerializedName("_id")
    private String id;

    //local only
    private LinkedList<String> tempRouteCoordonates;


    public RouteObj(String id) {
        this.id = id;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LinkedList<String> getTempRouteCoordonates() {
        return tempRouteCoordonates;
    }

    public void setTempRouteCoordonates(LinkedList<String> tempRouteCoordonates) {
        this.tempRouteCoordonates = tempRouteCoordonates;
    }
}