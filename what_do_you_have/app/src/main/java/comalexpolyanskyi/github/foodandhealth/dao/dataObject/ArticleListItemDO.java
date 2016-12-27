package comalexpolyanskyi.github.foodandhealth.dao.dataObject;

import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Article;

public class ArticleListItemDO {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("photo")
    private String photo;

    @SerializedName("type")
    private int type;

    @SerializedName("kind_id")
    private String kind;

    public ArticleListItemDO(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndex(Article.ID));
        this.name = cursor.getString(cursor.getColumnIndex(Article.NAME));
        this.photo = cursor.getString(cursor.getColumnIndex(Article.IMAGE_URI));
    }

    public String getKind() {
        return kind;
    }

    public int getType() {
        return type;
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
