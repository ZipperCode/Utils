package export.base;

import java.lang.annotation.*;
@Inherited
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataFieldName {
    String value() default "";
}
