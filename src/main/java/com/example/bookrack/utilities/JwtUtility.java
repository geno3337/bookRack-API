package com.example.bookrack.utilities;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JwtUtility {

    @Value("${authtoken.issuer}")
    private String claimsIssuer;

    @Value("${authtoken.signing.key}")
    private String signingKey;

    @Value("${authtoken.tokenvalidity.seconds}")
    private int tokenValidity;

    @Value("${authtoken.tokenname}")
    private String tokenName;

    @Value("${authtoken.prefix}")
    private String tokenPrefix;

    public String getJWTSubject(String jwt){
        Jws<Claims> jwsClaims = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(jwt);
        return jwsClaims.getBody().getSubject();
    }

    public String generateJWT(String accessTokenIdentifier) {
        Claims claims = Jwts.claims().setSubject(accessTokenIdentifier);
        return Jwts.builder().setClaims(claims).setIssuer(claimsIssuer)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidity * 1000))
                .signWith(SignatureAlgorithm.HS256, signingKey).compact();
    }

    public String getTokenName() {
        return tokenName;
    }

    public String getTokenPrefix(){
        return tokenPrefix;
    }


    private Date getExpirationDateFromToken(String jwt) {
        Date expirationDate = null;
        Jws<Claims> jwsClaims = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(jwt);
        expirationDate = jwsClaims.getBody().getExpiration();
        return expirationDate;
    }

    public Boolean isTokenExpired(String jwt)  {
        Date expiration = getExpirationDateFromToken(jwt);
        return expiration.before(new Date());
    }


//    public Boolean validateToken(String token, UserDetails userDetails) {
//        final String username = extractUsername(token);
//        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }

}
