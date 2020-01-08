package reflect;

import annotation.Inject;
import annotation.NonNull;

public class Test {
    @Inject
    @NonNull
    private String string;


    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public static void main(String[] args) {
        Test test = new Test();
        InjectHandler.injectObject(test);
        System.out.println(test.getString() == null);
    }
}
