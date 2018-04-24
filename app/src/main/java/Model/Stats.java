package Model;

/**
 * Created by robertwais on 4/24/18.
 */

public class Stats {
    private static final Stats ourInstance = new Stats();

    public static Stats getInstance() {
        return ourInstance;
    }

    private int attendingSize;
    private int commentSize;
    private Stats() {
    }

    public int getAttendingSize() {
        return attendingSize;
    }

    public int getCommentSize() {
        return commentSize;
    }

    public void setAttendingSize(int attendingSize) {
        this.attendingSize = attendingSize;
    }

    public void setCommentSize(int size) {
        this.commentSize = size;
    }
}
