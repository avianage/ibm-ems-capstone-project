package com.ibm.employee.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthClient {

    private final RestTemplate restTemplate;

    @Value("${auth-service.base-url:http://localhost:8080}")
    private String authServiceBaseUrl;

    public Optional<String> createUserFromEmployee(String employeeId, String email, String authorizationHeader) {
        try {
            if (authorizationHeader == null) {
                return Optional.empty();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.AUTHORIZATION, authorizationHeader);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> syncRequest = new HashMap<>();
            syncRequest.put("employeeId", employeeId);
            syncRequest.put("username", email.split("@")[0]);
            syncRequest.put("email", email);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(syncRequest, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    authServiceBaseUrl + "/api/v1/users/create-from-employee",
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map body = response.getBody();
                if (body.get("data") instanceof Map) {
                    Map data = (Map) body.get("data");
                    return Optional.ofNullable((String) data.get("id"));
                }
            }
        } catch (RestClientException ex) {
            log.error("Failed to sync employee to auth-service: {}", ex.getMessage());
        }
        return Optional.empty();
    }
}
