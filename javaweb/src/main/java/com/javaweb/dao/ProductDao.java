package com.javaweb.dao;

import com.javaweb.pojo.Order;
import com.javaweb.pojo.OrderItem;
import com.javaweb.pojo.Product;
import com.javaweb.pojo.User;
import com.javaweb.tool.DBUtil;
import com.sun.org.apache.bcel.internal.generic.DUP;
import com.sun.org.apache.xpath.internal.operations.Or;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import javax.servlet.http.HttpSession;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ProductDao {
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;



    //查询商品
    public List<Product> findAll(Integer page, Integer pagesize, String sort, String order,String cid){
        List<Product> list = new ArrayList<Product>();
        conn = DBUtil.getConnection();
        //  select id,name,price,create_date from car where 1=1 order by price desc limit 1,5;
        String sql = "select pname,pimage,shop_price,pdesc,pid from product where 1=1 ";

        if(cid!=null&&!"".equals(cid)){
            sql += " and cid = ? ";
        }
        sql += " order by "+sort+" "+order+" limit "+page+","+pagesize;

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,cid);


            rs = ps.executeQuery();
            while(rs.next()){
                Product product = new Product();
                product.setPname(rs.getString(1));
                product.setPimage(rs.getString(2));
                product.setShop_price(rs.getDouble(3));
                product.setPdesc(rs.getString(4));
                product.setPid(rs.getString(5));
                list.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(conn, ps, rs);
        }


        return list;
    }

    //产品分类表的最大页数
    public int getMaxPage(String cid,Integer pageSize){
        int maxPage = 0;
        conn = DBUtil.getConnection();
        String sql = "select count(pid) from product where 1=1 and cid = "+cid;
        try {

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            rs.next();

            maxPage = rs.getInt(1);


        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(conn, ps, rs);
        }


        return (maxPage%pageSize != 0)?(maxPage/pageSize+1):(maxPage/pageSize+1);

    }
    //根据pid查找商品具体信息
    public Product findById(String pid){

        Product product = new Product();
        conn = DBUtil.getConnection();
        String sql = "select pid,pname,market_price,shop_price,pimage,pdate,pdesc,cid from product where pid=?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,pid);
            rs = ps.executeQuery();

            while(rs.next()) {

                product.setPid(rs.getString(1));
                product.setPname(rs.getString(2));
                product.setMarket_price(rs.getDouble(3));
                product.setShop_price(rs.getDouble(4));
                product.setPimage(rs.getString(5));
                product.setPdate(rs.getDate(6));
                product.setPdesc(rs.getString(7));
                product.setCid(rs.getString(8));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(conn, ps, rs);
        }

        return product;
    }

    //向order表插入数据
    public int insertOrder(Order order){
        //时间！
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String time = sdf.format(order.getOrdertime());

        System.out.println(order);

        System.out.println(order);
        int count = 0;
        conn = DBUtil.getConnection();
        /*String sql = "insert into orders(oid,ordertime,total,state,address,telephone,uid) values(?,"+"'"+time+"'"+",?,?,?,?,?)";*/     //(?,?,?,"+"'"+time+"'"+",?)";
        String sql = "insert into orders(oid,total,state,address,telephone,uid,ordertime) values(?,?,?,?,?,?,?)";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,order.getOid());
            ps.setDouble(2,order.getTotal());
            ps.setInt(3,order.getState());
            ps.setString(4,order.getAddress());
            ps.setString(5,order.getTelephone());
            ps.setString(6,order.getUser().getUid());

            ps.setDate(7, new Date(order.getOrdertime().getTime()));

            count = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(conn, ps, rs);
        }


        return count;
    }
    //向orderItem表中插入数据
    public int insertOrderItem(OrderItem orderItem){

        int count = 0;
        conn = DBUtil.getConnection();
            String sql = "insert into orderitem(itemid,count,subtotal,pid,oid) values(?,?,?,?,?)";
            try {
                ps = conn.prepareStatement(sql);
                ps.setString(1,orderItem.getItemid());
                ps.setInt(2,orderItem.getCount());
                ps.setDouble(3,orderItem.getSubtotal());
                ps.setString(4,orderItem.getProduct().getPid());
                ps.setString(5,orderItem.getOrder().getOid());

                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                DBUtil.close(conn, ps, rs);
            }


        return count;
    }

    public List<Order> findAllOrders(String uid){
        List<Order> list = new ArrayList<Order>();

        conn = DBUtil.getConnection();
        String sql = "select oid,ordertime,total,state,address,name,telephone,uid from orders where uid=?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,uid);
            rs = ps.executeQuery();
            while(rs.next()){
                Order order = new Order();
                order.setOid(rs.getString(1));
                order.setOrdertime(rs.getDate(2));
                order.setTotal((rs.getDouble(3)));
                order.setState(rs.getInt(4));
                order.setAddress(rs.getString(5));
                order.setName(rs.getString(6));
                order.setTelephone(rs.getString(7));
                //将user存入order中
                UserDao uDao = new UserDao();
                User user = uDao.findByUid(rs.getString(8));
                order.setUser(user);

                list.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(conn, ps, rs);
        }

        return list;
    }

        public List<OrderItem> findAllOrderItemByOid(String oid){

            int count =0;     /////////////////
            List<OrderItem> orderItemsList = new ArrayList<OrderItem>();
            conn = DBUtil.getConnection();
            String sql = "select i.count,i.subtotal,p.pimage,p.pname,p.shop_price from orderitem i,product p where i.pid=p.pid and i.oid=?";
            try {
                ps = conn.prepareStatement(sql);
                ps.setString(1,oid);

                rs = ps.executeQuery();
                while(rs.next()){

                    Product product = new Product();
                    OrderItem orderItem = new OrderItem();

                    orderItem.setCount(rs.getInt(1));
                    orderItem.setSubtotal(rs.getDouble(2));
                    product.setPimage(rs.getString(3));
                    product.setPname(rs.getString(4));
                    product.setShop_price(rs.getDouble(5));
                    orderItem.setProduct(product);

                    orderItemsList.add(orderItem);
                    count++;           /////////
                    System.out.println("第"+count+"次");       ////////
                }


            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                DBUtil.close(conn, ps, rs);
            }

            return  orderItemsList;
        }

    //确认付款后，更新表单的收货人
    public int updataOrder(String address,String name,String telephone,String oid){
        int count = 0;
        conn = DBUtil.getConnection();
        String sql = "update orders set address=?,name=?,telephone=? where oid = ?";

        try {

            ps = conn.prepareStatement(sql);
            ps.setString(1,address);
            ps.setString(2,name);
            ps.setString(3,telephone);
            ps.setString(4,oid);

            ps.executeUpdate();

            System.out.println(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return count;
    }

}
