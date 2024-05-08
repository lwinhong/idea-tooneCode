package com.tooneCode.core.api;

//import java.util.HashMap;
//import java.util.Map;
//
//import com.alibaba.fastjson2.JSON;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//
//import java.nio.charset.StandardCharsets;

public class ApiTokenUtil {
//    public static String generateClientToken(String apikey) {
//        String[] apiKeyParts = apikey.split("\\.");
//        String api_key = apiKeyParts[0];
//        String secret = apiKeyParts[1];
//
//        Map<String, Object> header = new HashMap<>();
//        header.put("alg", Jwts.SIG.HS256);
//        header.put("sign_type", "SIGN");
//        Map<String, Object> payload = new HashMap<>();
//        payload.put("api_key", api_key);
//        payload.put("exp", System.currentTimeMillis() + 5 * 600 * 1000);
//        payload.put("timestamp", System.currentTimeMillis());
//        String token = null;
//        try {
//            token = Jwts.builder().setHeader(header)
//                    .setPayload(JSON.toJSONString(payload))
//                    .signWith(SignatureAlgorithm.HS256, secret.getBytes(StandardCharsets.UTF_8))
//                    .compact();
//        } catch (Exception e) {
//            System.out.println();
//        }
//
//        return token;
//    }

}
