package com.guantao;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import android.util.Log;

public class SecurityTools
{
	// 使用单向加密，代表了信息摘要MD5，安全散列SHA
	public static String EncryptMD5(String password)
	{
		if (password.equals(""))
		{
			return "";
		}
		try
		{
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] srcbytes = password.getBytes();
			md5.update(srcbytes);
			byte[] resultbytes = md5.digest();
			return byte2hex(resultbytes);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * @param b 要转换的 byte[]
	 * @return String 返回十六进制字符串
	 */
	private static String byte2hex(byte[] b)
	{
		StringBuilder sBuilder = new StringBuilder("");
		String stmp = "";
		for (int n = 0; n < b.length; n++)
		{
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			sBuilder.append(stmp.length() == 0 ? "0" + stmp : stmp);
		}
		return sBuilder.toString().toLowerCase();
	}

	/**
	 * bytes字符串转换为Byte值
	 * 
	 * @param String
	 *            src Byte字符串，每个Byte之间没有分隔符
	 * @return byte[]
	 */
	public static byte[] hexStr2Bytes(String src)
	{
		int m = 0, n = 0;
		int l = src.length() / 2;
		System.out.println(l);
		byte[] ret = new byte[l];
		for (int i = 0; i < l; i++)
		{
			m = i * 2 + 1;
			n = m + 1;
			ret[i] = Byte.decode("0x" + src.substring(i * 2, m)
					+ src.substring(m, n));
		}
		return ret;
	}

	/**
	 * 十六进制转换字符串
	 * 
	 * @param String
	 *            str Byte字符串(Byte之间无分隔符 如:[616C6B])
	 * @return String 对应的字符串
	 */
	public static String hexStr2Str(String hexStr)
	{
		String str = "0123456789ABCDEF";
		char[] hexs = hexStr.toCharArray();
		byte[] bytes = new byte[hexStr.length() / 2];
		int n;

		for (int i = 0; i < bytes.length; i++)
		{
			n = str.indexOf(hexs[2 * i]) * 16;
			n += str.indexOf(hexs[2 * i + 1]);
			bytes[i] = (byte) (n & 0xff);
		}
		return new String(bytes);
	}

	/**
	 * 字符串转换成十六进制字符串
	 * 
	 * @param String
	 *            str 待转换的ASCII字符串
	 * @return String 每个Byte之间空格分隔，如: [61 6C 6B]
	 */
	public static String str2HexStr(String str)
	{

		char[] chars = "0123456789ABCDEF".toCharArray();
		StringBuilder sb = new StringBuilder("");
		byte[] bs = str.getBytes();
		int bit;

		for (int i = 0; i < bs.length; i++)
		{
			bit = (bs[i] & 0x0f0) >> 4;
			sb.append(chars[bit]);
			bit = bs[i] & 0x0f;
			sb.append(chars[bit]);
			sb.append(' ');
		}
		return sb.toString().trim();
	}

	// 使用AES加密 代表了对称机密的 DES,3DES,AES
	public static String EncryptAES(String password)
	{
		byte[] result = null;
		try
		{
			KeyGenerator keygen = KeyGenerator.getInstance("AES");
			SecretKey secretKey = keygen.generateKey();
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);

			result = cipher.doFinal(password.getBytes());
		} catch (Exception e)
		{
			Log.e("Security", e.toString());
		}

		return byte2hex(result);
	}

	// 使用RSA加密,代表了非对称加密的RSA,DSA
	public static String EncryptRSA(String password)
	{
		byte[] result = null;
		try
		{
			KeyPairGenerator keypairGen = KeyPairGenerator.getInstance("RSA");
			keypairGen.initialize(1024);
			KeyPair keyPair = keypairGen.generateKeyPair();
			RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
			RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			result = cipher.doFinal(password.getBytes());

		} catch (NoSuchAlgorithmException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e)
		{
			// TODO: handle exception
		}
		return byte2hex(result);
	}
}
