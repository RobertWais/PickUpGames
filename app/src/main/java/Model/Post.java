package Model;

/**
 * Created by robertwais on 4/4/18.
 *
 * Class to store the Posts retrieved from Firebase
 */

public class Post {
    private int attending;
    private int comments;
    private String description;
    private String time;
    private String title;
    private String postID;

    public Post(int attending, int comments, String description, String time, String title) {
        this.attending = attending;
        this.comments = comments;
        this.description = description;
        this.time = time;
        this.title = title;
    }
    public Post(){}

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    //GETTERS
    public int getAttending() {
        return attending;
    }

    public int getComments() {
        return comments;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    //SETTERS
    public void setAttending(int attending) {
        this.attending = attending;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
