/**
 * 用户页面相关
 */

var user_datagrid;
var user_form;
var user_dialog;


$(function() {
	//数据列表
    user_datagrid = $('#user_datagrid').datagrid({
        url: ctx+"/user/toList",
        width : 'auto',
		height : fixHeight(0.83),
		pagination:true,
		rownumbers:true,
		border:false,
		singleSelect:true,
		striped:true,
        columns : [ 
            [ 
              {field : 'name',title : '用户名',width : fixWidth(0.2),align : 'left',sortable: true},
              {field : 'passwd',title : '密码',width : fixWidth(0.2),align : 'left',editor : {type:'validatebox',options:{required:true}}},
              {field : 'registerDate', title : '注册时间', width : fixWidth(0.2), editor : "datebox"},
              {field : 'locked',title : '状态',width : fixWidth(0.2),
            	  formatter:function(value,row){
            		  if("0"==row.locked){
						return "<font color=green>正常<font>";
            		  }else{
            			return "<font color=red>停用<font>";  
            		  }
				  },
            	  editor : "text"},
              {field : 'group_name',title : '用户组',width : fixWidth(0.2)}
    	    ] 
        ],
        toolbar: "#toolbar"
    });
    
	$("#searchbox").searchbox({ 
		menu:"#searchMenu", 
		prompt :'模糊查询',
	    searcher:function(value,name){   
	    	var str="{\"searchName\":\""+name+"\",\"searchValue\":\""+value+"\"}";
            var obj = eval('('+str+')');
            user_datagrid.datagrid('reload',obj); 
	    }
	});
});

//高级搜索 del row
function userSearchRemove(curr) {
	$(curr).closest('tr').remove();
}
//高级查询
function  userSearch() {
	jqueryUtil.gradeSearch(user_datagrid, "#userSearch", "/user/userSearch");
}

//修正宽高
function fixHeight(percent) {   
	return parseInt($(this).height() * percent);
}

function fixWidth(percent) {   
	return parseInt(($(this).width() - 50) * percent);
}

//初始化表单
function formInit(row) {
	var _url = ctx+"/user/doAdd";
	if (row != undefined && row.id) {
		_url = ctx+"/user/doUpdate";
	}
    user_form = $('#user_form').form({
        url: _url,
        onSubmit: function (param) {
            $.messager.progress({
                title: '提示信息！',
                text: '数据处理中，请稍后....'
            });
            var isValid = $(this).form('validate');
            
            //验证原密码
            var oldPass = $("#oldPasswd").val();
            if(oldPass != null) {
            	var userId = $("#id").val();
            	$.ajax({
            		async: false,
            		cache: false,
            		url: ctx+"/user/validPasswd",
            		data: { userId: userId, passwd: oldPass },
            		type: 'POST',
            		dataType: "json",
            		success: function(result){ 
            			isValid = result.status;
            			$.extend($.fn.validatebox.defaults.rules, {
            				//直接抛出错误
            				passwdError: {
            					validator: function (value, param) { 
            						return false; 
            					}, 
            					message: '原密码不正确' 
            				}	
            			});
            			if(!isValid){
            				$.messager.alert("提示","原密码不正确","error");
            			}
            			
            		}
            	});
            }
			if (!isValid) {
                $.messager.progress('close');
            }
            return isValid;
        },
        success: function (data) {
            $.messager.progress('close');
            var json = $.parseJSON(data);
            if (json.status) {
                user_dialog.dialog('destroy');//销毁对话框
                user_datagrid.datagrid('reload');//重新加载列表数据
            } 
            $.messager.show({
				title : json.title,
				msg : json.message,
				timeout : 1000 * 2
			});
        }
    });
}


//显示弹出窗口 新增：row为空 编辑:row有值
function showUser(row) {
	var _url = ctx+"/user/toAdd";
	if (row) {
		_url = ctx+"/user/toUpdate/"+row.id;
	}
    //弹出对话窗口
    user_dialog = $('<div/>').dialog({
    	title : "用户信息",
		top: 20,
		width : 600,
		height : 300,
        modal: true,
        minimizable: true,
        maximizable: true,
        href: _url,
        onLoad: function () {
            formInit(row);
            if (row) {
            	user_form.form('load', row);  //通过row初始化表单中的数据
            } else {
            	$("input[name=locked]:eq(0)").attr("checked", 'checked');//状态 初始化值
            }

        },
        buttons: [
            {
                text: '保存',
                iconCls: 'icon-save',
                handler: function () {
                    user_form.submit();
                }
            },
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                    user_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
            user_dialog.dialog('destroy');
        }
    });
}



//编辑
function edit() {
    //选中的行（第一次选择的行）
    var row = user_datagrid.datagrid('getSelected');
    if (row) {
        showUser(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//删除用户
function del() {
    var row = user_datagrid.datagrid('getSelected');
    if (row) {
        $.messager.confirm('确认提示！', '您确定要删除选中的所有行?', function (result) {
            if (result) {
                var id = row.id;
                $.ajax({
                    url: ctx + '/user/delete/'+id,
                    type: 'post',
                    dataType: 'json',
                    data: {id: id},
                    success: function (data) {
                        if (data.status) {
                            user_datagrid.datagrid('load');	// reload the user data
                        }
                        $.messager.show({
        					title : data.title,
        					msg : data.message,
        					timeout : 1000 * 2
        				});
                    }
                });
            }
        });
    } else {
    	$.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//同步
function sync(){
    $.messager.confirm('提示', '重新同步所有用户信息到工作流表,确认？', function (result) {
        if (result) {
            $.ajax({
                url: ctx + '/user/syncUserToActiviti',
                type: 'post',
                dataType: 'json',
                data: {},
                success: function (data) {
                    if (data.status) {
                        user_datagrid.datagrid('load');	// reload the user data
                    }
                    $.messager.show({
    					title : data.title,
    					msg : data.message,
    					timeout : 1000 * 2
    				});
                }
            });
        }
    });
}
