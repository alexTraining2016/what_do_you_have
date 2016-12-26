package comalexpolyanskyi.github.foodandhealth.sql;

import android.support.annotation.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Locale;

import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.Table;
import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.dbInteger;
import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.dbLong;
import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.dbString;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.CachedTable;

public class SQLQueryCreator implements QueryCreator {

    @Nullable
    @Override
    public String getTableCreateQuery(Class<?> clazz) {
        final Table table = clazz.getAnnotation(Table.class);

        if (table != null) {
            try {
                final String name = table.name();
                final StringBuilder builder = new StringBuilder();
                final Field[] fields = clazz.getFields();

                for (int i = 0; i < fields.length; i++) {
                    final Field field = fields[i];
                    String type = getType(field.getAnnotations());

                    if (type == null) {
                        continue;
                    }
                    final String value = (String) field.get(null);

                    if (value.equals(CachedTable.ID)) {
                        type += SQLConstants.UNIQUE;
                    }
                    builder.append(String.format(Locale.US, SQLConstants.SQL_TABLE_CREATE_FIELD_TEMPLATE, value, type));

                    if (i < (fields.length - 1)) {
                        builder.append(",");
                    }
                }

                if (builder.substring(builder.length() - 1).equals(",")) {
                    builder.deleteCharAt(builder.length() - 1);
                }

                return String.format(Locale.US, SQLConstants.SQL_TABLE_CREATE_TEMPLATE, name, builder);
            } catch (final Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public String getSelectCreateQuery(Class<?> clazz, String template) {
        final Table table = clazz.getAnnotation(Table.class);

        if (table != null) {
            try {
                final StringBuilder builder = new StringBuilder();
                final Field[] fields = clazz.getFields();

                for (int i = 0; i < fields.length; i++) {
                    final Field field = fields[i];
                    final String type = getType(field.getAnnotations());

                    if (type == null) {
                        continue;
                    }

                    final String value = (String) field.get(null);
                    builder.append(String.format(Locale.US, template, value));

                    if (i < (fields.length - 1)) {
                        builder.append(",");
                    }
                }

                if (builder.substring(builder.length() - 1).equals(",")) {
                    builder.deleteCharAt(builder.length() - 1);
                }

                return builder.toString();
            } catch (final Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    private String getType(Annotation[] annotations) {
        for (final Annotation annotation : annotations) {
            if (annotation instanceof dbInteger) {
                return ((dbInteger) annotation).value();
            } else if (annotation instanceof dbLong) {
                return ((dbLong) annotation).value();
            } else if (annotation instanceof dbString) {
                return ((dbString) annotation).value();
            }
        }
        return null;
    }
}
