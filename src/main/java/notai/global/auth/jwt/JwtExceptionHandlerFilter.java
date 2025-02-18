package notai.global.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import notai.global.exception.CustomException;
import notai.global.exception.errorCode.ErrorCode;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (CustomException e) {
            ObjectMapper objectMapper = new ObjectMapper();

            ErrorCode errorCode = e.getErrorCode();

            response.setStatus(errorCode.getHttpStatus().value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(e.toResponse()));
        }
    }
}
