<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/static_path.jsp"%>
<!DOCTYPE html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="en">
<head>
	<%@ include file="/common/global.jsp"%>
	<title>任务办理</title>
	<%@ include file="/common/head_meta.jsp"%>
	<%@ include file="/common/head_link.jsp" %>
	<%@ include file="/common/head_script.jsp" %>
	<%@ include file="/common/global.jsp" %>
	<script type = "text/javascript" src = "${r"${fPath }"}/statics/js/dateformat.js?v=${r"${fVersion}"}"></script>
	<script type="text/javascript" src="${r"${fPath }"}/statics/js/easyui-datagrid-plugin.js?v=${r"${fVersion}"}"></script>


	<script type="text/javascript">

		function wfPass(ispass){
		    var url="/pubserver/activiti/completeWf";
		    $("#isPass").val(ispass);
            $.ajax({
                cache: true,
                type: "POST",
                url:url,
                data:$('#workFlowDoForm').serialize(),
                async: false,
                error: function(request) {
                    $.messager.alert("Connection error","操作失败，请联系管理员！");
                },
                success: function(data) {
                    $.messager.alert("提示",data);
                    $('#dg').datagrid("reload",{});
                    $('#oper_diglog').dialog('close');
                }
            });
		}

		var taskId=${r"${taskId}"};
        var taskType="${r"${taskType}"}";

        $(function() {
            $("#taskId").textbox("setValue",taskId);
            $("#taskType").val(taskType);
        });

	</script>
</head>

<body>




	<!-- 办理 -->
	<div id="oper_diglog" class="easyui-panel" title="办理任务" style="width:100%"
		 data-options="buttons: '#add-buttons'">
		<form id="workFlowDoForm">
			<div style="margin:20px">
				<table>
					<tr>
						<td>任务id:</td>
						<td><input id="taskId" name="taskId" class="easyui-textbox" style="width:100px;height:32px" /></td>
					</tr>
				</table>
				<input type=text style="display:none" id="isPass" name="isPass">
				<input type=text style="display:none" id="taskType" name="taskType">
			</div>
		</form>
	</div>
	<div id="add-buttons">
		<a href="javascript:wfPass(true);" class="easyui-linkbutton" iconCls="icon-add">同意</a>
		<a href="javascript:wfPass(false);" class="easyui-linkbutton" iconCls="icon-add">拒绝</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#oper_diglog').dialog('close')">关闭</a>
	</div>
	<!-- 添加候选人功能 -->

</body>
</html>
