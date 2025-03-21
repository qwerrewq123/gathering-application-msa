package gathering.msa.api_gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.response.Response;
import gathering.msa.api_gateway.entity.User;
import gathering.msa.api_gateway.repository.JwtRepository;
import gathering.msa.api_gateway.repository.UserRepository;
import gathering.msa.api_gateway.validator.JwtValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

import static util.ConstClass.*;


@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    private final JwtValidator jwtValidator;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final JwtRepository jwtRepository;

    public AuthorizationHeaderFilter(@Value("${jwt.secretKey}") String secretKey,
                                     JwtValidator jwtValidator,UserRepository userRepository,JwtRepository jwtRepository) {
        super(Config.class);
        this.jwtValidator = jwtValidator;
        this.objectMapper = new ObjectMapper();
        this.userRepository = userRepository;
        this.jwtRepository = jwtRepository;
    }

    public static class Config {
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.replace("Bearer ", "");
            String findJwt = jwtRepository.read(jwt);
            if(findJwt!=null){
                //TODO : 로직처리
                return chain.filter(exchange);
            }
            if (!isJwtValid(jwt)) {
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }
            //TODO : clientId 확인
            jwtRepository.createOrUpdate(1L,jwt);
            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus){
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        Response errorResponse = new Response(AUTHORIZATION_FAIL_CODE, AUTHORIZATION_FAIL_MESSAGE);
        try {
            String errorResponseJson = objectMapper.writeValueAsString(errorResponse);
            byte[] bytes = errorResponseJson.getBytes(StandardCharsets.UTF_8);
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
            return response.writeWith(Flux.just(buffer));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isJwtValid(String token) {

        String username = jwtValidator.validateToken(token);
        User findUser = userRepository.findByUsername(username);
        return findUser != null;
    }


}
