package com.example.server.config.jwt;

import com.auth0.jwt.JWT;
import com.example.server.config.auth.PrincipalDetails;
import com.example.server.constants.StatusCode;
import com.example.server.domain.tokenRepository.TokenRepository;
import com.example.server.domain.userRepository.User;
import com.example.server.domain.userRepository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private UserRepository userRepository;
    private TokenRepository tokenRepository;

    private JwtTokenProvider jwtTokenProvider;
    private ObjectMapper om = new ObjectMapper();
    private StatusCode statusCode = new StatusCode();
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, TokenRepository tokenRepository, JwtTokenProvider jwtTokenProvider) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwtHeader = jwtTokenProvider.resolveJwtToken(request);

        if (jwtHeader == null || !jwtHeader.startsWith(JwtProperties.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        String accessToken = jwtHeader.replace(JwtProperties.TOKEN_PREFIX, "");

        if (jwtTokenProvider.accessTokenValid(accessToken)) { // AccessToken ?????????(????????????) ??????
            String username = jwtTokenProvider.getVerifyToken(accessToken).getClaim("username").asString();

            if (username != null && !username.equals("")) {
                User user = userRepository.findByUsername(username);
                PrincipalDetails principalDetails = new PrincipalDetails(user);

                Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails.getUsername(), null, principalDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                chain.doFilter(request, response);
            }else{
                System.out.println("[ERR] ACCESS TOKEN ????????? ?????? ??????");
                statusCode.setResCode(1); statusCode.setResMsg("[ERR] ACCESS TOKEN ????????? ?????? ??????");
                String result = om.writeValueAsString(statusCode);
                response.getWriter().write(result);
                // : /login ?????? ??????????????? ?
                return;
            }
        }
        else{
            // Access Token ????????? ??????
            System.out.println("[WARN] Expired Access Token");

            // ?????????????????? Refresh Token??? ?????? ????????? ??????
            if(request.getHeader(JwtProperties.REFRESH_HEADER_STRING) != null){

                // Refresh ?????????(????????????) ??????
                String refresh = request.getHeader(JwtProperties.REFRESH_HEADER_STRING);
                System.out.println("refresh = " + refresh);
                String username = JWT.decode(accessToken).getClaim("username").asString();
                // DB??? Refresh??? ????????????????????? ?????? Refresh ??????
                if(refresh.equals(tokenRepository.findByUsername(username).getToken())){
                    System.out.println("[SUCCESS] ???????????? Refresh Token");

                    if(jwtTokenProvider.refreshTokenValid(refresh)){ // refresh token ?????? ?????? ??????
                        String reissueAccessToken = jwtTokenProvider.creatAccessToken(username);
                        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + reissueAccessToken);
                    }else{
                        System.out.println("[WARN] Refresh Token ?????????, ???????????? ??????");
                        tokenRepository.deleteById(username);
                        statusCode.setResCode(2); statusCode.setResMsg("????????? Refresh Token");
                        String result = om.writeValueAsString(statusCode);
                        response.getWriter().write(result);
                        return;
                    }
                }else{
                    System.out.println("[ERR] ??????????????? Refresh Token");
                    tokenRepository.deleteById(username); // DB??? ???????????? refresh token ??????
                    statusCode.setResCode(2); statusCode.setResMsg("??????????????? Refresh Token");
                    String result = om.writeValueAsString(statusCode);
                    response.getWriter().write(result);
                    return;
                }

            }else{
                statusCode.setResCode(1); statusCode.setResMsg("Access Token ?????????");
                String result = om.writeValueAsString(statusCode);
                response.getWriter().write(result);
                return;
            }

        }


    }
}
