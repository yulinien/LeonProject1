package com.example.leonproject.config.security;

import com.example.leonproject.dao.entity.AccountDO;
import com.example.leonproject.dao.repository.AccountRepository;
import com.example.leonproject.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtil tokenGenerator;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return !"/LeonAPI/dev/changePassword".equals(path);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = getJWTFromRequest(request);

        String username = tokenGenerator.getUsernameFromJWT(token);

        Optional<AccountDO> accountDO = accountRepository.findAccountByUsername(username);

        if (StringUtils.hasText(token) && tokenGenerator.validateToken(token)) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(accountDO, null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        filterChain.doFilter(request, response);
    }

    private String getJWTFromRequest(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
