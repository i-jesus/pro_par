package com.lotbyte.inter;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * 返回ps 对象 函数式接口
 */
@FunctionalInterface
public interface PreparedCreatorFunction {
    public PreparedStatement getPs(Connection connection) throws Exception;


}
