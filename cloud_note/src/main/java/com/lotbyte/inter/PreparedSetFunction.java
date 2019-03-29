package com.lotbyte.inter;

import java.sql.PreparedStatement;

/**
 * 参数设置接口
 */
@FunctionalInterface
public interface PreparedSetFunction {
    void addParams(PreparedStatement ps) throws Exception;
}
