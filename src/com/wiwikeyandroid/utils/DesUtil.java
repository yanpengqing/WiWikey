package com.wiwikeyandroid.utils;

import java.io.IOException;  
import java.security.SecureRandom;  
  
import javax.crypto.Cipher;  
import javax.crypto.SecretKey;  
import javax.crypto.SecretKeyFactory;  
import javax.crypto.spec.DESKeySpec;  
  
import Decoder.BASE64Decoder;  
import Decoder.BASE64Encoder; 

/**
 * 数据加解密
 * @author chenwen
 *
 */
public class DesUtil {
	private final static String DES = "DES";
	   
	public static final String DESKEY = "Up2016!@wiwikey.com";
	
    public static void main(String[] args) throws Exception {  
        String data = "{\"errno\":\"103\",\"errmsg\":\"验证码错误\"}";
        data = new String(data.getBytes(),"UTF-8");
        String key = "Up2016!@wiwikey.com";  
        System.err.println(encrypt(data, key));  
        System.err.println(decrypt(encrypt(data, key), key));
          
        System.out.println(decrypt("v9ddpSbM05kmpZXxLMIkDUuw7S+T7BJEw5sA/Cb8VkxZL+NyO6849ZLaytq6pQxXw7tyA7qGwYYyJr0zdcWPsXCH6vRFrAqF3ULupX/FSMCxalzCHwGRYkzPeDBOyTFnXTYOgfL3vVCramSpb6dkfJW60KApd0D1MNSX+piZ9AYhgSurW7+74RDW2Xt6CmeQwx7Omyref9ia1m+SNxPa1Tv6zcd7n2D9YiO6ORIrKLmvmz7bDCocri9NSAvwtwPUay4hcqqBbyJUbDZeaulrjyGdX7Syscu+KqH0mGtb9sd25qLVL30Rqo0d5Id5LCIiu5H1rpWyh4+06jyPEtp4PE+Vgx0TE5h2", key));  
   
    }  
       
    /** 
     * Description 根据键值进行加密 
     * @param data  
     * @param key  加密键byte数组 
     * @return 
     * @throws Exception 
     */  
    public static String encrypt(String data, String key) throws Exception {  
        byte[] bt = encrypt(data.getBytes("UTF-8"), key.getBytes("UTF-8"));  
        String strs = new BASE64Encoder().encode(bt);  
        return strs;  
    }  
   
    /** 
     * Description 根据键值进行解密 
     * @param data 
     * @param key  加密键byte数组 
     * @return 
     * @throws IOException 
     * @throws Exception 
     */  
    public static String decrypt(String data, String key) throws IOException,  
            Exception {  
        if (data == null)  
            return null;  
        BASE64Decoder decoder = new BASE64Decoder();  
        byte[] buf = decoder.decodeBuffer(data);  
        byte[] bt = decrypt(buf,key.getBytes("UTF-8"));  
        return new String(bt);  
    }  
   
    /** 
     * Description 根据键值进行加密 
     * @param data 
     * @param key  加密键byte数组 
     * @return 
     * @throws Exception 
     */  
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {  
        // 生成一个可信任的随机数源  
        SecureRandom sr = new SecureRandom();  
   
        // 从原始密钥数据创建DESKeySpec对象  
        DESKeySpec dks = new DESKeySpec(key);  
   
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);  
        SecretKey securekey = keyFactory.generateSecret(dks);  
   
        // Cipher对象实际完成加密操作  
        Cipher cipher = Cipher.getInstance(DES);  
   
        // 用密钥初始化Cipher对象  
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);  
   
        return cipher.doFinal(data);  
    }  
       
       
    /** 
     * Description 根据键值进行解密 
     * @param data 
     * @param key  加密键byte数组 
     * @return 
     * @throws Exception 
     */  
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {  
        // 生成一个可信任的随机数源  
        SecureRandom sr = new SecureRandom();  
   
        // 从原始密钥数据创建DESKeySpec对象  
        DESKeySpec dks = new DESKeySpec(key);  
   
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);  
        SecretKey securekey = keyFactory.generateSecret(dks);  
   
        // Cipher对象实际完成解密操作  
        Cipher cipher = Cipher.getInstance(DES);  
   
        // 用密钥初始化Cipher对象  
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);  
   
        return cipher.doFinal(data);  
    } 
}
