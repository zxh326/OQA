package common;

import java.math.BigInteger;
import java.security.MessageDigest;

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
}
