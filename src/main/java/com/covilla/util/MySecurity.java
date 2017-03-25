package com.covilla.util;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Description:加密算法
 * @author xuys
 * @date 2013年8月7日 下午3:28:56
 */
public class MySecurity {
	public static final String SHA_1 = "SHA-1";
	public static final String MD5 = "MD5";
	public static final String MAC_NAME = "HmacSHA1";
	private static final String ENCODING = "UTF-8";

	public String encode(String strSrc, String encodeType) {
		MessageDigest md = null;
		String strDes = null;
		byte[] bt = strSrc.getBytes();
		try {
			if ((encodeType == null) || ("".equals(encodeType)))
				encodeType = "MD5";
			md = MessageDigest.getInstance(encodeType);
			md.update(bt);
			strDes = bytes2Hex(md.digest());
		} catch (NoSuchAlgorithmException e) {
			return strSrc;
		}
		return strDes;
	}

	public String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; ++i) {
			tmp = Integer.toHexString(bts[i] & 0xFF);
			if (tmp.length() == 1)
				des = des + "0";

			des = des + tmp;
		}
		return des;
	}

	/**
	 * 使用 HMAC-SHA1 签名方法对对encryptText进行签名
	 * @param encryptText 被签名的字符串
	 * @param encryptKey  密钥
	 * @return
	 * @throws Exception
	 */
	public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception
	{
		byte[] data=encryptKey.getBytes(ENCODING);
		//根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
		SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
		//生成一个指定 Mac 算法 的 Mac 对象
		Mac mac = Mac.getInstance(MAC_NAME);
		//用给定密钥初始化 Mac 对象
		mac.init(secretKey);

		byte[] text = encryptText.getBytes(ENCODING);
		//完成 Mac 操作
		return mac.doFinal(text);
	}

	public static byte[] AES256Encrypt(String str, String algorithm, byte[] keySpec, byte[] iv) {
		try {
			return AES256Encrypt(str.getBytes("UTF-8"), algorithm, keySpec, iv);
		}catch (Exception e){
			System.out.println(e.toString());
			return null;
		}
	}

	public static byte[] AES256Encrypt(byte[] bytes, String algorithm, byte[] keySpec, byte[] iv){
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keySpec, algorithm), new IvParameterSpec(iv));
			byte[] cipherData = cipher.doFinal(bytes);
			return cipherData;
		}catch (Exception e){
			System.out.println(e.toString());
			return null;
		}
	}

	public static void main(String[] args) {
		MySecurity te = new MySecurity();
		String strSrc = "可以加密汉字";
		System.out.println("Source String:" + strSrc);
		System.out.println("Encrypted String:");
		System.out.println("Use MD5:" + te.encode(strSrc, null));
		System.out.println("Use MD5:" + te.encode(strSrc, "MD5"));
		System.out.println("Use SHA:" + te.encode(strSrc, "SHA-1"));
		System.out.println("Use SHA-256:" + te.encode(strSrc, "SHA-256"));
	}
}
