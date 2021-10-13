package it.bx.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.micrometer.core.instrument.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.HashMap;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.*;
import static java.time.LocalDateTime.*;
import static java.time.ZoneId.*;
import static java.util.Date.from;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    @SneakyThrows
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        User u  = new Gson().fromJson(request.getReader() , User.class);
        var authenticationToken = new UsernamePasswordAuthenticationToken(u.getUsername(),u.getPassword());
         return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        var user = (User)authResult.getPrincipal();
        var algorithm = Algorithm.HMAC256("secret".getBytes(UTF_8));
        String access_token = JWT.create().
                withSubject(user.getUsername()).
                withExpiresAt(from(now().plusMinutes(10).atZone(systemDefault())
                        .toInstant()))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles" , user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        String refresh_token = JWT.create().
                withSubject(user.getUsername()).
                withExpiresAt(from(now().plusMinutes(30).atZone(systemDefault())
                        .toInstant()))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
        var bodyResponse = new HashMap<String , String>();
        bodyResponse.put("access_token" , access_token);
        bodyResponse.put("refresh_token",refresh_token);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), bodyResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        /*
            add 1 attempt
         */
        super.unsuccessfulAuthentication(request, response, failed);
    }
}
