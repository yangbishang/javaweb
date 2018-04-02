<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>会员登录</title>
<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css" />
<script src="js/jquery-1.11.3.min.js" type="text/javascript"></script>
<script src="js/bootstrap.min.js" type="text/javascript"></script>
<!-- 引入自定义css文件 style.css -->
<link rel="stylesheet" href="css/style.css" type="text/css" />

<style>
body {
	margin-top: 20px;
	margin: 0 auto;
	width: 100%;
}

.carousel-inner .item img {
	width: 100%;
	height: 300px;
}
</style>
</head>

<body>

<div>
	<!-- 引入header.jsp -->
	<jsp:include page="/header.jsp"></jsp:include>
</div>

	<div class="row" style="width: 1210px; margin: 0 auto;">
		<div class="col-md-12">
			<ol class="breadcrumb">
				<li><a href="#">首页</a></li>
			</ol>
		</div>
		<div class="col-md-12">

			<table class="table table-striped table-hover">

				<thead>
				<tr align="center">
					<td>No.</td>
					<td>产品名</td>
					<td>图片</td>
					<td>价格</td>
					<td>评价</td>
					<td>操作</td>
				</tr>
				</thead>


				<tfoot>
				<tr align="center">
					<td>No.</td>
					<td>产品名</td>
					<td>图片</td>
					<td>价格</td>
					<td>评价</td>
					<td>操作</td>
				</tr>
				</tfoot>

				<tbody>
				<c:forEach  var="product" items="${requestScope.productList}"  varStatus="row">
					<tr align="center">
						<td>${row.index+1}</td>
						<td>${product.pname}</td>
						<td><a href="ProductServlet?pid=${product.pid}&paction=oneProduct"><img src="${product.pimage}"></a></td>
						<td>${product.shop_price}</td>
						<td>${product.pdesc}</td>
						<td>操作</td>

					</tr>
				</c:forEach>
				</tbody>

				<tr>
					<td colspan="5">
						<select id="pagesize" onchange="cargo()">
							<option value="5" ${pagesize==5?'selected':''}>5</option>
							<option value="10" ${pagesize==10?'selected':''}>10</option>
							<option value="50" ${pagesize==50?'selected':''}>50</option>
							<option value="100" ${pagesize==100?'selected':''}>100</option>
						</select>&nbsp;&nbsp;
						<a href="ProductServlet?pageno=1&pagesize=${pagesize}&sort=${sort}&order=${order}&cid=${cid}&paction=allProduct">首页</a>
						&nbsp;&nbsp;
						<a href="ProductServlet?pageno=${pageno-1}&pagesize=${pagesize}&sort=${sort}&order=${order}&cid=${cid}&paction=allProduct">上一页</a>
						&nbsp;&nbsp;
						<a href="ProductServlet?pageno=${pageno+1}&pagesize=${pagesize}&sort=${sort}&order=${order}&cid=${cid}&paction=allProduct">下一页</a>
						&nbsp;&nbsp;
						<a href="ProductServlet?pageno=${maxpage}&pagesize=${pagesize}&sort=${sort}&order=${order}&cid=${cid}&paction=allProduct">尾页</a>
						&nbsp;&nbsp;
						第 ${pageno} 页,共 ${maxpage} 页
						&nbsp;&nbsp;
						<input id="pageno" type="number" onblur="cargo()">
					</td>
				</tr>
				</table>

		</div>

        <div class="col-md-12">
	      <!-- 引入footer.jsp -->
	      <jsp:include page="/footer.jsp"></jsp:include>
        </div>
	</div>
</body>

</html>