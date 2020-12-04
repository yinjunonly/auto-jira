package in.auto.jira.common.utils.okhttp.error;

import in.auto.jira.common.exceptions.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * okhttp 错误码
 * 
 * @author yinjun (yinjunonly@163.com)
 * @date 2019年5月9日 上午11:36:51
 */
@Getter
@ToString
@AllArgsConstructor
public enum OkHttpErrorcode implements ErrorCode {
	/** 网络错误，请检查url */
	IO_ERROR(60000, "网络错误，请检查url"),
	/** 服务端返回状态错误 */
	RESP_STATUS_ERROR(60001, "服务端返回状态错误，错误码：{}"),
	/** 返回值读取出错 */
	RESP_IO_ERROR(60002, "返回值读取出错");

	private Integer status;
	private String message;
}
