package export.base;

public class Test{
    private String a = null;
    protected String b = null;
    public String c = null;

    public Test() {
    }

    public Test(String a, String b, String c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    private void methodA(){
        System.out.println("methodA");
    }
    protected void methodB(){
        System.out.println("methodB");
    }
    public void methodC(){
        System.out.println("methodC");
    }

    @Override
    public String toString() {
        return "Test{" +
                "a='" + a + '\'' +
                ", b='" + b + '\'' +
                ", c='" + c + '\'' +
                '}';
    }
}