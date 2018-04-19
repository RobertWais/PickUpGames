package Model;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Markus on 4/1/2018.
 */

public class ListItem {

    private String name;
    private String userName;
    private String attending;

    public ListItem(String name, String userName, int attending) {
        this.name = name;
        this.userName = userName;
        this.attending = attending + " attending";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAttending() {return attending;}

    public void setAttending(int attending) {this.attending = attending + " attending";}
}
