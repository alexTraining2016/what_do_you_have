package comalexpolyanskyi.github.foodandhealth.dao.dataObjects;

import com.google.gson.annotations.SerializedName;

public class ArticleListItemDO {

    public ArticleListItemDO(int id, String name, String photo, int type) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.type = type;
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
