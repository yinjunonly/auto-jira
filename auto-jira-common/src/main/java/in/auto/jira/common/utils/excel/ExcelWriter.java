package in.auto.jira.common.utils.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class ExcelWriter {

    private Class<?> clazz;
    private Collection<?> list; // 集合
    private String fileType; // 文件类型，xls、xlsx
    private Field[] fields;
    // 静态变量
    public final static String XLS = "xls";
    public final static String XLSX = "xlsx";

    // 配置
    private final int columnWidth = 4000;
    private final String dateFormat = "yyyy-MM-dd";
    private final String fontType = "仿宋_GB2312";
    // 用于标题的样式
    private CellStyle headerStyle;
    // 用于正文的样式
    private CellStyle bodyStyle;

    // private String fileName;

    public ExcelWriter(Class<?> clazz, Collection<?> list, String fileType) {
        super();
        this.clazz = clazz;
        this.list = list;
        this.fileType = fileType;
        loadFileds();
    }

    public ExcelWriter(Class<?> clazz, Collection<?> list) {
        super();
        this.clazz = clazz;
        this.list = list;
        this.fileType = "xls";
        loadFileds();
    }

    // 得到所有拥有注解的字段
    private void loadFileds() {
        // 得到字段
        Field[] fields = clazz.getDeclaredFields();
        List<Field> list = new ArrayList<Field>();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            ExcelPattern patt = field.getAnnotation(ExcelPattern.class);
            if (patt == null) {
                continue;
            } else {
                list.add(field);
            }
        }
        this.fields = list.toArray(new Field[list.size()]);
    }

    // 获得工作簿
    private Workbook getWorkBook(String type) throws IllegalArgumentException {

        if (type != null && type.toLowerCase().equals("xls")) {
            return new HSSFWorkbook();
        } else if (type != null && type.equals("xlsx")) {
            return new XSSFWorkbook();
        } else {
            throw new IllegalArgumentException("不支持除：xls/xlsx以外的文件格式!!!");
        }
    }

    // 读取到excel
    /**
     * @discription 用于将集合写入到文件中
     * @throws IOException
     */
    public void writeToExcel(OutputStream out) throws IOException {
        Workbook wb = this.getWorkBook(fileType);
        // 设置样式
        this.setCellStyle(wb);
        // 得到工作簿
        Sheet sheet = wb.createSheet();

        int rowIndex = 0;
        // 1.加载第一行
        this.loadFirstRow(sheet.createRow(rowIndex), wb, sheet);
        rowIndex = 1;
        // 2.执行正文
        if (this.list != null) {
            for (Object obj : this.list) {
                Row row = sheet.createRow(rowIndex);
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];

                    sheet.setColumnWidth(i, this.columnWidth);
                    Cell cell = row.createCell(i);
                    cell.setCellStyle(this.bodyStyle);
                    this.setCellValue(cell, field, obj, wb);

                }
                rowIndex++;
            }
        }
        wb.write(out);
    }

    // 设置值
    private void setCellValue(Cell cell, Field field, Object obj, Workbook wb) {

        try {
            field.setAccessible(true);
            if (obj == null) {
                return;
            }
            if (field.get(obj) == null) {
                cell.setCellValue("");
                return;
            }
            if (field.getType().equals(String.class)) {
                // 2.1 java.lang.String类型
                cell.setCellValue((String) field.get(obj));
            } else if (field.getType().equals(Integer.class)) {
                // 2.2 java.lang.Integer
                cell.setCellValue((Integer) field.get(obj));
            } else if (field.getType().equals(Double.class)) {
                // 2.3 java.lang.Double
                cell.setCellValue((Double) field.get(obj));
            } else if (field.getType().equals(Boolean.class)) {
                // 2.4 java.lang.Boolean
                cell.setCellValue((Boolean) field.get(obj));
            } else if (field.getType().equals(Long.class)) {
                // 2.5 java.lang.Long
                cell.setCellValue((Long) field.get(obj));
            } else if (field.getType().equals(Date.class)) {
                // 2.5 java.lang.Date
                Font nomalFont = wb.createFont();
                nomalFont.setFontName(this.fontType);// 字体
                CellStyle bodyStyle = wb.createCellStyle();
                bodyStyle.setDataFormat(wb.createDataFormat().getFormat(this.dateFormat));
                bodyStyle.setFont(nomalFont);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue((Date) field.get(obj));
            } else {
                // 2.6 其他类型，将配置的字段写入
                // 2.6.1 首先拿到当前字段上的的object
                Object childobj = field.get(obj);
                // 2.6.2 拿到当前的注解中的field，表示写入哪个字段
                String fieldname = field.getAnnotation(ExcelPattern.class).field();
                // 2.6.3 得到对应的字段
                Field childfield = field.getType().getDeclaredField(fieldname);
                // 2.6.4 赋值，进行递归
                this.setCellValue(cell, childfield, childobj, wb);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } finally {
            field.setAccessible(false);
        }

    }

    // 加载第一行的标题
    private void loadFirstRow(Row row, Workbook wb, Sheet sheet) {

        // 创建加粗字体,用于表头,设置样式
        for (int i = 0; i < fields.length; i++) {
            ExcelPattern patt = fields[i].getAnnotation(ExcelPattern.class);
            if (patt == null) {
                continue;
            }
            sheet.setColumnWidth(i, this.columnWidth);
            Cell cell = row.createCell(i);
            cell.setCellStyle(this.headerStyle);
            cell.setCellValue(patt.header());
        }
    }

    // 设置样式的放法
    private void setCellStyle(Workbook wb) {
        // 设置样式
        Font boldFont = wb.createFont();
        boldFont.setFontName(this.fontType);
        CellStyle cellStyle = wb.createCellStyle();
        // cellStyle.setAlignment (this.align);

        cellStyle.setFont(boldFont);
        this.headerStyle = cellStyle;
        Font nomalFont = wb.createFont();
        nomalFont.setFontName(this.fontType);// 字体
        CellStyle bodyStyle = wb.createCellStyle();
        bodyStyle.setFont(nomalFont);
        this.bodyStyle = bodyStyle;
    }
}
