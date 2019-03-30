package com.lotbyte.inter;

import java.util.function.Predicate;
import java.util.function.Supplier;

@FunctionalInterface
public interface ThrowingPredicate<T,E extends  Exception>{
    boolean test(T t) throws E;

    static <T> Predicate<T> predicateWrapper(ThrowingPredicate<T,Exception> throwingSupplier){
        return (s)->{
            // 这里进行异常捕捉 消除lambda try catch 代码
            try {
                return  throwingSupplier.test(s);
            }catch (Exception ex){
                throw  new RuntimeException(ex);
            }
        };
    }
}
