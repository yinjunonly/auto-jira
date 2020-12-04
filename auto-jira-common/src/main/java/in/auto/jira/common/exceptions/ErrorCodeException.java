package in.auto.jira.common.exceptions;

import in.auto.jira.common.utils.Tools;

/**
 * 错误异常类
 * 
 * @author yinju
 * @date 2017年12月11日上午11:09:27
 */
public class ErrorCodeException extends RuntimeException {
	private static final long serialVersionUID = -108850437448500102L;
	private ErrorCode errorCode;
	private Object[] params;

	public ErrorCodeException() {
	}

	public ErrorCodeException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return the params
	 */
	public Object[] getParams() {
		return params;
	}

	/**
	 * @param params the params to set
	 */
	public void setParams(Object[] params) {
		this.params = params;
	}

	@Override
	public String getMessage() {
		if (errorCode != null) {
			StringBuilder stringBuilder = new StringBuilder(errorCode.getClass().getSimpleName()).append("(")
					.append("status=").append(errorCode.getStatus()).append(", ").append("message=");
			if (this.params != null && this.params.length > 0) {
				stringBuilder.append(Tools.fillPlaceholder(errorCode.getMessage(), this.params));
			} else {
				stringBuilder.append(errorCode.getMessage());
			}
			return stringBuilder.append(")").toString();
		}
		return super.getMessage();
	}

	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}


}
