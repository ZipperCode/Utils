package enumType;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

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
        CompleteString.valueOf(doMain.type).getStringNotifyMechanismInterface().invoke(this,new Object[]{doMain});
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
    A("A",StringNotifyMechanismInterface.doA()),
    B("B",StringNotifyMechanismInterface.doB()),
    C("C",StringNotifyMechanismInterface.doC());

    private String string;
    private StringNotifyMechanismInterface stringNotifyMechanismInterface;

    CompleteString(String string, StringNotifyMechanismInterface stringNotifyMechanismInterface) {
        this.string = string;
        this.stringNotifyMechanismInterface = stringNotifyMechanismInterface;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public StringNotifyMechanismInterface getStringNotifyMechanismInterface() {
        return stringNotifyMechanismInterface;
    }

    public void setStringNotifyMechanismInterface(StringNotifyMechanismInterface stringNotifyMechanismInterface) {
        this.stringNotifyMechanismInterface = stringNotifyMechanismInterface;
    }
}

