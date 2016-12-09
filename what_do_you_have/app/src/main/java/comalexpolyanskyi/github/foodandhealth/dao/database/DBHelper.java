package comalexpolyanskyi.github.foodandhealth.dao.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.Table;
import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.dbAutoInteger;
import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.dbInteger;
import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.dbLong;
import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.dbString;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.CachedTable;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Contract;

public class DBHelper extends SQLiteOpenHelper implements DbOperations {

    private static final String SQL_TABLE_CREATE_TEMPLATE = "CREATE TABLE IF NOT EXISTS %s (%s);";
    private static final String SQL_TABLE_CREATE_FIELD_TEMPLATE = "%s %s";

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

    @Nullable
    private static String getTableCreateQuery(final Class<?> clazz) {
        final Table table = clazz.getAnnotation(Table.class);

        if (table != null) {
            try {
                final String name = table.name();
                final StringBuilder builder = new StringBuilder();
                final Field[] fields = clazz.getFields();

                for (int i = 0; i < fields.length; i++) {
                    final Field field = fields[i];
                    final Annotation[] annotations = field.getAnnotations();
                    String type = null;

                    for (final Annotation annotation : annotations) {
                        if (annotation instanceof dbInteger) {
                            type = ((dbInteger) annotation).value();
                        } else if (annotation instanceof dbAutoInteger) {
                            type = ((dbAutoInteger) annotation).value();
                        } else if (annotation instanceof dbLong) {
                            type = ((dbLong) annotation).value();
                        } else if (annotation instanceof dbString) {
                            type = ((dbString) annotation).value();
                        }
                    }

                    if (type == null) {
                        continue;
                    }
                    final String value = (String) field.get(null);

                    if (value.equals(CachedTable.ID)) {
                        type += " unique";
                    }
                    builder.append(String.format(Locale.US, SQL_TABLE_CREATE_FIELD_TEMPLATE, value, type));

                    if (i < fields.length - 2) {
                        builder.append(",");
                    }
                }

                return String.format(Locale.US, SQL_TABLE_CREATE_TEMPLATE, name, builder);
            } catch (final Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        for (final Class<?> clazz : Contract.MODELS) {
            final String sql = getTableCreateQuery(clazz);

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
    public long bulkUpdate(Class<?> table, final List<ContentValues> values) {
        final String name = getTableName(table);
        if (name != null) {
            final SQLiteDatabase database = getWritableDatabase();
            int count = 0;
            try {
                database.beginTransaction();
                for (final ContentValues value : values) {
                    database.insertWithOnConflict(name, null, value, SQLiteDatabase.CONFLICT_IGNORE);
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
    public void updateForParam(Class<?> table, ContentValues values, String wClause, String[] wArg) {
        final String name = getTableName(table);
        if (name != null) {
            final SQLiteDatabase database = getWritableDatabase();
            try {
                database.beginTransaction();
                database.updateWithOnConflict(name, values, wClause, wArg, SQLiteDatabase.CONFLICT_REPLACE);
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public int delete(final Class<?> table, final String sql, final String... args) {
        final String name = getTableName(table);
        if (name != null) {
            final SQLiteDatabase database = getWritableDatabase();
            int count = 0;
            try {
                database.beginTransaction();
                count = database.delete(name, sql, args);
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }
            return count;
        } else {
            throw new RuntimeException();
        }
    }
}