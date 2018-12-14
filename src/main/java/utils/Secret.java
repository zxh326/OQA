package utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Random;

public class Secret {

    public static final String KEY_SHA = "SHA";

    public static String enPassword(String password){
        BigInteger sha =null;
        byte[] inputData = password.getBytes();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(KEY_SHA);
            messageDigest.update(inputData);
            sha = new BigInteger(messageDigest.digest());
        } catch (Exception e) {e.printStackTrace();}
        assert sha != null;
        return sha.toString(32);
    }


    public static boolean checkPassword(String password, String sha){

        String tmp = enPassword(password);

        return sha.contentEquals(tmp);
    }

    static String getRandomString(Integer num) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < num; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }


    public static String getRandomString() {
        return getRandomString(10);
    }
}
