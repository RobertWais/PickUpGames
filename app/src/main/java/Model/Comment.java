package Model;

/**
 * Created by robertwais on 4/19/18.
 */

public class Comment {
    private String user;
    private String comment;

    public Comment(String user, String comment) {
        this.user = user;
        this.comment = comment;
    }


    //GETTERS

    public String getUser() {
        return user;
    }

    public String getComment() {
        return comment;
    }

    //SETTERS

    public void setUser(String user) {
        this.user = user;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
