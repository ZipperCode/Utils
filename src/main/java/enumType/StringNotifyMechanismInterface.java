package enumType;

public interface StringNotifyMechanismInterface extends NotifyMechanismInterface<Controller> {
    static StringNotifyMechanismInterface doA(){
        return ((targetObject, args) -> {
            System.out.println(targetObject + " - doA()");
            DoMain doMains = (DoMain) args[0];
            return targetObject.getServiceA().service(doMains);
        });
    }

    static StringNotifyMechanismInterface doB(){
        return (targetObject, args) -> {
            System.out.println(targetObject + " - doB()");
            DoMain doMains = (DoMain) args[0];
            return targetObject.getServiceB().service(doMains);
        };
    }

    static StringNotifyMechanismInterface doC(){
        return ((targetObject, args) -> {
            System.out.println(targetObject + " - doC()");
            DoMain doMains = (DoMain) args[0];
            int flag = 0;
            flag += targetObject.getServiceA().service(doMains);
            flag += targetObject.getServiceB().service(doMains);
            return flag;
        });
    }
}
