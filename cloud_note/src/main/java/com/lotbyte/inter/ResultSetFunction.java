package com.lotbyte.inter;

import java.sql.ResultSet;

/**
 * @author lp
 * @Date 2019/3/28 18:13
 * @Version 1.0
 */
@FunctionalInterface
public interface ResultSetFunction<T> {
    /**
     * 执行结果集处理
     * @param rs
     */
    public T execute(ResultSet rs) throws Exception;





}
