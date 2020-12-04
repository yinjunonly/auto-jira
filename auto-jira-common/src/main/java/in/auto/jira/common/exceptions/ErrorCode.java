package in.auto.jira.common.exceptions;

/**
 * 系统错误码接口
 * 
 * @author yinju
 * @date 2017年12月11日上午11:08:01
 */
public interface ErrorCode {
	/**
	 * 获取错误状态
	 * 
	 * @author yinju
	 * @return
	 */
	public Integer getStatus();

	/**
	 * 获取错误信息
	 * 
	 * @author yinju
	 * @return
	 */
	public String getMessage();
}
