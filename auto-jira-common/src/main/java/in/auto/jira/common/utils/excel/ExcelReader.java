package in.auto.jira.common.utils.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader<T> {

	private Class<T> clazz;
	// private Boolean isHasFirst; // 是否有第一行
	/**
	 * 通过列索引进行
	 */
	private Map<Integer, Field> map = new HashMap<Integer, Field>();

	private Map<String, Field> strmap = new HashMap<String, Field>();

	/**
	 * 自定义的正则类型不匹配的错误提示字符串
	 */
	private static Map<String, String> regexMap = new HashMap<String, String>();
	// 为定义好的正则进行增加提示字符串
	static {
		Map<String, String> mymap = new HashMap<String, String>();
		mymap.put(ExcelPattern.MAIL, "不合法的邮件类型！");
		mymap.put(ExcelPattern.PHONE, "不合法的电话类型！");
		mymap.put(ExcelPattern.NUMBER, "不合法的数字类型！");
		mymap.put(ExcelPattern.IPV4, "不合法的ipv4地址类型！");
		mymap.put(ExcelPattern.URL, "不合法的url地址类型！");
		mymap.put(ExcelPattern.CHINESE, "只能是汉字！");
		mymap.put(ExcelPattern.CHINESE2, "不能拥有特殊字符！");
		mymap.put(ExcelPattern.ISNOTNUMBER, "不能为数字！");
		ExcelReader.regexMap = mymap;
	}

	public ExcelReader(Class<T> clazz) {
		this.clazz = clazz;
		// this.isHasFirst = isHasFirst;
		loadMap();
	}

	// 主方法
	public ExcelResult<T> readFromExcel(InputStream inputStream, String fileName)
			throws FileNotFoundException, IOException {
		ExcelResult<T> list = new ExcelResult<T>();
		try {
			Workbook work = this.createWb(inputStream, fileName);
			// 获得第一个
			Sheet sheet = this.getSheet(work, 0);
			list = this.listFromSheet(sheet);
		} catch (InstantiationException e) {
			list.getErrs().add(e.getMessage());
		} catch (IllegalAccessException e) {
			list.getErrs().add(e.getMessage());
		}
		return list;
	}

	// 主方法
	public ExcelResult<T> readFromExcel(InputStream inputStream, String fileName, int sheetIdx)
			throws FileNotFoundException, IOException {
		ExcelResult<T> list = new ExcelResult<T>();
		try {
			Workbook work = this.createWb(inputStream, fileName);
			// 获得第一个
			Sheet sheet = this.getSheet(work, sheetIdx);
			list = this.listFromSheet(sheet);
		} catch (InstantiationException e) {
			list.getErrs().add(e.getMessage());
		} catch (IllegalAccessException e) {
			list.getErrs().add(e.getMessage());
		}
		return list;
	}

	// 主方法
	public ExcelResult<T> readFromExcel(File file) throws FileNotFoundException, IOException {
		return readFromExcel(new FileInputStream(file), file.getName());
	}

	// 主方法
	public ExcelResult<T> readFromExcel(File file, int sheetIdx) throws FileNotFoundException, IOException {
		return readFromExcel(new FileInputStream(file), file.getName(), sheetIdx);
	}

	// 加载从loder中取到的方法
	private void loadMap() {
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			// 获得字段上的ExcelPattern注解
			ExcelPattern pattern = field.getAnnotation(ExcelPattern.class);
			// 如果此字段没有这个注解
			if (pattern == null) {
				continue;
			}
			// 判断列的索引是否有重复
			if (strmap.get(pattern.header().trim()) != null) {
				// 如果map有连个重复的key
				throw new CellIndexException("列的索引有重复：" + pattern.header());
			}
			// 判断列的索引是否小于0
			// if (pattern.cellIndex () < 0) { throw new CellIndexException
			// ("列的索引不可小于0：" + pattern.cellIndex ()); }
			strmap.put(pattern.header().trim(), field);
		}
	}

	/**
	 * 创建工作簿对象
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 * @date 2013-5-11
	 */
	@SuppressWarnings({ "unused", "resource" })
	private final Workbook createWb(String filePath) throws IOException {
		if (filePath == null || filePath.length() <= 0) {
			throw new IllegalArgumentException("参数错误!!!");
		}
		if (filePath.trim().toLowerCase().endsWith("xls")) {
			return new HSSFWorkbook(new FileInputStream(filePath));
		} else if (filePath.trim().toLowerCase().endsWith("xlsx")) {
			return new XSSFWorkbook(new FileInputStream(filePath));
		} else {
			throw new IllegalArgumentException("不支持除：xls/xlsx以外的文件格式!!!");
		}
	}

	/**
	 * 创建工作簿对象
	 * 
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @date 2013-5-11
	 */
	private final Workbook createWb(InputStream inputStream, String fileName)
			throws FileNotFoundException, IOException {
		if (inputStream == null) {
			throw new IllegalArgumentException("参数错误!!!");
		}
		if (fileName == null || fileName.length() <= 0) {
			throw new IllegalArgumentException("参数错误!!!");
		}
		if (fileName.toLowerCase().endsWith("xls")) {
			return new HSSFWorkbook(inputStream);
		} else if (fileName.trim().toLowerCase().endsWith("xlsx")) {
			return new XSSFWorkbook(inputStream);
		} else {
			throw new IllegalArgumentException("不支持除：xls/xlsx以外的文件格式!!!");
		}
	}

	// private final Sheet getSheet(Workbook wb,String sheetName){
	// return wb.getSheet (sheetName);
	// }

	private final Sheet getSheet(Workbook wb, int index) {
		return wb.getSheetAt(index);
	}

	private final ExcelResult<T> listFromSheet(Sheet sheet) throws InstantiationException, IllegalAccessException {
		// int rowTotal = sheet.getPhysicalNumberOfRows ();
		// Debug.printf ("{}共有{}行记录！", sheet.getSheetName (), rowTotal);
		// test

		// end test
		List<String> errlist = new ArrayList<String>();// 错误返回的信息
		List<T> list = new ArrayList<T>();
		// 是否有第一行
		int first = sheet.getFirstRowNum();
		// load第一行
		this.loadIndexMap(sheet, errlist);
		first++;// 从第二行开始
		for (int r = first; r <= sheet.getLastRowNum(); r++) {
			Row row = sheet.getRow(r);
			if (row == null || row.getLastCellNum() < 0)
				continue;
			// 不能用row.getPhysicalNumberOfCells()，可能会有空cell导致索引溢出
			// 使用row.getLastCellNum()至少可以保证索引不溢出，但会有很多Null值，如果使用集合的话，就不说了
			// test
			// 新实例
			T obj = (T) clazz.newInstance();
			// 循环进行设置
			for (Integer index : map.keySet()) {
				Cell cell = row.getCell(index);
				String err = getValueFromCell(cell, r, index, obj);
				// 返回的错误
				if (err != null) {
					errlist.add(err);
				}
			}
			// for ( int c = row.getFirstCellNum () ; c <= row.getLastCellNum ()
			// ; c++ ) {
			// Cell cell = row.getCell (c);
			// // if (cell == null) continue;
			// // 返回值为错误信息
			// String err = getValueFromCell (cell, r, c, obj);
			// }
			list.add(obj);
		}

		return new ExcelResult<T>(list, errlist);
	}

	// 读取第一行，进行设置index
	private void loadIndexMap(Sheet sheet, List<String> errlist) {
		if (sheet.getFirstRowNum() >= 0) {
			Row row = sheet.getRow(sheet.getFirstRowNum());
			for (Cell cell : row) {
				String value = cell.getStringCellValue() == null ? "" : cell.getStringCellValue();
				Field filed = this.strmap.get(value.trim());
				if (filed == null) {
					continue;
				} else {
					map.put(cell.getColumnIndex(), filed);
				}
			}
		}
	}

	/**
	 * 获取单元格内文本信息
	 * 
	 * @param cell
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @date 2013-5-8
	 */
	@SuppressWarnings("finally")
	private final String getValueFromCell(Cell cell, int rowIndex, int columnIndex, Object entity) {
		String err = null;
		Field field = this.map.get(columnIndex);
		// 获得验证注解
		ExcelPattern patt = field.getAnnotation(ExcelPattern.class);
		field.setAccessible(true);
		// 1.首先判断这个字段是否必须的
		if (cell == null) {
			// 1.1如果是必须的，则返回错误
			if (patt.require()) {
				err = this.makeError(rowIndex, columnIndex, "此列为必须的,不可为空");
			} else {
				// 1.2 如果不是必须的，则设置空
				try {
					field.set(entity, null);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			field.setAccessible(false);
			return err;
		}

		// 2.进行设置值
		// Class<? extends Object> fieldType = field.getType ();
		try {
			if (field.getType().equals(String.class)) {
				// 2.1 java.lang.String类型
				String str = null;
				try {
					str = this.getStringValue(cell);
					this.validate(field, str);
					field.set(entity, str);
				} catch (IllegalArgumentException | IllegalStateException e) {
					field.setAccessible(false);
					throw new IllegalArgumentException("应为 字符串 类型！却为 " + this.getFromType(cell));
				}
			} else if (field.getType().equals(Integer.class)) {
				// 2.2 java.lang.Integer
				try {
					double value = cell.getNumericCellValue();
					this.validate(field, String.valueOf(value));
					field.set(entity, new Double(value).intValue());
				} catch (IllegalArgumentException | IllegalStateException e) {
					field.setAccessible(false);
					throw new IllegalArgumentException("应为 int数字 类型！却为 " + this.getFromType(cell));
				}
			} else if (field.getType().equals(Double.class)) {
				// 2.3 java.lang.Double
				try {
					double value = cell.getNumericCellValue();
					this.validate(field, String.valueOf(value));
					field.set(entity, value);
				} catch (IllegalArgumentException | IllegalStateException e) {
					field.setAccessible(false);
					throw new IllegalArgumentException("应为 double数字 类型！却为 " + this.getFromType(cell));
				}
			} else if (field.getType().equals(Boolean.class)) {
				// 2.4 java.lang.Boolean
				try {
					field.set(entity, cell.getBooleanCellValue());
				} catch (IllegalArgumentException | IllegalStateException e) {
					field.setAccessible(false);
					throw new IllegalArgumentException("应为 boolean布尔 类型！却为 " + this.getFromType(cell));
				}
			} else if (field.getType().equals(Long.class)) {
				// 2.5 java.lang.Long
				try {
					double value = cell.getNumericCellValue();
					this.validate(field, String.valueOf(value));
					field.set(entity, new Double(value).longValue());
				} catch (IllegalArgumentException | IllegalStateException e) {
					field.setAccessible(false);
					throw new IllegalArgumentException("应为 long数字 类型！却为 " + this.getFromType(cell));
				}
			} else if (field.getType().equals(Date.class)) {
				// 2.5 java.lang.Date
				try {
					field.set(entity, cell.getDateCellValue());
				} catch (IllegalArgumentException | IllegalStateException e) {
					field.setAccessible(false);
					throw new IllegalArgumentException("应为 日期 类型！却为 " + this.getFromType(cell));
				}
			} else {
				// 2.6 为一个object类型，注入id
				Field idfield = field.getType().getDeclaredField("id");
				if (idfield == null) {
					field.setAccessible(false);
					throw new IllegalArgumentException("实体类没有id属性，无法注入！");
				} else {
					idfield.setAccessible(true);
					Object obj = field.getType().newInstance();
					try {
						idfield.set(field.getType().cast(obj), new Double(cell.getNumericCellValue()).longValue());
					} catch (IllegalArgumentException | IllegalStateException e) {
						throw new IllegalArgumentException("应为 Long 数字类型！却为 " + this.getFromType(cell));
					} finally {
						idfield.setAccessible(false);
					}
					field.set(entity, obj);
				}
			}
		} catch (IllegalArgumentException | IllegalStateException e) {
			// 字段类型不匹配异常
			err = this.makeError(rowIndex, columnIndex, "类型不匹配！" + e.getMessage());
		} catch (IllegalAccessException e) {
			err = this.makeError(rowIndex, columnIndex, "访问权限不足！" + e.getMessage());
		} catch (SizeOutException e) {
			// 字段大小超出范围异常
			err = this.makeError(rowIndex, columnIndex, e.getMessage());
		} catch (RegexNotMatchException e) {
			// 正则不匹配异常
			err = this.makeError(rowIndex, columnIndex, e.getMessage());
		} finally {
			field.setAccessible(false);
			return err;
		}
	}

	// 制作错误的字符串
	private String makeError(int rowIndex, int columnIndex, String err) {
		return "[ 第" + (rowIndex + 1) + "行,第" + (columnIndex + 1) + "列 ]" + " " + err;
	}

	// 用于验证的方法
	private void validate(Field field, String value) throws SizeOutException, RegexNotMatchException {
		// Size size = field.getAnnotation (Size.class);
		// // 验证长度
		// if (size != null) {
		// if (value.length () > size.max ()) { throw new SizeOutException
		// ("长度高于指定最大长度！"); }
		// if (value.length () < size.min ()) { throw new SizeOutException
		// ("长度低于指定最小长度！"); }
		// }
		// 验证正则
		ExcelPattern patt = field.getAnnotation(ExcelPattern.class);
		if (!value.matches(patt.pattern())) {
			// 正则验证不通过进行提示验证信息
			String message = "";
			// 如果配置了提示的字符串，则使用配置的字符串
			if (patt.message() != null && patt.message().length() > 0) {
				message = patt.message();
			} else {
				message = ExcelReader.regexMap.get(patt.pattern());
				if (message == null) {
					message = "";
				}
			}
			throw new RegexNotMatchException("格式不匹配！" + message);
		}
	}

	// 获得字符串的值。
	private String getStringValue(Cell cell) {
		if (cell == null) {
			return null;
		}
		String value = null;
		switch (cell.getCellType()) {
			case NUMERIC: // 数字
				if (HSSFDateUtil.isCellDateFormatted(cell)) { // 如果是日期类型
					value = new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue());
				} else
					value = String.valueOf(cell.getNumericCellValue());
				break;
			case STRING: // 字符串
				value = cell.getStringCellValue();
				break;
			case FORMULA: // 公式
				// 用数字方式获取公式结果，根据值判断是否为日期类型
				double numericValue = cell.getNumericCellValue();
				if (HSSFDateUtil.isValidExcelDate(numericValue)) { // 如果是日期类型
					value = new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue());
				} else
					value = String.valueOf(numericValue);
				break;
			case BLANK: // 空白
				value = "";
				break;
			case BOOLEAN: // Boolean
				value = String.valueOf(cell.getBooleanCellValue());
				break;
			case ERROR: // Error，返回错误码
				value = String.valueOf(cell.getErrorCellValue());
				break;
			default:
				value = "";
				break;
		}
		return value;
	}

	// 获得单元格的类型
	private String getFromType(Cell cell) {
		String value = "";
		switch (cell.getCellType()) {
			case NUMERIC: // 数字
				value = "数字";
				break;
			case STRING: // 字符串
				value = "字符串";
				break;
			case FORMULA: // 公式
				// 用数字方式获取公式结果，根据值判断是否为日期类型
				value = "公式";
				break;
			case BLANK: // 空白
				value = "空白";
				break;
			case BOOLEAN: // Boolean
				value = "布尔";
				break;
			case ERROR: // Error，返回错误码
				value = "错误";
				break;
			default:
				value = "";
				break;
		}
		return value;
	}
}

// 列索引重复异常
class CellIndexException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CellIndexException() {
		super();
	}

	public CellIndexException(String message) {
		super(message);
	}
}

// 长度不符合异常
class SizeOutException extends Exception {

	private static final long serialVersionUID = 1L;

	public SizeOutException() {
		super();
	}

	public SizeOutException(String message) {
		super(message);
	}
}

// 正则不匹配异常
class RegexNotMatchException extends Exception {

	private static final long serialVersionUID = 1L;

	public RegexNotMatchException() {
		super();
	}

	public RegexNotMatchException(String message) {
		super(message);
	}
}
