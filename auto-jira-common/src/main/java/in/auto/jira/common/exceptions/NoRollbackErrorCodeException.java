package in.auto.jira.common.exceptions;

import in.auto.jira.common.utils.Tools;

/**
 * 不回滚事务的统一异常
 * 
 * @author yinjun (yinjunonly@163.com)
 * @date 2019年6月18日 下午5:49:26
 */
public class NoRollbackErrorCodeException extends RuntimeException {
	private static final long serialVersionUID = -108850437448500102L;
	private ErrorCode errorCode;
	private Object[] params;

	private NoRollbackErrorCodeException(ErrorCodeException e) {
		this.errorCode = e.getErrorCode();
		this.params = e.getParams();
	}

	public NoRollbackErrorCodeException(ErrorCode errorCode) {
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

	/**
	 * 根据ErrorCodeException 生成不回滚的异常
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年6月18日 下午5:48:49
	 * @param e
	 * @return
	 */
	public static NoRollbackErrorCodeException of(ErrorCodeException e) {
		return new NoRollbackErrorCodeException(e);
	}
}