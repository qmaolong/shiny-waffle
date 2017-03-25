/**
 * Created by qmaolong on 2016/9/2.
 */
var Global = {
    editObj : []//操作中的对象
}

//获取form表单数据，转化为object
function getDataFromForm(formId) {
    var disabledSelect = $("select[disabled='disabled']");
    disabledSelect.removeAttr("disabled");//打开禁选域
    var serializeVal = decodeURIComponent($("#" + formId).serialize(), true).replace("+", "%20");
    disabledSelect.attr("disabled", "disabled");//重新关闭禁选域
    var serializeArray = serializeVal.split("&");
    var result = {};
    $.each(serializeArray, function (i, value) {
        var array = value.split("=");
        if (array[1] == 'on') {
            array[1] = true;
        }
        if (result[array[0]] == undefined) {
            result[array[0]] = unescape(array[1]);
        } else {
            var temp = result[array[0]];
            temp += "," + unescape(array[1]);
            result[array[0]] = temp;
        }
    })
    return result;
}
//获取当前选中行
function getSelectedRow(table) {
    if (table == undefined) {
        table = "table";
    }
    var index = $(table).find('tr.success').data('index');
    return $(table).bootstrapTable('getData')[index];
}


//提交按钮绑定
function submitEvent(editUrl, index, oper, formNamae, callBack) {
    layer.load();
    if (undefined == formNamae) {
        formNamae = "signupForm";
    }
    var formData = getDataFromForm(formNamae);
    formData = transFormToObj(formData);
    $.ajax({
        url: editUrl,
        type: "post",
        data: {dataStr: JSON.stringify(formData), oper: oper},
        success: function (data) {
            if (undefined != callBack && typeof callBack == "function") {
                callBack(data);
            } else {
                if (data.resultCode == "SUCCESS") {
                    layer.close(index);
                    layer.closeAll('loading');
                    $("button[name='refresh']").click();
                } else {
                    layer.closeAll('loading');
                    layer.alert(data.errCodeDesc);
                }
            }
        }
    })
}

//编辑初始化数据
function iniDataToForm(selectData, callBack) {
    if (undefined == selectData) {
        selectData = getSelectedRow();
    }
    for (var data in selectData) {
        if(isNull(selectData[data])){
            continue;
        }
        if ($("input[name=" + data + "]").attr("data-type") == "date") {
            $("input[name=" + data + "]").val(Formatter.date(selectData[data]));
        } else if ($("input[name=" + data + "]").attr("data-type") == "dateTime") {
            $("input[name=" + data + "]").val(Formatter.time(selectData[data]));
        } else if ($("input[name=" + data + "]").attr("type") == "checkbox") {
            if(typeof selectData[data] == 'boolean'){//布尔值
                if(selectData[data]){
                    $("input[name=" + data + "]").iCheck('check');
                }else {
                    $("input[name=" + data + "]").iCheck('uncheck');
                }
            }else if(typeof selectData[data] == 'string'){//字符串
                var values = selectData[data].split(",");
                jQuery.each(values, function (i, value) {
                    $("input[name='" + data + "'][value=" + value + "]").iCheck('check');
                })
            }
        } else if ($("input[name=" + data + "]").attr("data-type") == "imgUrl") {
            $("input[name=" + data + "]").val(selectData[data]);
            $("input[name=" + data + "]").parent().next().find("#fileList").html("<img src=" + selectData[data] + "'/static' style='width:60px;height:60px'>");
        } else if ($("input[name=" + data + "]").attr("type") == "radio") {
            $("input[name=" + data + "][value=" + selectData[data] + "]").iCheck('check');
        } else if ($("select[name=" + data + "]").attr("data-type") == "multiselect") {
            var selectValues = selectData[data].split(",");
            $.each(selectValues, function(i, value){
                $("select[name=" + data + "]").find("option[value=" + value + "]").attr("selected", true);
            })
            $("select[name=" + data + "]").multiSelect( 'refresh');
        } else {
            $("input[name=" + data + "]").val(selectData[data]);
            $("select[name=" + data + "]").val(selectData[data]);
            $("textarea[name=" + data + "]").text(selectData[data]);
        }
        if (data == 'fitDays' && null != selectData[data]) {
            var val = selectData[data];
            val.substring(0, 1) == 1 ? $("input[name='sun']").iCheck('check') : $("input[name='sun']").iCheck('unchecked');
            val.substring(1, 2) == 1 ? $("input[name='mon']").iCheck('check') : $("input[name='mon']").iCheck('unchecked');
            val.substring(2, 3) == 1 ? $("input[name='tue']").iCheck('check') : $("input[name='tue']").iCheck('unchecked');
            val.substring(3, 4) == 1 ? $("input[name='wed']").iCheck('check') : $("input[name='wed']").iCheck('unchecked');
            val.substring(4, 5) == 1 ? $("input[name='thu']").iCheck('check') : $("input[name='thu']").iCheck('unchecked');
            val.substring(5, 6) == 1 ? $("input[name='fri']").iCheck('check') : $("input[name='fri']").iCheck('unchecked');
            val.substring(6, 7) == 1 ? $("input[name='sat']").iCheck('check') : $("input[name='sat']").iCheck('unchecked');
        }
    }

    if (undefined != callBack) {
        callBack();
    }
}

var Formatter = {
    date: function (data) {
        if (isNull(data) || data == 'Invalid Date') {
            return "-";
        }
        var date = new Date(data);
        //date.setTime(data);
        return date.Format("yyyy-MM-dd");
    },
    runningFormatter: function (value, row, index) {
        return index + 1;
    },
    bool: function (value) {
        if (value) {
            return "是";
        } else {
            return "否";
        }
    },
    medium: function (value) {
        if (value == 0) {
            return "虚拟卡";
        } else if (value == 1) {
            return "IC卡";
        } else if (value == 2) {
            return "磁卡";
        }
    },
    cardState: function (value, row) {
        if (isNull(row.mediumKey)){
            return "未发卡";
        }
        if (value == 0) {
            return "未激活";
        } else if (value == 1) {
            return "已激活";
        } else if (value == 2) {
            return "已冻结";
        }
    },
    couponState : function(value){
        if(value == 0){
            return "未激活";
        }else if (value == 1) {
            return "已激活";
        } else if (value == 3) {
            return "已使用";
        } else if (value == 4) {
            return "作废";
        }
    },cardOption: function (value) {
        if (value == 1) {
            return "充值";
        } else if (value == 2) {
            return "提现";
        } else if (value == 3) {
            return "消费";
        } else if (value == 4) {
            return "撤销消费";
        } else if (value == 5) {
            return "撤销充值";
        }
    },
    time: function (value) {
        if (isNull(value) || value == 'Invalid Date') {
            return "-";
        }
        var date = new Date(value);
        //date.setTime(data);
        return date.Format("yyyy-MM-dd  hh:mm:ss");
    },
    count: function (value) {
        if (null == value || undefined == value || typeof value != "object") {
            return 0;
        }
        return value.length;
    },
    none: function(value){
        if (isNull(value)) {
            return "无";
        }
        return value;
    },
    orderState : function(value){
        if(isNull(value)){
            return "-";
        }else if(value == 0){
            return "新订单";
        }else if(value == 1){
            return "已关闭";
        }else if(value == 2){
            return "已付款";
        }else if(value == 3){
            return "退款";
        }
    },
    sex : function (value) {
        if (value == 1)
            return "男";
        else if(value == 2)
            return "女";
        else
            return "未知";
    },
    scale : function (value) {
        return Math.round(value*100)/100
    }
}
/**
 * 初始化按钮操作
 * @param params
 */
function initEdit(params) {
    if (undefined == params.name) {
        params.name = "内容";
    }
    if (undefined == params.table) {
        params.table = "table";
    }
    if (undefined == params.layerType) {
        params.layerType = 1;
    }
    if (undefined == params.content) {
        params.content = $("#edit-content").html();
    }
    //绑定添加按钮
    if (null != params.addButton) {
        $(params.addButton).on("click", function () {
            crudEvent.addEvent(params);
        })
    }
    //绑定编辑按钮
    if (null != params.editButton) {
        $(params.editButton).on("click", function () {
            var row = getSelectedRow(params.table);
            if (row == undefined) {
                layer.msg("请先选中一行");
                return;
            }
            Global.editObj = row;
            crudEvent.editEvent(params, row);
        })
    }
    //绑定删除按钮
    if (null != params.deleteButton) {
        $(params.deleteButton).on("click", function () {
            var rows = $(params.table).bootstrapTable("getSelections");
            if (rows.length == 0) {
                rows = [];
                var checkedData = trimNull(getSelectedRow(params.table));
                if (undefined != checkedData) {
                    rows.push(checkedData);
                }
            }
            crudEvent.deleteEvent(params, rows);
        })
    }

    //绑定查看按钮
    if (null != params.lookButton) {
        $(params.lookButton).on("click", function () {
            var row = getSelectedRow(params.table);
            if (row == undefined) {
                layer.msg("请先选中一行");
                return;
            }
            Global.editObj = row;//赋值给全局变量editObj
            crudEvent.lookEvent(params, row);
        })
    }
    //绑定数据项上的按钮
    window.rudEvents = {
        'click .editEvent': function (e, value, row, index) {//编辑
            Global.editObj = row;//赋值给全局变量editObj
            crudEvent.editEvent(params, row);
        },
        'click .deleteEvent': function (e, value, row, index) {//删除
            crudEvent.deleteEvent(params, [row]);
        },
        'click .searchEvent': function (e, value, row, index) {//查看
            Global.editObj = row;//赋值给全局变量editObj
            crudEvent.lookEvent(params, row);
        }
    };
}

/**
 * bootstrap table项增删改按钮
 */
function rudFormatter(){
    //rudEvents();
    return ['<a class="btn btn-sm btn-white btn-bitbucket searchEvent"> <i class="glyphicon glyphicon-search"></i></a>',
        '<a class="btn btn-sm btn-white btn-bitbucket editEvent"> <i class="glyphicon glyphicon-pencil"></i></a>',
        '<a class="btn btn-sm btn-white btn-bitbucket deleteEvent"> <i class="glyphicon glyphicon-trash"></i></a>'].join("&nbsp;&nbsp;");
}

/**
 * 增删改查事件
 * @type {{addEvent: crudEvent.addEvent, editEvent: crudEvent.editEvent}}
 */
var crudEvent = {
    addEvent : function(params){
        var content = params.content;
        if (params.layerType == 2) {//拼接参数
            if (undefined == params.parameters) {
                params.parameters = {oper: "add"}
            } else {
                params.parameters.oper = "add";
            }
            content = transObjToParam(params.parameters, content);
        }
        var index = layer.open({
            title: params.hiddenTitle?false:"新增" + params.name,
            type: params.layerType,
            skin: 'layui-layer-demo', //样式类名
            closeBtn: 1, //不显示关闭按钮
            shift: 2,
            maxmin: true,
            moveOut: true,
            area: params.area,
            shadeClose: false, //开启遮罩关闭
            content: content,
            end: function () {
                $("button[name='refresh']").click();
                if(undefined != params.afterClose && typeof params.afterClose=="function"){
                    params.afterClose();
                }
            },
            success: function (layero, index) {
                $("input[data-type='date']").on("click", function () {
                    laydate({istime: false, format: 'YYYY-MM-DD', fixed : true})
                });
                $("input[data-type='dateTime']").on("click", function () {
                    laydate({istime: true, format: 'YYYY-MM-DD hh:mm:ss', fixed : true})
                });
                $("#signupForm").validate({
                    submitHandler: function () {
                        submitEvent(params.editUrl, index, "add");
                    }
                });
                if (undefined != params.afterInitAddContent) {
                    params.afterInitAddContent();
                }
                $("#closeLayer").on("click", function () {
                    layer.close(index);
                })
            }
        });
    },
    editEvent : function(params, row){
        var content = params.content;
        if (params.layerType == 2) {//拼接参数
            if (undefined == params.parameters) {
                params.parameters = {id: row.id, oper: "edit"}
            } else {
                params.parameters.id = row.id;
                params.parameters.oper = "edit";
            }
            content = transObjToParam(params.parameters, content);
        }
        var index = layer.open({
            title: params.hiddenTitle?false:"编辑" + params.name,
            type: params.layerType,
            skin: 'layui-layer-demo', //样式类名
            closeBtn: 1, //不显示关闭按钮
            shift: 2,
            maxmin: true,
            area: params.area,
            shadeClose: false, //开启遮罩关闭
            content: content,
            end: function () {
                $("button[name='refresh']").click();
                if(undefined != params.afterClose && typeof params.afterClose=="function"){
                    params.afterClose();
                }
            },
            success: function (layero, index) {
                if (params.layerType != 2) {
                    $("input[data-type='date']").on("click", function () {
                        laydate({istime: false, format: 'YYYY-MM-DD', fixed : true})
                    });
                    $("input[data-type='dateTime']").on("click", function () {
                        laydate({istime: true, format: 'YYYY-MM-DD hh:mm:ss', fixed : true})
                    });
                    iniDataToForm(row);
                    $("#signupForm").validate({
                        submitHandler: function () {
                            submitEvent(params.editUrl, index, "edit");
                        }
                    });
                }
                if (undefined != params.afterInitEditContent) {
                    params.afterInitEditContent();
                }
                $("#closeLayer").on("click", function () {
                    layer.close(index);
                })
            }
        });
    },
    deleteEvent : function(params, rows){
        if (rows == undefined || rows.length == 0) {
            layer.msg("请先选中一行");
            return;
        }
        layer.confirm('确认删除？', {
            btn: ['确认', '取消'] //按钮
        }, function () {
            if(!isNull(params.editUrl)){
                $.ajax({
                    url: params.editUrl,
                    data: {dataStr: JSON.stringify(rows), oper: "del"},
                    type: "post",
                    success: function (data) {
                        if (data.resultCode == "SUCCESS") {
                            layer.msg("删除成功");
                            $("button[name='refresh']").click();
                        } else {
                            layer.alert(data.errCodeDesc);
                        }
                    }
                })
            }else {
                var ids = [];
                jQuery.each(rows, function(i, row){
                    ids.push(row.id);
                })
                $(params.table).bootstrapTable('remove', {field: 'id', values: ids});
                layer.msg("删除成功");
                $("button[name='refresh']").click();
            }
        }, function () {
            if(undefined != params.afterClose && typeof params.afterClose=="function"){
                params.afterClose();
            }
        });
    },
    lookEvent : function(params, row){
        var content = params.content;
        if (params.layerType == 2) {//拼接参数
            if (undefined == params.parameters) {
                params.parameters = {id: row.id, oper: "look"}
            } else {
                params.parameters.id = row.id;
                params.parameters.oper = "look";
            }
            content = transObjToParam(params.parameters, content);
        }
        var index = layer.open({
            title: params.hiddenTitle?false:"查看" + params.name,
            type: params.layerType,
            skin: 'layui-layer-demo', //样式类名
            closeBtn: 1, //不显示关闭按钮
            shift: 2,
            maxmin: true,
            area: params.area,
            shadeClose: true, //开启遮罩关闭
            content: content,
            success: function (layero, index) {
                if (params.layerType != 2) {
                    $("input[data-type='date']").on("click", function () {
                        laydate({istime: false, format: 'YYYY-MM-DD', fixed : true})
                    });
                    iniDataToForm(row);
                    $("#signupForm").validate({
                        submitHandler: function () {
                            submitEvent(params.editUrl, index, "edit");
                        }
                    });
                }
                if (undefined != params.afterInitEditContent) {
                    params.afterInitEditContent();
                }
                $("button[type='submit']").css("display", "none");
                /*$("form input").attr("disabled", "disabled");
                 $("form select").attr("disabled", "disabled");
                 $("form textarea").attr("disabled", "disabled");*/

                $("#closeLayer").on("click", function () {
                    layer.close(index);
                })
            },
            end: function () {
                $("button[type='submit']").css("display", "");
                $("form input").removeClass("disabled");
                $("form select").removeClass("disabled");
                $("form textarea").removeClass("disabled");
            }
        });
    }

}

//转化对象成url参数
function transObjToParam(obj, url) {
    if (undefined == obj || null == obj || obj.length == 0) {
        return url;
    }
    if (url.indexOf("?") < 0) {
        url += "?time=" + Date.parse(new Date())
    }
    for (var key in obj) {
        var val = obj[key];
        if (null != val && undefined != val) {
            url += "&" + key + "=" + val;
        }
    }
    return url;
}

/**
 * 日期格式化
 * @param fmt
 * @returns {*}
 * @constructor
 */
Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1,                 //月份
        "d+": this.getDate(),                    //日
        "h+": this.getHours(),                   //小时
        "m+": this.getMinutes(),                 //分
        "s+": this.getSeconds(),                 //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds()             //毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

/**
 * 去除object中为null的值
 * @param data
 * @returns {{}}
 */
function trimNull(data) {
    if (undefined == data || null == data) {
        return data;
    }
    var result = {};
    $.each(data, function (key, val) {
        if (null != val && "" != val) {
            result[key] = val;
        }
    })
    return result;
}

/**
 * 初始化上传图片插件
 * @param params
 */
function initUploader(params) {
    // 初始化Web Uploader
    var uploader = WebUploader.create({
        auto: true,
        server: '/upload/imgUpload',
        pick: params.picker,
        accept: {
            title: 'Images',
            extensions: 'gif,jpg,jpeg,bmp,png',
            mimeTypes: 'image/*'
        }
    });
    // 当有文件添加进来的时候
    uploader.on('fileQueued', function (file) {
        var li = $(
                '<div id="' + file.id + '" class="file-item thumbnail">' +
                '<img>' +
                '<div class="info">' + file.name + '</div>' +
                '</div>'
            ),
            img = li.find('img');

        var list = $("#fileList");
        list.html(li);

        // 创建缩略图
        // 如果为非图片文件，可以不用调用此方法。
        // thumbnailWidth x thumbnailHeight 为 100 x 100
        uploader.makeThumb(file, function (error, src) {
            if (error) {
                img.replaceWith('<span>不能预览</span>');
                return;
            }

            img.attr('src', src);
        }, 60, 60);
    });

    // 文件上传过程中创建进度条实时显示。
    uploader.on('uploadProgress', function (file, percentage) {
        var li = $('#' + file.id),
            percent = li.find('.progress span');

        // 避免重复创建
        if (!percent.length) {
            percent = $('<p class="progress"><span></span></p>')
                .appendTo(li)
                .find('span');
        }

        percent.css('width', percentage * 100 + '%');
    });

    // 文件上传成功，给item添加成功class, 用样式标记上传成功。
    uploader.on('uploadSuccess', function (file, response) {
        $(params.picker).parent().find(".img-url").val(response.result);
        $('#' + file.id).addClass('upload-state-done');
    });

    // 文件上传失败，显示上传出错。
    uploader.on('uploadError', function (file) {
        layer.msg("图片上传失败");
        var li = $('#' + file.id),
            error = li.find('div.error');
        // 避免重复创建
        if (!error.length) {
            error = $('<div class="error"></div>').appendTo(li);
        }
        error.text('上传失败');
    });

// 完成上传完了，成功或者失败，先删除进度条。
    uploader.on('uploadComplete', function (file) {
        $('#' + file.id).find('.progress').remove();
    });
}


function transFormToObj(formData) {
    if (undefined != formData.startTime) {
        formData.startTime = new Date(formData.startTime);
    }
    if (undefined != formData.endTime) {
        formData.endTime = new Date(formData.endTime);
    }
    if (undefined != formData.sun || undefined != formData.mon || undefined != formData.tue || undefined != formData.wed || undefined != formData.thu || undefined != formData.fri || undefined != formData.sat) {
        formData.fitDays = "";
        formData.fitDaysCH = "星期";
        formData.fitDays += formData.sun ? "1" : "0";
        formData.fitDays += formData.mon ? "1" : "0";
        formData.fitDays += formData.tue ? "1" : "0";
        formData.fitDays += formData.wed ? "1" : "0";
        formData.fitDays += formData.thu ? "1" : "0";
        formData.fitDays += formData.fri ? "1" : "0";
        formData.fitDays += formData.sat ? "1" : "0";

        formData.fitDaysCH += formData.sun ? "日、" : "";
        formData.fitDaysCH += formData.mon ? "一、" : "";
        formData.fitDaysCH += formData.tue ? "二、" : "";
        formData.fitDaysCH += formData.wed ? "三、" : "";
        formData.fitDaysCH += formData.thu ? "四、" : "";
        formData.fitDaysCH += formData.fri ? "五、" : "";
        formData.fitDaysCH += formData.sat ? "六、" : "";
    }
    if (undefined != formData.discountType) {
        if (formData.discountType == 1) {
            formData.discountName = undefined != formData.minLine ? "满" + formData.minLine : "";
            formData.discountName += "打" + formData.percent / 10 + "折";
        } else if (formData.discountType == 2) {
            formData.discountName = undefined != formData.minLine ? "满" + formData.minLine : "";
            formData.discountName += "减" + formData.reduction + "元";
        } else if (formData.discountType == 3) {
            formData.discountName = "特价";
        }
    }
    if (undefined != formData.giftType) {
        if (formData.giftType == 1) {
            formData.giftName = "赠送" + formData.giftAmount + "元";
        } else if (formData.giftType == 2) {
            formData.giftName = "赠送" + formData.giftPercent + "%";
        }
    }
    return formData;
}
//关闭当前layer页
function closeCurrentLayerPage() {
    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
    parent.layer.close(index);
}
//根据已有的数组，生成id
function generateIdFromCurrentArray(array) {
    if (undefined == array || array.length == 0) {
        return 1;
    }
    var max = 0;
    $.each(array, function (i, item) {
        if (undefined != item.id && typeof item.id == "number" && item.id > max) {
            max = item.id;
        }
    })
    return max + 1;
}
/**
 * 排序后刷新表格数据
 * @param table
 */
function tableSortRefresh(table) {
    var trs = $(table).find("tbody").find("tr");
    var arrays = $(table).bootstrapTable("getData");
    var newArrays = [];
    $.each(trs, function (i, tr) {
        var index = $(tr).data("index");
        newArrays.push(arrays[index]);
    })
    $(table).bootstrapTable("load", newArrays);
}

/**
 * 绑定表格可编辑
 * @param dom
 */
function editableTable(field, value, row, ele, dom, usableValue) {
    var content = $(dom).find("input").val();
    //点击编辑框是返回
    if (undefined != content) {
        return;
    }
    //释放所有编辑框
    var inputs = $(".table-input");
    jQuery.each(inputs, function (i, input) {
        $(input).parent().html($(input).val())
    })
    if (row == undefined){
        row = "";
    }
    if (value == usableValue) {
        //添加当前编辑框
        $(dom).html("<input type='text' class='form-control table-input' value='" + row + "'/>");
        //绑定onblur事件
        $("input").on("blur", function () {
            var val = $(dom).find("input").val();
            $(dom).html(val);
            ele[value] = val;
            //alert(JSON.stringify(ele));
        })
    }
}
//判空
function isNull(value) {
    if (undefined == value || null == value || (typeof value == 'string'&&value.length == 0)) {
        return true;
    } else {
        return false;
    }
}

//删除表数据
function bindDeleteTr(button, table, index) {
    if(isNull(index)){
        index = "id";
    }
    $(button).on("click", function () {
        var rows = $(table).bootstrapTable("getSelections");
        if (rows.length == 0) {
            var singleRow = trimNull(getSelectedRow(table));
            if (!isNull(singleRow)) {
                rows = [];
                rows.push(singleRow);
            }
        }
        if (rows == undefined || rows.length == 0) {
            layer.msg("请先选中一行");
            return;
        }
        layer.confirm('确认删除？', {
            btn: ['确认', '取消'] //按钮
        }, function () {
            var indexs = [];
            jQuery.each(rows, function (i, row) {
                indexs.push(row[index]);
            })
            $(table).bootstrapTable('remove', {field: index, values: indexs});
            layer.closeAll('dialog');
        }, function () {

        });
    })
}

/**
 * bootstrap table返回数据过滤器
 * @param value
 * @returns {*}
 */
function responseHandler(value) {
    if (isNull(value)) {
        return [];
    }
    if (value.resultCode == "FAIL") {
        layer.alert(value.errCodeDesc);
        return [];
    }
    return value;
}
/**
 * bootstrap table加载remote数据出错处理
 * @param table
 */
function errorHandler(table){
    layer.msg("数据加载错误");
    $(table).bootstrapTable("removeAll")
}

function queryParams(params) {  //配置参数
    var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        pageSize: params.limit,   //页面大小
        pageNumber: params.pageNumber,  //页码
        minSize: $("#leftLabel").val(),
        maxSize: $("#rightLabel").val(),
        minPrice: $("#priceleftLabel").val(),
        maxPrice: $("#pricerightLabel").val(),
        /*Cut: Cut,
        Color: Color,
        Clarity: Clarity,*/
        sort: params.sort,  //排序列名
        sortOrder: params.order,//排位命令（desc，asc）
        deskName : "大厅1"
    };
    return temp;
}

function customSearch(text){
    alert(text)
    //return this.data;
}

function downloadFile(fileName, content){
    var aLink = document.createElement('a');
    //var blob = new Blob([content]);
    var evt = document.createEvent("HTMLEvents");
    evt.initEvent("click", false, false);//initEvent 不加后两个参数在FF下会报错, 感谢 Barret Lee 的反馈
    aLink.download = fileName;
    aLink.href = "data:text/csv;charset=utf-8,\ufeff"+encodeURIComponent(content);
    aLink.dispatchEvent(evt);
}


$(".showTips").mouseover(function(){
    var content = $(this).data("tip-content");
    var tip = layer.tips(content, $(this), {
        tips: [1, '#3595CC'],
        time: 4000
    });
    $(".showTips").mouseout(function(){
        layer.close(tip)
    })
})


