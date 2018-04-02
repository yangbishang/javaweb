package com.javaweb.dao;

import com.javaweb.pojo.User;
import com.javaweb.tool.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public int regist(User user){
        int count = 0;
        conn = DBUtil.getConnection();
        String sql = "insert into User(uid,username,password,name,email,telephone,sex) values(?,?,?,?,?,?,?)";
        try {
            ps = conn.prepareStatement(sql);

            ps.setString(1,user.getUid());
            ps.setString(2,user.getUsername());
            ps.setString(3,user.getPassword());
            ps.setString(4,user.getName());
            ps.setString(5,user.getEmail());
            ps.setString(6,user.getTelephone());
            ps.setString(7,user.getSex());
            System.out.println(user.getUid()+" "+user.getUsername()+" "+user.getPassword()+" "+user.getName()+" "+user.getEmail()
            +" "+user.getTelephone()+" "+user.getSex());
            count = ps.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(conn, ps, rs);
        }
        return count;
    }

    public boolean login(String username,String password){
        boolean flag = false;
        conn = DBUtil.getConnection();
        String sql = "select username,password from user where username=? and password=?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,username);
            ps.setString(2,password);

            rs = ps.executeQuery();
            rs.next();
            String dataUsername = rs.getString("username");
            String dataPassword = rs.getString("password");

            if(dataUsername!=null&&dataPassword!=null){
                flag = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(conn, ps, rs);
        }


        return flag;
    }
    //根据姓名来获取user
    public User findByUser(String username){
        User user = new User();
        conn = DBUtil.getConnection();

        String sql = "select uid,username,password,name,email,telephone from user where username=?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,username);
            rs = ps.executeQuery();
            rs.next();
            user.setUid(rs.getString(1));
            user.setUsername(rs.getString(2));
            user.setPassword(rs.getString(3));
            user.setName(rs.getString(4));
            user.setEmail(rs.getString(5));
            user.setTelephone(rs.getString(6));


        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(conn, ps, rs);
        }


        return user;
    }
    //根据uid来获取user
    public User findByUid(String uid){
        User user = new User();
        conn = DBUtil.getConnection();

        String sql = "select uid,username,password,name,email,telephone from user where uid=?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,uid);
            rs = ps.executeQuery();
            rs.next();
            user.setUid(rs.getString(1));
            user.setUsername(rs.getString(2));
            user.setPassword(rs.getString(3));
            user.setName(rs.getString(4));
            user.setEmail(rs.getString(5));
            user.setTelephone(rs.getString(6));


        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(conn, ps, rs);
        }


        return user;
    }
}
