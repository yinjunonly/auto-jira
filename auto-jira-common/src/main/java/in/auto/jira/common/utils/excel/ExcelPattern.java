package in.auto.jira.common.utils.excel;

import java.lang.annotation.*;

// 注解会在class字节码文件中存在，在运行时可以通过反射获取到
@Retention(RetentionPolicy.RUNTIME)
// 定义注解的作用目标**作用范围字段、枚举的常量/方法
@Target({ ElementType.FIELD, ElementType.METHOD })
// 说明该注解将被包含在javadoc中
@Documented
// 说明子类可以继承父类中的该注解
@Inherited
public @interface ExcelPattern {

    // 当前字段是第几行
    // public int cellIndex( );

    // 验证字符串的正则表达式
    public String pattern() default ExcelPattern.ALL;

    // 正则不通过的验证信息
    public String message() default "";

    // 是否是必须的
    public boolean require() default false;

    // 标题头
    public String header();

    public String field() default "id";

    /**
     * @field 进行正则验证的正则方法，所有的
     */
    public final static String ALL         = "[\\s\\S]*";
    /**
     * @field 电子邮箱
     */
    public final static String MAIL        = "^(((([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\\.([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\\x22)((((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(([\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x7f]|\\x21|[\\x23-\\x5b]|[\\x5d-\\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\\\([\\x01-\\x09\\x0b\\x0c\\x0d-\\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(\\x22)))@((([a-z]|\\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\\d|-|\\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\\d|-|\\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\\.?)?$";
    /**
     * @field 电话号码
     */
    public final static String PHONE       = "^(([\\+][0-9]{1,3}[ \\-])?([\\(]{1}[0-9]{2,6}[\\)])?([0-9 \\-\\/]{3,20})((x|ext|extension)[ ]?[0-9]{1,4})?)?$";
    /**
     * @field 一个数字
     */
    public final static String NUMBER      = "^([\\-\\+]?[\\.\\d]+)?$";
    /**
     * @field ipv4地址
     */
    public final static String IPV4        = "^([1-9][0-9]{0,2})+\\.([1-9][0-9]{0,2})+\\.([1-9][0-9]{0,2})+\\.([1-9][0-9]{0,2})+$";
    /**
     * @field url地址
     */
    public final static String URL         = "^(https?|ftp):\\/\\/(((([a-z]|\\d|-|\\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\\da-f]{2})|[!\\$&'\\(\\)\\*\\+,;=]|:)*@)?(((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]))|((([a-z]|\\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\\d|-|\\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\\d|-|\\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\\.?)(:\\d*)?)(\\/((([a-z]|\\d|-|\\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\\da-f]{2})|[!\\$&'\\(\\)\\*\\+,;=]|:|@)+(\\/(([a-z]|\\d|-|\\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\\da-f]{2})|[!\\$&'\\(\\)\\*\\+,;=]|:|@)*)*)?)?(\\?((([a-z]|\\d|-|\\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\\da-f]{2})|[!\\$&'\\(\\)\\*\\+,;=]|:|@)|[\uE000-\uF8FF]|\\/|\\?)*)?(\\#((([a-z]|\\d|-|\\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\\da-f]{2})|[!\\$&'\\(\\)\\*\\+,;=]|:|@)|\\/|\\?)*)?$/";
    /**
     * @field 只能是汉字
     */
    public final static String CHINESE     = "^([\u4e00-\u9fa5]+$)?$";
    /**
     * @field 不能拥有特殊符号
     */
    public final static String CHINESE2    = "^([0-9a-zA-Z_\\（\\）\\(\\)\\?\\？\\\"\\'\\.\\“\\”\\,\\，\\。\\、\\<\\>《》\\!\\@\\#\\$\\&\\;\\:\u4e00-\u9fa5]+$)?$";
    /**
     * @field 不能为数字
     */
    public final static String ISNOTNUMBER = "^([a-zA-Z\u4e00-\u9fa5]+$)?$";
}
