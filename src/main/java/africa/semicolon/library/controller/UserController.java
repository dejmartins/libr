package africa.semicolon.library.controller;

import africa.semicolon.library.config.KeycloakProvider;
import africa.semicolon.library.data.dto.LoginRequest;
import africa.semicolon.library.data.dto.RegisterRequest;
import africa.semicolon.library.service.KeycloakAdminClient;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/user")
@AllArgsConstructor
public class UserController {

    private final KeycloakAdminClient kcAdminClient;

    private final KeycloakProvider kcProvider;

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody RegisterRequest user) {
        Response createdResponse = kcAdminClient.createKeycloakUser(user);
        return ResponseEntity.status(createdResponse.getStatus()).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponse> login(@NotNull @RequestBody LoginRequest loginRequest) {
        Keycloak keycloak = kcProvider.newKeycloakBuilderWithPasswordCredentials(loginRequest.getEmailAddress(), loginRequest.getPassword()).build();

        AccessTokenResponse accessTokenResponse = null;
        try {
            accessTokenResponse = keycloak.tokenManager().getAccessToken();
            return ResponseEntity.status(HttpStatus.OK).body(accessTokenResponse);
        } catch (Exception ex) {
            LOG.warn("invalid account. User probably hasn't verified email." + ex);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(accessTokenResponse);
        }

    }
}
