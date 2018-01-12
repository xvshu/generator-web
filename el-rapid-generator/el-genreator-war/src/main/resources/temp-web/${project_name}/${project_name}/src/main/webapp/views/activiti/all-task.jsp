<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/static_path.jsp"%>
<!DOCTYPE html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="en">
<head>
	<%@ include file="/common/global.jsp"%>
	<title>待办任务</title>
	<%@ include file="/common/head_meta.jsp"%>
	<%@ include file="/common/head_link.jsp" %>
	<%@ include file="/common/head_script.jsp" %>
	<%@ include file="/common/global.jsp" %>
	<script type = "text/javascript" src = "${r"${fPath }"}/statics/js/dateformat.js?v=${r"${fVersion}"}"></script>
	<script type="text/javascript" src="${r"${fPath }"}/statics/js/easyui-datagrid-plugin.js?v=${r"${fVersion}"}"></script>


	<script type="text/javascript">

        /**
         * 初始化用户列表
         */
        function initList(){
            $('#dg').datagrid({
                loadMsg: "数据加载中...",
                loadFilter: function(data){
                    var total = data.totalCount;
                    var data_list = data.result;
                    var data_return = {};
                    data_return.total = total;
                    data_return.rows = data_list;
                    return data_return;
                }
            });

            //设置分页控件
            var pager = $('#dg').datagrid('getPager');
            $(pager).pagination({
                pageSize: 10,//每页显示的记录条数，默认为10
                pageList: [10,20],//可以设置每页记录条数的列表
                beforePageText: '第',//页数文本框前显示的汉字
                afterPageText: '页    共 {pages} 页',
                displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录',
            });
        }

        //格式化操作按钮单元格
        function formatOper(val,row,index){
			var assigneeWF = row.task.assignee;
			var taskId = row.task.id;
            var jumpUrl = row.task.formKey;
            if(jumpUrl || jumpUrl==null || jumpUrl.length==0){
                jumpUrl='/pubserver/activiti/taskdo';
            }
			var operHtml = "";
			if(assigneeWF==null || assigneeWF==""){
                operHtml = "<a onclick=\"climeWF('${r"${ctx }"}/pubserver/activiti/task/claimWF/"+taskId+"')\" href=\"#\">签收</a>";
			}else{
                operHtml = "<a onclick=\"openTaskDo('"+jumpUrl+"','"+taskId+"')\" href=\"#\">办理</a>";
            }
			return operHtml;
        }

        function climeWF(url){
            $.messager.confirm("警示","您确认要执行此操作吗？",function(data){
                if(data){
                    $.post(url,null,
                        function(data){
                            $.messager.alert("提示",data);
                        },
                        "text");//这里返回的类型有：json,html,xml,text
                }
            });
		}


        function openTaskDo(jumpUrl,taskId){
            var taskDourl="${r"${ctx }"}"+jumpUrl+"?taskId="+taskId;
            parent.addUrlTab(taskDourl,taskId);
        }


        function formatTime(val,row,index){
            if(val==null){
                return "";
            }
            var str=""+val;
            if(str==""){
                return "";
            }
            return dateUnixFormat(str.substring(0,10));
        }


		$(function() {
		    //初始化表格
			initList();
		});


	</script>
</head>

<body>


	<table id="dg" class="easyui-datagrid" title="所有待办任务列表" style="width:100%;height:auto;"
		   data-options="pagination:true,rownumbers:true,singleSelect:true,url:'${r"${ctx }"}/pubserver/activiti/list/taskall/findall',method:'post'">
		<thead>
		<tr>
			<th data-options="field:'task.id',hidden:'true'">任务id</th>

			<th data-options="field:'elProcessInstance.processDefinitionId',align:'right',hidden:'true'">流程定义ID</th>
			<th data-options="field:'elProcessInstance.processDefinitionKey',align:'right',hidden:'true'">流程定义KEY</th>
			<th data-options="field:'elProcessInstance.processDefinitionName',align:'right',width:100">流程名称</th>

			<th data-options="field:'task.name',width:100">当前任务名称</th>
			<th data-options="field:'_message',width:120">当前任务说明</th>
			<th data-options="field:'elProcessInstance.id',hidden:'true'">流程实例id</th>

			<th data-options="field:'task.createTime',align:'right',width:100,formatter:formatTime,width:100">创建时间</th>

			<th data-options="field:'_operate',align:'left',formatter:formatOper,width:100">操作</th>
		</tr>
		</thead>
	</table>



</body>
</html>
