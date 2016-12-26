package comalexpolyanskyi.github.foodandhealth.sql;


public interface QueryCreator {

    String getTableCreateQuery(final Class<?> clazz);

    String getSelectCreateQuery(final Class<?> clazz, final String template);

}
