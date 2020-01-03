package export.base;

import java.lang.annotation.*;

@Inherited // 注解是否可以被继承
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataName {
    String value() default "";
    boolean hasIndex() default false; // 是否需要序号
}
