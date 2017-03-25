package com.covilla.vo;

import java.util.List;
import java.util.Map;

public class SheetBean {
	
	//每页的名称
	private String sheetName;
	
	//字段列表
	private String fields;
	
	//每页首行字段名称——大部分为中文名称
	private String titleNames;
	
	//每页的数据
	private List<Map<String, Object>> dataList;
	
	//需要统计计算的字段列表
	private String statisFields;
	
	public SheetBean(){
		
	}

	public SheetBean(String sheetName, String fields, String titleNames,
			List<Map<String, Object>> dataList) {
		this.sheetName = sheetName;
		this.fields = fields;
		this.titleNames = titleNames;
		this.dataList = dataList;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

	public String getTitleNames() {
		return titleNames;
	}

	public void setTitleNames(String titleNames) {
		this.titleNames = titleNames;
	}

	public List<Map<String, Object>> getDataList() {
		return dataList;
	}

	public void setDataList(List<Map<String, Object>> dataList) {
		this.dataList = dataList;
	}

	public String getStatisFields() {
		return statisFields;
	}

	public void setStatisFields(String statisFields) {
		this.statisFields = statisFields;
	}
}
