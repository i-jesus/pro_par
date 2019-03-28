package com.lotbyte.inter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author lp
 * @Date 2019/3/28 19:53
 * @Version 1.0
 */
public class Test {
    public static void main(String[] args) {
        List<Integer> integers = Arrays.asList(3, 9, 7, 0, 10, 20);
        integers.forEach(i -> {
            try {
                System.out.println(50 / i);
            } catch (ArithmeticException e) {
                System.err.println(
                        "Arithmetic Exception occured : " + e.getMessage());
            }
        });

        integers.forEach(lambdaWrapper(i -> { System.out.println(50 / i); }));

        System.out.println("--------------");
        integers.forEach(consumerWrapper(i -> { System.out.println(50 / i); },Exception.class));

        List<Integer> is = Arrays.asList(3, 9, 7, 0, 10, 20);
        is.forEach(i->{
            try {
                writeToFile(i);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        is.forEach(ThrowingConsumer.consumerWrapper(i->writeToFile(i),IOException.class));

    }



    static void writeToFile(Integer integer) throws IOException {
        System.out.println("参数-->"+integer);
    }

    static Consumer<Integer> lambdaWrapper(Consumer<Integer> consumer) {
        return i -> {
            try {
                consumer.accept(i);
            } catch (ArithmeticException e) {
                System.err.println(
                        "Arithmetic Exception occured : " + e.getMessage());
            }
        };
    }


    static <T, E extends Exception> Consumer<T> consumerWrapper(Consumer<T> consumer, Class<E> clazz) {
        return i -> {
            try {
                consumer.accept(i);
            } catch (Exception ex) {
                try {
                    E exCast = clazz.cast(ex);
                    System.err.println(
                            "Exception occured : " + exCast.getMessage());
                } catch (ClassCastException ccEx) {
                    throw ex;
                }
            }
        };
    }


}
