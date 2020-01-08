package annotation;

import java.lang.annotation.*;

/**
 * 运行时校验不可空字段不为空，注意与@NonNULL的编译期前验证参数注解不同
 */
@Inherited
@Target({ElementType.FIELD,ElementType.METHOD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NonNull {
}
