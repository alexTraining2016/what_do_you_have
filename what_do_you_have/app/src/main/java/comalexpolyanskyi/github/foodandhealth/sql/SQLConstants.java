package comalexpolyanskyi.github.foodandhealth.sql;

public interface SQLConstants {

    String SELECT = "SELECT ";
    String FROM = " FROM ";
    String S_F = "SELECT * FROM ";
    String WHERE = " WHERE ";
    String INNER_JOIN = " INNER JOIN ";
    String GROUP_BY = " GROUP BY ";
    String ON = " ON ";
    String IN = " IN ";
    String AND = " AND ";
    String LIKE = " LIKE ";
    String ORDER_BY = " ORDER BY ";
    String ASC = " ASC";
    String EQ = " = ";
    String SQL_TABLE_CREATE_TEMPLATE = "CREATE TABLE IF NOT EXISTS %s (%s);";
    String SQL_TABLE_CREATE_FIELD_TEMPLATE = "%s %s";
    String UNIQUE = " unique ";
    String SQL_ARTICLE_SELECT = "a.%s";
    String SQL_FAVORITES_SELECT = "f.%s";
}
