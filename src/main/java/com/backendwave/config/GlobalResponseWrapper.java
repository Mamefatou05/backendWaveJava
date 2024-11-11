package com.backendwave.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalResponseWrapper implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;

    public GlobalResponseWrapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {

        // Exclure les routes Swagger
        String path = request.getURI().getPath();
        if (path.startsWith("/api/v1/v3/api-docs") || path.startsWith("/swagger-ui") || path.startsWith("/swagger-resources")) {
            return body;
        }

        // Vérifier si le corps de la réponse est déjà au format enveloppé
        if (body instanceof Map && ((Map<?, ?>) body).containsKey("status") && ((Map<?, ?>) body).containsKey("message")) {
            return body;
        }

        Map<String, Object> wrapper = new HashMap<>();
        wrapper.put("status", "SUCCESS");
        wrapper.put("message", "Resource trouvée");
        wrapper.put("data", body);

        // Si le corps est déjà une chaîne, retourner comme un JSON
        if (body instanceof String) {
            try {
                return objectMapper.writeValueAsString(wrapper);
            } catch (Exception e) {
                e.printStackTrace();
                return wrapper; // Au cas où la sérialisation échoue
            }
        }

        return wrapper;
    }


}