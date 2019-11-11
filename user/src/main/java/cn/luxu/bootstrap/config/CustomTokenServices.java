package cn.luxu.bootstrap.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description:
 * @author: luxu
 * @date: 2019-11-06 10:58
 **/
@Slf4j
@Data
public class CustomTokenServices implements ResourceServerTokenServices {

    private static final String CHECK_TOKEN_URI = "/oauth/check_token";

    //轮训算法参数
    public static AtomicInteger pos = new AtomicInteger(0);

    private RestOperations restTemplate;

    private String serviceId;

    private String clientId;

    private String clientSecret;

    private String tokenName = "token";

    private AccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();

    private DiscoveryClient discoveryClient;

    public CustomTokenServices() {
        restTemplate = new RestTemplate();
        ((RestTemplate) restTemplate).setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            // Ignore 400
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() != 400) {
                    super.handleError(response);
                }
            }
        });
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
        formData.add(tokenName, accessToken);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", getAuthorizationHeader(clientId, clientSecret));
        //获取认证服务实例信息
        String checkTokenEndpointUrl = getServiceUrl(serviceId);
        Map<String, Object> map = postForMap(checkTokenEndpointUrl, formData, headers);
        if (map.containsKey("error")) {
            if (log.isDebugEnabled()) {
                log.debug("check_token returned error: " + map.get("error"));
            }
            throw new InvalidTokenException(accessToken);
        }

        // gh-838
        if (!Boolean.TRUE.equals(map.get("active"))) {
            log.debug("check_token returned active attribute: " + map.get("active"));
            throw new InvalidTokenException(accessToken);
        }

        return tokenConverter.extractAuthentication(map);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        throw new UnsupportedOperationException("Not supported: read access token");
    }

    private String getAuthorizationHeader(String clientId, String clientSecret) {

        if(clientId == null || clientSecret == null) {
            log.warn("Null Client ID or Client Secret detected. Endpoint that requires authentication will reject request with 401 error.");
        }

        String creds = String.format("%s:%s", clientId, clientSecret);
        try {
            return "Basic " + new String(Base64.encode(creds.getBytes("UTF-8")));
        }
        catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Could not convert String");
        }
    }

    private Map<String, Object> postForMap(String path, MultiValueMap<String, String> formData, HttpHeaders headers) {
        if (headers.getContentType() == null) {
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        }
        @SuppressWarnings("rawtypes")
        Map map = restTemplate.exchange(path, HttpMethod.POST,
                new HttpEntity<MultiValueMap<String, String>>(formData, headers), Map.class).getBody();
        @SuppressWarnings("unchecked")
        Map<String, Object> result = map;
        return result;
    }

    private String getServiceUrl(String serviceId) {
        //实现一个轮询算法,以后看情况抽象出一个负载均衡算法类
        List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceId);
        if (pos.get() >= serviceInstances.size()) {
            pos = new AtomicInteger(0);
        }
        ServiceInstance instance = serviceInstances.get(pos.get());
        pos.getAndIncrement();
        return instance.getUri() + CHECK_TOKEN_URI;
    }

}
