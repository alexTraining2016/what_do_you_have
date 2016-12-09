package comalexpolyanskyi.github.foodandhealth.dao.database.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface dbAutoInteger {

    String value() default "INTEGER PRIMARY KEY";

}