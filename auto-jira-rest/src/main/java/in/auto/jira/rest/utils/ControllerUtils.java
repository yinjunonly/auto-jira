package in.auto.jira.rest.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import in.auto.jira.common.enums.RestErrorCode;
import in.auto.jira.common.exceptions.ErrorCode;
import in.auto.jira.common.model.RestResult;
import in.auto.jira.common.model.Result;
import com.google.common.base.Charsets;

import lombok.extern.slf4j.Slf4j;

/**
 * 控制器工具类
 * 
 * @author yinju
 * @date 2017年12月11日下午3:19:52
 */
@Slf4j
public class ControllerUtils {
	/**
	 * 成功
	 * 
	 * @author yinju
	 * @return
	 */
	public static <T> Result<T> success() {
		return new RestResult<>();
	}

	/**
	 * 成功
	 * 
	 * @author yinju
	 * @param data 数据
	 * @return
	 */
	public static <T> Result<T> success(T data) {
		RestResult<T> restResult = new RestResult<>();
		restResult.setData(data);
		return restResult;
	}

	/**
	 * 错误（请求参数错误）
	 * 
	 * @return
	 */
	public static <T> Result<T> failForParameter() {
		RestResult<T> restResult = new RestResult<>();
		restResult.setErrorCode(RestErrorCode.REQUEST_PARAMETER_ERROR);
		return restResult;
	}

	/**
	 * 错误
	 * 
	 * @author yinju
	 * @param errorCode 错误枚举
	 * @return
	 */
	public static <T> Result<T> fail(ErrorCode errorCode) {
		RestResult<T> restResult = new RestResult<>();
		restResult.setErrorCode(errorCode);
		return restResult;
	}

	/**
	 * 错误
	 * 
	 * @author yinju
	 * @param message 文字描述
	 * @param status  状态
	 * @return
	 */
	public static <T> Result<T> fail(String message, Integer status) {
		RestResult<T> restResult = new RestResult<>();
		restResult.setMessage(message);
		restResult.setStatus(status);
		return restResult;
	}

	/**
	 * 发送文件给客户端下载
	 * 
	 * @author yinjun(yinjunonly@163.com)
	 * @param response 返回对象
	 * @param file     文件对象
	 * @param fileName 文件名
	 */
	public static void sendFile(HttpServletResponse response, File file, String fileName) {
		response.setContentType(MediaType.APPLICATION_OCTET_STREAM.getType());
		try {
			response.addHeader(HttpHeaders.CONTENT_DISPOSITION, new StringBuilder("attachment;filename=")
					.append(URLEncoder.encode(fileName, Charsets.UTF_8.name())).toString());
			response.addHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
			response.addHeader(HttpHeaders.PRAGMA, "no-cache");
			response.addHeader(HttpHeaders.EXPIRES, "0");
			// 实现文件下载
			byte[] buffer = new byte[1024];
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			try {
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);
				OutputStream os = response.getOutputStream();
				int i = bis.read(buffer);
				while (i != -1) {
					os.write(buffer, 0, i);
					i = bis.read(buffer);
				}
				log.info("Download the song successfully!,{}", fileName);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				log.error("Download the song failed!,{}", fileName);
			} finally {
				if (bis != null) {
					try {
						bis.close();
					} catch (IOException e) {
						log.error(e.getMessage(), e);
					}
				}
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
						log.error(e.getMessage(), e);
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 发送文件给客户端下载
	 * 
	 * @author yinjun(yinjunonly@163.com)
	 * @param response 返回对象
	 * @param pathName 文件路径
	 * @param fileName 文件名
	 */
	public static void sendFile(HttpServletResponse response, String pathName, String fileName) {
		File file = new File(pathName);
		sendFile(response, file, fileName);
	}

	/**
	 * 设置下载信息
	 * 
	 * @author yinjun(yinjunonly@163.com)
	 * @param response
	 * @param fileName
	 */
	public static void setDownloadInfo(HttpServletResponse response, String fileName) {
		response.setContentType(MediaType.APPLICATION_OCTET_STREAM.getType());
		try {
			response.addHeader(HttpHeaders.CONTENT_DISPOSITION, new StringBuilder("attachment;filename=")
					.append(URLEncoder.encode(fileName, Charsets.UTF_8.name())).toString());
			response.addHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
			response.addHeader(HttpHeaders.PRAGMA, "no-cache");
			response.addHeader(HttpHeaders.EXPIRES, "0");
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		}
	}
}
