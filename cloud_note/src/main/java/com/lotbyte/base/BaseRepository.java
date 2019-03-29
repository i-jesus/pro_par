package com.lotbyte.base;

import com.lotbyte.inter.PreparedCreatorFunction;
import com.lotbyte.inter.PreparedSetFunction;
import com.lotbyte.inter.ResultSetFunction;
import com.lotbyte.util.StringUtil;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;
import java.util.stream.IntStream;

/**
 * @author lp
 * @Date 2019/3/28 18:15
 * @Version 1.0
 */
@SuppressWarnings("all")
public interface BaseRepository<T> {
    public Logger logger = Logger.getLogger(BaseRepository.class);
    /**
     * 数据更新操作
     *
     * @param sql
     * @param params
     * @return
     */
    public static int update(String sql, Object... params) {
        return update(connection -> DBUtil.getConnection().prepareStatement(sql), (ps) -> {
            logger.info("更新sql-->"+sql);
            Optional.ofNullable(params).ifPresent((p) -> {
                int cnt = 0;
                for (Object obj : params) {
                    try {
                        cnt +=1;
                        ps.setObject(cnt, obj);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        });
    }


    public static int update(PreparedCreatorFunction pcf, PreparedSetFunction psf) {
        Connection conn = null;
        PreparedStatement ps = null;
        int row = 0;
        try {
            conn = DBUtil.getConnection();
            ps = pcf.getPs(conn);
            // 添加参数操作
            psf.addParams(ps);
            // 执行更新操作
            row = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            DBUtil.close(null, ps, conn);
        }
        return row;
    }


    /**
     * 统计处理情况
     * @param sql
     * @param params
     * @return
     */
    public  default  Object querySingValue(String sql,Object... params){
        return query(connection -> DBUtil.getConnection().prepareStatement(sql), (ps) -> {
            logger.info("查询单条记录sql-->"+sql);
            Optional.ofNullable(params).ifPresent((p) -> {
                int cnt = 0;
                for (Object obj : params) {
                    try {
                        cnt +=1;
                        ps.setObject(cnt, obj);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
            return ps.executeQuery();
        });
    }


    /**
     * 查詢单个对象  返回Optinal 客户端调用防止空指针异常
     * @param sql
     * @param clazz
     * @param params
     * @return
     */
    public default Optional<T> queryObject(String sql, Class<T> clazz, Object... params) {
        Optional<List<T>> results = queryRows(sql, clazz, params);
        //System.out.println("单条记录查询结果:"+results.get().get(0));
        return results.isPresent()?Optional.of(results.get().get(0)):Optional.empty();
    }


    /**
     * 查询列表   返回Optional 防止空指针
     * @param sql
     * @param clazz
     * @param params
     * @return
     */
    public default Optional<List<T>> queryRows(String sql, Class clazz, Object... params) {
        return query(connection -> DBUtil.getConnection().prepareStatement(sql), (ps) -> {
            logger.info("查询列表sql-->"+sql);
            Optional.ofNullable(params).ifPresent((p) -> {
                int cnt = 0;
                for (Object obj : params) {
                    try {
                        cnt += 1;
                        System.out.println("设置参数-->"+obj);
                        ps.setObject(cnt, obj);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
            return ps.executeQuery();
        }, clazz);
    }


    public default Optional<List<T>> query(PreparedCreatorFunction pcf, ResultSetFunction rsf, Class clz) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list =null;
        try {
            conn = DBUtil.getConnection();
            ps = pcf.getPs(conn);
            rs=rsf.execute(ps);
            ResultSetMetaData rsmd = rs.getMetaData();
            // 一共查询了几个字段， 一共要设置几个属性
            int fildNum = rsmd.getColumnCount();
            list =new ArrayList();
            while (rs.next()) {
                // 创建一个对象
                Object obj = clz.newInstance();
                ResultSet finalRs = rs;
                IntStream.rangeClosed(0, fildNum - 1).forEach((i) -> {
                    try {
                        // 获取属性的名字， 字段的名字（别名）
                        String fieldName = rsmd.getColumnLabel(i + 1);
                        // 通过反射，通过指定列名得到实体类中的具体属性，获取某个属性 name
                        Field fild = clz.getDeclaredField(fieldName);
                        // 通过反射，拼接实体类中的set方法，并设置指定类型， set + 列名（列名首字母大写） name setName  fild.getType()：属性的类型
                        Method method = clz.getDeclaredMethod("set" + StringUtil.firstChar2Upper(fieldName),
                                fild.getType());
                        // 将method放到newInstance()之后的对象中，并且将列名对应的结果设置到方法中
                        method.invoke(obj, finalRs.getObject(fieldName));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                list.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list.size()>0?Optional.of(list):Optional.empty();
    }


    /**
     * 统计操作
     * @param pcf
     * @param rsf
     * @return
     */
    public default Object query(PreparedCreatorFunction pcf, ResultSetFunction rsf) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Object result =null;
        try {
            conn = DBUtil.getConnection();
            ps = pcf.getPs(conn);
            rs=rsf.execute(ps);
            // 解析结果集
            if (rs.next()) {
                result = rs.getObject(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
       return  result;
    }


}
