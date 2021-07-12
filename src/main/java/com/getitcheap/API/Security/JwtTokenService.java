package com.getitcheap.API.Security;

import com.getitcheap.API.Users.UserEntity;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class JwtTokenService {

    private final Logger logger = LoggerFactory.getLogger(JwtTokenService.class);

    private String jwtSecret = "secretKey";

    private int jwtExpirationMs = 86400000; // 24 Hrs

    public String createJwtToken(UserEntity user) {

        return Jwts.builder()
                .setSubject((user.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }


    public void authenticate(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        String authToken = getJwtFromRequest(request);
        if (authToken == null) {
            return;
        }

        try {
            Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken).getBody();
            String username = claims.getSubject();
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        }
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }
        return null;
    }

}
