package com.lotbyte.base;

import com.lotbyte.inter.ThrowingConsumer;
import com.lotbyte.inter.ThrowingFunction;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 数据库连接类
 *
 * @author Administrator
 */
@SuppressWarnings("all")
public class DBUtil {

    private static Properties prop;

    static {
        Consumer<String> c = ThrowingConsumer.consumerWrapper((fileName)->{
            Supplier<InputStream> s = () -> DBUtil.class.getClassLoader().getResourceAsStream(fileName);
            prop = new Properties();
            prop.load(s.get());
        },Exception.class);
        c.accept("db.properties");
    }


    /**
     * 得到数据库的连接
     *
     * @return
     */
    public static Connection getConnection() {
        Function<Properties, Connection> f = (ThrowingFunction.functionWrapper(pro -> {
            Class.forName(prop.getProperty("jdbcName"));
            return DriverManager.getConnection(pro.getProperty("dbUrl")
                    ,pro.getProperty("dbUserName"),
                    pro.getProperty("dbPassword"));
        }, Exception.class));
        return f.apply(prop);
    }


    public static void main(String[] args) {
    }

    /**
     * 关闭数据库连接
     */
    public static void close(ResultSet rs
            , PreparedStatement ps, Connection conn) {
        Optional.ofNullable(rs).ifPresent(ThrowingConsumer.consumerWrapper(r -> { r.close(); },Exception.class));
        Optional.ofNullable(ps).ifPresent(ThrowingConsumer.consumerWrapper(p -> { p.close(); },Exception.class));
        Optional.ofNullable(conn).ifPresent(ThrowingConsumer.consumerWrapper(c -> { c.close(); },Exception.class));
    }


}
