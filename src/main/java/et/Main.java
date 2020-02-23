package et;

public class Main {
    public static void main(String[] args) {
        String a = "B";
        String content = "hahahha";
        DoMain doMainA = new DoMain("A", "AAA");
        DoMain doMainB = new DoMain("B", "BBB");
        DoMain doMainC = new DoMain("C", "CCC");
        method(doMainC);
    }
    public static void method(DoMain doMain){
        ServiceA serviceA = new ServiceA();
        ServiceB serviceB = new ServiceB();
        Controller controller = new Controller(serviceA,serviceB);
        controller.method(doMain);
    }
}

class Controller{
    private ServiceA serviceA;
    private ServiceB serviceB;
    private String name = "hahah";

    public void method(DoMain doMain){
        CompleteString.valueOf(doMain.type).getTestNotifyMechanismInterface().invoke(this,new Object[]{doMain});
    }

    public Controller(ServiceA serviceA, ServiceB serviceB) {
        this.serviceA = serviceA;
        this.serviceB = serviceB;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ServiceA getServiceA() {
        return serviceA;
    }

    public ServiceB getServiceB() {
        return serviceB;
    }
}

class ServiceA{
    public int service(DoMain doMain){
        System.out.println("serviceA method" + doMain.name);
        return 1;
    }
}
class ServiceB{
    public int service(DoMain doMain){
        System.out.println("serviceB method " + doMain.name);
        return 1;
    }
}
class DoMain{
    String type;
    String name;

    public DoMain(String type, String name) {
        this.type = type;
        this.name = name;
    }

}

enum CompleteString{
    A("A", TestNotifyMechanismInterface.doA()),
    B("B", TestNotifyMechanismInterface.doB()),
    C("C", TestNotifyMechanismInterface.doC());

    private String string;
    private TestNotifyMechanismInterface testNotifyMechanismInterface;

    CompleteString(String string, TestNotifyMechanismInterface testNotifyMechanismInterface) {
        this.string = string;
        this.testNotifyMechanismInterface = testNotifyMechanismInterface;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public TestNotifyMechanismInterface getTestNotifyMechanismInterface() {
        return testNotifyMechanismInterface;
    }

    public void setTestNotifyMechanismInterface(TestNotifyMechanismInterface testNotifyMechanismInterface) {
        this.testNotifyMechanismInterface = testNotifyMechanismInterface;
    }
}

