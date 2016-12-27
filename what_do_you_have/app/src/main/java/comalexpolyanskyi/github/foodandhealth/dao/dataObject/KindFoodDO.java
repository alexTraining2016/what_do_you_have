package comalexpolyanskyi.github.foodandhealth.dao.dataObject;

import com.google.gson.annotations.SerializedName;

public class KindFoodDO {

    @SerializedName("id")
    private int id;

    @SerializedName("type_name")
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {

        return name;
    }

}
