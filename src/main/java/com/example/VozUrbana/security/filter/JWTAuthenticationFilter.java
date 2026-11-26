package com.example.VozUrbana.security.filter;

import java.io.IOException;

import com.example.VozUrbana.security.util.JWTUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JWTAuthenticationFilter(
            AuthenticationManager authenticationManager,
            JWTUtil jwtUtil) {

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        boolean requisicaoDeLogin =
                "POST".equalsIgnoreCase(request.getMethod())
                        && "/login".equals(request.getServletPath());

        if (!requisicaoDeLogin) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            JsonNode body = objectMapper.readTree(request.getInputStream());

            String username = body == null
                    ? ""
                    : body.path("username").asString("");

            String password = body == null
                    ? ""
                    : body.path("password").asString("");

            if (username.isBlank() || password.isBlank()) {
                escreverErro(
                        response,
                        HttpServletResponse.SC_UNAUTHORIZED,
                        "Username e password são obrigatórios"
                );
                return;
            }

            Authentication authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(
                            username,
                            password
                    )
            );

            String token = jwtUtil.generateToken(authentication.getName());

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Authorization", "Bearer " + token);

            response.getWriter().write(
                    "{\"token\":\"" + token + "\","
                            + "\"type\":\"Bearer\","
                            + "\"username\":\"" + escaparJson(authentication.getName()) + "\"}"
            );

        } catch (AuthenticationException exception) {
            escreverErro(
                    response,
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Usuário ou senha inválidos"
            );
        } catch (Exception exception) {
            escreverErro(
                    response,
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Requisição de login inválida"
            );
        }
    }

    private void escreverErro(
            HttpServletResponse response,
            int status,
            String message) throws IOException {

        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                "{\"status\":" + status + ","
                        + "\"message\":\"" + escaparJson(message) + "\"}"
        );
    }

    private String escaparJson(String valor) {
        return valor
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}
