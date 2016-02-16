package au.com.lumo.ameego.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Karl on 10/09/2015.
 */
public class AESEncryption {

    private final static String key = "kaus@LA;AS(7qNsi";
    private final static String ivs = "";


    public static byte[] encrypt(byte[] data) {
        byte[] keyByte = key.getBytes();
        byte[] ivsByte = ivs.getBytes();
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyByte, "AES");
            byte[] finalIvs = new byte[16];
            int len = ivsByte.length > 16 ? 16 : ivsByte.length;
            System.arraycopy(ivsByte, 0, finalIvs, 0, len);
            IvParameterSpec ivps = new IvParameterSpec(finalIvs);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivps);
            return cipher.doFinal(data);
        } catch (Exception e) {
        }

        return null;
    }

    public static byte[] decrypt(byte[] data) {
        byte[] keyByte = key.getBytes();
        byte[] ivsByte = ivs.getBytes();
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyByte, "AES");
            byte[] finalIvs = new byte[16];
            int len = ivsByte.length > 16 ? 16 : ivsByte.length;
            System.arraycopy(ivsByte, 0, finalIvs, 0, len);
            IvParameterSpec ivps = new IvParameterSpec(finalIvs);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivps);
            return cipher.doFinal(data);
        } catch (Exception e) {
        }

        return null;
    }
}