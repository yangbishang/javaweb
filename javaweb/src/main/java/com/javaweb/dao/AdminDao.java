package com.javaweb.dao;

import com.javaweb.pojo.Admin;
import com.javaweb.tool.DBUtil;
import com.mysql.cj.api.mysqla.result.Resultset;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDao {
   private Connection conn;
   private PreparedStatement ps;
   private ResultSet rs;

   //用户登录时用户名和密码验证
    public Admin checkAdmin(String username,String password){
        System.out.println("adminDao 开始了");  //
        System.out.println(username+","+password);
        Admin admin = new Admin();
        conn = DBUtil.getConnection();

        String sql = "select username,password,id from admin where username=? and password = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,username);
            ps.setString(2,password);

            rs = ps.executeQuery();
            rs.next();
                admin.setUsername(rs.getString(1));
                admin.setPassword(rs.getString(2));
                admin.setId(rs.getInt(3));


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return admin;
    }
}
