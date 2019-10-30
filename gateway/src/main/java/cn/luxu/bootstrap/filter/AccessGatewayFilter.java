//package cn.luxu.bootstrap.filter;
//
//import cn.luxu.bootstrap.client.AuthRemote;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
///**
// * @description: 请求url权限校验
// * @author: luxu
// * @date: 2019-10-21 14:11
// **/
//
//@Slf4j
//@Component
//public class AccessGatewayFilter implements GlobalFilter {
//
//    @Autowired
//    private AuthRemote authRemote;
//
//    private static final  String X_CLIENT_TOKEN_USER = "x-client-token-user";
//    private static final  String X_CLIENT_TOKEN = "x-client-token";
//    private static final String BEARER = "Bearer";
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        ServerHttpRequest request = exchange.getRequest();
//        String authentication = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//        String method = request.getMethodValue();
//        String url = request.getPath().value();
//        log.debug("url:{},method:{},headers:{}", url, method, request.getHeaders());
//        //不需要网关签权的url
//        if (authRemote.ignoreAuthentication(url)) {
//            return chain.filter(exchange);
//        }
//        // 如果请求未携带token信息, 直接跳出
//        if (StringUtils.isBlank(authentication) || !authentication.startsWith(BEARER)) {
//            log.debug("url:{},method:{},headers:{}, 请求未携带token信息", url, method, request.getHeaders());
//            return unauthorized(exchange);
//        }
//        //调用签权服务看用户是否有权限，若有权限进入下一个filter
//        if (authRemote.hasPermission(authentication, url, method)) {
//            ServerHttpRequest.Builder builder = request.mutate();
//            return chain.filter(exchange.mutate().request(builder.build()).build());
//        }
//        return unauthorized(exchange);
//    }
//
//    /**
//     * 网关拒绝，返回401
//     *
//     * @param
//     */
//    private Mono<Void> unauthorized(ServerWebExchange serverWebExchange) {
//        serverWebExchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//        DataBuffer buffer = serverWebExchange.getResponse()
//                .bufferFactory().wrap(HttpStatus.UNAUTHORIZED.getReasonPhrase().getBytes());
//        return serverWebExchange.getResponse().writeWith(Flux.just(buffer));
//    }
//}
