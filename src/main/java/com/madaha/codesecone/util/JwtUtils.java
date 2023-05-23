package com.madaha.codesecone.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Slf4j
public class JwtUtils {

    private static final long EXPIRE = 1400 * 60 * 1000;
    private static final String SECRET = "123456";
    private static final String B64_SECRET = Base64.getEncoder().encodeToString(SECRET.getBytes(StandardCharsets.UTF_8));

    /**
     * Generate JWT Token by jjwt
     *
     * @param userId
     * @return
     */
    public static String generateTokenByJjwt(String userId){
        return Jwts.builder()
                .setHeaderParam("type", "JWT")      // set Header
                .setHeaderParam("alg", "HS256")     // set Header
                .setIssuedAt(new Date())                  // set Issue，设置发布时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))       // Expiration 设置过期时间
                .claim("username", userId)      // 设置载荷信息，且可以设置多个；（claims 荷载信息，用于设置payload载荷的。）
                // secret在singWith会base64解码，但网上很多代码示例并没有对secret做base64编码，所以在爆破key的时候 可以注意下。   (使用HS256算法 并使用盐进行加密）
                .signWith(SignatureAlgorithm.HS256, B64_SECRET)
                .compact();
    }

    public static String getUserIdFromJjwtToken(String token){
        try {
            Claims claims = Jwts.parser().setSigningKey(B64_SECRET).parseClaimsJws(token).getBody();
            return (String) claims.get("username");
        } catch (Exception e) {
            return e.toString();
        }
    }

    /**
     * Generate jwt token by java-jwt
     *
     * @param nickname  (nickname 昵称)
     * @return jwt token
     */
    public static String generateTokenByJavaJwt(String nickname){
        return JWT.create()
                .withClaim("nickname", nickname)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRE))
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC256(SECRET));
    }

    /**
     * Verify JWT Token
     *
     * @param token token
     * @return Valid token returns true. Invalid token returns false.
     */
    public static Boolean verifyTokenByJavaJwt(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);        // algorithm eg：算法
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            log.error(exception.toString());
            return false;
        }
    }

    public static String getNicknameByJavaJwt(String token){
        // If the signature is not verified, there will be security issues.(如果未验证签名，则会出现安全问题。)
        if(!verifyTokenByJavaJwt(token)){
            log.error("token is invalid");
            return null;
        }
        return JWT.decode(token).getClaim("nickname").asString();
    }


    public static void main(String[] args) {
        String jjwtToken = generateTokenByJjwt("admin");
        System.out.println(jjwtToken);
        System.out.println(getUserIdFromJjwtToken(jjwtToken));

        // 使用JoyChou昵称，是因为这个代码是来自JoyChou师傅的。
        String token = generateTokenByJavaJwt("JoyChou");
        System.out.println(token);
        System.out.println(getNicknameByJavaJwt(token));
    }

}
