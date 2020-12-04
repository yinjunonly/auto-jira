package in.auto.jira.common.utils.okhttp.client;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import in.auto.jira.common.exceptions.ErrorCodeExceptionFactory;
import in.auto.jira.common.utils.ParamSortToUrlUtils;
import in.auto.jira.common.utils.Tools;
import in.auto.jira.common.utils.okhttp.HttpClientUtils;
import in.auto.jira.common.utils.okhttp.error.OkHttpErrorcode;
import in.auto.jira.common.utils.okhttp.model.Headers;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * okhttp简单的客户端，delete请求实例
 * 
 * @author yinjun (yinjunonly@163.com)
 * @date 2019年8月12日 上午11:54:34
 */
@Slf4j
@Component
public class DeleteClient {
	@Autowired
	private OkHttpClient okHttpClient;

	private static final String Q = "?";

	/**
	 * 发出一个delete请求
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:37:48
	 * @param url 请求链接
	 * @return
	 */
	public Response delete(String url) {
		Request request = new Request.Builder().url(url).delete().build();
		try {
			Response response = okHttpClient.newCall(request).execute();
			return response;
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw ErrorCodeExceptionFactory.build(OkHttpErrorcode.IO_ERROR);
		}
	}

	/**
	 * 发出一个delete请求，并携带请求头
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:37:48
	 * @param url 请求链接
	 * @param headers 请求头映射表
	 * @return
	 */
	public Response delete(String url, Headers<String, String> headers) {
		Request request = new Request.Builder().headers(okhttp3.Headers.of(headers)).url(url).delete().build();
		try {
			Response response = okHttpClient.newCall(request).execute();
			return response;
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw ErrorCodeExceptionFactory.build(OkHttpErrorcode.IO_ERROR);
		}
	}

	/**
	 * 发出一个delete请求，并传递参数
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:38:25
	 * @param url 请求链接
	 * @param params 参数映射表
	 * @return
	 */
	public Response delete(String url, Map<String, Object> params) {
		if (params.size() > 0) {
			url = new StringBuilder(url).append(Q).append(Tools.mapToUrl(params)).toString();
		}
		return this.delete(url);
	}

	/**
	 * 发出一个delete请求，并传递参数与请求头
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:38:25
	 * @param url 请求链接
	 * @param params 参数映射表
	 * @param headers 请求头映射表
	 * @return
	 */
	public Response delete(String url, Map<String, Object> params, Headers<String, String> headers) {
		if (params.size() > 0) {
			url = new StringBuilder(url).append(Q).append(Tools.mapToUrl(params)).toString();
		}
		return this.delete(url, headers);
	}

	/**
	 * 发出一个delete请求，并传递参数
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:38:25
	 * @param url 请求链接
	 * @param params 参数对象，把对象不为空的属性拼接到参数中
	 * @return
	 */
	public Response delete(String url, Object params) {
		String urlParams = ParamSortToUrlUtils.toURLForAsciiSort(params);
		if (!Tools.isNullOrEmpty(urlParams)) {
			url = new StringBuilder(url).append(Q).append(urlParams).toString();
		}
		return this.delete(url);
	}

	/**
	 * 发出一个delete请求，并传递参数与请求头
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:38:25
	 * @param url 请求链接
	 * @param params 参数对象，把对象不为空的属性拼接到参数中
	 * @param headers 请求头映射表
	 * @return
	 */
	public Response delete(String url, Object params, Headers<String, String> headers) {
		String urlParams = ParamSortToUrlUtils.toURLForAsciiSort(params);
		if (!Tools.isNullOrEmpty(urlParams)) {
			url = new StringBuilder(url).append(Q).append(urlParams).toString();
		}
		return this.delete(url, headers);
	}

	/**
	 * 发出一个delete请求，并且返回字符串格式的请求返回值
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:41:51
	 * @param url 请求链接
	 * @return
	 */
	public String deleteResp2String(String url) {
		Response response = this.delete(url);
		return HttpClientUtils.handleResp(response);
	}

	/**
	 * 发出一个delete请求，携带请求头，并且返回字符串格式的请求返回值
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:41:51
	 * @param url 请求链接
	 * @param headers 请求头映射表
	 * @return
	 */
	public String deleteResp2String(String url, Headers<String, String> headers) {
		Response response = this.delete(url, headers);
		return HttpClientUtils.handleResp(response);
	}

	/***
	 * 发出一个delete请求，传递参数，并且返回字符串格式的请求返回值
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:41:51
	 * @param url 请求链接
	 * @param params 请求参数映射表
	 * @return
	 */

	public String deleteResp2String(String url, Map<String, Object> params) {
		Response response = this.delete(url, params);
		return HttpClientUtils.handleResp(response);
	}

	/***
	 * 发出一个delete请求，传递参数与请求头，并且返回字符串格式的请求返回值
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:41:51
	 * @param url 请求链接
	 * @param params 请求参数映射表
	 * @param headers 请求头映射表
	 * @return
	 */

	public String deleteResp2String(String url, Map<String, Object> params, Headers<String, String> headers) {
		Response response = this.delete(url, params, headers);
		return HttpClientUtils.handleResp(response);
	}

	/***
	 * 发出一个delete请求，传递参数，并且返回字符串格式的请求返回值
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:41:51
	 * @param url 请求链接
	 * @param params 参数对象，把对象不为空的属性拼接到参数中
	 * @return
	 */

	public String deleteResp2String(String url, Object params) {
		Response response = this.delete(url, params);
		return HttpClientUtils.handleResp(response);
	}

	/***
	 * 发出一个delete请求，传递参数与请求头，并且返回字符串格式的请求返回值
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:41:51
	 * @param url 请求链接
	 * @param params 参数对象，把对象不为空的属性拼接到参数中
	 * @param headers 请求头映射表
	 * @return
	 */

	public String deleteResp2String(String url, Object params, Headers<String, String> headers) {
		Response response = this.delete(url, params, headers);
		return HttpClientUtils.handleResp(response);
	}

	/**
	 * 发出一个delete请求，并且将JSON格式的返回值转换为目标对象
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param clazz 目标对象类型
	 * @return
	 */
	public <T> T deleteResp2ObjFromJson(String url, Class<T> clazz) {
		String respString = this.deleteResp2String(url);
		return JSON.parseObject(respString, clazz);
	}

	/**
	 * 发出一个delete请求，携带请求头，并且将JSON格式的返回值转换为目标对象
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param headers 请求头映射表
	 * @param clazz 目标对象类型
	 * @return
	 */
	public <T> T deleteResp2ObjFromJson(String url, Headers<String, String> headers, Class<T> clazz) {
		String respString = this.deleteResp2String(url, headers);
		return JSON.parseObject(respString, clazz);
	}

	/**
	 * 发出一个delete请求，并且将JSON格式的返回值转换为目标对象
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param typeReference 目标对象类型
	 * @return
	 */
	public <T> T deleteResp2ObjFromJson(String url, TypeReference<T> typeReference) {
		String respString = this.deleteResp2String(url);
		return JSON.parseObject(respString, typeReference);
	}

	/**
	 * 发出一个delete请求，携带请求头，并且将JSON格式的返回值转换为目标对象
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param headers 请求头映射表
	 * @param typeReference 目标对象类型
	 * @return
	 */
	public <T> T deleteResp2ObjFromJson(String url, Headers<String, String> headers, TypeReference<T> typeReference) {
		String respString = this.deleteResp2String(url, headers);
		return JSON.parseObject(respString, typeReference);
	}

	/**
	 * 发出一个delete请求，传递参数，并且将JSON格式的返回值转换为目标对象
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param params 请求参数映射表
	 * @param clazz 目标对象类型
	 * @return
	 */
	public <T> T deleteResp2ObjFromJson(String url, Map<String, Object> params, Class<T> clazz) {
		String respString = this.deleteResp2String(url, params);
		return JSON.parseObject(respString, clazz);
	}

	/**
	 * 发出一个delete请求，传递参数与请求头，并且将JSON格式的返回值转换为目标对象
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param params 请求参数映射表
	 * @param headers 请求头映射表
	 * @param clazz 目标对象类型
	 * @return
	 */
	public <T> T deleteResp2ObjFromJson(String url, Map<String, Object> params, Headers<String, String> headers, Class<T> clazz) {
		String respString = this.deleteResp2String(url, params, headers);
		return JSON.parseObject(respString, clazz);
	}

	/**
	 * 发出一个delete请求，传递参数，并且将JSON格式的返回值转换为目标对象
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param params 参数对象，把对象不为空的属性拼接到参数中
	 * @param typeReference 目标对象类型
	 * @return
	 */
	public <T> T deleteResp2ObjFromJson(String url, Map<String, Object> params, TypeReference<T> typeReference) {
		String respString = this.deleteResp2String(url, params);
		return JSON.parseObject(respString, typeReference);
	}

	/**
	 * 发出一个delete请求，传递参数与请求头，并且将JSON格式的返回值转换为目标对象
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param params 参数对象，把对象不为空的属性拼接到参数中
	 * @param headers 请求头映射表
	 * @param typeReference 目标对象类型
	 * @return
	 */
	public <T> T deleteResp2ObjFromJson(String url, Map<String, Object> params, Headers<String, String> headers,
			TypeReference<T> typeReference) {
		String respString = this.deleteResp2String(url, params, headers);
		return JSON.parseObject(respString, typeReference);
	}

	/**
	 * 发出一个delete请求，传递参数，并且将JSON格式的返回值转换为目标对象
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param params 参数对象，把对象不为空的属性拼接到参数中
	 * @param clazz 目标对象类型
	 * @return
	 */
	public <T> T deleteResp2ObjFromJson(String url, Object params, Class<T> clazz) {
		String respString = this.deleteResp2String(url, params);
		return JSON.parseObject(respString, clazz);
	}

	/**
	 * 发出一个delete请求，传递参数与请求头，并且将JSON格式的返回值转换为目标对象
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param params 参数对象，把对象不为空的属性拼接到参数中
	 * @param headers 请求头映射表
	 * @param clazz 目标对象类型
	 * @return
	 */
	public <T> T deleteResp2ObjFromJson(String url, Object params, Headers<String, String> headers, Class<T> clazz) {
		String respString = this.deleteResp2String(url, params, headers);
		return JSON.parseObject(respString, clazz);
	}

	/**
	 * 发出一个delete请求，传递参数，并且将JSON格式的返回值转换为目标对象
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param params 参数对象，把对象不为空的属性拼接到参数中
	 * @param typeReference 目标对象类型
	 * @return
	 */
	public <T> T deleteResp2ObjFromJson(String url, Object params, TypeReference<T> typeReference) {
		String respString = this.deleteResp2String(url, params);
		return JSON.parseObject(respString, typeReference);
	}

	/**
	 * 发出一个delete请求，传递参数与请求头，并且将JSON格式的返回值转换为目标对象
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param params 参数对象，把对象不为空的属性拼接到参数中
	 * @param headers 请求头映射表
	 * @param typeReference 目标对象类型
	 * @return
	 */
	public <T> T deleteResp2ObjFromJson(String url, Object params, Headers<String, String> headers, TypeReference<T> typeReference) {
		String respString = this.deleteResp2String(url, params, headers);
		return JSON.parseObject(respString, typeReference);
	}

	/**
	 * 发出一个delete请求，并且将JSON格式的返回值转换为目标对象集合
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param clazz 目标对象类型
	 * @return
	 */
	public <T> List<T> deleteResp2ListFromJson(String url, Class<T> clazz) {
		String respString = this.deleteResp2String(url);
		return JSON.parseArray(respString, clazz);
	}

	/**
	 * 发出一个delete请求，并且将JSON格式的返回值转换为目标对象集合
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param clazz 目标对象类型
	 * @return
	 */
	public <T> List<T> deleteResp2ListFromJson(String url, Headers<String, String> headers, Class<T> clazz) {
		String respString = this.deleteResp2String(url, headers);
		return JSON.parseArray(respString, clazz);
	}

	/**
	 * 发出一个delete请求，传递参数，并且将JSON格式的返回值转换为目标对象集合
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param params 请求参数映射表
	 * @param clazz 目标对象类型
	 * @return
	 */
	public <T> List<T> deleteResp2ListFromJson(String url, Map<String, Object> params, Class<T> clazz) {
		String respString = this.deleteResp2String(url, params);
		return JSON.parseArray(respString, clazz);
	}

	/**
	 * 发出一个delete请求，传递参数与请求头，并且将JSON格式的返回值转换为目标对象集合
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param params 请求参数映射表
	 * @param headers 请求头映射表
	 * @param clazz 目标对象类型
	 * @return
	 */
	public <T> List<T> deleteResp2ListFromJson(String url, Map<String, Object> params, Headers<String, String> headers, Class<T> clazz) {
		String respString = this.deleteResp2String(url, params, headers);
		return JSON.parseArray(respString, clazz);
	}

	/**
	 * 发出一个delete请求，传递参数，并且将JSON格式的返回值转换为目标对象集合
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param params 参数对象，把对象不为空的属性拼接到参数中
	 * @param clazz 目标对象类型
	 * @return
	 */
	public <T> List<T> deleteResp2ListFromJson(String url, Object params, Class<T> clazz) {
		String respString = this.deleteResp2String(url, params);
		return JSON.parseArray(respString, clazz);
	}

	/**
	 * 发出一个delete请求，传递参数与请求头，并且将JSON格式的返回值转换为目标对象集合
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param params 参数对象，把对象不为空的属性拼接到参数中
	 * @param headers 请求头映射表
	 * @param clazz 目标对象类型
	 * @return
	 */
	public <T> List<T> deleteResp2ListFromJson(String url, Object params, Headers<String, String> headers, Class<T> clazz) {
		String respString = this.deleteResp2String(url, params, headers);
		return JSON.parseArray(respString, clazz);
	}
}
