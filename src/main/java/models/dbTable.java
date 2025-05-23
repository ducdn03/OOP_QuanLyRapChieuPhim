package models;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface dbTable {
    public String columnName();
    boolean autoIncrement() default false;
}
