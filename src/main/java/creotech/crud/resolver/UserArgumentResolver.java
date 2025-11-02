package creotech.crud.resolver;

import creotech.crud.entity.User;
import creotech.crud.repository.AuthRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String TOKEN_HEADER = "X-API-TOKEN";

    private final AuthRepository authRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // injeksi hanya jika parameternya bertipe User
        return User.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest req = (HttpServletRequest) webRequest.getNativeRequest();
        String token = req.getHeader(TOKEN_HEADER);

        if (token == null || token.isBlank()) {
            throw unauthorized("Missing token");
        }

        User user = authRepository.findFirstByToken(token)
                .orElseThrow(() -> unauthorized("Invalid token"));

        if (isExpired(user.getTokenExpiredAt())) {
            throw unauthorized("Token expired");
        }

        if (log.isDebugEnabled()) {
            log.debug("Authenticated user uuid={} email={}", user.getUuid(), user.getEmail());
        }

        return user;
    }

    private boolean isExpired(Long epochMillis) {
        return epochMillis == null || epochMillis < System.currentTimeMillis();
    }

    private ResponseStatusException unauthorized(String msg) {
        // sengaja pesan singkat; detail jangan terlalu bocor
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized: " + msg);
    }
}
