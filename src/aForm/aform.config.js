/*!
 * AForm v1.1
 * Copyright 2014, 谢慧琦
 * Date: 2014年5月10日
 */
 
 
//默认全局配置

AForm.Config = AForm.Config || {};
AForm.Config.defaultDelimiter = ",";//默认字符串分隔符，用于处理复选框的逗号隔开的值
//表格行操作
AForm.Config.defaultAction = {
    "aform_array_add_row": {
        html: "<a href='javascript:void(null)' title='增加'>＋</a>"
    },
    "aform_array_delete_row": {
        html: "<a href='javascript:void(null)' title='删除'>×</a>"
    }
};

//标签
AForm.Config.tags = {
	"basicContainer" : "div",//div
	"objectContainer" : "fieldset",
	"label" : "label", //label
	"controlContainer" : "" //默认为空
};
//额外样式名
AForm.Config.extClassName = {
	"basicContainer" : "form-group",
    "label" : "",
	"table" : "table table-bordered",
	"control" : "form-control",
    "controlContainer" : ""
};
//模板
AForm.Config.tpl = {
	"tips" : '&nbsp;<a title="{tips}" href="#nolink">[?]</a>',
	"thTips" : "<sup title='{tips}'>[?]</sup>"
};
//术语
AForm.Config.wording = {
	"numText" : "NO.",
    "addRowText" : "增加",
    "oprText" : "操作",
    "labelColon" : "："
};

//处理函数
AForm.Config.fn = {
    "showTips" : function(input , errMsg){
        alert(errMsg);
    },
    "onEmpty": function (input, conf) {
        var name = input.getAttribute("name");

        var errMsg = conf ? ("字段[" + (conf.label) + "]不能为空") : input.title;
        if (!errMsg) {
            errMsg = "字段[" + (name) + "]不能为空";
        }

        AForm.Config.fn.showTips(input , errMsg);
        if (typeof input.focus === "function" || typeof input.focus === "object") {
            input.focus();
        }
    },
    "onInvalid": function (input, conf, errorMsg) {
        var errMsg = errorMsg ? errorMsg : (conf ? ("字段[" + (conf.label) + "]的值非法") : input.title);
        if (!errMsg) {
            errMsg = "字段[" + (input.getAttribute("name")) + "]非法";
        }

        AForm.Config.fn.showTips(input , errMsg);
        if (typeof input.focus === "function" || typeof input.focus === "object") {
            input.focus();
        }
    },
    "onValid" : function(input){
    },
    "onGlobalInvalid" : function(msg){
        alert(msg);
    }
};



