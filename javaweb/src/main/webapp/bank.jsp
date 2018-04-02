<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/3/19 0019
  Time: 上午 11:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <title>Document</title>
    <style type="text/css">
        body {
            font-family: Tahoma, Verdana;
            font-size: 12px
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp"></jsp:include>
<div id="title" align="center"><h1>银行个人网上银行系统</h1></div>
<div id="operation" class="container;;">
    <div id="link_student" align="center" class="form-group" class="container">
        <form action="LoginServlet" method="post" name="f">
            <TABLE class="divTab" width="500">
                <TR>
                    <TD colspan="2" align="center" style="font-size:25px;">个人客户登录</TD>
                </TR>
<%--                <TR>
                    <TD colspan="2" align="center" style="font-size:15px;color:red;">
                        账号或密码不对！
                    </TD>
                </TR>--%>
                <TR>
                    <TD align="right" width="35%">账号：</TD>
                    <TD align="left" width="65%"><INPUT TYPE="text"  NAME="AccountID"></TD>
                </TR>
                <TR>
                    <TD align="right" width="35%">密码：</TD>
                    <TD align="left" width="65%"><INPUT TYPE="password" NAME="Password"></TD>
                </TR>
                <TR>
                    <TD colspan="2" align="center" style="font-size:30px;">
                        <INPUT TYPE="submit" value="提　　交">
                        <INPUT TYPE="reset" value="重　　置">
                    </TD>
                </TR>
            </TABLE>
        </form>
    </div>
</div>
<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>
