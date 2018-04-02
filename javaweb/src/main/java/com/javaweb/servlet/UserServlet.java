package com.javaweb.servlet;

import com.javaweb.dao.UserDao;
import com.javaweb.pojo.User;
import com.javaweb.tool.MD5;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

public class UserServlet extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        // action 实现控制器的流程控制  CarServlet  | CarServlet?action=save
        String action = request.getParameter("action");
        action = (action == null) ? "login.jsp" : action; // 默认查询所有
        UserDao userDao = new UserDao();
        HttpSession session = request.getSession();

        //注册流程
        if("regist".equals(action)){
            int count = 0;
            //先判断验证码
            String checkWord = request.getParameter("checkWord");
            String randWord = (String) session.getAttribute("Rand");
            if(!checkWord.equals(randWord)){
                out.println("验证码错误");
                response.setHeader("refresh","1,register.jsp");
            }

            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String email = request.getParameter("email");
            //生成随机uid
            String uid = MD5.getMD5(password).substring(1,5);

            //密码加密
            password = MD5.getMD5(password);
            String name = request.getParameter("name");
            String telephone = request.getParameter("telephone");
            String sex = request.getParameter("sex");

            User user = new User(uid,username,password,name,email,telephone,sex);
            System.out.println(user.toString());  //
            count = userDao.regist(user);

            if(count>0){
                out.println("注册成功！请登录...");
                response.setHeader("refresh","1;login.jsp");
            }else{
                out.println("注册失败！请重新注册...");
                response.setHeader("refresh","1;register.jsp");
            }
        }else if("login".equals(action)){

            String checkWord = request.getParameter("checkWord");
            String randWord = (String) session.getAttribute("Rand");

            if(!checkWord.equals( randWord)){
                out.println("验证码错误！");
                response.setHeader("refresh","1,login.jsp");
            }else{

                String username = request.getParameter("username");
                String password = request.getParameter("password");
                //密码转换
                password = MD5.getMD5(password);

                boolean flag = userDao.login(username,password);

                if(flag){
                    session.setAttribute("username",username);
                    request.getRequestDispatcher("index.jsp").forward(request,response);
                }else{
                    out.println("用户名或密码错误，请重新登录！");
                    response.setHeader("refresh","1,login.jsp");
                }
            }



        }else if("logout".equals(action)){
            session.removeAttribute("username");
            session.invalidate(); // session 过期
            out.println("你已经成功下线！");
            response.setHeader("refresh","1,login.jsp");

        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }


}
