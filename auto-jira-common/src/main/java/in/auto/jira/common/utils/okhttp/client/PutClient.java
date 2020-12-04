package in.auto.jira.common.utils.okhttp.client;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import in.auto.jira.common.exceptions.ErrorCodeExceptionFactory;
import in.auto.jira.common.utils.okhttp.HttpClientUtils;
import in.auto.jira.common.utils.okhttp.error.OkHttpErrorcode;
import in.auto.jira.common.utils.okhttp.model.Headers;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.FormBody.Builder;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * okhttp简单的客户端，GET请求实例
 * 
 * @author yinjun (yinjunonly@163.com)
 * @date 2019年8月12日 上午11:54:34
 */
@Slf4j
@Component
public class PutClient {
	@Autowired
	private OkHttpClient okHttpClient;

	/**
	 * 发起一个put请求
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月9日 上午10:43:59
	 * @param request 请求对象
	 * @return
	 */
	private Response put(Request request) {
		try {
			Response response = okHttpClient.newCall(request).execute();
			return response;
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw ErrorCodeExceptionFactory.build(OkHttpErrorcode.IO_ERROR);
		}
	}

	/**
	 * 发出一个put请求
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:37:48
	 * @param url 请求链接
	 * @return
	 */
	public Response put(String url) {
		Builder formBodyBuilder = new FormBody.Builder();
		Request request = new Request.Builder().url(url).put(formBodyBuilder.build()).build();
		return this.put(request);
	}

	/**
	 * 发出一个put请求，并携带请求头
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:37:48
	 * @param url 请求链接
	 * @param headers 请求头映射表
	 * @return
	 */
	public Response put(String url, Headers<String, String> headers) {
		Builder formBodyBuilder = new FormBody.Builder();
		Request request = new Request.Builder().url(url).headers(okhttp3.Headers.of(headers)).put(formBodyBuilder.build()).build();
		return this.put(request);
	}

	/**
	 * 发出一个put请求，并使用formData的形式传递参数
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:38:25
	 * @param url 请求链接
	 * @param params 参数映射表
	 * @return
	 */
	public Response putByFormData(String url, Map<String, Object> params) {
		if (params.size() > 0) {
			Builder formBodyBuilder = new FormBody.Builder();
			for (Entry<String, Object> entry : params.entrySet()) {
				formBodyBuilder.add(entry.getKey(), entry.getValue().toString());
			}
			Request request = new Request.Builder().url(url).put(formBodyBuilder.build()).build();
			return this.put(request);
		} else {
			return this.put(url);
		}
	}

	/**
	 * 发出一个put请求，并使用formData的形式传递参数，且携带请求头
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:38:25
	 * @param url 请求链接
	 * @param params 参数映射表
	 * @return
	 */
	public Response putByFormData(String url, Map<String, Object> params, Headers<String, String> headers) {
		if (params.size() > 0) {
			Builder formBodyBuilder = new FormBody.Builder();
			for (Entry<String, Object> entry : params.entrySet()) {
				formBodyBuilder.add(entry.getKey(), entry.getValue().toString());
			}
			Request request = new Request.Builder().url(url).headers(okhttp3.Headers.of(headers)).put(formBodyBuilder.build()).build();
			return this.put(request);
		} else {
			return this.put(url, headers);
		}
	}

	/**
	 * 发出一个put请求，并使用formData的形式传递参数
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:38:25
	 * @param url 请求链接
	 * @param params 参数对象，把对象不为空的属性放到参数中
	 * @return
	 */
	public Response putByFormData(String url, Object params) {
		return this.putByFormData(url, HttpClientUtils.obj2Map(params));
	}

	/**
	 * 发出一个put请求，并使用formData的形式传递参数，且传递请求头
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:38:25
	 * @param url 请求链接
	 * @param params 参数对象，把对象不为空的属性放到参数中
	 * @param headers 请求头映射表
	 * @return
	 */
	public Response putByFormData(String url, Object params, Headers<String, String> headers) {
		return this.putByFormData(url, HttpClientUtils.obj2Map(params), headers);
	}

	/**
	 * 发出一个put请求，并使用JSON的形式传递参数
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:38:25
	 * @param url 请求链接
	 * @param jsonString JSON格式的参数
	 * @return
	 */
	public Response putByJson(String url, String jsonString) {
		Request request = new Request.Builder().url(url)
				.put(RequestBody.create(jsonString, MediaType.parse(HttpClientUtils.APPLICATION_JSON_UTF8))).build();
		return this.put(request);
	}

	/**
	 * 发出一个put请求，并使用JSON的形式传递参数
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:38:25
	 * @param url 请求链接
	 * @param jsonString JSON格式的参数
	 * @param headers 请求头映射表
	 * @return
	 */
	public Response putByJson(String url, String jsonString, Headers<String, String> headers) {
		Request request = new Request.Builder().url(url).headers(okhttp3.Headers.of(headers))
				.put(RequestBody.create(jsonString, MediaType.parse(HttpClientUtils.APPLICATION_JSON_UTF8))).build();
		return this.put(request);
	}

	/**
	 * 发出一个put请求，并使用JSON的形式传递参数
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:38:25
	 * @param url 请求链接
	 * @param params 参数对象，将对象转换为JSON参数传递
	 * @return
	 */
	public Response putByJson(String url, Object params) {
		return this.putByJson(url, JSON.toJSONString(params));
	}

	/**
	 * 发出一个put请求，并使用JSON的形式传递参数，且携带请求头
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:38:25
	 * @param url 请求链接
	 * @param params 参数对象，将对象转换为JSON参数传递
	 * @param headers 请求头隐射表
	 * @return
	 */
	public Response putByJson(String url, Object params, Headers<String, String> headers) {
		return this.putByJson(url, JSON.toJSONString(params), headers);
	}

	/**
	 * 发出一个put请求，并且返回字符串格式的返回值
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月9日 下午3:55:09
	 * @param url
	 * @return
	 */
	public String putResp2String(String url) {
		Response response = this.put(url);
		return HttpClientUtils.handleResp(response);
	}

	/**
	 * 发出一个put请求，携带请求头，并且返回字符串格式的返回值
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月9日 下午3:55:09
	 * @param url 请求链接
	 * @param headers 请求头映射表
	 * @return
	 */
	public String putResp2String(String url, Headers<String, String> headers) {
		Response response = this.put(url, headers);
		return HttpClientUtils.handleResp(response);
	}

	/**
	 * 发出一个put请求,使用formData传递数据，并返回String格式的返回值
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月9日 下午3:55:09
	 * @param url 请求链接
	 * @param params 参数映射表
	 * @return
	 */
	public String putByFormDataResp2String(String url, Map<String, Object> params) {
		Response response = this.putByFormData(url, params);
		return HttpClientUtils.handleResp(response);
	}

	/**
	 * 发出一个put请求，使用formData传递数据，携带请求头，并返回String格式的返回值
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月9日 下午3:55:09
	 * @param url 请求链接
	 * @param params 参数映射表
	 * @param headers 请求头映射表
	 * @return
	 */
	public String putByFormDataResp2String(String url, Map<String, Object> params, Headers<String, String> headers) {
		Response response = this.putByFormData(url, params, headers);
		return HttpClientUtils.handleResp(response);
	}

	/**
	 * 发出一个put请求,使用formData传递数据，并返回String格式的返回值
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月9日 下午3:55:09
	 * @param url 请求链接
	 * @param params 参数映射表
	 * @return
	 */
	public String putByFormDataResp2String(String url, Object params) {
		Response response = this.putByFormData(url, params);
		return HttpClientUtils.handleResp(response);
	}

	/**
	 * 发出一个put请求，使用formData传递数据，携带请求头，并返回String格式的返回值
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月9日 下午3:55:09
	 * @param url 请求链接
	 * @param params 参数映射表
	 * @param headers 请求头映射表
	 * @return
	 */
	public String putByFormDataResp2String(String url, Object params, Headers<String, String> headers) {
		Response response = this.putByFormData(url, params, headers);
		return HttpClientUtils.handleResp(response);
	}

	/**
	 * 发出一个put请求,使用JSON格式传递数据，并返回String格式的返回值
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月9日 下午3:55:09
	 * @param url 请求链接
	 * @param jsonString 参数对象
	 * @return
	 */
	public String putByJsonResp2String(String url, String jsonString) {
		Response response = this.putByJson(url, jsonString);
		return HttpClientUtils.handleResp(response);
	}

	/**
	 * 发出一个put请求，使用JSON格式传递数据，携带请求头，并返回String格式的返回值
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月9日 下午3:55:09
	 * @param url 请求链接
	 * @param jsonString 参数对象
	 * @param headers 请求头映射表
	 * @return
	 */
	public String putByJsonResp2String(String url, String jsonString, Headers<String, String> headers) {
		Response response = this.putByJson(url, jsonString, headers);
		return HttpClientUtils.handleResp(response);
	}

	/**
	 * 发出一个put请求,使用JSON格式传递数据，并返回String格式的返回值
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月9日 下午3:55:09
	 * @param url 请求链接
	 * @param params 参数对象
	 * @return
	 */
	public String putByJsonResp2String(String url, Object params) {
		Response response = this.putByJson(url, params);
		return HttpClientUtils.handleResp(response);
	}

	/**
	 * 发出一个put请求，使用JSON格式传递数据，携带请求头，并返回String格式的返回值
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月9日 下午3:55:09
	 * @param url 请求链接
	 * @param params 参数对象
	 * @return
	 */
	public String putByJsonResp2String(String url, Object params, Headers<String, String> headers) {
		Response response = this.putByJson(url, params, headers);
		return HttpClientUtils.handleResp(response);
	}

	/**
	 * 发出一个put请求，并且将JSON格式的返回值转换为目标对象
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param clazz 目标对象类型
	 * @return
	 */
	public <T> T putResp2ObjFromJson(String url, Class<T> clazz) {
		String respString = this.putResp2String(url);
		return JSON.parseObject(respString, clazz);
	}

	/**
	 * 发出一个put请求，携带请求头，并且将JSON格式的返回值转换为目标对象
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param clazz 目标对象类型
	 * @param headers 请求头映射表
	 * @return
	 */
	public <T> T putResp2ObjFromJson(String url, Headers<String, String> headers, Class<T> clazz) {
		String respString = this.putResp2String(url, headers);
		return JSON.parseObject(respString, clazz);
	}

	/**
	 * 发出一个put请求，并且将JSON格式的返回值转换为目标对象
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param clazz 目标对象类型
	 * @return
	 */
	public <T> T putResp2ObjFromJson(String url, TypeReference<T> typeReference) {
		String respString = this.putResp2String(url);
		return JSON.parseObject(respString, typeReference);
	}

	/**
	 * 发出一个put请求，携带请求头，并且将JSON格式的返回值转换为目标对象
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param headers 请求头映射表
	 * @param clazz 目标对象类型
	 * @return
	 */
	public <T> T putResp2ObjFromJson(String url, Headers<String, String> headers, TypeReference<T> typeReference) {
		String respString = this.putResp2String(url, headers);
		return JSON.parseObject(respString, typeReference);
	}

	/**
	 * 发出一个put请求，使用formData传递数据，并且将JSON格式的返回值转换为目标对象
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param params 请求参数映射表
	 * @param clazz 目标对象类型
	 * @return
	 */
	public <T> T putByFormDataResp2ObjFromJson(String url, Map<String, Object> params, Class<T> clazz) {
		String respString = this.putByFormDataResp2String(url, params);
		return JSON.parseObject(respString, clazz);
	}

	/**
	 * 发出一个put请求，使用formData传递数据，携带请求头，并且将JSON格式的返回值转换为目标对象
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param params 请求参数映射表
	 * @param headers 请求头映射表
	 * @param clazz 目标对象类型
	 * @return
	 */
	public <T> T putByFormDataResp2ObjFromJson(String url, Map<String, Object> params, Headers<String, String> headers, Class<T> clazz) {
		String respString = this.putByFormDataResp2String(url, params, headers);
		return JSON.parseObject(respString, clazz);
	}

	/**
	 * 发出一个put请求，使用formData传递数据，并且将JSON格式的返回值转换为目标对象
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param params 请求参数映射表
	 * @param typeReference 目标对象类型
	 * @return
	 */
	public <T> T putByFormDataResp2ObjFromJson(String url, Map<String, Object> params, TypeReference<T> typeReference) {
		String respString = this.putByFormDataResp2String(url, params);
		return JSON.parseObject(respString, typeReference);
	}

	/**
	 * 发出一个put请求，使用formData传递数据，传递请求头，并且将JSON格式的返回值转换为目标对象
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param params 请求参数映射表
	 * @param headers 请求头映射表
	 * @param typeReference 目标对象类型
	 * @return
	 */
	public <T> T putByFormDataResp2ObjFromJson(String url, Map<String, Object> params, Headers<String, String> headers,
			TypeReference<T> typeReference) {
		String respString = this.putByFormDataResp2String(url, params, headers);
		return JSON.parseObject(respString, typeReference);
	}

	/**
	 * 发出一个put请求，使用formData传递数据，并且将JSON格式的返回值转换为目标对象
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param params 参数对象，把对象不为空的属性放到参数中
	 * @param clazz 目标对象类型
	 * @return
	 */
	public <T> T putByFormDataResp2ObjFromJson(String url, Object params, Class<T> clazz) {
		String respString = this.putByFormDataResp2String(url, params);
		return JSON.parseObject(respString, clazz);
	}

	/**
	 * 发出一个put请求，使用formData传递数据，传递请求头，并且将JSON格式的返回值转换为目标对象
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param params 参数对象，把对象不为空的属性放到参数中
	 * @param headers 请求头映射表
	 * @param clazz 目标对象类型
	 * @return
	 */
	public <T> T putByFormDataResp2ObjFromJson(String url, Object params, Headers<String, String> headers, Class<T> clazz) {
		String respString = this.putByFormDataResp2String(url, params, headers);
		return JSON.parseObject(respString, clazz);
	}

	/**
	 * 发出一个put请求，使用formData传递数据，并且将JSON格式的返回值转换为目标对象
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param params 请求参数映射表
	 * @param typeReference 目标对象类型
	 * @return
	 */
	public <T> T putByFormDataResp2ObjFromJson(String url, Object params, TypeReference<T> typeReference) {
		String respString = this.putByFormDataResp2String(url, params);
		return JSON.parseObject(respString, typeReference);
	}

	/**
	 * 发出一个put请求，使用formData传递数据，携带请求头，并且将JSON格式的返回值转换为目标对象
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param params 请求参数映射表
	 * @param headers 请求头映射表
	 * @param typeReference 目标对象类型
	 * @return
	 */
	public <T> T putByFormDataResp2ObjFromJson(String url, Object params, Headers<String, String> headers, TypeReference<T> typeReference) {
		String respString = this.putByFormDataResp2String(url, params, headers);
		return JSON.parseObject(respString, typeReference);
	}

	/**
	 * 发出一个put请求，使用JSON格式传递数据，并且将JSON格式的返回值转换为目标对象
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param jsonString JSON格式字符串
	 * @param clazz 目标对象类型
	 * @return
	 */
	public <T> T putByJsonResp2ObjFromJson(String url, String jsonString, Class<T> clazz) {
		String respString = this.putByJsonResp2String(url, jsonString);
		return JSON.parseObject(respString, clazz);
	}

	/**
	 * 发出一个put请求，使用JSON格式传递数据，携带请求头，并且将JSON格式的返回值转换为目标对象
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param jsonString JSON格式字符串
	 * @param headers 请求头映射表
	 * @param clazz 目标对象类型
	 * @return
	 */
	public <T> T putByJsonResp2ObjFromJson(String url, String jsonString, Headers<String, String> headers, Class<T> clazz) {
		String respString = this.putByJsonResp2String(url, jsonString, headers);
		return JSON.parseObject(respString, clazz);
	}

	/**
	 * 发出一个put请求，使用JSON格式传递数据，并且将JSON格式的返回值转换为目标对象
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param params JSON格式字符串
	 * @param typeReference 目标对象类型
	 * @return
	 */
	public <T> T putByJsonResp2ObjFromJson(String url, String jsonString, TypeReference<T> typeReference) {
		String respString = this.putByJsonResp2String(url, jsonString);
		return JSON.parseObject(respString, typeReference);
	}

	/**
	 * 发出一个put请求，使用JSON格式传递数据，携带请求头，并且将JSON格式的返回值转换为目标对象
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param jsonString JSON格式字符串
	 * @param headers 请求头映射表
	 * @param typeReference 目标对象类型
	 * @return
	 */
	public <T> T putByJsonResp2ObjFromJson(String url, String jsonString, Headers<String, String> headers, TypeReference<T> typeReference) {
		String respString = this.putByJsonResp2String(url, jsonString, headers);
		return JSON.parseObject(respString, typeReference);
	}

	/**
	 * 发出一个put请求，使用JSON格式传递数据，并且将JSON格式的返回值转换为目标对象
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param params 参数对象，把对象不为空的属性放到参数中
	 * @param clazz 目标对象类型
	 * @return
	 */
	public <T> T putByJsonResp2ObjFromJson(String url, Object params, Class<T> clazz) {
		String respString = this.putByJsonResp2String(url, params);
		return JSON.parseObject(respString, clazz);
	}

	/**
	 * 发出一个put请求，使用JSON格式传递数据，携带请求头，并且将JSON格式的返回值转换为目标对象
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param params 参数对象，把对象不为空的属性放到参数中
	 * @param headers 请求头映射表
	 * @param clazz 目标对象类型
	 * @return
	 */
	public <T> T putByJsonResp2ObjFromJson(String url, Object params, Headers<String, String> headers, Class<T> clazz) {
		String respString = this.putByJsonResp2String(url, params, headers);
		return JSON.parseObject(respString, clazz);
	}

	/**
	 * 发出一个put请求，使用JSON格式传递数据，并且将JSON格式的返回值转换为目标对象
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param params 请求参数映射表
	 * @param typeReference 目标对象类型
	 * @return
	 */
	public <T> T putByJsonResp2ObjFromJson(String url, Object params, TypeReference<T> typeReference) {
		String respString = this.putByJsonResp2String(url, params);
		return JSON.parseObject(respString, typeReference);
	}

	/**
	 * 发出一个put请求，使用JSON格式传递数据，携带请求头，并且将JSON格式的返回值转换为目标对象
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param params 请求参数映射表
	 * @param headers 请求头映射表
	 * @param typeReference 目标对象类型
	 * @return
	 */
	public <T> T putByJsonResp2ObjFromJson(String url, Object params, Headers<String, String> headers, TypeReference<T> typeReference) {
		String respString = this.putByJsonResp2String(url, params, headers);
		return JSON.parseObject(respString, typeReference);
	}

	/**
	 * 发出一个put请求，并且将JSON格式的返回值转换为目标对象集合
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param clazz 目标对象类型
	 * @return
	 */
	public <T> List<T> putResp2ListFromJson(String url, Class<T> clazz) {
		String respString = this.putResp2String(url);
		return JSON.parseArray(respString, clazz);
	}

	/**
	 * 发出一个put请求，携带请求头，并且将JSON格式的返回值转换为目标对象集合
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param headers 请求头映射表
	 * @param clazz 目标对象类型
	 * @return
	 */
	public <T> List<T> putResp2ListFromJson(String url, Headers<String, String> headers, Class<T> clazz) {
		String respString = this.putResp2String(url, headers);
		return JSON.parseArray(respString, clazz);
	}

	/**
	 * 发出一个put请求，使用FormData格式传递数据，并且将JSON格式的返回值转换为目标对象集合
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param params 请求参数映射表
	 * @param clazz 目标对象类型
	 * @return
	 */
	public <T> List<T> putByFormDataResp2ListFromJson(String url, Map<String, Object> params, Class<T> clazz) {
		String respString = this.putByFormDataResp2String(url, params);
		return JSON.parseArray(respString, clazz);
	}

	/**
	 * 发出一个put请求，使用FormData格式传递数据，携带请求头，并且将JSON格式的返回值转换为目标对象集合
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param params 请求参数映射表
	 * @param headers 请求头映射表
	 * @param clazz 目标对象类型
	 * @return
	 */
	public <T> List<T> putByFormDataResp2ListFromJson(String url, Map<String, Object> params, Headers<String, String> headers,
			Class<T> clazz) {
		String respString = this.putByFormDataResp2String(url, params, headers);
		return JSON.parseArray(respString, clazz);
	}

	/**
	 * 发出一个put请求，使用FormData格式传递数据，并且将JSON格式的返回值转换为目标对象集合
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param params 参数对象，把对象不为空的属性拼接到参数中
	 * @param clazz 目标对象类型
	 * @return
	 */
	public <T> List<T> putByFormDataResp2ListFromJson(String url, Object params, Class<T> clazz) {
		String respString = this.putByFormDataResp2String(url, params);
		return JSON.parseArray(respString, clazz);
	}

	/**
	 * 发出一个put请求，使用FormData格式传递数据，携带请求头，并且将JSON格式的返回值转换为目标对象集合
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param params 参数对象，把对象不为空的属性拼接到参数中
	 * @param headers 请求头映射表
	 * @param clazz 目标对象类型
	 * @return
	 */
	public <T> List<T> putByFormDataResp2ListFromJson(String url, Object params, Headers<String, String> headers, Class<T> clazz) {
		String respString = this.putByFormDataResp2String(url, params, headers);
		return JSON.parseArray(respString, clazz);
	}

	/**
	 * 发出一个put请求，使用JSON格式传递数据，携带请求头，并且将JSON格式的返回值转换为目标对象集合
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param jsonString JSON格式字符串
	 * @param headers 请求头映射表
	 * @param clazz 目标对象类型
	 * @return
	 */
	public <T> List<T> putByJsonResp2ListFromJson(String url, String jsonString, Headers<String, String> headers, Class<T> clazz) {
		String respString = this.putByJsonResp2String(url, jsonString, headers);
		return JSON.parseArray(respString, clazz);
	}

	/**
	 * 发出一个put请求，使用JSON格式传递数据，并且将JSON格式的返回值转换为目标对象集合
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param jsonString JSON格式字符串
	 * @param clazz 目标对象类型
	 * @return
	 */
	public <T> List<T> putByJsonResp2ListFromJson(String url, String jsonString, Class<T> clazz) {
		String respString = this.putByJsonResp2String(url, jsonString);
		return JSON.parseArray(respString, clazz);
	}

	/**
	 * 发出一个put请求，使用JSON格式传递数据，并且将JSON格式的返回值转换为目标对象集合
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param params 参数对象，把对象不为空的属性拼接到参数中
	 * @param clazz 目标对象类型
	 * @return
	 */
	public <T> List<T> putByJsonResp2ListFromJson(String url, Object params, Class<T> clazz) {
		String respString = this.putByJsonResp2String(url, params);
		return JSON.parseArray(respString, clazz);
	}

	/**
	 * 发出一个put请求，使用JSON格式传递数据，携带请求头，并且将JSON格式的返回值转换为目标对象集合
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年8月12日 上午11:46:44
	 * @param url 请求链接
	 * @param params 参数对象，把对象不为空的属性拼接到参数中
	 * @param headers 请求头映射表
	 * @param clazz 目标对象类型
	 * @return
	 */
	public <T> List<T> putByJsonResp2ListFromJson(String url, Object params, Headers<String, String> headers, Class<T> clazz) {
		String respString = this.putByJsonResp2String(url, params);
		return JSON.parseArray(respString, clazz);
	}
}
