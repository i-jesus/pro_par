package com.lotbyte.inter;

import java.util.function.Function;

public interface ThrowingFunction<T,R,E extends  Exception> {
    R apply(T t) throws E;


    static <T,R,E extends  Exception> Function<T,R> functionWrapper(
            ThrowingFunction<T,R,Exception> throwingFunction, Class<E> eClass){
        return (t)->{
            // 这里进行异常捕捉 消除lambda try catch 代码
            try {
              return  throwingFunction.apply(t);
            }catch (Exception ex){
                throw new RuntimeException(ex);
            }
        };
    }



}
