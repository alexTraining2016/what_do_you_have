package comalexpolyanskyi.github.foodandhealth.dao.database;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.List;

public interface DbOperations {
    String FOOD_AND_HEAL = "foodAndHeal";
    int VERSION = 1;

    Cursor query(String sql, String... args);

    long bulkUpdate(Class<?> table, List<ContentValues> values);

    void updateForParam(Class<?> table, ContentValues values, String wClause, String[] wArg);

    int delete(Class<?> table, String sql, String... args);

}
