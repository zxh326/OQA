package utils;

import model.vo.Token;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class TokenManager {
    /*
     * TokenManager
     * TODO:redis save
     */
    private static Map<String, Token> tokenMap = new HashMap<>();
    private static Map<Integer, Token> idMap = new HashMap<>();

    public static Integer getUserId(String token) {

        Token userToken = tokenMap.get(token);
        if (userToken == null) {
            return null;
        }

        if (userToken.getExpireTime().isBefore(LocalDateTime.now())) {
            tokenMap.remove(token);
            idMap.remove(userToken.getUserId());
            return null;
        }

        return userToken.getUserId();
    }

    public static Token generateToken(Integer id) {
        Token userToken = null;

        String token = CharUtil.getRandomString(32);
        while (tokenMap.containsKey(token)) {
            token = CharUtil.getRandomString(32);
        }

        LocalDateTime update = LocalDateTime.now();
        LocalDateTime expire = update.plusDays(1);

        userToken = new Token();
        userToken.setToken(token);
        userToken.setUpdateTime(update);
        userToken.setExpireTime(expire);
        userToken.setUserId(id);
        tokenMap.put(token, userToken);
        idMap.put(id, userToken);

        return userToken;
    }


}
