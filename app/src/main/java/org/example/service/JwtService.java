package org.example.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    public static final String SECRET = "db5c87233bc64f0eeb4ab8092badf4f775e00b98d8f44cdb751fe39d80847f4ce2b59a9574bac3dd7bd6f1bcc01e47a5ed1019df41920260d187d529977bdc3769ccece9969c6f41a0788272021313e8174c8b6a278288a9446f41342b9a820d8830a6577a69d979c7442e7098c60525b8443235cb1f987ce8153776115e5ce18cd63032161309b92b20cd8cd6b8c7616f29da7a3925745fc403cd8637018172c1a50349f39216d8d4a4659c8ca4921547b0494fa84a6d703353846a4644e9790d3d2c852cb04c2d5da3609837a58d09c80f665e6378d0861104ef8869b852fd27b429cb30acf93b7d3d1d9fa16998bacd575407245e0d16b3dd5f3b9afcf2bf";


    //claims : passing empty claims
    public String createToken(String username){
        return Jwts.builder()
                //.setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*1))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public Date extractExpiration(String token){
        return extractClaims(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }



}
