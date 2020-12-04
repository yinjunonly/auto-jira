package in.auto.jira.common.exceptions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 统一的错误提示类
 * 
 * @author yinjun(yinjunonly@163.com)
 * @date 2018年8月29日上午10:34:34
 *
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class CommonErrorCode implements ErrorCode {
	private Integer status;
	private String message;
}
