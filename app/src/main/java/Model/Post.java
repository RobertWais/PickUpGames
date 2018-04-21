package Model;

/**
 * Created by robertwais on 4/4/18.
 *
 * Class to store the Posts retrieved from Firebase
 */

public class Post {
    private String timeCreated;
    private String description;
    private String time;
    private String title;
    private String userID;
    private String postId;

    public Post(String timeCreated, String description, String time, String title,String userID) {
        this.timeCreated = timeCreated;
        this.description = description;
        this.time = time;
        this.title = title;
        this.userID = userID;
    }

    public Post(){}



    //GETTERS


    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public String getUserID() {
        return userID;
    }

    public String getPostId() {
        return postId;
    }

//SETTERS


    public void setPostId(String postId) {
        this.postId = postId;
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

    //EQUALS METHOD


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        if (getTimeCreated() != null ? !getTimeCreated().equals(post.getTimeCreated()) : post.getTimeCreated() != null)
            return false;
        if (getDescription() != null ? !getDescription().equals(post.getDescription()) : post.getDescription() != null)
            return false;
        if (getTime() != null ? !getTime().equals(post.getTime()) : post.getTime() != null)
            return false;
        if (getTitle() != null ? !getTitle().equals(post.getTitle()) : post.getTitle() != null)
            return false;
        if (getUserID() != null ? !getUserID().equals(post.getUserID()) : post.getUserID() != null)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = timeCreated.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + time.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + userID.hashCode();
        result = 31 * result + (postId != null ? postId.hashCode() : 0);
        return result;
    }
}
