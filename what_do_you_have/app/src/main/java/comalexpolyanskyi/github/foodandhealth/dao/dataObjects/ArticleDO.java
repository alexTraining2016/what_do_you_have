package comalexpolyanskyi.github.foodandhealth.dao.dataObjects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ArticleDO implements Serializable {

    @SerializedName("like")
    private int likeCount;

    @SerializedName("repost_count")
    private int repostCount;

    @SerializedName("description")
    private String description;

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("photo")
    private String photo;

    @SerializedName("type")
    private int type;

    public ArticleDO(int id, String name,  String description, String photo, int likeCount, int repostCount) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.likeCount = likeCount;
        this.repostCount = repostCount;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public int getFavCount() {
        return repostCount;
    }

    public int getType() {
        return  type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhotoUrl() {
        return photo;
    }
}
