import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class Main {

    public static void main(String[] args) {
        ReferenceQueue referenceQueue = new ReferenceQueue();
        WeakReference weakReference = new WeakReference(null,referenceQueue);
        for (int i = 0; i < 10; i++) {
            WeakReference reference = new WeakReference(new String("I="+i),referenceQueue);
        }
        System.out.println(referenceQueue);

    }
}
