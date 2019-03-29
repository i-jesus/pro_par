package com.lotbyte.inter;

import java.sql.ResultSet;

@FunctionalInterface
public interface ResultSetHandlerFunction<T> {
    public T executeHandle(ResultSet rs) throws  Exception;
}
