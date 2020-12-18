package in.auto.jira.common.utils.okhttp.config;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.google.common.collect.Sets;

import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import in.auto.jira.common.utils.Tools;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * okhttp配置
 * 
 * @author yinjun (yinjunonly@163.com)
 * @date 2019年4月29日 上午10:57:14
 */
@Slf4j
@Configuration
public class OkHttpConfig {
	@Autowired
	OkHttpProperties okHttpProperties;
	@Autowired
	ConnectionPoolProperties connectionPoolProperties;

	@Bean
	@ConditionalOnMissingBean(OkHttpClient.class)
	public OkHttpClient okHttpClient() {
		return this.newClient();
	}

	public OkHttpClient newClient() {
		return new OkHttpClient.Builder().sslSocketFactory(sslSocketFactory(), x509TrustManager())
				.retryOnConnectionFailure(okHttpProperties.getRetryOnConnectionFailure())// 是否重试失败连接
				.connectionPool(pool())// 连接池
				.connectTimeout(okHttpProperties.getConnectTimeout(), TimeUnit.SECONDS)
				.readTimeout(okHttpProperties.getReadTimeout(), TimeUnit.SECONDS)
				.hostnameVerifier(new HostnameVerifier() {
					@Override
					public boolean verify(String arg0, SSLSession arg1) {
						return true;
					}
				}).cookieJar(new CookieJar() {
					private final HashMap<String, Set<Cookie>> cookieStore = new HashMap<>();

					@Override
					public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
						if (cookieStore.containsKey(url.host())) {
							Set<Cookie> current = cookieStore.get(url.host());
							for (Cookie cookie : cookies) {
								current.add(cookie);
							}
						} else {
							Set<Cookie> current = Sets.newHashSet();
							for (Cookie cookie : cookies) {
								current.add(cookie);
							}
							cookieStore.put(url.host(), current);
						}
					}

					@Override
					public List<Cookie> loadForRequest(HttpUrl url) {
						Set<Cookie> cookies = cookieStore.get(url.host());
						List<Cookie> current = Lists.newArrayList();
						if (!Tools.isNullOrEmpty(cookies)) {
							for (Cookie cookie : cookies) {
								current.add(cookie);
							}
						}
						return cookies != null ? current : new ArrayList<Cookie>();
					}
				}).build();
	}

	public X509TrustManager x509TrustManager() {
		return new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}
		};
	}

	public SSLSocketFactory sslSocketFactory() {
		try {
			// 信任任何链接
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, new TrustManager[] { x509TrustManager() }, new SecureRandom());
			return sslContext.getSocketFactory();
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage(), e);
		} catch (KeyManagementException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Create a new connection pool with tuning parameters appropriate for a
	 * single-user application. The tuning parameters in this pool are subject to
	 * change in future OkHttp releases. Currently
	 */
	public ConnectionPool pool() {
		return new ConnectionPool(connectionPoolProperties.getMaxIdleConnections(),
				connectionPoolProperties.getKeepAliveDuration(), connectionPoolProperties.getTimeUnit());
	}
}
