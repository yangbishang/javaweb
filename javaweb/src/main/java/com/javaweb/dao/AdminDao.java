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
            while(rs.next()) {
                admin.setUsername(rs.getString(1));
                admin.setPassword(rs.getString(2));
                admin.setId(rs.getInt(3));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return admin;
    }
    //根据传入的密码判断是否有此用户
    public String findByPassword(String password,String username){
        String cPassword = null;

        conn = DBUtil.getConnection();
        String sql = "select password from admin where password = ? and username=?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,password);
            ps.setString(2,username);
            rs = ps.executeQuery();
            while(rs.next()){
                cPassword = rs.getString(1);
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }

        return  cPassword;
    }
    //更新旧密码
    public Integer updatePassword(String oldPassword,String newPassword,String username){
        int count = 0;
        System.out.println(oldPassword+","+newPassword+","+username);
        conn = DBUtil.getConnection();
        String sql = "update admin set password=? where username=? and password = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,newPassword);
            ps.setString(2,username);
            ps.setString(3,oldPassword);

            count = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
}
