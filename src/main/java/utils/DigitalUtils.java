package utils;

import java.util.Random;

public class DigitalUtils {

    public static Random random = new Random();

    public static int getRandomDigital(int start, int end){
        return (int) (start + (Math.random() * (end - start)));
    }


    public static int getRangeDigital(int rand,int offset){
        int i = random.nextInt() % offset;
        return rand + i;
    }

    public static double getRandomPI(){
        return random.nextBoolean() ? -Math.PI : Math.PI;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(getRangeDigital(100,5));
        }
    }
}
