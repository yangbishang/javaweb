package com.javaweb.servlet;

import com.javaweb.dao.AdminDao;
import com.javaweb.pojo.Admin;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class AdminServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        AdminDao adminDao = new AdminDao();
        JSONObject jo = new JSONObject();
        String baction = request.getParameter("baction");

        if ("login".equals(baction)) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            Admin admin = new Admin();
            admin = adminDao.checkAdmin(username, password);


            if (admin.getUsername() != null) {
                //如果admin不为空，则把admin存入session
                admin = adminDao.checkAdmin(username, password);
                session.setAttribute("admin", admin);

                JsonConfig jsonConfig = new JsonConfig();
                jo = (JSONObject) JSONSerializer.toJSON(admin, jsonConfig);
            } else {
                jo.put("code", 400);
                jo.put("message", "错误的用户名或密码!");
            }
            out.println(jo.toString());
            out.flush();
            out.close();
        }else if("checkOldPassword".equals(baction)){
            String lastPassword = request.getParameter("lastPassword");
            Admin admin = (Admin) session.getAttribute("admin");
            String username =admin.getUsername();
            String password = null;
            password = adminDao.findByPassword(lastPassword,username);



            if(password!=null&&!"".equals(password)){
                jo.put("code","200");
                jo.put("message","密码正确！");
            }else{
                jo.put("code","400");
                jo.put("message","旧密码错误！");
            }
            out.println(jo.toString());

        }else if("updateOldPassword".equals(baction)){
            String oldPassword = request.getParameter("oldPassword");
            String newPassword = request.getParameter("newPassword");
            Admin admin = (Admin) session.getAttribute("admin");
            String username = admin.getUsername();

            int count = 0;
            count = adminDao.updatePassword(oldPassword,newPassword,username);
            if(count!=0){
                jo.put("code",200);
                jo.put("message","成功");
            }else{
                jo.put("code",400);
                jo.put("message","失败");
            }

            out.println(jo.toString());


        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
