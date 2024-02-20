package com.hororok.monta.jwt;

import com.hororok.monta.util.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class TokenProvider implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private static final String AUTHORITIES_KEY = "role";
    private final String secret;
    private final long tokenValidityInMilliseconds;
    private Key key;

    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds) {
        this.secret = secret;
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
    }

    // Override 하는 이유?
    // Bean(@Component)이 생성되고 의존성 주입(TokenProvider)까지 받은 후, 주입받은 secret 값을 base64에 맞춰 decode하고 key에 담기 위함.
    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // authentication 객체에 포함되어 있는 권한 정보들을 담은 토큰을 생성하는 createToken 메소드
    public String createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);

        // 로그에 권한 정보 출력
        logger.info("Token will be created with authorities: {}", authorities);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(validity)
                .compact();
    }

    // 토큰을 parameter로 받아서 토큰에 담긴 권한 정보들을 이용하여 Authentication 객체를 리턴하는 getAuthentication 메소드
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // JWT에서 email 추출
        String email = claims.get("email", String.class);

        String authoritiesString = claims.get(AUTHORITIES_KEY).toString();

        // 정규 표현식으로 name의 value를 추출합니다.
        Pattern pattern = Pattern.compile("name=([^,}]+)");
        Matcher matcher = pattern.matcher(authoritiesString);

        Collection<? extends GrantedAuthority> authorities = matcher.results()
                .map(matchResult -> new SimpleGrantedAuthority(matchResult.group(1))) // group(1)은 첫 번째 캡쳐 그룹, 즉 name의 value입니다.
                .collect(Collectors.toList());

        CustomUserDetails principal = new CustomUserDetails(claims.getSubject(), "", authorities, email);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }


    // 토큰을 arameter로 받아서 토큰의 유효성 검사를 할 수 있는 validateToken 메소드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("잘못된 JWT 서명입니다.", e);
        } catch (ExpiredJwtException e) {
            logger.info("만료된 JWT 토큰입니다.", e);
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 토큰입니다.", e);
        } catch (IllegalArgumentException e) {
            logger.info("JWT 토큰이 잘못되었습니다.", e);
        }
        return false;
    }
}
