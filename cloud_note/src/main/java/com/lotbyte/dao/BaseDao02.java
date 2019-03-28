package com.lotbyte.dao;

import com.lotbyte.inter.ResultSetFunction;
import com.lotbyte.po.User;
import com.lotbyte.util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author lp
 * @Date 2019/3/28 18:15
 * @Version 1.0
 */
public class BaseDao02 {


    public static int executeUpdate (String sql, Object... params){
        return 0;
    }


    public static Object query(Connection conn, String sql, ResultSetFunction rsf, Object... params) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            int cnt = 0;
            // 增强for 设置参数值
            for (Object param : params) {
                ps.setObject(++cnt, param);
            }
            rs = ps.executeQuery();
            return rs.next() ? rsf.execute(rs) : null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            DBUtil.close(Optional.ofNullable(rs), Optional.ofNullable(ps), Optional.ofNullable(conn));
        }
    }

    public static void main(String[] args) {
        String sql = "select * from tb_user where uname = ? and upwd = ?";
        System.out.println(query(DBUtil.getConnection(), sql, (rs) -> {
            final User user = new User();
            try {
                user.setHead(rs.getString("head"));
                user.setMood(rs.getString("mood"));
                user.setNick(rs.getString("nick"));
                user.setUname(rs.getString("uname"));
                user.setUpwd(rs.getString("upwd"));
                user.setUserId(rs.getInt("userId"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return user;
        }, "shsxt", "4QrcOUm6Wau+VuBX8g+IPg=="));
    }

}
