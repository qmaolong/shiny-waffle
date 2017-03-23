/**
 * add by zengshibin 2015-03-16
 * 功能描述:EXCEL工具类
 * 版本：1.0
 */
package com.covilla.util;

import com.covilla.util.wechat.util.StringUtil;
import com.covilla.vo.SXSSFWorkbookGgcar;
import com.covilla.vo.SheetBean;
import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 功能描述:EXCEL工具类,用于导入和导出excel数据
 * 
 * @author zengshibin
 */
public class ExcelUtil {

	private static Logger log = LoggerFactory.getLogger(ExcelUtil.class);

	/**
	 * 
	 * @param fileName
	 *            报表名称
	 * @param fieldsName
	 *            报表字段名称
	 * @param fields
	 *            EXCEL字段集合，以逗号分隔
	 * @param dataList
	 *            要导入EXCEL的数据，一个MAP一行数据
	 * @return
	 */
	public static SXSSFWorkbook createExcelXls(String fileName,
			String fieldsName, String fields,
			List<Map<String, Object>> dataList, HttpServletResponse resp)
			throws Exception {
		SXSSFWorkbook wb = null;
		SXSSFSheet sheet = null;
		try {
			if (fields != null) {
				resp.setHeader("Content-Disposition", "attachment;filename=\""
						+ URLEncoder.encode(fileName, "UTF-8") + ".xlsx" + "\"");

				// 获取字段名称集合
				String[] fieldNameArray = fieldsName.split(",");
				if (fieldNameArray != null && fieldNameArray.length > 0) {
					wb = new SXSSFWorkbook(100);
					sheet = (SXSSFSheet) wb.createSheet(fileName);
					SXSSFRow row = null;

					// 创建首行
					row = (SXSSFRow) sheet.createRow(0);
					for (int i = 0; i < fieldNameArray.length; i++) {

						// 创建一个单元格
						row.createCell(i).setCellValue(fieldNameArray[i]);
					}

					// 通过集合添加数据
					String[] fieldsArray = fields.split(",");
					if (dataList != null) {
						for (int dataNum = 0; dataNum < dataList.size(); dataNum++) {

							// 再建一行数据
							row = (SXSSFRow) sheet.createRow(dataNum + 1);
							for (int i = 0; i < fieldsArray.length; i++) {
								Cell cell = row.createCell(i);
								// cell.setCellType(XSSFCell.CELL_TYPE_STRING);

								// 获取字段值
								Object cellValue = dataList.get(dataNum).get(
										fieldsArray[i]);
								if (null != cellValue) {
									row.createCell(i).setCellValue(
											cellValue.toString());
								} else {
									row.createCell(i).setCellValue("");
								}
							}
						}
					}
				}
			}
			wb.write(resp.getOutputStream());
		} catch (Exception e) {
			log.error("导出Excel时异常！", e);
			throw e;
		} finally {
			try {
				resp.getOutputStream().close();
			} catch (IOException e) {
				log.error("导出Excel时异常！", e);
				throw e;
			}
		}
		return wb;
	}

	/**
	 * 根据数据生成EXCEL列表
	 * 
	 * @param fileName
	 *            报表名称
	 * @param titleNames
	 *            报表字段名称
	 * @param fields
	 *            EXCEL字段集合，以逗号分隔
	 * @param dataList
	 *            要导入EXCEL的数据，一个MAP一行数据
	 * @return
	 * @throws IOException
	 */
	/**
	 * public static WritableWorkbook createExcelXls(String fileName, String
	 * titleNames, String fields, List<Map<String, Object>> dataList,
	 * HttpServletResponse resp) throws Exception { log.info("开始导出" + fileName +
	 * "。xls文件!"); WritableWorkbook wwb = null; WritableSheet ws = null; try {
	 * resp.setHeader("Content-Disposition", "attachment;filename=\"" +
	 * URLEncoder.encode(fileName, "UTF-8") + ".xls" + "\""); wwb =
	 * Workbook.createWorkbook(resp.getOutputStream()); ws =
	 * wwb.createSheet(fileName, 0); ws.getSettings().setDefaultColumnWidth(18);
	 * int rowsNum = 0;// excel行数 WritableFont wfc2 = new
	 * WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false,
	 * UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK); WritableCellFormat
	 * wcfFC2 = new WritableCellFormat(wfc2);
	 * wcfFC2.setAlignment(jxl.format.Alignment.CENTRE);// 居中 String[]
	 * fieldNames = titleNames.split(","); int titleLength = fieldNames.length;
	 * 
	 * // 添加第一行数据，第一行是字段名称集合 if (fieldNames != null && titleLength > 0) { for
	 * (int i = 0; i < titleLength; i++) {// 第三行处理 String tableTitle =
	 * fieldNames[i]; Label label = new Label(i, rowsNum, tableTitle, wcfFC2);
	 * ws.addCell(label); } }
	 * 
	 * rowsNum++; WritableCellFormat wcfFC3 = new WritableCellFormat();
	 * wcfFC3.setAlignment(jxl.format.Alignment.CENTRE);// 居中 int dataLength =
	 * dataList.size();
	 * 
	 * // 通过集合添加数据 String[] fieldsArray = fields.split(","); if (dataLength > 0)
	 * {// 表单 for (int j = 0; j < dataLength; j++) { for (int k = 0; k <
	 * titleLength; k++) { String field = fieldsArray[k]; Object obj =
	 * dataList.get(j).get(field); if (null == obj ||
	 * "null".equals(obj.toString())) { obj = new String(); } ws.addCell(new
	 * Label(k, rowsNum, obj.toString(), wcfFC3)); } rowsNum++; } } wwb.write();
	 * resp.getOutputStream().flush(); log.info("导出" + fileName + "。xls文件成功!");
	 * } catch (IOException e) { log.error("导出Excel时异常！", e); throw e; } catch
	 * (RowsExceededException e) { log.error("导出Excel时异常！", e); throw e; } catch
	 * (WriteException e) { log.error("导出Excel时异常！", e); throw e; } catch
	 * (Exception e) { log.error("导出Excel时异常！", e); throw e; } finally {
	 * 
	 * try { wwb.close(); resp.getOutputStream().close(); } catch
	 * (WriteException e) { log.error("导出Excel时异常！", e); throw e; } catch
	 * (IOException e) { log.error("导出Excel时异常！", e); throw e; } } return wwb; }
	 */

	/**
	 * 根据数据生成EXCEL列表,此方法可以根据统计字段进行统计
	 * 
	 * @param fileName
	 *            报表名称
	 * @param titleNames
	 *            报表字段名称
	 * @param fields
	 *            EXCEL字段集合，以逗号分隔
	 * @param dataList
	 *            要导入EXCEL的数据，一个MAP一行数据
	 * @param statisFields
	 *            需要统计的字段，多个字段以逗号隔开
	 * @return
	 * @throws IOException
	 */
	public static SXSSFWorkbook createExcelXlsWithSum(String fileName,
			String titleNames, String fields,
			List<Map<String, Object>> dataList, HttpServletResponse resp,
			String statisFields) throws Exception {
		log.info("开始导出" + fileName + "。xls文件!");
		SXSSFWorkbook wwb = null;
		SXSSFSheet ws = null;
		try {
			resp.setHeader("Content-Disposition", "attachment;filename=\""
					+ fileName + ".xls" + "\"");
			wwb = new SXSSFWorkbook(100);
			ws = (SXSSFSheet) wwb.createSheet(fileName);
			ws.setDefaultColumnWidth(18);
			WritableFont wfc2 = new WritableFont(WritableFont.ARIAL, 10,
					WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
					jxl.format.Colour.BLACK);
			WritableCellFormat wcfFC2 = new WritableCellFormat(wfc2);
			wcfFC2.setAlignment(jxl.format.Alignment.CENTRE);// 居中
			String[] fieldNames = titleNames.split(",");
			int titleLength = fieldNames.length;

			// 添加第一行数据，第一行是字段名称集合
			if (fieldNames != null && titleLength > 0) {
				SXSSFRow row = (SXSSFRow) ws.createRow(0);
				for (int i = 0; i < titleLength; i++) {// 第三行处理

					String tableTitle = fieldNames[i];
					row.createCell(i).setCellValue(tableTitle);
				}
			}
			WritableCellFormat wcfFC3 = new WritableCellFormat();
			wcfFC3.setAlignment(jxl.format.Alignment.CENTRE);// 居中
			int dataLength = dataList.size();

			// 通过集合添加数据
			String[] fieldsArray = fields.split(",");

			// 获取需要统计的字段，并生成对应的MAP
			String[] statisFieldsArray = statisFields.split(",");
			Map<String, Double> statisMap = new HashMap<String, Double>();
			for (String statisField : statisFieldsArray) {
				statisMap.put(statisField, new Double(0));
			}
			if (dataLength > 0) {// 表单
				for (int j = 0; j < dataLength; j++) {
					SXSSFRow row = (SXSSFRow) ws.createRow(j + 1);
					for (int k = 0; k < titleLength; k++) {
						String field = fieldsArray[k];
						Object obj = dataList.get(j).get(field);
						if (null == obj || "null".equals(obj.toString())) {
							obj = new String();
						}

						// 针对需要统计的字段进行叠加
						Double statisValue = statisMap.get(field);
						if (null != statisValue
								&& !StringUtil.isEmpty(obj.toString())) {
							statisMap
									.put(field,
											statisValue
													+ Double.parseDouble(obj
															.toString()));
						}
						row.createCell(k).setCellValue(obj.toString());
					}
				}
				SXSSFRow row = (SXSSFRow) ws.createRow(dataLength + 1);
				for (int k = 0; k < titleLength; k++) {
					String field = fieldsArray[k];

					// 针对需要统计的字段进行叠加
					Double statisValue = statisMap.get(field);
					if (null == statisValue) {
						if (k == 0) {
							row.createCell(k).setCellValue("总计");
						} else {
							row.createCell(k).setCellValue("");
						}
					} else {
						statisValue = Math.round(statisValue * 100) / 100.0;
						row.createCell(k).setCellValue(statisValue.toString());
					}
				}
			}
			wwb.write(resp.getOutputStream());
			log.info("导出" + fileName + "。xls文件成功!");
		} catch (IOException e) {
			log.error("导出Excel时异常！", e);
			throw e;
		} catch (Exception e) {
			log.error("导出Excel时异常！", e);
			throw e;
		} finally {
			try {
				resp.getOutputStream().close();
			} catch (IOException e) {
				log.error("导出Excel时异常！", e);
				throw e;
			}
		}
		return wwb;
	}

	/**
	 * 根据数据生成EXCEL列表
	 * 
	 * @param <T>
	 * @param fileName
	 *            报表名称
	 * @param titleNames
	 *            报表字段名称
	 * @param fields
	 *            EXCEL字段集合，以逗号分隔
	 * @param dataList
	 *            要导入EXCEL的数据，一个MAP一行数据
	 * @return
	 * @throws IOException
	 */
	public static <T> WritableWorkbook createExcel(String fileName,
			String titleNames, String fields, Collection<T> dataList,
			HttpServletResponse resp) throws Exception {
		log.info("开始导出" + fileName + "。xls文件!");
		WritableWorkbook wwb = null;
		WritableSheet ws = null;
		try {
			resp.setHeader("Content-Disposition", "attachment;filename=\""
					+ URLEncoder.encode(fileName, "UTF-8") + ".xls" + "\"");
			wwb = Workbook.createWorkbook(resp.getOutputStream());
			ws = wwb.createSheet(fileName, 0);
			ws.getSettings().setDefaultColumnWidth(18);
			int rowsNum = 0;// excel行数
			WritableFont wfc2 = new WritableFont(WritableFont.ARIAL, 10,
					WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
					jxl.format.Colour.BLACK);
			WritableCellFormat wcfFC2 = new WritableCellFormat(wfc2);
			wcfFC2.setAlignment(jxl.format.Alignment.CENTRE);// 居中
			String[] fieldNames = titleNames.split(",");
			int titleLength = fieldNames.length;

			// 添加第一行数据，第一行是字段名称集合
			if (fieldNames != null && titleLength > 0) {
				for (int i = 0; i < titleLength; i++) {// 第三行处理
					String tableTitle = fieldNames[i];
					Label label = new Label(i, rowsNum, tableTitle, wcfFC2);
					ws.addCell(label);
				}
			}

			rowsNum++;
			WritableCellFormat wcfFC3 = new WritableCellFormat();
			wcfFC3.setAlignment(jxl.format.Alignment.CENTRE);// 居中

			Iterator<T> it = dataList.iterator();
			// 通过集合添加数据
			String[] fieldsArray = fields.split(",");
			while (it.hasNext()) {
				T t = (T) it.next();
				Class tCls = t.getClass();
				for (int k = 0; k < titleLength; k++) {
					String field = fieldsArray[k];
					Object value = null;
					if (field.contains(".")) {
						String[] subFieldArr = field.split("\\.");
						if (ValidatorUtil.isNotNull(subFieldArr)) {
							String pGetMethodName = "get"
									+ subFieldArr[0].substring(0, 1)
											.toUpperCase()
									+ subFieldArr[0].substring(1);
							Method pGetMethod = tCls.getMethod(pGetMethodName,
									new Class[] {});
							Object obj = pGetMethod.invoke(t, new Object[] {});

							String sGetMethodName = "get"
									+ subFieldArr[1].substring(0, 1)
											.toUpperCase()
									+ subFieldArr[1].substring(1);

							Method sGetMethod = obj.getClass().getMethod(
									sGetMethodName, new Class[] {});
							value = sGetMethod.invoke(obj, new Object[] {});
						}
					} else {
						String getMethodName = "get"
								+ field.substring(0, 1).toUpperCase()
								+ field.substring(1);
						Method getMethod = tCls.getMethod(getMethodName,
								new Class[] {});
						value = getMethod.invoke(t, new Object[] {});
					}

					String textValue = "";
					if (value instanceof Date) {
						textValue = DateUtil.formateDateToStr((Date) value,
								"yyyyMMdd HH:mm:ss");
					} else {
						textValue = null != value ? String.valueOf(value) : "";
					}
					ws.addCell(new Label(k, rowsNum, textValue, wcfFC3));
				}
				rowsNum++;
			}

			wwb.write();
			resp.getOutputStream().flush();
			log.info("导出" + fileName + "。xls文件成功!");
		} catch (IOException e) {
			log.error("导出Excel时异常！", e);
			throw e;
		} catch (RowsExceededException e) {
			log.error("导出Excel时异常！", e);
			throw e;
		} catch (WriteException e) {
			log.error("导出Excel时异常！", e);
			throw e;
		} catch (Exception e) {
			log.error("导出Excel时异常！", e);
			throw e;
		} finally {

			try {
				wwb.close();
				resp.getOutputStream().close();
			} catch (WriteException e) {
				log.error("导出Excel时异常！", e);
				throw e;
			} catch (IOException e) {
				log.error("导出Excel时异常！", e);
				throw e;
			}
		}
		return wwb;
	}

	/**
	 * 获取单元格数据内容为字符串类型的数据
	 * 
	 * @param cell
	 * @return
	 */
	public static String getStringCellValue(XSSFCell cell) {
		if (cell == null) {
			return "";
		}
		String strCell = "";
		switch (cell.getCellType()) {
		case XSSFCell.CELL_TYPE_STRING:
			strCell = cell.getStringCellValue();
			break;
		case XSSFCell.CELL_TYPE_NUMERIC:
			strCell = String.valueOf(cell.getNumericCellValue());
			break;
		case XSSFCell.CELL_TYPE_BOOLEAN:
			strCell = String.valueOf(cell.getBooleanCellValue());
			break;
		case XSSFCell.CELL_TYPE_BLANK:
			strCell = "";
			break;
		default:
			strCell = "";
			break;
		}
		return strCell;
	}

	/**
	 * add by zengshibin 解析导入的EXCEL数据
	 * 
	 * @param importFile
	 *            导入文件
	 * @param fieldNames
	 *            字段名称列表
	 * @throws Exception
	 */
	public static List<Map<String, String>> analysisImportFile(
            MultipartFile importFile, String fieldNames) throws Exception {
		log.info("开始分析导入文件数据!");
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		XSSFWorkbook readwb = null;
		try {
			readwb = new XSSFWorkbook(importFile.getInputStream());

			// 获取第一页数据
			XSSFSheet firstSheet = readwb.getSheetAt(0);

			// 获取总行数
			int rows = firstSheet.getLastRowNum();

			// 字段集合
			String[] fields = fieldNames.split(",");

			// 获取字段个数
			int cloums = fields.length;

			// 从第二行开始取值
			for (int i = 1; i <= rows; i++) {
				Map<String, String> keyValueMap = new HashMap<String, String>();
				XSSFRow row = firstSheet.getRow(i);
				XSSFCell cellContent = row.getCell(0);
				
				//遇到空行则跳出循环
				if (null == cellContent || XSSFCell.CELL_TYPE_STRING == cellContent.getCellType() && StringUtils.isEmpty(cellContent.getStringCellValue())) {
					break;
				}
				for (int j = 0; j < cloums; j++) {
					XSSFCell cell = row.getCell(j);
					if (null != cell) {

						// 行记录放一个MAP,map中放字段名称和取值
						if (XSSFCell.CELL_TYPE_STRING == cell.getCellType()) {
							keyValueMap.put(fields[j],
									cell.getStringCellValue());
						} else if (XSSFCell.CELL_TYPE_NUMERIC == cell
								.getCellType()) {
							if(HSSFDateUtil.isCellDateFormatted(cell)){
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								String dateValue = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())).toString();
								keyValueMap.put(fields[j],dateValue);
							}else{
								keyValueMap.put(fields[j],cell.getNumericCellValue() + "");
							}
						} else {
							keyValueMap.put(fields[j], "");
						}
					} else {
						keyValueMap.put(fields[j], null);
					}
				}
				if (keyValueMap.size() > 0) {
					resultList.add(keyValueMap);
				}
			}

		} catch (Exception e) {
			log.info("导入文件数据分析失败!");
			throw e;
		}

		log.info("导入文件数据分析成功!");
		return resultList;
	}

	/**
	 * add by zengshibin 2016-02-25 生成ggcar专用的Excel(暂时用于发邮件)
	 * 
	 * @param fileName
	 *            报表名称
	 *            要导入EXCEL的数据，一个MAP一行数据,列表大小与sheet也个数有关
	 * @return
	 */
	public static SXSSFWorkbookGgcar createMutilExcels(String fileName,List<SheetBean> sheetBeanList) throws Exception {
		SXSSFWorkbookGgcar result = new SXSSFWorkbookGgcar();
		result.setBookFileName(fileName + ".xlsx");
		SXSSFWorkbook wb = null;
		try {
			if (CollectionUtils.isNotEmpty(sheetBeanList)) {
				wb = new SXSSFWorkbook(100);
				for (SheetBean sheetBean : sheetBeanList) {
					String fields = sheetBean.getFields();
					
					// 获取需要统计的字段，并生成对应的MAP
					String statisFields = sheetBean.getStatisFields();
					Map<String, Double> statisMap = null;
					if(StringUtils.isNotEmpty(statisFields)){
						String[] statisFieldsArray = statisFields.split(",");
						if(null != statisFieldsArray){
							statisMap = new HashMap<String, Double>();
							for (String statisField : statisFieldsArray) {
								statisMap.put(statisField, new Double(0));
							}
						}
					}
					if (StringUtils.isNotEmpty(fields)) {
						
						// 获取字段名称集合
						String[] fieldNameArray = sheetBean.getTitleNames().split(",");
						if (fieldNameArray != null && fieldNameArray.length > 0) {
							
							SXSSFSheet sheet = (SXSSFSheet) wb.createSheet(sheetBean.getSheetName());
							SXSSFRow row = null;
							
							// 创建首行
							row = (SXSSFRow) sheet.createRow(0);
							for (int i = 0; i < fieldNameArray.length; i++) {

								// 创建一个单元格
								row.createCell(i).setCellValue(
										fieldNameArray[i]);
								sheet.setColumnWidth(i, fieldNameArray[i].length()*650);
							}

							// 通过集合添加数据
							String[] fieldsArray = fields.split(",");
							List<Map<String, Object>> dataList = sheetBean.getDataList();
							if (dataList != null) {
								for (int dataNum = 0; dataNum < dataList.size(); dataNum++) {

									// 再建一行数据
									row = (SXSSFRow) sheet
											.createRow(dataNum + 1);
									for (int i = 0; i < fieldsArray.length; i++) {
										// cell.setCellType(XSSFCell.CELL_TYPE_STRING);

										// 获取字段值
										Object cellValue = dataList
												.get(dataNum).get(
														fieldsArray[i]);
										if (null != cellValue) {
											if(cellValue instanceof Integer || cellValue instanceof BigDecimal || cellValue instanceof Double){
												row.createCell(i).setCellValue(Double.parseDouble(cellValue.toString()));
											}else{
												row.createCell(i).setCellValue(cellValue.toString());
											}
										} else {
											row.createCell(i).setCellValue("");
										}
										
										if(null != statisMap){
											
											// 针对需要统计的字段进行叠加
											Double statisValue = statisMap.get(fieldsArray[i]);
											if (null != statisValue
													&& !StringUtil.isEmpty(String.valueOf(cellValue))) {
												statisMap
														.put(fieldsArray[i],
																statisValue
																		+ Double.parseDouble(cellValue
																				.toString()));
											}
										}
									}
								}
								
								//添加统计字段
								if(null != statisMap){
									SXSSFRow totalRow = (SXSSFRow) sheet.createRow(dataList.size() + 1);
									int titleLength = fieldsArray.length;
									for (int k = 0; k < titleLength; k++) {
										String field = fieldsArray[k];

										// 针对需要统计的字段进行叠加
										Double statisValue = statisMap.get(field);
										if (null == statisValue) {
											if (k == 0) {
												totalRow.createCell(k).setCellValue("总计");
											} else {
												totalRow.createCell(k).setCellValue("");
											}
										} else {
											statisValue = Math.round(statisValue * 100) / 100.0;
											totalRow.createCell(k).setCellValue(statisValue);
										}
									}
								}
							}
							
						}
					}
				}
			}
			if (null == wb) {
				return null;
			} else {
				result.setsXSSFWorkbook(wb);
			}
		} catch (Exception e) {
			log.error("导出Excel时异常,可能是sheet页名称列表、数据列表、报表字段名称列表及字段列表长度不一致导致！", e);
			throw e;
		}
		return result;
	}
	
	/**
	 * 整理Sheet页数据
	 * 
	 * @param dataList
	 * @param sheetName
	 * @param fields
	 * @param titleNames
	 * @return
	 */
	public static SheetBean getSheetBean(List<Map<String, Object>> dataList,
			String sheetName, String fields, String titleNames) {
		SheetBean sheetBean = new SheetBean();
		sheetBean.setDataList(dataList);
		sheetBean.setSheetName(sheetName);
		sheetBean.setFields(fields);
		sheetBean.setTitleNames(titleNames);
		return sheetBean;
	}
}
