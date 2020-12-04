package in.auto.jira.common.enums;

import in.auto.jira.common.exceptions.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 系统错误代码
 * 
 * @author yinju
 * @date 2017年12月11日上午11:12:14
 */
@Getter
@ToString
@AllArgsConstructor
public enum SystemErrorCode implements ErrorCode {
	/** 服务异常 */
	SERVER_ABNORMAL_ERROR(500, "服务异常，请重试"),
	/** 树形结构转换时，未找到模型父主键属性，请检查数据中是否存在标记@TreeParent或者名称为parentId的字段 */
	TREE_PARENT_ID_NOT_FOUND_ERROR(628, "树形结构转换时，未找到模型父主键属性，请检查数据中是否存在名称为parentId的字段"),
	/** 树形结构转换时，未找到模型主键，请检查数据中是否存在标记@TreeId或者是DomainModel未标记Id字段 */
	TREE_PK_NOt_FOUND_ERROR(629, "树形结构转换时，未找到模型主键，请检查数据中是否存在标记@TreeId"),
	/** 树形结构转换时，被转换模型必须继承BaseTreeBean（QueryBean系统已默认处理） */
	TREE_NOT_EXTENDS_BASETREEBEAN(630, "树形结构转换时，被转换模型必须继承BaseTreeBean（QueryBean系统已默认处理）"),
	/** 值转换失败，与期待类型不一致 */
	STRING_TO_VALUE_ERROR(631, "值转换失败，与期待类型不一致"),
	/** 指定的值与注解指定的属性类型不一致，无法设置 */
	VAL_SET_FAIL_ERROR(632, "指定的值与注解指定的属性类型不一致，无法设置");

	private Integer status;
	private String message;
}
