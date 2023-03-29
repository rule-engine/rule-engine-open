/*
 * Copyright (c) 2020 dingqianwen (761945125@qq.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ruleengine.web.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @date 2019/8/13
 * @since 1.0.0
 */
@Component
public class JWTUtils {

    /**
     * token加密时使用的secretKey
     */
    private static String secretKey;
    /**
     * 代表token的有效时间
     */
    private static long keepTime;

    @Autowired
    public void setSecretKey(@Value("${auth.jwt.secretKey}") String secretKey) {
        JWTUtils.secretKey = secretKey;
    }

    @Autowired
    public void setKeepTime(@Value("${auth.jwt.keepTime}") Long keepTime) {
        JWTUtils.keepTime = keepTime;
    }

    /**
     * JWT由3个部分组成,分别是 头部Header,Payload负载一般是用户信息和声明,签证Signature一般是密钥和签名
     * 而payload又包含几个部分,issuer签发者,subject面向用户,iat签发时间,exp过期时间,aud接收方。
     *
     * @param id      用户id
     * @param issuer  签发者
     * @param subject 一般用户名
     * @return String
     */
    public static String genderToken(String id, String issuer, String subject) {
        //使用Hash256算法进行加密
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        //获取系统时间以便设置token有效时间
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        //将密钥转码为base64形式,再转为字节码
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        //对其使用Hash256进行加密
        JwtBuilder builder = Jwts.builder().setId(id).setIssuedAt(now);
        //JWT生成类,此时设置iat,以及根据传入的id设置token
        if (subject != null) {
            builder.setSubject(subject);
        }
        if (issuer != null) {
            builder.setIssuer(issuer);
        }
        //由于Payload是非必须加入的,所以这时候要加入检测
        builder.signWith(signatureAlgorithm, signingKey);
        //进行签名,生成Signature
        if (keepTime >= 0) {
            //当前的时间加上keepTime（1800000）后为过期的时间
            long expMillis = nowMillis + keepTime;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        //返回最终的token结果
        return builder.compact();
    }

    /**
     * 该函数用于更新token
     *
     * @param token token
     * @return String
     */
    public static String updateToken(String token) {
        //Claims就是包含了我们的Payload信息类
        Claims claims = verifyToken(token);
        String id = claims.getId();
        String subject = claims.getSubject();
        String issuer = claims.getIssuer();
        //生成新的token,根据现在的时间
        return genderToken(id, issuer, subject);
    }

    /**
     * 将token解密出来,将payload信息包装成Claims类返回
     * 可能会解密失败，这种情况为非法访问
     *
     * @param token token
     * @return Claims
     */
    public static Claims verifyToken(String token) {
        return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                .parseClaimsJws(token).getBody();
    }

}
