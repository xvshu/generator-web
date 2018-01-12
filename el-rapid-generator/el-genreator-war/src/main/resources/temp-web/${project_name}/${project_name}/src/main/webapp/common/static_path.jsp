<%--
  static image and file path
  Created by IntelliJ IDEA.
  User: qinxf
  Date: 2017/11/28
  Time: 11:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.*,org.apache.commons.lang3.StringUtils,org.apache.commons.lang3.ObjectUtils" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="iPath" value="${r"${applicationScope['static.image.path']}"}"/>
<c:set var="iVersion" value="${r"${applicationScope['static.image.path.version']}"}"/>
<c:set var="fPath" value="${r"${applicationScope['static.file.path']}"}"/>
<c:set var="fVersion" value="${r"${applicationScope['static.file.path.version']}"}"/>


<script type="text/javascript">
	var iPath = '<%= application.getAttribute("static.image.path")%>';
	var iVersion = '<%= application.getAttribute("static.image.path.version")%>';
	var fPath = '<%= application.getAttribute("static.file.path")%>';
	var fVersion = '<%= application.getAttribute("static.file.path.version")%>';
</script>

