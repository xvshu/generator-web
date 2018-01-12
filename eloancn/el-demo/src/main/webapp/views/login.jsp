<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/static_path.jsp"%>
<!DOCTYPE html>

<html lang="en">
<head>
    <title>demo-login</title>
	<%@ include file="/common/head_meta.jsp"%>

	<%@ include file="/common/head_link.jsp" %>
    <link rel="stylesheet" type="text/css" href="${fPath }/statics/css/login.css?v=${fVersion}"/>

    <%@ include file="/common/head_script.jsp" %>
    <script type = "text/javascript" src = "${fPath }/statics/js/checkCode.js?v=${fVersion}"></script>

    <style type="text/css">
        #code
        {
            font-family:Arial;
            font-style:italic;
            font-weight:bold;
            border:0;
            letter-spacing:2px;
            color:blue;
        }
    </style>

    <script type = "text/javascript" >
        function confirmLogin(){
            return validate();
        }

    </script>

</head>

<body>
<c:if test="${not empty param.error}">
    <%--<h2 id="error" style="text-align: center;color: red;font-size:50px;">用户名或密码错误！！！</h2>--%>
    <script type = "text/javascript" >
        alert("用户名或密码错误！！！");
    </script>
</c:if>
<c:if test="${not empty param.timeout}">
    <%--<h2 id="error" style="text-align: center;color: red;font-size:50px;">未登录或超时！！！</h2>--%>
    <script type = "text/javascript" >
        alert("未登录或超时！！！");
    </script>
</c:if>
<div class="login">
    <div class="message">统一架构示例项目</div>
    <div id="darkbannerwrap"></div>


		<form onsubmit="return confirmLogin()" action="/login/check" method="get">
            <input  id="username" name="username" placeholder="用户名" required="" type="text">
            <hr class="hr15">
            <input id="password" name="password" placeholder="密码" required="" type="password">
            <hr class="hr15">
            <input type = "text" style="width:50%" placeholder="验证码" required="" id = "codeText"/>
            <input type = "button" style="width:48%" id="code" onclick="createCode()"/>
            <hr class="hr15">
            <input value="登录" style="width:100%;" type="submit">
            <hr class="hr20">
		</form>
    </div>
</div>
<div class="copyright">© 2017 Demo  Login by<a href="https://www.eloancn.com/" target="_blank">翼龙贷</a></div>
</body>
</html>
