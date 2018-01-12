<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/static_path.jsp"%>
<!DOCTYPE html>

<html lang="en">
<head>
    <title>demo</title>
    <%@ include file="/common/head_meta.jsp"%>
    <%@ include file="/common/head_link.jsp" %>
    <%@ include file="/common/head_script.jsp" %>
    <script type = "text/javascript" src = "${r"${fPath }"}/statics/js/index.js?v=${r"${fVersion}"}"></script>
    <script type = "text/javascript" src = "${r"${fPath }"}/statics/js/nano.js?v=${r"${fVersion}"}"></script>

    <script type="text/javascript" >
        function addUrlTab(url,title){
            var content = '<iframe scrolling="yes" frameborder="0"  src="'+url+'" style="width:100%;height: 98%;"></iframe>';
            $('#tabs').tabs('add',{
                title:title,
                content:content,
                cache:false,
                closable:true
            });
        }
    </script>

</head>

<body class="easyui-layout">
    <!-- 消息弹框及公告 -->
    <jsp:include page="../message/messageNoticeDialog.jsp"/>
    <!-- 消息弹框及公告 -->
    
    <div data-options="region:'north',border:false" style="height: 52px">
        <div style="position:relative;float:left;width:150px;line-height: 30px;"><img src="${r"${iPath }"}/statics/img/logo.jpg?v=${r"${iVersion}"}"></div>

        <!-- 头部公告滚动条 -->
        <div style="position: absolute;margin: auto;left: 400px;top: 15px;" id="showNoticeMarquee"></div>
        <!-- 头部公告滚动条 -->

        <div style="position: relative; float: right;">
            <div align="right" style="float: right; padding: 4px;vertical-align:middle">
                <div> <a href="/pubserver/logout" >退出</a></div>
            </div>
        </div>
    </div>
    <div data-options="region:'west',split:true,title:'菜单'" style="width: 200px">
        <div class="easyui-accordion" data-options="fit:false,border:false,selected:-1" style="padding: 0px !important;">
            <div title="示例" class="icon_lists" style="padding: 10px;">

                <#list  tables?split(",") as oneTname>
                    <div class="index-nav-item">
                        <a href="javascript:void(0);" src="${r"${ctx}"}/${oneTname}/index"  class="index--navi-tab">
                            <span><i  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-transDetail'"></i><ttitle>${oneTname}</ttitle></span>
                        </a>
                    </div>
                </#list>

            </div>

            <div title="消息管理" class="icon_lists" style="padding: 10px;">
                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${r"${ctx}"}/pubserver/message/view/loadAll"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-tip'"></i><ttitle>站内信列表</ttitle></span>
                    </a>
                </div>
                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${r"${ctx}"}/pubserver/message/view/loadAllNotice"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-tip'"></i><ttitle>公告列表</ttitle></span>
                    </a>
                </div>
                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${r"${ctx}"}/pubserver/message/view/sendMessageListJsp"  class="index--navi-tab">
                        <span><i class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-tip'"></i><ttitle>发送记录</ttitle></span>
                    </a>
                </div>
            </div>


        </div>
    </div>
    <div data-options="region:'center'">
        <div id="tabs" class="easyui-tabs" data-options="fit:true">
            <div title="待办任务">
                <iframe scrolling="yes" frameborder="0"  src="${r"${ctx}"}/pubserver/activiti/list/taskall" style="width:100%;height: 98%;"></iframe>
            </div>
	    </div>
    </div>
</body>

</html>


