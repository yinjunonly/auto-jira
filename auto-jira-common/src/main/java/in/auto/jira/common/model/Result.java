package in.auto.jira.common.model;

import java.io.Serializable;

import in.auto.jira.common.enums.ErrorLevel;
import in.auto.jira.common.exceptions.ErrorCode;

/**
 * 返回值接口
 * 
 * @author yinju
 * @date 2017年12月11日上午10:55:39
 * @param <T（数据类型）>
 */
public interface Result<T> extends Serializable {
	/**
	 * 获取数据
	 * 
	 * @author yinju
	 * @return
	 */
	public T getData();

	/**
	 * 设置数据
	 * 
	 * @author yinju
	 * @param data 数据
	 */
	public void setData(T data);

	/**
	 * 设置字符信息
	 * 
	 * @author yinju
	 * @param message 字符信息
	 */
	public void setMessage(String message);

	/**
	 * 获取字符消息
	 * 
	 * @author yinju
	 * @return
	 */
	public String getMessage();

	/**
	 * 设置数据状态
	 * 
	 * @author yinju
	 * @param status 状态
	 */
	public void setStatus(Integer status);

	/**
	 * 获取数据状态
	 * 
	 * @author yinju
	 * @return
	 */
	public Integer getStatus();

	/**
	 * 设置错误枚举
	 * 
	 * @author yinju
	 * @param errorCode 错误枚举
	 */
	public void setErrorCode(ErrorCode errorCode);

	/**
	 * 设置错误等级
	 * 
	 * @author yinju
	 * @param level 等级枚举
	 */
	public void setErrorLevel(ErrorLevel level);

	/**
	 * 获取错误等级
	 * 
	 * @author yinju
	 * @return
	 */
	public ErrorLevel getErrorLevel();
}
