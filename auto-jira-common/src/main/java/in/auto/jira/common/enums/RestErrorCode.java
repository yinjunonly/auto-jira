package in.auto.jira.common.enums;

import in.auto.jira.common.exceptions.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * rest请求错误码枚举
 * 
 * @author yinjun(yinjunonly@163.com)
 * @date 2018年12月12日下午3:27:54
 *
 */
@Getter
@ToString
@AllArgsConstructor
public enum RestErrorCode implements ErrorCode {
	/** 请求参数错误 */
	REQUEST_204_ERROR(204, "无广告"),
	NOT_CHINA_ADDRESS_IP(205, "非中国地址ip"),
	/** 请求参数错误 */
	REQUEST_PARAMETER_ERROR(400, "请求参数错误"),
	/** 未授权 */
	REQUEST_NOT_AUTHORIZED_ERROR(401, "未授权或授权已过期"),
	/** 授权已过期 */
	AUTHORIZED_TIME_OUT_ERROR(403, "授权已过期"),
	/** 请求不存在 */
	REQUEST_NOT_FIND_ERROR(404, "请求不存在"),
	/** 不支持的请求类型，请检查你的URL及Request Method. */
	REQUEST_TYPE_NOT_SUPPORTED(452, "不支持的请求类型，请检查你的URL及Request Method."),

	/** 服务器忙，请重试 */
	REQUEST_BUSY(504,"服务器忙,请重试"),

	ARTICLE_NOT_EXIST(10001, "请求文章不存在或已删除"),
	JS_LIMIT_EXIST(10002, "该广告主控量已存在"),
	FILE_UPLOAD_FAIL(10003, "文件上传失败"),
	ANDROID_ID_IS_NULL(10004,"缺少设备id"),
	OS_IS_NULL(10004,"缺少设备id")
	;


	private Integer status;
	private String message;
}
