package com.javaweb.servlet;

import com.javaweb.dao.ProductDao;
import com.javaweb.pojo.Product;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        List<Product> list = new ArrayList<Product>();
        /*JSONObject jo = new JSONObject();*/                //？？？？json 和 jsonobject的区别
        JSON jo = null;
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());

        String pid = request.getParameter("pid");
        if(pid==null || pid.equals("")){
            //查询所有
            list = (new ProductDao()).findAdPhone();

            jo = JSONSerializer.toJSON(list,jsonConfig);

        }else{

            Product product = new Product();
            product = (new ProductDao()).findById(pid);
            jo = (JSONObject) JSONSerializer.toJSON(product,jsonConfig);
        }


        out.println(jo.toString());

        out.flush();
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
