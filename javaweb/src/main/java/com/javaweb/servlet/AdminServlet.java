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
import java.io.IOException;
import java.io.PrintWriter;

public class AdminServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        AdminDao adminDao = new AdminDao();
        Admin admin = new Admin();

        admin = adminDao.checkAdmin(username,password);

        JSONObject  jo = new JSONObject();
        if(admin != null){
            JsonConfig jsonConfig = new JsonConfig();
            jo = (JSONObject) JSONSerializer.toJSON(admin,jsonConfig);
        }else{

            jo.put("code", 400);
            jo.put("message", "错误的用户名或密码!");
        }


        out.println(jo.toString());


        out.flush();
        out.close();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
