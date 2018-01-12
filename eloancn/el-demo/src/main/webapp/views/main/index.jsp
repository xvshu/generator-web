<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/static_path.jsp"%>
<!DOCTYPE html>

<html lang="en">
<head>
    <title>demo</title>
    <%@ include file="/common/head_meta.jsp"%>
    <%@ include file="/common/head_link.jsp" %>
    <%@ include file="/common/head_script.jsp" %>
    <script type = "text/javascript" src = "${fPath }/statics/js/index.js?v=${fVersion}"></script>
    <script type = "text/javascript" src = "${fPath }/statics/js/nano.js?v=${fVersion}"></script>


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
    <div data-options="region:'north',border:false" style="height: 52px">
        <div style="position:relative;float:left;width:150px;line-height: 30px;"><img src="${iPath }/statics/img/logo.jpg?v=${iVersion}"></div>

        <div style="position: relative; float: right;">
            <div align="right" style="float: right; padding: 4px;vertical-align:middle">
                <div> <a href="/logout" >退出</a></div>
            </div>
        </div>
    </div>
    <div data-options="region:'west',split:true,title:'菜单'" style="width: 200px">
        <div class="easyui-accordion" data-options="fit:false,border:false,selected:-1" style="padding: 0px !important;">
            <div title="示例" class="icon_lists" style="padding: 10px;">
                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="https://www.eloancn.com/"  class="index--navi-tab">
                        <span><i class="icon iconfont">&#xe602;</i>&nbsp;&nbsp;<ttitle>翼龙贷</ttitle></span>
                    </a>
                </div>

                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx}/activiti/list/taskall"  class="index--navi-tab">
                        <span><i class="icon iconfont">&#xe602;</i>&nbsp;&nbsp;<ttitle>activiti待办任务</ttitle></span>
                    </a>
                </div>

                <div class="index-nav-item">
                    <a href="javascript:void(0);" src="${ctx}/name/index"  class="index--navi-tab">
                        <span><i class="icon iconfont">&#xe602;</i>&nbsp;&nbsp;<ttitle>增删改查模板</ttitle></span>
                    </a>
                </div>

            </div>


        </div>
    </div>
    <div data-options="region:'center'">
        <div id="tabs" class="easyui-tabs" data-options="fit:true">
            <div title="首页">
                <img src="${iPath }/statics/img/bg.jpg?v=${iVersion}" style="width:100%;height:99%" alt="翼龙贷配置中心欢迎您" />
            </div>
	    </div>
    </div>
</body>

</html>


