<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/static_path.jsp"%>
<!DOCTYPE html>

<html lang="en">
<head>
    <title>demo</title>
    <%@ include file="/common/head_meta.jsp"%>
    <%@ include file="/common/head_link.jsp" %>
    <%@ include file="/common/head_script.jsp" %>
    <%@ include file="/common/global.jsp" %>
    <script type = "text/javascript" src = "${fPath }/statics/js/dateformat.js.js?v=${fVersion}"></script>


    <script type="text/javascript" >

        /**
         * 初始化用户列表
         */
        function initList(){
            $('#dg').datagrid({
                url:'${ctx}/name/page',
                loadMsg: "数据加载中...",
                loadFilter: function(data){
                    var total = data.totalCount;
                    var data_list = data.items;
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
                pageList: [10,20,40],//可以设置每页记录条数的列表
                beforePageText: '第',//页数文本框前显示的汉字
                afterPageText: '页    共 {pages} 页',
                displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录',
            });
        }

        function showDialogAdd(){
            $("#modelForm").form('clear');
            $('#add_diglog').dialog('open');
        }


        function showDialogEdit(){
            var row = getSelected();
            if (!row){
                $.messager.alert("操作提示", "请选择要修改的数据");
                return;
            }
            $("#modelEditForm").form('clear');
            $('#edit_diglog').dialog('open');

            $('#edit_id').val(row.id);
            $('#edit_name').textbox("setValue",row.name);
            $('#edit_sex').textbox("setValue",row.sex);
        }

        /**
         * 获取选中的行信息
         */
        function getSelected(){
            return $('#dg').datagrid('getSelected');
        }

        function formatOper(val,row,index){
            return '<a href="#" onclick="deletename(\''+row.id+'\')">删除</a>';
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

        function addname(){
            if(!$("#modelForm").form("validate")){
                return ;
            }
            var addurl ="${ctx}/name/insert";
            $.ajax({
                cache: true,
                type: "POST",
                url:addurl,
                data:$('#modelForm').serialize(),
                async: false,
                error: function(request) {
                    $.messager.alert("提示","添加失败，请联系管理员！");
                },
                success: function(data) {
                    $.messager.alert("提示",data);
                    $('#dg').datagrid("reload",{});
                    $('#add_diglog').dialog('close');
                }
            });
        }
        function editname(){
            if(!$("#modelEditForm").form("validate")){
                return ;
            }
            var editurl ="${ctx}/name/update";
            $.ajax({
                cache: true,
                type: "POST",
                url:editurl,
                data:$('#modelEditForm').serialize(),
                async: false,
                error: function(request) {
                    $.messager.alert("提示","操作失败，请联系管理员！");
                },
                success: function(data) {
                    $.messager.alert("提示","操作成功！");
                    $('#dg').datagrid("reload",{});
                    $('#edit_diglog').dialog('close');
                }
            });
        }

        function deletename(id){
            $.messager.confirm("警示","您确认要删除此数据吗？",function(data){
                if(data){
                    url = '${ctx}/name/delete/'+id;
                    $.post(url,null,
                        function(data){
                            $.messager.alert("提示",data);
                            $('#dg').datagrid('reload');//刷新
                        },
                        "text");//这里返回的类型有：json,html,xml,text
                }

            });

        }


        $(function() {
            initList()
        });

    </script>

</head>

<body >

    <table id="dg" class="easyui-datagrid" title="Name数据集合" style="width:100%;height:auto;"
       data-options="pagination:true,rownumbers:true,singleSelect:true,method:'post',toolbar:'#tb'">
        <thead>
        <tr>
            <th data-options="field:'id'">ID</th>
            <th data-options="field:'name'">姓名</th>
            <th data-options="field:'createDate',formatter:formatTime">创建时间</th>
            <th data-options="field:'_opear',formatter:formatOper">操作</th>
        </tr>
        </thead>
    </table>

    <!-- 头部功能按钮 -->
    <div id="tb" style="padding:2px 5px;">
        <a href="javascript:showDialogAdd();" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a>
        <a href="javascript:showDialogEdit();" class="easyui-linkbutton" iconCls="icon-edit" plain="true">编辑</a>
    </div>
    <!-- 头部功能按钮End -->

    <!-- 添加功能 -->
    <div id="add_diglog" class="easyui-dialog" title="添加name" style="width:400px;height:400px;"
         data-options="resizable:true,modal:true,buttons: '#add-buttons'" closed="true">
        <form id="modelForm">
            <div style="margin:20px">
                <div>姓名:</div>
                <input id="add_name" name="name" class="easyui-textbox" style="width:70%;height:32px" data-options="required:true,missingMessage:'不能为空'"/>
            </div>

        </form>
    </div>
    <div id="add-buttons">
        <a href="javascript:addname();" class="easyui-linkbutton" iconCls="icon-add">确认</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#add_diglog').dialog('close')">关闭</a>
    </div>
    <!-- 添加功能 -->

    <!-- 修改功能 -->
    <div id="edit_diglog" class="easyui-dialog" title="修改流程" style="width:400px;height:400px;"
         data-options="resizable:true,modal:true,buttons: '#edit-buttons'" closed="true">
        <form id="modelEditForm">
            <div style="margin:20px">
                <div>姓名:</div>
                <input id="edit_name" name="name" class="easyui-textbox" style="width:70%;height:32px" data-options="required:true,missingMessage:'不能为空'"/>
            </div>
            <input type="text" id="edit_id" name="id" style="display: none;">
        </form>
    </div>
    <div id="edit-buttons">
        <a href="javascript:editname();" class="easyui-linkbutton" iconCls="icon-add">确认</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#add_diglog').dialog('close')">关闭</a>
    </div>
    <!-- 修改功能 -->


</body>

</html>


