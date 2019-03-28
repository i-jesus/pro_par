package com.lotbyte.inter;

import java.sql.Connection;

@FunctionalInterface
public interface ConnectionFunction<T,R,H,Z> {
    Z get(T t,R r,H h);
}
