package it.bx.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.bx.bean.FormRole;
import it.bx.bean.FormUser;
import it.bx.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.list.ImmutableList;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.HashMap;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.LocalDateTime.now;
import static java.time.ZoneId.systemDefault;
import static java.util.Date.from;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/api")
@RequiredArgsConstructor
@Slf4j
public class FormResource {

    private final UserService userService;

    public static final String BEARER = "Bearer ";

    @GetMapping(path = "/users")
    public ImmutableList<FormUser> getUsers(){
        return userService.getUsers();
    }

    @GetMapping(path = "/user")
    public String getUser(Principal principal) {
        return principal.getName();
    }

    @PostMapping(path = "/user/save")
    public FormUser saveUser(@RequestBody FormUser formUser){
        return userService.saveUser(formUser);
    }

    @PostMapping(path = "/role/save")
    public FormRole saveRole(@RequestBody FormRole formRole){
        return userService.saveRole(formRole);
    }

    @SneakyThrows
    @PostMapping(path = "/refreshToken")
    public void refreshToken(HttpServletRequest request , HttpServletResponse response){
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){

            try {
                String refresh_token = authorizationHeader.split(BEARER)[1];
                var algorithm = Algorithm.HMAC256("secret".getBytes(UTF_8));
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String userName = decodedJWT.getSubject();
                var user = userService.getFormUser(userName);

                String access_token = JWT.create().
                        withSubject(user.getUsername()).
                        withExpiresAt(from(now().plusMinutes(10).atZone(systemDefault())
                                .toInstant()))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles" , user.getRoles().stream().map(FormRole::getName).collect(Collectors.toList()))
                        .sign(algorithm);

                refresh_token = JWT.create().
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

            }catch (Exception e ){
                log.error("Authentication Exception: {}" , e.getMessage());
                response.setHeader("error" , e.getMessage());
                response.setStatus(FORBIDDEN.value());
                var errorResponse = new HashMap<String , String>();
                errorResponse.put("error_message",e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), errorResponse);
            }

        }else {
            log.error("Authentication Exception: {}" , "Missing Token");
            throw new RuntimeException("Refresh Token is Missing");
        }
    }

}
