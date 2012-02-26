package com.guantao;

import java.security.MessageDigest;

public class SecurityTools
{
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
	
	private  static String byte2hex(byte[] b)
	{
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++)
		{
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
			{
				hs = hs + "0" + stmp;
			} else
			{
				hs = hs + stmp;
			}
		}
		return hs.toLowerCase();
	}	
	
}
