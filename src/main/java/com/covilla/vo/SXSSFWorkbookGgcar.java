package com.covilla.vo;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class SXSSFWorkbookGgcar{
	
	private SXSSFWorkbook sXSSFWorkbook;
	
	/**
	 * Excel文件名称
	 */
	public String bookFileName;

	public String getBookFileName() {
		return bookFileName;
	}

	public void setBookFileName(String bookFileName) {
		this.bookFileName = bookFileName;
	}

	public SXSSFWorkbook getsXSSFWorkbook() {
		return sXSSFWorkbook;
	}

	public void setsXSSFWorkbook(SXSSFWorkbook sXSSFWorkbook) {
		this.sXSSFWorkbook = sXSSFWorkbook;
	}
}
