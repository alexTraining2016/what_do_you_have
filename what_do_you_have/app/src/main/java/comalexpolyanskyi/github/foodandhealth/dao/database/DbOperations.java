package comalexpolyanskyi.github.foodandhealth.dao.database;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.List;

public interface DbOperations {

    Cursor query(String sql, String... args);

    long insert(Class<?> table, ContentValues values);

    int bulkInsert(Class<?> table, List<ContentValues> values);

    int delete(Class<?> table, String sql, String... args);

}
