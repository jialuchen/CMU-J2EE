/**
 * Author : Jialu Chen
 */
package databean;

import org.genericdao.PrimaryKey;

/**
 * Author :  Jialu Chen
 * Andrew ID: jialuc
 */
@PrimaryKey("id")
public class Favorite {


    private int id;
    private int userId;
    private int clickCount;
    private String url;
    private String comment;

    public Favorite() {
    }

    public Favorite(int userId, int clickCount, String url, String comment) {
        this.userId = userId;
        this.clickCount = clickCount;
        this.url = url;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
