/**
 * 根据index获取行数据
 */
function getRowDataByIndex(id,rowIndex){
    var rows = $('#'+id).datagrid('getRows');
    return rows[rowIndex];
}

/**
 * 格式化状态
 */
function statusFormatter(value, row, index) {
    var status = row.status;
    if (status == 0 || status == '0') {
        return "未读";
    } else if (status == 1 || status == '1') {
        return "已读";
    } else {
        return "其他";
    }
}
/**
 * 格式化发送类型
 */
function sendTypeFormatter(value, row, index) {
    var sendType = value;
    if (sendType == 1 || sendType == '1') {
        return "私人消息";
    } else if (sendType == 2 || sendType == '2') {
        return "公共消息";
    } else {
        return "系统消息";
    }
}
/**
 * 消息类型
 */
function typeFormatter(value, row, index){
    var type = value;
    if (type == 1 || type == '1') {
        return "站内信";
    } else if (type == 2 || type == '2') {
        return "邮件";
    } else if (type == 3 || type == '3') {
        return "短信";
    }else if (type == 4 || type == '4') {
        return "公告";
    }
}

/**
 * 发送类型
 */
function sendTypeFormatter(value, row, index){
    var sendType = value;
    if (sendType == 1 || sendType == '1') {
        return "私人消息";
    } else if (sendType == 2 || sendType == '2') {
        return "公共消息";
    } else if (sendType == 3 || sendType == '3') {
        return "系统消息";
    }
}

/**
 * 阅读消息
 */
function readMessage(id,index){
    showReadMessage(id,index);
    var row = getRowDataByIndex(id,index);
    if (row.status == 0 || row.status == '0'){
        var param = {};
        if (row.id != 0 && row.id > 0){
            param.id = row.id;
        }else{
            param.recId = row.recId;
            param.messageId = row.messageId;
        }
        param.status = 1;
        updateMessage(param,null,id);
    }
}

/**
 * 展示消息
 */
function showReadMessage(id,index){
    var row = getRowDataByIndex(id,index);
    $("#showMessageTitle").html("标题："+row.title+"<br\>发送人："+row.sendName+"<br\>发送时间：" + dateTimeFormatter(row.createTime));
    $("#showMessage").html(row.message);
    $('#showMessageRead').dialog('open');
}

/**
 * 删除消息
 */
function delMessage(id,index){
    var row = getRowDataByIndex(id,index);
    var configMsg = '确认删除该消息吗？';
    $.messager.defaults = {ok:"确定", cancel:"取消",width:250};
    $.messager.confirm("确认", configMsg, function (r) {
        var param = {};
        if (row.id != 0 && row.id > 0){
            param.id = row.id;
        }else{
            param.recId = row.recId;
            param.messageId = row.messageId;
        }
        param.status = 2;
        if (r) {
            updateMessage(param,index,id);
        }
    })
}
/**
 * 更新消息
 */
function updateMessage(param,index,id){
    $.ajax({
        url: '/pubserver/message/updateMessageRec',
        data: param,
        type: "post",
        async:false,
        success: function (data, textStatus, XMLHttpRequest) {
            if(data != null && data.code=='0'){
                if (index && index != null){
                    $.messager.alert('提示','操作成功！','info',function(){
                        $('#'+id).datagrid('deleteRow',index);
                    });
                }
                if (id && id == 'showDataTable'){
                    $('#'+id).datagrid("reload",{});
                }
            }else{
                $.messager.alert('提示',data.message,'error');
            }

        }
    });
}

/**
 * 格式化日期时间
 */
function dateTimeFormatter(value) {
    if (value == undefined) {
        return "";
    }
    var date = new Date();
    date.setTime(value);

    return date.Format("yyyy-MM-dd hh:mm:ss");
}

// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
// 例子：
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
Date.prototype.Format = function(fmt) {
    var o = {
        "M+" : this.getMonth()+1,                 //月份
        "d+" : this.getDate(),                    //日
        "h+" : this.getHours(),                   //小时
        "m+" : this.getMinutes(),                 //分
        "s+" : this.getSeconds(),                 //秒
        "q+" : Math.floor((this.getMonth()+3)/3), //季度
        "S"  : this.getMilliseconds()             //毫秒
    };
    if(/(y+)/.test(fmt))
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    return fmt;
}

/**
 * 获取选中的行信息
 */
function getSelected(id){
    return $('#'+id).datagrid('getSelected');
}
/**
 * 获取选中的多行信息
 */
function getSelections(){
    var ss = [];
    var rows = $('#showDataTable').datagrid('getSelections');
    for(var i=0; i<rows.length; i++){
        var row = rows[i];
        ss.push('<span>'+row.id+":"+row.name+":"+row.status+'</span>');
    }
    $.messager.alert('Info', ss.join('<br/>'));
}

/**
 * 判断字符串是否以某个字符串结尾
 */
String.prototype.endWith=function(endStr){
    var d=this.length-endStr.length;
    return (d>=0&&this.lastIndexOf(endStr)==d)
}