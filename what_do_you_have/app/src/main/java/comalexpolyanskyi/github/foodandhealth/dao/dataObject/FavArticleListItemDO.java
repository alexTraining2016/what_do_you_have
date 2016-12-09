package comalexpolyanskyi.github.foodandhealth.dao.dataObject;


import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FavArticleListItemDO extends ArticleListItemDO {

    @SerializedName("user_data_binding")
    private List<BindingUserDataDO> userDataList;

    public FavArticleListItemDO(Cursor cursor) {
        super(cursor);
    }

    public List<BindingUserDataDO> getUserDataList() {
        return userDataList;
    }
}
