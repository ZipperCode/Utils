import annotation.SystemLog;
import aspectj.LogAspect;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Date;

public class Main {

    public static void main(String[] args) {
        AA a = new A();
        B b = new B();

        System.out.println(a instanceof A);
        System.out.println(a instanceof B);
        System.out.println(b instanceof A);
        System.out.println(b instanceof B);
        System.out.println(a instanceof AA);
        System.out.println(((A)a).a);
        LogAspect logAspect = new LogAspect();

        ((A)a).aMethod();

        System.out.println(new Date().getTime());;

    }

    public static class A implements AA{
        public int a = 10;
        @SystemLog
        void aMethod(){
            System.out.println("A 方法");
        }
    }

    public static class B extends A{

    }

    public interface AA{

    }
}
