package in.auto.jira.common.utils;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.namespace.QName;

import com.google.common.base.Charsets;

/**
 * xml与bean互相转换工具类
 * 
 * @author yinju
 * @date 2017年12月8日下午4:43:47
 */
public class XmlUtils {
	/**
	 * xml 转 对象
	 * 
	 * @author yinju
	 * @param xml xml字符串
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T xml2Object(String xml, Class<T> clazz) {
		Class<?>[] types = { clazz, CollectionWrapper.class };
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(types);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			StringReader reader = new StringReader(xml);
			return (T) unmarshaller.unmarshal(reader);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 对象 转 xml
	 * 
	 * @author yinju
	 * @param obj 对象
	 * @return xmlStr
	 */
	public static String object2Xml(Object obj) {
		StringWriter writer = new StringWriter();
		Class<?>[] types = { obj.getClass(), CollectionWrapper.class };
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(types);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, Charsets.UTF_8.name());
			marshaller.marshal(obj, writer);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return writer.toString();
	}

	/**
	 * 集合转xml
	 * 
	 * @author yinju
	 * @param collection 集合
	 * @param clazz      类型
	 * @param rootName   根节点名称
	 * @return xmlStr
	 */
	public static String collection2Xml(Collection<?> collection, Class<?> clazz, String rootName) {
		CollectionWrapper wrapper = new CollectionWrapper();
		wrapper.collection = collection;
		JAXBElement<CollectionWrapper> wrapperElement = new JAXBElement<CollectionWrapper>(new QName(rootName),
				CollectionWrapper.class, wrapper);
		StringWriter writer = new StringWriter();
		Class<?>[] types = { clazz, CollectionWrapper.class };
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(types);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, Charsets.UTF_8.name());
			marshaller.marshal(wrapperElement, writer);
			return writer.toString();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return writer.toString();
	}

	/**
	 * 封装Root Element 是 Collection的情况.
	 * 
	 * @author yinju
	 * @date 2017年12月8日下午4:52:30
	 */
	private static class CollectionWrapper {
		@XmlAnyElement
		protected Collection<?> collection;
	}
}
