package in.auto.jira.common.rsa;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.util.Base64Utils;

import in.auto.jira.common.exceptions.ErrorCodeExceptionFactory;

import lombok.extern.slf4j.Slf4j;

/**
 * RSA工具类
 * 
 * @author yinjun (yinjunonly@163.com)
 * @date 2019年5月5日 下午4:24:39
 */
@Slf4j
public class RSAUtils {
	/**
	 * 加密算法RSA
	 */
	private static final String KEY_ALGORITHM = "RSA";

	/**
	 * MD5RSA签名算法
	 */
	public static final String SIGNATURE_MD5RSA_ALGORITHM = "MD5WithRSA";

	/**
	 * SH1RSA签名算法
	 */
	public static final String SIGNATURE_SHA1RSA_ALGORITHM = "SHA1WithRSA";

	/**
	 * RSA2签名算法
	 */
	public static final String SIGN_SHA256RSA_ALGORITHMS = "SHA256WithRSA";

	/**
	 * 获取公钥的key
	 */
	private static final String PUBLIC_KEY = "RSAPublicKey";

	/**
	 * 获取私钥的key
	 */
	private static final String PRIVATE_KEY = "RSAPrivateKey";

	/**
	 * RSA最大加密明文大小
	 */
	private static final int MAX_ENCRYPT_BLOCK = 117;

	/**
	 * RSA最大解密密文大小
	 */
	private static final int MAX_DECRYPT_BLOCK = 128;

	/**
	 * 生成密钥对(公钥和私钥)，默认keySize = 2048
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> genKeyPair() {
		return genKeyPair(2048);
	}

	/**
	 * 生成密钥对（公钥和私钥）
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年5月27日 下午2:12:56
	 * @param keySize key大小
	 * @return
	 */
	public static Map<String, Object> genKeyPair(Integer keySize) {
		KeyPairGenerator keyPairGen;
		try {
			keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
			keyPairGen.initialize(keySize);
			KeyPair keyPair = keyPairGen.generateKeyPair();
			RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
			RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
			Map<String, Object> keyMap = new HashMap<String, Object>(2);
			keyMap.put(PUBLIC_KEY, publicKey);
			keyMap.put(PRIVATE_KEY, privateKey);
			return keyMap;
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage(), e);
			throw ErrorCodeExceptionFactory.build("密钥对生成失败！", 9999);
		}
	}

	/**
	 * 用私钥对信息生成数字签名,加密算法默认为RSA,默认签名算法为SHA1WithRSA
	 * 
	 * @param data       已加密数据
	 * @param privateKey 私钥(BASE64编码)
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String sign(byte[] data, String privateKey) {
		return sign(data, privateKey, SIGNATURE_SHA1RSA_ALGORITHM);
	}

	/**
	 * 用私钥对信息生成数字签名
	 * 
	 * @param data          已加密数据
	 * @param privateKey    私钥(BASE64编码)
	 * @param signAlgorithm 签名算法
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String sign(byte[] data, String privateKey, String signAlgorithm) {
		byte[] keyBytes = Base64Utils.decodeFromString(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory;
		try {
			keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
			Signature signature = Signature.getInstance(signAlgorithm);
			signature.initSign(privateK);
			signature.update(data);
			return Base64Utils.encodeToString(signature.sign());
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | SignatureException e) {
			log.error(e.getMessage(), e);
			throw ErrorCodeExceptionFactory.build("签名失败！", 9999);
		}
	}

	/**
	 * 校验数字签名（默认签名算法为SHA1WithRSA）
	 * 
	 * @param data      已加密数据
	 * @param publicKey 公钥(BASE64编码)
	 * @param sign      数字签名
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	public static boolean verify(byte[] data, String publicKey, String sign) {
		return verify(data, publicKey, sign, SIGNATURE_SHA1RSA_ALGORITHM);
	}

	/**
	 * 校验数字签名
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年5月27日 下午2:24:35
	 * @param data          已加密数据
	 * @param publicKey     公钥
	 * @param sign          数字签名
	 * @param signAlgorithm 签名算法（常见有：MD5withRSA、SHA1withRSA、SHA256withRSA）
	 * @return
	 */
	public static boolean verify(byte[] data, String publicKey, String sign, String signAlgorithm) {
		byte[] keyBytes = Base64Utils.decodeFromString(publicKey);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			PublicKey publicK = keyFactory.generatePublic(keySpec);
			Signature signature = Signature.getInstance(signAlgorithm);
			signature.initVerify(publicK);
			signature.update(data);
			return signature.verify(Base64Utils.decodeFromString(sign));
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | SignatureException e) {
			log.error(e.getMessage(), e);
			throw ErrorCodeExceptionFactory.build("验证签名失败！", 9999);
		}
	}

	/**
	 * 私钥解密
	 * 
	 * @param encryptedData 已加密数据
	 * @param privateKey    私钥(BASE64编码)
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) {
		byte[] keyBytes = Base64Utils.decodeFromString(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, privateK);
			int inputLen = encryptedData.length;
			int offSet = 0;
			byte[] cache;
			int i = 0;
			// 对数据分段解密
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
					cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * MAX_DECRYPT_BLOCK;
			}
			byte[] decryptedData = out.toByteArray();
			return decryptedData;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException | NoSuchPaddingException | IOException e) {
			log.error(e.getMessage(), e);
			throw ErrorCodeExceptionFactory.build("私钥解密失败！", 9999);
		}
	}

	/**
	 * 公钥解密
	 * 
	 * @param encryptedData 已加密数据
	 * @param publicKey     公钥(BASE64编码)
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) {
		byte[] keyBytes = Base64Utils.decodeFromString(publicKey);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			Key publicK = keyFactory.generatePublic(x509KeySpec);
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, publicK);
			int inputLen = encryptedData.length;
			int offSet = 0;
			byte[] cache;
			int i = 0;
			// 对数据分段解密
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
					cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * MAX_DECRYPT_BLOCK;
			}
			byte[] decryptedData = out.toByteArray();
			return decryptedData;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw ErrorCodeExceptionFactory.build("公钥解密失败！", 9999);
		}
	}

	/**
	 * 公钥加密
	 * 
	 * @author yinjun (yinjunonly@163.com)
	 * @date 2019年5月27日 下午2:41:33
	 * @param data      源数据
	 * @param publicKey 公钥(BASE64编码)
	 * @return
	 */
	public static byte[] encryptByPublicKey(byte[] data, String publicKey) {
		byte[] keyBytes = Base64Utils.decodeFromString(publicKey);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			Key publicK = keyFactory.generatePublic(x509KeySpec);
			// 对数据加密
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, publicK);
			int inputLen = data.length;
			int offSet = 0;
			byte[] cache;
			int i = 0;
			// 对数据分段加密
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
					cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(data, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * MAX_ENCRYPT_BLOCK;
			}
			byte[] encryptedData = out.toByteArray();
			return encryptedData;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw ErrorCodeExceptionFactory.build("公钥加密失败！", 9999);
		}
	}

	/**
	 * 私钥加密
	 * 
	 * @param data       源数据
	 * @param privateKey 私钥(BASE64编码)
	 * @return
	 */
	public static byte[] encryptByPrivateKey(byte[] data, String privateKey) {
		byte[] keyBytes = Base64Utils.decodeFromString(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, privateK);
			int inputLen = data.length;
			int offSet = 0;
			byte[] cache;
			int i = 0;
			// 对数据分段加密
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
					cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(data, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * MAX_ENCRYPT_BLOCK;
			}
			byte[] encryptedData = out.toByteArray();
			return encryptedData;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw ErrorCodeExceptionFactory.build("私钥加密失败！", 9999);
		}
	}

	/**
	 * 获取私钥
	 * 
	 * @param keyMap 密钥对
	 * @return
	 * @throws Exception
	 */
	public static String getPrivateKey(Map<String, Object> keyMap) {
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		return Base64Utils.encodeToString(key.getEncoded());
	}

	/**
	 * 获取公钥
	 * 
	 * @param keyMap 密钥对
	 * @return
	 * @throws Exception
	 */
	public static String getPublicKey(Map<String, Object> keyMap) {
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		return Base64Utils.encodeToString(key.getEncoded());
	}

	public static void main(String[] args) {
//		String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDHXacrpsfJ1HhEbUSQdJbAvKA1DB4lT5yyhuLGdkDt/WuQA1wMDEiZeCv8PVRXew+LrAXBn735TFMvI9limZl1/2gcsoPUbOoaAb3rmk9QsPSJOXlffkBPKy7Qpv3DBl1//M7PkRLkDhHpS9FVilXjrfS/bY7jaSqXd0eU/JoJQQIDAQAB";
//		String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMddpyumx8nUeERtRJB0lsC8oDUMHiVPnLKG4sZ2QO39a5ADXAwMSJl4K/w9VFd7D4usBcGfvflMUy8j2WKZmXX/aByyg9Rs6hoBveuaT1Cw9Ik5eV9+QE8rLtCm/cMGXX/8zs+REuQOEelL0VWKVeOt9L9tjuNpKpd3R5T8mglBAgMBAAECgYEAmBCzJfR8h1HsuiSuG5ESO8ikWlH33HWxC+sepkoklGKr3JjzaK0l4wlqY2dUIbXX0a1QZrO1b6Xr/RTXBwIpriFoSgn1lTFquQuKwQIE68jY7RjElhiaZ9RPYUJXltqQS8uWB33y+QX/YszbbArOhAWCjYrCTMDJgqSdmVAEvwECQQD1SgLvYSd2a9kYAVWLy9UrwxWSQozouEDJu3H9HlyUo7DZSKaA3RI5VIe/raK+iCSvujVECdzO34h0smuuptD5AkEA0BJInsbTW1IDlw+Bm9mCiZ1BAXuI6gy22u4l+RVfLnpqrmxFrkzZlv8CuLQH0U0Q2cXXjCfCre04W7YXWfbUiQJAeIkXKiT27HWQ5//31suihG/IUOhNMCsjAz8Og9EAs7ZAuHXABIEebHfCgYnE+JYUWRpLBJVIIYytFJN84QVumQJADzx3guDuUy6TQrmI9R+dVnRkcTZZlUHvFeMAdtfgy2d9bl5RT+0itdLgoSPVurbY+AlrukCjGddYHCceWKEheQJAY6CyYP0I0xgDMz1hNBsJhItBb0AAvWPafSykc5TQtFo3Nr04s6N1QbcpZTdHVvcDsm1M+lZ3VQyH9CwFSzgSjQ==";
//		byte[] pw = encryptByPublicKey("yinjun666".getBytes(Charsets.UTF_8), publicKey);
//		String base64pwd = Base64Utils.encodeToString(pw);
//		byte[] pp = decryptByPrivateKey(Base64Utils.decodeFromString(base64pwd), privateKey);
//		System.err.println(new String(pp));
		// Map<String, Object> keyMap = genKeyPair(2048);
		// System.err.println(publicKey);
//		String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvIuqsexbQZnwfp8JD5MwdAgL094Ghc/JYK+v0gXBEwoBaGfW51Mvc0XJKw52fooBLC0KQD/X7RNM2Wjd7bO1nNY9hx9spf4a0u24g1UIdLiuVVUom6YvsZb5JuGP3nwdaWJVFHRoAw30X+H0aPmxCvaTfYG9xmg/5enrO3rJS+PusXfqhNRDSiH+l/tK0dAfyqhybI90iwwGl73q2c5vRhQ3XH+bqcjpkwkammRGc+QSuoasw+L8IJZTvbFVXO2BuQSWvHfHhNAJWTprR4sORE/cYD7lHrDjYhNvR+5NVDCF+O6frSGyTNQCy1rlFK/CMgAeXGnDsr3NPpQjsMcPrwIDAQAB";
//		String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC8i6qx7FtBmfB+nwkPkzB0CAvT3gaFz8lgr6/SBcETCgFoZ9bnUy9zRckrDnZ+igEsLQpAP9ftE0zZaN3ts7Wc1j2HH2yl/hrS7biDVQh0uK5VVSibpi+xlvkm4Y/efB1pYlUUdGgDDfRf4fRo+bEK9pN9gb3GaD/l6es7eslL4+6xd+qE1ENKIf6X+0rR0B/KqHJsj3SLDAaXverZzm9GFDdcf5upyOmTCRqaZEZz5BK6hqzD4vwgllO9sVVc7YG5BJa8d8eE0AlZOmtHiw5ET9xgPuUesONiE29H7k1UMIX47p+tIbJM1ALLWuUUr8IyAB5cacOyvc0+lCOwxw+vAgMBAAECggEAAr9aRmt2TPdsTLwOir6PVzGC0pyKpcqIWbNVsY7J6Hgd2yKgPBC3QUvECa+VRHTIXZdubc9AW0ELvqNLkJoszFNG5EAt+iFAzNAP/0Hu9Jx4Vd/+zn3WdhUIHmvtB8XxO8bmDH1BwsGiTP9i62CwFsPPFGYemNSOEdCdz+kTI9rxAmiDYHZqy5OFjJ2CgFyyQeq8tmssdc+1u190R4aM5WP3EVqePkEcKmCDktT3qjnUm8I2bVAgLx4wdLhsDjzjnCk+AgewZfr7TRtb3gBA2QLXo6C1cq9rMtlhgr8SAGtRSy8BbW0mw0teaiJ5C6CvC3ou8wN92PIibEWkxmhByQKBgQDw2NAavO3NqcuSWUrH24dw2Vjr9MQBOdoyyvL+cUt1p07DXv0J851HyuuUf3vSpA3AG9pOpwha262mXWFyT9T9jXO7bvboIr7x6nNS/6mSjZLn9r4BnKhoDZXnxs5cXAHuc8qyj7R3uY47OCdOJJKkHx3tP0AQQgHTq6sPQxVq+wKBgQDIaHcU8pPTt9eYcEPqM1lnvdgRjQCPMyBB85ykTgTfXatUAVjb/8sETFsxCVbC1qyMoauDgCg8JOjKnokLWgtsB/P+nTMFAB85L1aD8hGBzvJkmrvXm4jqPHo3Uco6jjYsslzcHHeh7ZMPR+fylqdB83WDFpx4FMsn1vsHAv8P3QKBgQCs7B5OdEORHE4LH40vfZD7FzJfB3gLIUxml4xr31A8gwlsALFRCdZtC2alUfrHO/6bZeQSAJ9hDMTa48TpXCQigAMOGhewh/Hso1Z1yxYnIPrPDTtS0PWg79Anf3f+nBjO6dgo6EC8jT9viVu/IUcgCy9jfbwzcBD7tw+0f4fLLQKBgEXygC2lhQcOUYd5F5s0lO3qivGFPz6733RlbLTELlsvvE6zsaoHm3pClEmHMusKNmtXOh/JqUFkuIOP1dFKelYYaIbjox0Jr9wrs4yV/ppJ+rw2bX2RYbYvqZCJUYYigLIVBugkX3T7EKrys/gtsO8J5Gf+ilo3F7kuBauq1g9ZAoGAdBT3LCYl0sVOthHSiU4B48KvJgUTqiFQo7fr8ndWZmSC5c42BbG8wJme9jhgzkXkhfLbgGw1dtE8GvMRRu6whnYKITWFHuTml8FDzZ3zF7/x4NoKJ5OiZiRhdCf4I0y+ldnrs8RdBGYXcqt69W5+CjBPjBlbd8JTrLyMh8Om7oY=";
//		// System.err.println(privateKey);
//		String sign = sign("123".getBytes(), privateKey, SIGN_SHA256RSA_ALGORITHMS);
//		System.err.println(sign);
//		boolean success = verify("123".getBytes(), publicKey, sign, SIGN_SHA256RSA_ALGORITHMS);
//		System.out.println(success);
	}
}
