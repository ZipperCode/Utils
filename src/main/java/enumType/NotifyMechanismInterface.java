package enumType;

import java.lang.reflect.Method;

public interface NotifyMechanismInterface<T> {
    Object invoke(T targetObject, Object[] args);
}
