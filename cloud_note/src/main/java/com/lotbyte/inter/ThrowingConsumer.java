package com.lotbyte.inter;

import java.util.function.Consumer;

@FunctionalInterface
public interface ThrowingConsumer<T,E extends  Exception> {
    void accept(T t) throws  Exception;



    // 通用异常处理静态方法
    static <T,E extends  Exception> Consumer<T> consumerWrapper(ThrowingConsumer<T,E> throwingConsumer,Class<E> eClass){
        return e->{
            // 这里进行异常捕捉 消除lambda try catch 代码
            try {
                throwingConsumer.accept(e);
            }catch (Exception ex){
                try {
                    E exCast = eClass.cast(ex);
                    System.err.println(
                            "Exception-->" + exCast.getMessage());
                } catch (ClassCastException ccEx) {
                    throw new RuntimeException(ex);
                }

            }
        };
    }
}
