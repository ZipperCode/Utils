import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

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

    }

    public static class A implements AA{
        public int a = 10;
    }

    public static class B extends A{

    }

    public interface AA{

    }
}
