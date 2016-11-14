package comalexpolyanskyi.github.foodandhealth.dao.dataObjects;

import android.database.Cursor;
import com.google.gson.annotations.SerializedName;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Article;

public class ArticleListItemDO {

    public ArticleListItemDO(Cursor cursor){
        this.id = cursor.getInt(cursor.getColumnIndex(Article.ID));
        this.name = cursor.getString(cursor.getColumnIndex(Article.NAME));
        this.photo = cursor.getString(cursor.getColumnIndex(Article.IMAGE_URI));
    }

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("photo")
    private String photo;

    @SerializedName("type")
    private int type;

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
