package reflect;

import annotation.Inject;

import java.lang.reflect.Field;

public class InjectHandler {

    public static void injectObject(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                if(field.isAnnotationPresent(Inject.class)){
                    Inject inject = field.getAnnotation(Inject.class);
                    if (inject != null) {
                        field.setAccessible(true);
                        Object fieldObject = field.getType().getDeclaredConstructor().newInstance();
                        field.set(object,fieldObject);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
