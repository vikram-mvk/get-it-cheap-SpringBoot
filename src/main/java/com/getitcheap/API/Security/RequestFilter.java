package com.getitcheap.API.Security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RequestFilter extends OncePerRequestFilter {

    private final String[] POSSIBLE_IP_HEADERS = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    private Map<String, Integer> ipFrequency = new HashMap<>();

    private Set<String> blockedIps = new HashSet<>();

    private LocalDateTime dateTime = LocalDateTime.now();
    private LocalDateTime dateTimeAfterAMinute = dateTime.plusMinutes(1);

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private JwtTokenService jwtTokenService;

    public RequestFilter(JwtTokenService jwtTokenService) {
        super();
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        checkTooManyRequests(request);
        if(blockedIps.contains(getClientIpAddress(request))) {
            jwtTokenService.addToInvalidTokens(request);
            response.sendError(429);
        }
        jwtTokenService.authenticate(request);
        filterChain.doFilter(request, response);
    }

    private void checkTooManyRequests(HttpServletRequest req) {
         String ip = getClientIpAddress(req);
         if (ipFrequency.containsKey(ip)) {
             ipFrequency.put(ip, ipFrequency.get(ip) + 1);
             if (ipFrequency.get(ip) == 30) {
                    blockedIps.add(ip);
                    System.out.println("Malicious Ip" + ip + " found at "+ LocalDateTime.now().toString());
                 return;
             }
         } else {
             ipFrequency.put(ip, 1);
         }

         if (LocalDateTime.now().isAfter(dateTimeAfterAMinute)) {
             ipFrequency.clear();
             dateTime = LocalDateTime.now();
             dateTimeAfterAMinute = dateTime.plusMinutes(1);
             blockedIps.clear();
         }

    }

    private String getClientIpAddress(HttpServletRequest request) {
        for (String header : POSSIBLE_IP_HEADERS) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }
}
