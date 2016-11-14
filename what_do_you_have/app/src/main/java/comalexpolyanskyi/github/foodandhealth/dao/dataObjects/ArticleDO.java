package comalexpolyanskyi.github.foodandhealth.dao.dataObjects;

import android.database.Cursor;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.ArticleDescription;

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

    public ArticleDO(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndex(ArticleDescription.ID));
        this.name = cursor.getString(cursor.getColumnIndex(ArticleDescription.NAME));
        this.photo = cursor.getString(cursor.getColumnIndex(ArticleDescription.IMAGE_URI));
        this.likeCount = cursor.getInt(cursor.getColumnIndex(ArticleDescription.LIKE_COUNT));
        this.repostCount = cursor.getInt(cursor.getColumnIndex(ArticleDescription.REPOST_COUNT));
        this.description = cursor.getString(cursor.getColumnIndex(ArticleDescription.DESCRIPTION));
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
