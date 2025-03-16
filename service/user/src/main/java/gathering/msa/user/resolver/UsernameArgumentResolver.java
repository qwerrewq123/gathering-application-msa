package gathering.msa.user.resolver;

import exception.NotValidHeaderException;
import gathering.msa.user.annotation.Username;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class UsernameArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasUsernameAnnotation = parameter.hasParameterAnnotation(Username.class);
        boolean hasStringType = String.class.isAssignableFrom(parameter.getParameterType());
        return hasUsernameAnnotation && hasStringType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String username = request.getHeader("x-username");
        if(username == null){
            throw new NotValidHeaderException("Not valid x-username Header!!");
        }
        return username;

    }


}
