package br.com.api.infrastructure.filters;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import br.com.api.authorization.userdetails.UserServiceImpl;
import br.com.api.infrastructure.services.JwtService;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtService _jwtService;
    private UserServiceImpl _userService;


    public JwtAuthenticationFilter(JwtService jwtService, UserServiceImpl userService) {
        _jwtService = jwtService;
        _userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        if (authorization != null && authorization.startsWith("Bearer")) {
            String token = authorization.split(" ")[1];
            boolean isValid = _jwtService.validateToken(token);

            if (isValid) {
                String loginUserAccount = _jwtService.getSubject(token);
                UserDetails userLogged = _userService.loadUserByUsername(loginUserAccount);

                UsernamePasswordAuthenticationToken userToken =
                        new UsernamePasswordAuthenticationToken(userLogged, null,
                                userLogged.getAuthorities());

                userToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(userToken);
            }
        }

        filterChain.doFilter(request, response);
    }

}
