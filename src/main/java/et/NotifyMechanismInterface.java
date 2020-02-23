package et;

public interface NotifyMechanismInterface<T> {
    Object invoke(T targetObject, Object[] args);
}
