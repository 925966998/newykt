package com.ky.ykt.utils;



import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;

/**
 * @date 2020-03-23 10:9:49
 */
public class P_Sm4Util {
	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	private static String ENCODING = "UTF-8";
	public static final String ALGORITHM_NAME = "SM4";
	// 加密算法/分组加密模式/分组填充方式
	// PKCS5Padding-以8个字节为一组进行分组加密
	// 定义分组加密模式使用：PKCS5Padding
	public static final String ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS5Padding";
	// 128-32位16进制；256-64位16进制
	public static final int DEFAULT_KEY_SIZE = 128;

	/**
	 * 生成ECB暗号
	 * 
	 * @explain ECB模式（电子密码本模式：Electronic codebook）
	 * @param algorithmName
	 *            算法名称
	 * @param mode
	 *            模式
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static Cipher generateEcbCipher(String algorithmName, int mode,
											byte[] key) throws Exception {
		Cipher cipher = Cipher.getInstance(algorithmName,
				BouncyCastleProvider.PROVIDER_NAME);
		Key sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
		cipher.init(mode, sm4Key);
		return cipher;
	}

	/**
	 * 自动生成密钥
	 * 
	 * @explain
	 * @return
	 * @throws
	 * @throws
	 */
	public static byte[] generateKey() throws Exception {
		return generateKey(DEFAULT_KEY_SIZE);
	}



	/**
	 * @explain
	 * @param keySize
	 * @return
	 * @throws Exception
	 */
	public static byte[] generateKey(int keySize) throws Exception {
		KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM_NAME,
				BouncyCastleProvider.PROVIDER_NAME);
		kg.init(keySize, new SecureRandom());
		return kg.generateKey().getEncoded();
	}

	/**
	 * sm4加密
	 * 
	 * @explain 加密模式：ECB 密文长度不固定，会随着被加密字符串长度的变化而变化
	 * @param hexKey
	 *            16进制密钥（忽略大小写）
	 * @param paramStr
	 *            待加密字符串
	 * @return 返回base64编码的加密字符串
	 * @throws Exception
	 */
	public static String encryptEcb(String hexKey, String paramStr)
			throws Exception {
//		Base64Encoder encoder = new Base64Encoder();
//		Base64 encoder = new Base64();
		
//		BASE64Encoder encoder = new BASE64Encoder();
		String cipherText = "";
		// 16进制字符串-->byte[]
		byte[] keyData = ByteUtils.fromHexString(hexKey);
//		System.out.println(keyData.length);
		// String-->byte[]
		byte[] srcData = paramStr.getBytes(ENCODING);
		// 加密后的数组
		byte[] cipherArray = encrypt_Ecb_Padding(keyData, srcData);
		// byte[]-->base64String
		cipherText = new String(Base64.encode(cipherArray));
//		cipherText = ByteUtils.toHexString(cipherArray);
		return cipherText;
	}


	public static String encryptEcb(String hexKey, String paramStr,String charSet)
			throws Exception {
//		Base64Encoder encoder = new Base64Encoder();
//		Base64 encoder = new Base64();
		ENCODING = charSet;
//		BASE64Encoder encoder = new BASE64Encoder();
		String cipherText = "";
		// 16进制字符串-->byte[]
		byte[] keyData = ByteUtils.fromHexString(hexKey);
//		System.out.println(keyData.length);
		// String-->byte[]
		byte[] srcData = paramStr.getBytes(ENCODING);
		// 加密后的数组
		byte[] cipherArray = encrypt_Ecb_Padding(keyData, srcData);
		// byte[]-->base64String
		cipherText = new String(Base64.encode(cipherArray));
//		cipherText = ByteUtils.toHexString(cipherArray);
		return cipherText;
	}

	/**
	 * 加密模式之Ecb
	 * 
	 * @explain
	 * @param key
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt_Ecb_Padding(byte[] key, byte[] data)
			throws Exception {
		Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_PADDING,
				Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(data);
	}

	/**
	 * sm4解密
	 * 
	 * @explain 解密模式：采用ECB
	 * @param hexKey
	 *            16进制密钥
	 * @param cipherText
	 *            base64编码的加密字符串
	 * @return 解密后的字符串
	 * @throws Exception
	 */
	public static String decryptEcb(String hexKey, String cipherText)
			throws Exception {
//		BASE64Decoder decoder = new BASE64Decoder();
		// 用于接收解密后的字符串
		String decryptStr = "";

//		if(hexKey==null || "".equals(hexKey)){
//			hexKey = getDefaultKey();
//		}
		// hexString-->byte[]
		byte[] keyData = ByteUtils.fromHexString(hexKey);
		// base64String-->byte[]
		byte[] cipherData = Base64.decode(cipherText);
		// 解密
		byte[] srcData = decrypt_Ecb_Padding(keyData, cipherData);
		// byte[]-->String
		decryptStr = new String(srcData, ENCODING);
		return decryptStr;
	}

	public static String decryptEcb(String hexKey, String cipherText,String charSet)
			throws Exception {
//		BASE64Decoder decoder = new BASE64Decoder();
		// 用于接收解密后的字符串
		String decryptStr = "";

//		if(hexKey==null || "".equals(hexKey)){
//			hexKey = getDefaultKey();
//		}
		// hexString-->byte[]
		byte[] keyData = ByteUtils.fromHexString(hexKey);
		// base64String-->byte[]
		byte[] cipherData = Base64.decode(cipherText);
		// 解密
		byte[] srcData = decrypt_Ecb_Padding(keyData, cipherData);
		// byte[]-->String
		decryptStr = new String(srcData, charSet);
		return decryptStr;
	}
	/**
	 * 解密
	 * 
	 * @explain
	 * @param key
	 * @param cipherText
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt_Ecb_Padding(byte[] key, byte[] cipherText)
			throws Exception {
		Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_PADDING,
				Cipher.DECRYPT_MODE, key);
		return cipher.doFinal(cipherText);
	}

	/**
	 * 校验加密前后的字符串是否为同一数据
	 * 
	 * @explain
	 * @param hexKey
	 *            16进制密钥（忽略大小写）
	 * @param cipherText
	 *            base64编码加密后的字符串
	 * @param paramStr
	 *            加密前的字符串
	 * @return 是否为同一数据
	 * @throws Exception
	 */
	public static boolean verifyEcb(String hexKey, String cipherText,
			String paramStr) throws Exception {
//		BASE64Decoder decoder = new BASE64Decoder();
		// 用于接收校验结果
		boolean flag = false;
		// hexString-->byte[]
		byte[] keyData = ByteUtils.fromHexString(hexKey);
		// 将base64编码字符串转换成数组
		byte[] cipherData = Base64.decode(cipherText);
		// 解密
		byte[] decryptData = decrypt_Ecb_Padding(keyData, cipherData);
		// 将原字符串转换成byte[]
		byte[] srcData = paramStr.getBytes(ENCODING);
		// 判断2个数组是否一致
		flag = Arrays.equals(decryptData, srcData);
		return flag;
	}






	public static void main(String[] args) {
		try {
			String json = "<Service><Head><ID>BT006</ID><UUID>12fc5b58-aedd-42be-85c3-dc53f4512f40</UUID><CallDate>2022-09-08</CallDate><CallTime>17:00:00</CallTime><CallUser>350355000001</CallUser><District>140725</District><BankNo>601103</BankNo></Head><Body><Result>1</Result><ResultReason></ResultReason><SubAmt>105</SubAmt><SubRowCnt>2</SubRowCnt><SucAmt>105</SucAmt><SucRowCnt>2</SucRowCnt><Extend1></Extend1><Extend2></Extend2></Body></Service>\n";
			// 自定义的32位 16进制密钥713784A8F7284941CB9C1FC522B637BA
//			String key = "4A65463855397748464F4D6673325938";
			String key = "4A65463855397748464F4D6673325938";
//			System.out.println(new String(ByteUtils.fromHexString(key)));
			String cipher = encryptEcb(key, json);
			System.out.println(cipher);// 05a087dc798bb0b3e80553e6a2e73c4ccc7651035ea056e43bea9d125806bf41c45b4263109c8770c48c5da3c6f32df444f88698c5c9fdb5b0055b8d042e3ac9d4e3f7cc67525139b64952a3508a7619
//			System.out.println(new String(Base64.decode(cipher)));
			
//			System.out.println(cipher.toUpperCase());// 05a087dc798bb0b3e80553e6a2e73c4ccc7651035ea056e43bea9d125806bf41c45b4263109c8770c48c5da3c6f32df444f88698c5c9fdb5b0055b8d042e3ac9d4e3f7cc67525139b64952a3508a7619
//			System.out.println(verifyEcb(key, cipher, json));// true
			//json = decryptEcb(key, "sRP+EprvZ/2AUjim4MmWRPa1SrE4QyEqcXjggQZHzQFRq/ElbKwKYv8SpPPFsgq7KbC3khbdHVKpQY45pJpMDmWbvzR/adf5i83RzgZGEh1+8LxJp1mgWI3b6MbtBpCF");
			//json = decryptEcb(key, "GpatOZTzJCH5BjwaBBcWZ5wdznGe+/NufiWigbBPkoaop4S/Mw7gyAT34NSA84bn9D5XPQDfodXSJQK3J7ZyO23cdtOyfi0ZGYQP0qKx2c5clMWg4CO8Xsaz9Cp66XzjFqxC9Mhwe2HoAaXHE+meEtT+ljUjWTgKgmcImh4RvuxIPqTVznURfiQnQSSzz+v5QuoBU1fYoChTb806/xgGwMxJyzGHg5xMjXm5UXuY0Hx2ARnL7LFNlLCNCMnpiJOPVNVQ1tn7YZk0EDq92OIMv5c/eRbRbNnayqO5kR1UR1Lw1wVpsvg9QwfVn9NebA7gGbGvhrSH2cX0K4jD072a0NDgm+9jjxrKZEp0PNppRz/RjGVUE/MS2lrps2SJhKnl2RGUDYmJJiOkgH9JhXo2Gr+TbhA+S249F9BVZoqNIhIlJbl5YU0l/SJh6JLiuuoOIZIufmC6EBz5QKkgk9r5vZuJwklZ0e6paJQADLaoe7QTds6SogaZyCyqRGw7zQDUvpq5DdP7lWI94tB51f7+NjtkRooL0TyUrxS1ZYF0EaEEMO3DIs4EYQa78se2s/wc3gUFxcmRV+87hH3m4DHg9dxz5lX527bvJnsWFe+N5zU=","UTF-8");
			json = decryptEcb(key, "M6jY4tPTvR6s7/22w8n4lyiHK/RnOXwHSHJ8amyD6dQL55RVLz3juzOT5UjlG2XAb1CtR1yzQWN6OVaDbuRwu+32eJJcW/wB2EAkG4+n1bAz/qxq0ujCTkJ+8V0rUxjDmu8K+qszfO48MHsk73FZtioToN47f6NbGZQ2XWjWJocmj1Gd7fK94iliPgKCymsbV384O+gCSXl/YBGPs6gl5LmoRIUuBp5cwuwSc1DlTJX3+GgIKExuu6lQyP14unOue5Klzqm43Gh0JYmf1cR242e3fona2Ub8/jm0tmumHNaiPDgDd4eeWc08lcQuueQO8uCLMi7ZU4XLr9lXz86UYimOf6VXl+gp020dhknhhrfJwjFejerHaWQP2fLiJI0Z");

			System.out.println(json);
//			System.out.println(ByteUtils.toHexString(generateKey())
//					.toUpperCase());
//			System.out.println(ByteUtils.toHexString(generateKey(256))
//					.toUpperCase());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断字符串的编码
	 * @param str
	 * @return
	 */
	public static String getEncoding(String str){
		String encode = "GB2312";
		try{
			if(str.equals(new String(str.getBytes(encode),encode))){
				String s = encode;
				return s;
			}
		}catch(Exception exception){
		}
		encode = "ISO-8859-1";
		try{
			if(str.equals(new String(str.getBytes(encode),encode))){
				String s1 = encode;
				return s1;
			}
		}catch(Exception exception1){
		}
		encode = "UTF-8";
		try{
			if(str.equals(new String(str.getBytes(encode),encode))){
				String s2 = encode;
				return s2;
			}
		}catch(Exception exception2){
		}
		encode = "GBK";
		try{
			if(str.equals(new String(str.getBytes(encode),encode))){
				String s3 = encode;
				return s3;
			}
		}catch(Exception exception3){
		}
		return "";
	}

}
