package com.javaweb.servlet;

import com.javaweb.pojo.Admin;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class SessionServlet extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        Admin admin = new Admin();
        admin = (Admin) session.getAttribute("admin");

        JSONObject jo = new JSONObject();
        JsonConfig jsonConfig = new JsonConfig();
        jo = (JSONObject) JSONSerializer.toJSON(admin,jsonConfig);

        out.println(jo);

        out.flush();
        out.close();

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
