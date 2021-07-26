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
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class JwtTokenService {

    private final Logger logger = LoggerFactory.getLogger(JwtTokenService.class);

    private Set<String> invalidTokens = new HashSet<>();

    private String jwtSecret = "secretKey";

    public String createJwtToken(UserEntity user) {
        LocalDateTime dateTime = LocalDateTime.now();
        return Jwts.builder()
                .setSubject((user.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration( new Date(dateTime.getYear() + 1, dateTime.getMonthValue(), dateTime.getDayOfMonth()))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }


    public void authenticate(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        String authToken = getJwtFromRequest(request);
        if (authToken == null || invalidTokens.contains(authToken)) {
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

    public void addToInvalidTokens(HttpServletRequest req) {
        invalidTokens.add(getJwtFromRequest(req));
        if (invalidTokens.size() == 100) {
            invalidTokens.clear();
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
