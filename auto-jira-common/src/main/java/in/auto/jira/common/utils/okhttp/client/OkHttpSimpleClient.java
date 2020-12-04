package in.auto.jira.common.utils.okhttp.client;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

/**
 * okHttp简单的客户端
 * 
 * @author yinjun (yinjunonly@163.com)
 * @date 2019年8月12日 上午11:00:15
 */
@Component
public class OkHttpSimpleClient {
	/** 简单的get请求客户端，使用get请求时请使用这个客户端调用 */
	@Resource
	public GetClient getClient;
	/** 简单的post请求客户端，使用post请求时请使用这个客户端调用 */
	@Resource
	public PostClient postClient;
	/** 简单的put请求客户端，使用put请求时请使用这个客户端调用 */
	@Resource
	public PutClient putClient;
	/** 简单的patch请求客户端，使用patch请求时请使用这个客户端调用 */
	@Resource
	public PatchClient patchClient;
	/** 简单的delete请求客户端，使用delete请求时请使用这个客户端调用 */
	@Resource
	public DeleteClient deleteClient;
}
