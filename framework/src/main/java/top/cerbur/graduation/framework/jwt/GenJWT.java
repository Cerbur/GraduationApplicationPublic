package top.cerbur.graduation.framework.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.NonNull;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class GenJWT {
    private static final String ID_NAME = "id";
    private static final String SALT = "salt";
    private static final Integer EXPIRE = 6000;

    public static String encode(Integer userId) throws UnsupportedEncodingException {
        //当前时间
        Date nowDate = new Date();
        //过期时间
        Date expireDate = new Date(nowDate.getTime() + EXPIRE * 1000);
        //创建密钥
        Algorithm algorithm = Algorithm.HMAC256(SALT);

        //创建jwt
        var jwt = JWT.create();
        //设置过期时间和开始时间
        jwt.withIssuedAt(nowDate).withExpiresAt(expireDate);
        //填入payload参数
        jwt.withClaim(ID_NAME,userId);
        return jwt.sign(algorithm);
    }


    public static Integer decode(@NonNull String jwt) throws UnsupportedEncodingException {
        Boolean aBoolean = checkJwt(jwt);
        if (aBoolean) {
            return JWT.decode(jwt).getClaim(ID_NAME).asInt();
        } else {
            throw new UnsupportedEncodingException();
        }
    }

    private static Boolean checkJwt(@NonNull String jwt) throws UnsupportedEncodingException {
        Algorithm algorithm = Algorithm.HMAC256(SALT);
        var verifier = JWT.require(algorithm).build();
        try {
            verifier.verify(jwt);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
