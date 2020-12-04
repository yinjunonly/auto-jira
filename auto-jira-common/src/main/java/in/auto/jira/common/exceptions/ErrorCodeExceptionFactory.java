package in.auto.jira.common.exceptions;

/**
 * 异常工厂类
 * 
 * @author yinjun(yinjunonly@163.com)
 * @date 2018年9月5日下午3:50:30
 *
 */
public class ErrorCodeExceptionFactory {
	/**
	 * 使用errorCode的实现类建立一个异常
	 * 
	 * @author yinjun(yinjunonly@163.com)
	 * @param errorCode
	 * @return
	 */
	public static ErrorCodeException build(ErrorCode errorCode) {
		ErrorCodeException errorCodeException = new ErrorCodeException();
		errorCodeException.setErrorCode(errorCode);
		return errorCodeException;
	}

	/**
	 * 使用errorCode的实现类建立一个异常<br>
	 * message中存在占位符，可动态替换参数
	 * 
	 * @author yinjun(yinjunonly@163.com)
	 * @param errorCode
	 * @return
	 */
	public static ErrorCodeException build(ErrorCode errorCode, Object... params) {
		ErrorCodeException errorCodeException = new ErrorCodeException();
		errorCodeException.setErrorCode(errorCode);
		errorCodeException.setParams(params);
		return errorCodeException;
	}

	/**
	 * 使用自定义的文字与Code建立一个异常
	 * 
	 * @author yinjun(yinjunonly@163.com)
	 * @param errorMessage
	 * @param code
	 * @return
	 */
	public static ErrorCodeException build(String errorMessage, int code) {
		CommonErrorCode errorCode = new CommonErrorCode();
		errorCode.setMessage(errorMessage);
		errorCode.setStatus(code);
		ErrorCodeException codeException = new ErrorCodeException();
		codeException.setErrorCode(errorCode);
		return codeException;
	}
}
