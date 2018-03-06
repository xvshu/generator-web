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
    <link rel="stylesheet" type="text/css" href="${r"${fPath }"}/statics/css/treeNode.css?v=${r"${fVersion}"}"/>

    <script type="text/javascript" >
        function addUrlTab(url,title){
            if ($('#tabs').tabs('exists', title)){
                $('#tabs').tabs('select', title);
            } else {
                var content = '<iframe scrolling="yes" frameborder="0"  src="'+url+'" style="width:100%;height: 98%;"></iframe>';
                $('#tabs').tabs('add',{
                    title:title,
                    content:content,
                    cache:false,
                    closable:true
                });
            }

        }

        function addPanel(title,content){
            $('#menu_aa').accordion('add',{
                title:title,
                content:content
            });
        }

        /**
         * 加载树形结构数据
         */
        function loadMenuTreeNodeData(url){

            $.ajax({
                url:url
            }).done(function (data){
                var tree_munu=[];
                if (data.data){
                    tree_munu = myLoadFilter_tree(data.data,'closed');
                }else{
                    tree_munu =myLoadFilter_tree(data,'closed');
                }

                for(var i=0; i<tree_munu.length; i++){
                    var menu_one = tree_munu[i];
                    var menu_children = menu_one.children;
                    for(var c=0; c<menu_children.length; c++){
                        var menu_one_n = menu_children[c];
                        var menu_children_n = menu_one_n.children;
                        addPanel(menu_one_n.text,"<ul id='easyui_tree_menu_"+menu_one_n.id+"' ></ul>");
                    }

                }

                for(var i=0; i<tree_munu.length; i++){
                    var menu_one = tree_munu[i];
                    var menu_children = menu_one.children;
                    for(var c=0; c<menu_children.length; c++){
                        var menu_one_n = menu_children[c];
                        var menu_children_n = menu_one_n.children;
                        loadTreeInId("easyui_tree_menu_"+menu_one_n.id,menu_children_n);
                    }

                }

            });

        }

        function loadTreeInId(id,treeData){
            $("#"+id).tree({
                data:treeData,
                onClick: function(node){
                    if(node.url && node.url.length >0){
//                        alert(node.url);
                        addUrlTab('${r"${ctx }"}'+node.url,node.text);
                    }

                }
            });

        }


        /**
         * 解析树形结构数据
         * @param data
         * @returns {Array}
         */
        function myLoadFilter_tree(data,treeNodeState){

            function disTreeNode(node){
                if (node==null){
                    return null;
                }

                var node_msg = {id:node.id,text:node.name,attributes:{code:node.code},url:node.url};

                if (node.childNodes == null || node.childNodes == ''){
                    return node_msg;
                }
                var childNodes = node.childNodes;
                for(var i=0; i<childNodes.length; i++){
                    var childNode = childNodes[i];
                    var childNode_ex = disTreeNode(childNode);
                    if (childNode_ex != null){
                        if (node_msg.children){
                            node_msg.children.push(childNode_ex);
                        } else {
                            node_msg.children = [childNode_ex];
                        }
                    }
                }
                if (treeNodeState){
                    node_msg.state = treeNodeState;
                }
                return node_msg;
            }

            var nodes = [];
            if (data){
                // get the root nodes
                for(var i=0; i<data.length; i++){
                    var node = data[i];
                    var nodeMsg = disTreeNode(node);
                    nodes.push(nodeMsg);
                }
            }
            return nodes;
        }

        /**
         * 格式化节点信息
         * @param node
         * @returns {*}
         */
        function format_node(node){
            var s = node.text;
            if (node.children){
                s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
            }
            return s;
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
        <div id="menu_aa" class="easyui-accordion" data-options="fit:false,border:false,selected:-1" style="padding: 0px !important;">

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

<script>
    loadMenuTreeNodeData('/pubserver/loadMenu');
</script>

</html>


