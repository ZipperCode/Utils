package thread;

import manager.ThreadManager;

public class Main {
    public static void main(String[] args) {

        class Task1 implements Runnable{

            private Callback callback;

            public Task1(Callback callback) {
                this.callback = callback;
            }

            @Override
            public void run() {
//                System.out.println("代码执行前");
//                System.out.println("代码执行中");
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                System.out.println("代码执行后");
//                callback.onSuccess("hello 代码执行完了");
                int a = 1/0;
            }
        }

//        ThreadManager.executeNetWorkRunnable(()->{
//            System.out.println("代码执行前");
//            System.out.println("代码执行中");
//            int a = 1/0;
//            System.out.println("代码执行后");
//        });
//        ThreadManager.executeFileRunnable(()->{
//            System.out.println("代码执行前");
//            System.out.println("代码执行中");
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            int a = 1/0;
//            System.out.println("代码执行后");
//        });
//        ThreadManager.executeFileRunnable(()->{
//            System.out.println("代码执行前");
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println("代码执行中");
//            int a = 1/0;
//            System.out.println("代码执行后");
//        });
//        ThreadManager.executeFileRunnable(()->{
//            System.out.println("代码执行前");
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println("代码执行中");
//            int a = 1/0;
//            System.out.println("代码执行后");
//        });
//        ThreadManager.executeFileRunnable(()->{
//            System.out.println("代码执行前");
//            System.out.println("代码执行中");
//            int a = 1/0;
//            System.out.println("代码执行后");
//        });

    }
    interface Callback{
        void onSuccess(String result);
    }
}
