/**
 * 自定义左右选择框
 * Created by qmaolong on 2016/9/16.
 */
var MySelector = {
    tableData1 : [],
    tableData2 : parent.data,
    getTableData1 : function(){
        var data1 = $("#notSelectedTable").bootstrapTable("getData");
        return data1;
    },
    getTableData2 : function(){
        var data2 = $("#selectedTable").bootstrapTable("getData");
        return data2;
    },
    refreshUnselectedTable : null,
    //初始化
    init : function(params){
        MySelector.refreshUnselectedTable = params.refreshUnselectedTable;
    },
    //过滤掉data1中已在data2存在的数据
    filterData1ExitInData2 : function(data1, data2){
        if(undefined == data2 || null == data2 || [] == data2){
            return data1;
        }
        var result = [];
        $.each(data1, function(i, item1){
            var checked = false
            $.each(data2, function(j, item2){
                if(undefined != item1 && item1.id == item2.id){
                    checked = true;
                    return true;
                }
            })
            if(!checked){
                result.push(item1);
            }
        })
        return result;
    },
    filterCurrentCateInData2 : function(){
        var childrenId = $("#childrenId").val();

    },
    getSelectedRow : function(table){
        if(table == undefined){
            table = "table";
        }
        var index = $(table).find('tr.success').data('index');
        return $(table).bootstrapTable('getData')[index];
    },
    transDataToParent : function(){
        var selected = $("#selectedTable").bootstrapTable("getData");
        parent.data = selected;
        parent.$('#table').bootstrapTable("load", {
            data: selected
        });
    }
}

//选中全部
$("#keepRenderingSort_rightAll").on("click", function(){
    MySelector.tableData1 = MySelector.getTableData1();
    MySelector.tableData1 = MySelector.filterData1ExitInData2(MySelector.tableData1, MySelector.tableData2);
    $("#selectedTable").bootstrapTable("prepend", MySelector.tableData1);
    $("#notSelectedTable").bootstrapTable("removeAll");
})

//取消全部
$("#keepRenderingSort_leftAll").on("click", function(){
    var childrenId = $("#childrenId").val();
    var data2 = $("#selectedTable").bootstrapTable("getData");
    if([] != MySelector.tableData2){
        var ids = [];
        $.each(data2, function(i, row){
            if(undefined == childrenId || undefined != row && row.id.indexOf(childrenId) == 0){
                ids.push(row.id);
                $("#notSelectedTable").bootstrapTable("prepend", row);
            }
        })
        console.log(ids);
        $("#selectedTable").bootstrapTable('remove', {field: 'id', values: ids});
    }
})
//选中
$("#keepRenderingSort_rightSelected").on("click", function(){
    var selectedRow = $("#notSelectedTable").bootstrapTable("getSelections");
    if(selectedRow.length == 0){
        var clicked = MySelector.getSelectedRow("#notSelectedTable");
        if(undefined != clicked)
            selectedRow.push(clicked);
    }
    if(undefined == selectedRow || selectedRow.length == 0){
        layer.msg("请先选中数据");
        return;
    }
    var ids = [];
    $.each(selectedRow, function(i, row){
        row.state = false;
        ids.push(row.id);
        $("#selectedTable").bootstrapTable("prepend", row);
    })
    $("#notSelectedTable").bootstrapTable('remove', {field: 'id', values: ids});
})

//取消
$("#keepRenderingSort_leftSelected").on("click", function(){
    var selectedRow = $("#selectedTable").bootstrapTable("getSelections");
    if(selectedRow.length == 0){
        var clicked = MySelector.getSelectedRow("#selectedTable");
        if(undefined != clicked)
            selectedRow.push(clicked);
    }
    if(undefined == selectedRow || selectedRow.length == 0){
        layer.msg("请先选中数据");
        return;
    }
    var ids = [];
    $.each(selectedRow, function(i, row){
        row.state = false;
        ids.push(row.id);
        $("#notSelectedTable").bootstrapTable("prepend", row);
    })
    $("#selectedTable").bootstrapTable('remove', {field: 'id', values: ids});
})

