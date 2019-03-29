package com.lotbyte.inter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * @author lp
 * @Date 2019/3/28 18:13
 * @Version 1.0
 */
@FunctionalInterface
public interface ResultSetFunction<T> {
    /**
     * 执行结果集处理
     */
    public ResultSet execute(PreparedStatement ps) throws Exception;
}
