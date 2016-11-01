package comalexpolyanskyi.github.foodandhealth.models.dataObjects;

import com.google.gson.annotations.SerializedName;

public class ArticleListItemDO {

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
