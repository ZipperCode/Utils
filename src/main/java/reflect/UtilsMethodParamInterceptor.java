package reflect;

import annotation.NonNull;
import exception.EmptyException;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.poi.ss.formula.functions.T;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class UtilsMethodParamInterceptor implements MethodInterceptor {

    private Class target;

    public Object getInstance(Class<T> target){
        this.target = target;
        Enhancer enhancer = new Enhancer();
        // 设置代理对象
        enhancer.setSuperclass(target);
        // 设置单一回调，在调用中拦截目标方法调用
        enhancer.setCallback(this);
        // 设置类加载器
        enhancer.setClassLoader(target.getClassLoader());
        return enhancer.create();
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++){
            if(parameters[i].isAnnotationPresent(NonNull.class)
                && (args[i] == null || "".equals(args[i]))){
                throw new EmptyException();
            }
        }
        return true;
    }
}
