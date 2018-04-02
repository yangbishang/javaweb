package com.javaweb.servlet;

import com.javaweb.dao.ProductDao;
import com.javaweb.dao.UserDao;
import com.javaweb.pojo.*;
import com.javaweb.tool.CommonsUtils;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class ProductServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        ProductDao pDao = new ProductDao();
        String paction = request.getParameter("paction");

        //主页查找分类产品
        if(paction.equals("allProduct")) {
            String cid = request.getParameter("cid");

            String spageno = request.getParameter("pageno");
            spageno = (spageno == null || "".equals(spageno)) ? "1" : spageno;
            Integer pageno = Integer.parseInt(spageno);

            String spagesize = request.getParameter("pagesize");
            spagesize = (spagesize == null || "".equals(spagesize)) ? "5" : spagesize;
            Integer pagesize = Integer.parseInt(spagesize);

            String sort = request.getParameter("sort");
            sort = (sort == null || "".equals(sort)) ? "pid" : sort;

            String order = request.getParameter("order");
            order = (order == null || "".equals(order)) ? "asc" : order;

            Integer maxpage = pDao.getMaxPage(cid, pagesize);
            pageno = (pageno == 0) ? 1 : pageno;
            pageno = (pageno > maxpage) ? maxpage : pageno;
            Integer page = (pageno - 1) * pagesize;

            // 存储分页参数: 页面中使用
            request.setAttribute("pageno", pageno);
            request.setAttribute("pagesize", pagesize);
            request.setAttribute("maxpage", maxpage);
            request.setAttribute("sort", sort);
            request.setAttribute("order", order);
            request.setAttribute("cid", cid);

            List<Product> list = pDao.findAll(page, pagesize, sort, order, cid);
            request.setAttribute("productList", list);
            request.getRequestDispatcher("product_list.jsp").forward(request, response);
        } else if(paction.equals("oneProduct")){

            String pid = request.getParameter("pid");
            String pageno = request.getParameter("pageno");
            Product product = pDao.findById(pid);
            request.setAttribute("product",product);
            request.setAttribute("pageno",pageno);
            request.getRequestDispatcher("product_info.jsp").forward(request,response);


        } else if(paction.equals("addProductToCart")){
            HttpSession session = request.getSession();

           //获得该商品的购买数量
            int buyNum = Integer.parseInt(request.getParameter("buyNum"));
            //获的该商品对象
            String pid = request.getParameter("pid");
            Product product = pDao.findById(pid);
            //获得购买该商品的总额
            double subtotal = product.getShop_price()*buyNum;

            //封装CartItem
            CartItem item = new CartItem();
            item.setProduct(product);
            item.setBuyNum(buyNum);
            item.setSubtotal(subtotal);

            //获得购物车--判断再session中是否已经存在购物车
            Cart cart = (Cart) session.getAttribute("cart");
            if(cart==null){
                cart = new Cart();
            }

            //将购物项放到车中---key是pid
            //先判断购物车中是否已将包含此购物项了 ----- 判断key是否已经存在
            //如果购物车中已经存在该商品----将现在买的数量与原有的数量进行相加操作
            Map<String, CartItem> cartItems = cart.getCartItems();

            double newsubtotal = 0.0;

            if(cartItems.containsKey(pid)){
                //取出原有商品的数量
                CartItem cartItem = cartItems.get(pid);
                int oldBuyNum = cartItem.getBuyNum();
                //更新商品数量
                oldBuyNum+=buyNum;
                cartItem.setBuyNum(oldBuyNum);
                cart.setCartItems(cartItems);

                //修改单个商品小计
                //原来该商品的小计
                double oldsubtotal = cartItem.getSubtotal();
                //新买的商品的小计
                newsubtotal = buyNum*product.getShop_price();
                cartItem.setSubtotal(oldsubtotal+newsubtotal);

            }else{
                //如果车中没有该商品
                cart.getCartItems().put(product.getPid(), item);
                newsubtotal = buyNum*product.getShop_price();
            }

            //计算总计
            double total = cart.getTotal()+newsubtotal;
            cart.setTotal(total);

            //重新设置session
            session.setAttribute("cart",cart);

            response.sendRedirect("cart.jsp");



        }else if(paction.equals("delProFromCart")){
            String pid = request.getParameter("pid");

            //移除此商品
            HttpSession session = request.getSession();
            Cart cart = (Cart) session.getAttribute("cart");
            if(cart!=null){
                //修改总金额
                Product product = pDao.findById(pid);
                double total = cart.getTotal()- cart.getCartItems().get(pid).getSubtotal();
                cart.setTotal(total);
                //移除
                cart.getCartItems().remove(pid);
            }
            request.setAttribute("cart",cart);
            response.sendRedirect("cart.jsp");

        }else if(paction.equals("clearCart")){
            HttpSession session = request.getSession();
            session.removeAttribute("cart");
            response.sendRedirect("cart.jsp");
        }else if(paction.equals("submitOrder")){
            HttpSession session = request.getSession();
            if(session.getAttribute("username")==null){
                out.println("请先登录！！");
                response.sendRedirect("login.jsp");
                return;
            }
            Order order = new Order();
            //1.private String oid;//该订单的订单号
            String oid = CommonsUtils.getUUID();
            order.setOid(oid);

            //2.private Date ordertime;//下单时间
             order.setOrdertime(new Date());

            //3.private double total;//该订单的总金额
            //获得session中的购物车
            Cart cart = (Cart) session.getAttribute("cart");
            double total = cart.getTotal();
            order.setTotal(total);

            //4.private int state;//订单支付状态 1代表已付款 0代表未付款
            order.setState(0);

            //5.private String address;//收货地址
            order.setAddress(null);
            //6.private String name;//收货人
            order.setName(null);
            //7.private String telephone;//收货人电话
            order.setTelephone(null);
            //8.private User user;//该订单属于哪个用户
            String username = (String) session.getAttribute("username");
            UserDao userDao = new UserDao();
            User user = userDao.findByUser(username);
            order.setUser(user);


            //往order表中添加数据
            pDao.insertOrder(order);

            //9.该订单中有多少订单项List<OrderItem> orderItems = new ArrayList<OrderItem>();
                Map<String,CartItem> cartItems = cart.getCartItems();

                for(Map.Entry<String,CartItem> entry : cartItems.entrySet()){
                    //取出每一个购物项
                    CartItem cartItem = entry.getValue();

                    //创建新的订单项
                    OrderItem orderItem = new OrderItem();
                    //1)private String itemid;//订单项的id
                    orderItem.setItemid(CommonsUtils.getUUID());

                    //2)private int count;//订单项内商品的购买数量
                    orderItem.setCount(cartItem.getBuyNum());

                    //3)private double subtotal;//订单项小计
                    orderItem.setSubtotal(cartItem.getSubtotal());

                    //4)private Product product;//订单项内部的商品
                    orderItem.setProduct(cartItem.getProduct());

                    //5)private Order order;//该订单项属于哪个订单
                    orderItem.setOrder(order);

                    //往orderItem表中添加数据
                    pDao.insertOrderItem(orderItem);

                    //将该订单项添加到订单的订单集合中
                    order.getOrderItems().add(orderItem);


                }
/*            //往order表中添加数据
              pDao.insertOrder(order);*/   //为什么此句放在这里就会堆栈溢出!!!因为order表中的uid是order程序红的user

/*            //往orderItem表中添加数据
              pDao.insertOrderItem(order);*/

              session.setAttribute("user",user);
              session.setAttribute("order",order);

              //订单产生，消除session中的Cart购物车
              session.removeAttribute("cart");

              response.sendRedirect("order_info.jsp");

        }else if(paction.equals("myOrders")){
            HttpSession session = request.getSession();
             if(session.getAttribute("username")==null){
                out.println("请先登录！！");
                response.sendRedirect("login.jsp");
                return;
            }

             //order集合
             List<Order> orderlist = new ArrayList<Order>();
             User user= (User) session.getAttribute("user");
             if(user==null){
                 user = (new UserDao()).findByUser((String) session.getAttribute("username"));
             }
             String uid = user.getUid();
             orderlist = pDao.findAllOrders(uid);
             //一个order中的orderItem集合
             List<OrderItem> orderItemList = new ArrayList<OrderItem>();
             //循环拿出所有订单order,往order中添加orderItem集合，再放入新的orderList
            List<Order> newOrderList = new ArrayList<Order>();
            for(Order order : orderlist){
                 String oid = order.getOid();
                 orderItemList = pDao.findAllOrderItemByOid(order.getOid());
                 order.setOrderItems(orderItemList);
                 newOrderList.add(order);
            }
            session.setAttribute("orders",newOrderList);

            request.getRequestDispatcher("order_list.jsp").forward(request,response);
        }else if(paction.equals("updataOrder")){
            HttpSession session = request.getSession();
            Order order = (Order) session.getAttribute("order");

            String oid = order.getOid();
            String address = request.getParameter("address");
            String name = request.getParameter("name");
            String telephone = request.getParameter("telephone");

            System.out.println(address+"   "+name+"    "+telephone);     ///


            int count = pDao.updataOrder(address,name,telephone,oid);

            request.getRequestDispatcher("bank.jsp").forward(request,response);

        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
