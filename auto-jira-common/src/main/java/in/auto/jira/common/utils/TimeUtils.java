package in.auto.jira.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;

import com.google.common.base.Objects;

/**
 * 操作时间的工具类
 * 
 * @author yinju
 * @date 2016-8-24 10:38:40
 * 
 */
public class TimeUtils {

	// private static String pattern_prefix = "_";

	/**
	 * 当前时间
	 * 
	 * @return
	 */
	public static Date now() {
		return DateTime.now().toDate();
	}

	/**
	 * 为date增加second秒（可传负数）
	 * 
	 * @param date
	 * @param min
	 * @return
	 */
	public static Date addSeconds(Date date, int second) {
		DateTime now = new DateTime(date);
		return now.plusSeconds(second).toDate();
	}

	/**
	 * 为date增加min分钟（可传负数）
	 * 
	 * @param date
	 * @param min
	 * @return
	 */
	public static Date addMinutes(Date date, int min) {
		DateTime now = new DateTime(date);
		return now.plusMinutes(min).toDate();
	}

	/**
	 * 为date增加hour小时（可传负数）
	 * 
	 * @param date
	 * @param hour
	 * @return
	 */
	public static Date addHour(Date date, int hour) {
		DateTime now = new DateTime(date);
		return now.plusHours(hour).toDate();
	}

	/**
	 * 为date增加day天（可传负数）
	 * 
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date addDay(Date date, int day) {
		DateTime now = new DateTime(date);
		return now.plusDays(day).toDate();
	}

	/**
	 * 为date增加month月（可传负数）
	 * 
	 * @param date
	 * @param month
	 * @return
	 */
	public static Date addMonth(Date date, int month) {
		DateTime now = new DateTime(date);
		return now.plusMonths(month).toDate();
	}

	/**
	 * 比较两个时间相差天数
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static Integer betweenDays(Date startDate, Date endDate) {
		DateTime start = new DateTime(startDate);
		DateTime end = new DateTime(endDate);
		return Days.daysBetween(start, end).getDays();
	}

	/**
	 * 比较两个时间相差天数忽略时分秒
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static Integer betweenDaysIgnoreHms(Date startDate, Date endDate) {
		DateTime start = new DateTime(startDate).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
				.withMillisOfSecond(0);
		DateTime end = new DateTime(endDate).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
				.withMillisOfSecond(0);
		return Days.daysBetween(start, end).getDays();
	}

	/**
	 * 比较两个时间相差分钟
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long betweenMin(Date startDate, Date endDate) {
		DateTime start = new DateTime(startDate);
		DateTime end = new DateTime(endDate);
		return Minutes.minutesBetween(start, end).getMinutes();
	}

	/**
	 * 获取当前时间i分钟前的时间
	 * 
	 * @param i
	 * @return
	 */
	public static DateTime getDateTimePreMin(int i) {
		return DateTime.now().plusMinutes(0 - i);
	}

	/**
	 * 获取字符时间
	 * 
	 * @param reportTime
	 * @param formatter
	 * @return
	 */
	public static Date parseTime(String reportTime, String formatter) {
		return DateTime.parse(reportTime, DateTimeFormat.forPattern(formatter)).toDate();
	}

	/**
	 * 时间转换格式
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String time2String(Date date, String pattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		Calendar calendar = Calendar.getInstance();
		// 设置每周是周一开始
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		dateFormat.setCalendar(calendar);
		return dateFormat.format(date);
	}

	/**
	 * 比较timeOne是否大于timeTwo
	 * 
	 * @param timeOne
	 * @param timeTwo
	 * @return
	 */
	public static Boolean compare(Date timeOne, Date timeTwo) {
		return timeOne.getTime() > timeTwo.getTime();
	}

	/**
	 * 比较timeOne是否大于等于timeTwo
	 * 
	 * @param timeOne
	 * @param timeTwo
	 * @return
	 */
	public static Boolean compareEqual(Date timeOne, Date timeTwo) {
		return timeOne.getTime() >= timeTwo.getTime();
	}

	/**
	 * 判断date所处的月份是否为闰月
	 * 
	 * @param date
	 * @return
	 */
	public static Boolean isLeap(Date date) {
		DateTime d = new DateTime(date);
		return d.monthOfYear().isLeap();
	}

	/**
	 * 获取本月第一天零点的时间
	 * 
	 * @return
	 */
	public static Date getTimesMonthmorning() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 获取本月最后一天24点的时间
	 * 
	 * @return
	 */
	public static Date getTimesMonthnight() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, 24);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 获取当前月份加几个月（可传负数） 第一天零点的时间
	 * 
	 * @return
	 */
	public static Date getTimesMonthmorning(Integer month) {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY) + month, cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 获取本月加几个月（可传负数）最后一天24点的时间
	 * 
	 * @return
	 */
	public static Date getTimesMonthnight(Integer month) {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY) + month, cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, 24);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 判断两个时间时间是否是同一时间点
	 * 
	 * @param dateOne
	 * @param dateTwo
	 * @param formate 判断格式（判断年：YYYY,判断月："YYYYmm",判断天:"YYYYmmdd"）
	 * @return
	 */
	public static boolean isSameDate(Date dateOne, Date dateTwo, String formate) {
		String strDateOne = time2String(dateOne, formate);
		String strDateTwo = time2String(dateTwo, formate);
		return Objects.equal(strDateOne, strDateTwo);
	}

	/**
	 * 获取当前时间的n（前或后）的时间，例如：当前时间是201610 当num=-1 时 得到的值为201609
	 * 
	 * @param num
	 * @return
	 */
	public static String getMonth(String num) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, Integer.parseInt(num));
		String month = TimeUtils.time2String(c.getTime(), "yyyyMM");
		return month;
	}

	/**
	 * 把指定日期的时分秒变为：00:00:00
	 * 
	 * @param date
	 * @return Date
	 */
	public static Date getDate0Hms(Date date) {
		return new DateTime(date).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0)
				.toDate();
	}

	/**
	 * 把指定日期的时分秒变为：23:59:59
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDate59Hms(Date date) {
		return new DateTime(date).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(0)
				.toDate();
	}

	/**
	 * 把指定日期的天变成指定的天数
	 * 
	 * @param date 日期
	 * @param day  天数
	 * @return
	 */
	public static Date getFixedDayDate(Date date, int day) {
		return new DateTime(date).withDayOfMonth(day).toDate();
	}

	/**
	 * 获取date是本年的第几周
	 * 
	 * @param date 日期
	 * @return
	 */
	public static Integer getWeekOfYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		// 设置每周是周一开始
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setTime(date);
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}
}
