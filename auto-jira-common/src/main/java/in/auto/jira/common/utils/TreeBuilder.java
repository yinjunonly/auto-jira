package in.auto.jira.common.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;

import javax.persistence.Id;

import in.auto.jira.common.enums.SystemErrorCode;
import in.auto.jira.common.exceptions.ErrorCodeExceptionFactory;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * 树形数据编译器
 * 
 * @author yinjun(yinjunonly@163.com)
 * @date 2018年9月6日上午11:32:15
 *
 */
public class TreeBuilder {
	/** 常规父id名称 */
	private static final String PARENTID_NAME = "parentId";
	/** 子节点属性名称 */
	private static final String CHILDRE_NNAME = "childrens";

	/**
	 * 建立树形结构数据 另外，顶级的parentId必须为NULL或者0，不然无法识别顶级节点
	 * 
	 * @author yinjun(yinjunonly@163.com)
	 * @param list
	 * @return
	 */
	public static <T> List<T> build(Collection<T> list) {
		if (!Tools.isNullOrEmpty(list)) {
			// 1.先把list转成 parentId + Object的map方便处理
			Map<Object, Collection<T>> treeMaps = Maps.newLinkedHashMap();
			Field[] fields = ReflectionUtils.getAllDeclaredFields(list.iterator().next().getClass());
			// 2.查找父字段 和 主键
			Field parentIdField = null;
			Field idField = null;
			Field childrenField = null;
			for (Field field : fields) {
				// 2.1、查找有没有名称为parentId的字段
				if (Objects.equals(field.getName(), PARENTID_NAME)) {
					if (Tools.isNull(parentIdField)) {
						parentIdField = field;
					}
				}
				// 其次查看Domain中的Id字段
				if (Tools.isNull(idField)) {
					Annotation currentAnnotation = field.getAnnotation(Id.class);
					if (!Tools.isNull(currentAnnotation)) {
						idField = field;
					}
				}
				// 2.4、查找子节点字段
				if (Objects.equals(field.getName(), CHILDRE_NNAME)) {
					childrenField = field;
				}
				if (!Tools.isNull(childrenField)) {
					break;
				}
			}
			// 3、如果找不到父字段则抛出异常
			if (Tools.isNull(parentIdField)) {
				throw ErrorCodeExceptionFactory.build(SystemErrorCode.TREE_PARENT_ID_NOT_FOUND_ERROR);
			}
			// 4、如果找不到id字段也抛异常
			if (Tools.isNull(idField)) {
				throw ErrorCodeExceptionFactory.build(SystemErrorCode.TREE_PK_NOt_FOUND_ERROR);
			}
			ReflectionUtils.makeAccessible(parentIdField);
			ReflectionUtils.makeAccessible(idField);
			ReflectionUtils.makeAccessible(childrenField);
			for (T t : list) {
				// 获取parentId
				Object parentId = ReflectionUtils.getFieldVal(parentIdField, t);
				if (treeMaps.containsKey(parentId)) {
					treeMaps.get(parentId).add(t);
				} else {
					treeMaps.put(parentId, Lists.newArrayList(t));
				}
			}
			// 5、设置关系
			Set<Object> notFirstNode = Sets.newConcurrentHashSet();
			for (Entry<Object, Collection<T>> item : treeMaps.entrySet()) {
				for (T t : item.getValue()) {
					notFirstNode.add(ReflectionUtils.getFieldVal(idField, t));
					ReflectionUtils.setFieldVal(childrenField, t,
							treeMaps.get(ReflectionUtils.getFieldVal(idField, t)));
				}
			}
			for (Object key : notFirstNode) {
				treeMaps.remove(key);
			}
			// 6、返回顶级节点
			List<T> result = Lists.newArrayList();
			for (Entry<Object, Collection<T>> item : treeMaps.entrySet()) {
				result.addAll(item.getValue());
			}
			return result;
		}
		return Lists.newArrayList();
	}
}
