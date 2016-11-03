package comalexpolyanskyi.github.foodandhealth.dao.dataObjects;

import com.google.gson.annotations.SerializedName;

public class ArticleListItemDO {

    public ArticleListItemDO(int id, String name, String photo) {
        this.id = id;
        this.name = name;
        this.photo = photo;
    }

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("photo")
    private String photo;

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
