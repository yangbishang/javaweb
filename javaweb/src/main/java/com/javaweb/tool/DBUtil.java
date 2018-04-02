package com.javaweb.tool;


import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * 数据库的管理类 (获取连接 + 关闭连接)
 *
 * @author SONG
 */
public class DBUtil {
    private static Properties props;
    // 数据源(c3p0 数据库连接池)
    private static ComboPooledDataSource dataSource;

    static {
        try {
            props = new Properties();
            InputStream in = DBUtil.class.getClassLoader().
                    getResourceAsStream("jdbc.properties");
            props.load(in);

            // 实例化数据源对象
            dataSource = new ComboPooledDataSource();
            // 设置连接数据库的参数
            dataSource.setDriverClass(props.getProperty("driver"));
            dataSource.setJdbcUrl(props.getProperty("url"));
            dataSource.setUser(props.getProperty("username"));
            dataSource.setPassword(props.getProperty("password"));

            // 连接池的参数
            dataSource.setMinPoolSize(4);       // 最少连接个数
            dataSource.setMaxPoolSize(20);      // 最大连接个数
            dataSource.setInitialPoolSize(8);   // 初始化连接个数
            dataSource.setCheckoutTimeout(1800);// 连接超时(秒)
            dataSource.setAcquireIncrement(4);  // 自动增长
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库连接
     *
     * @return
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 关闭数据库连接
     *
     * @param conn
     * @param ps
     * @param rs
     */
    public static void close(Connection conn, PreparedStatement ps, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
