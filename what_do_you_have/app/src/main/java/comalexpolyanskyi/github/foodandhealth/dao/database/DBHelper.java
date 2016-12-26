package comalexpolyanskyi.github.foodandhealth.dao.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.lang.reflect.AnnotatedElement;
import java.util.List;

import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.Table;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Contract;
import comalexpolyanskyi.github.foodandhealth.sql.QueryCreator;
import comalexpolyanskyi.github.foodandhealth.sql.SQLQueryCreator;

public class DBHelper extends SQLiteOpenHelper implements DbOperations {

    public DBHelper(final Context context, final String name, final int version) {
        super(context, name, null, version);
    }

    @Nullable
    public static String getTableName(final AnnotatedElement clazz) {
        final Table table = clazz.getAnnotation(Table.class);
        if (table != null) {
            return table.name();
        } else {
            return null;
        }
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        final QueryCreator creator = new SQLQueryCreator();
        for (final Class<?> clazz : Contract.MODELS) {
            final String sql = creator.getTableCreateQuery(clazz);

            if (sql != null) {
                db.execSQL(sql);
            }
        }
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
    }

    @Override
    public Cursor query(final String sql, final String... args) {
        final SQLiteDatabase database = getReadableDatabase();

        return database.rawQuery(sql, args);
    }

    @Override
    public long bulkInsert(Class<?> table, final List<ContentValues> values) {
        final String name = getTableName(table);
        if (name != null) {
            final SQLiteDatabase database = getWritableDatabase();
            int count = 0;
            try {
                database.beginTransaction();
                for (final ContentValues value : values) {
                    database.insertWithOnConflict(name, null, value, SQLiteDatabase.CONFLICT_REPLACE);
                    count++;
                }
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }
            return count;
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public void insert(final Class<?> table, final ContentValues value) {
        final String name = getTableName(table);
        if (name != null) {
            final SQLiteDatabase database = getWritableDatabase();
            try {
                database.beginTransaction();
                database.insertWithOnConflict(name, null, value, SQLiteDatabase.CONFLICT_REPLACE);
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }

        } else {
            throw new RuntimeException();
        }
    }
}