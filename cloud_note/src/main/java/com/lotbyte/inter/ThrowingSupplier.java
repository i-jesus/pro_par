package com.lotbyte.inter;

import java.util.function.Supplier;

public interface ThrowingSupplier<T,E extends  Exception> {
    T get() throws E;


    static <T> Supplier<T> supplierWrapper(ThrowingSupplier<T,Exception> throwingSupplier){
        return ()->{
            // 这里进行异常捕捉 消除lambda try catch 代码
            try {
              return  throwingSupplier.get();
            }catch (Exception ex){
                throw  new RuntimeException(ex);
            }
        };
    }


}
