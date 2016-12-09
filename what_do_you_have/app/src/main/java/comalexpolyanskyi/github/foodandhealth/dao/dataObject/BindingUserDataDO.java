package comalexpolyanskyi.github.foodandhealth.dao.dataObject;


import com.google.gson.annotations.SerializedName;

public class BindingUserDataDO {

    @SerializedName("uid")
    private int id;

    @SerializedName("recepte_id")
    private int artId;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("isLike")
    private int isLike;

    @SerializedName("isRepost")
    private int isRepost;

    public int getId() {
        return id;
    }

    public int getArtId() {
        return artId;
    }

    public int getUserId() {
        return userId;
    }

    public int getIsLike() {
        return isLike;
    }

    public int getIsRepost() {
        return isRepost;
    }
}
