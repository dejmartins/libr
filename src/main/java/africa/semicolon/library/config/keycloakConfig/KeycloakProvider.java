package africa.semicolon.library.config.keycloakConfig;

import lombok.Getter;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class KeycloakProvider {

    @Value("${keycloak.auth-server-url}")
    public String serverURL;
    @Value("${keycloak.realm}")
    public String realm;
    @Value("${keycloak.resource}")
    public String clientID;
    @Value("${keycloak.credentials.secret}")
    public String clientSecret;
    @Value("${my-keycloak.username}")
    public String username;
    @Value("${my-keycloak.password}")
    public String password;

    private static Keycloak keycloak = null;

    public Keycloak getInstance() {
        if (keycloak == null) {
            keycloak = KeycloakBuilder.builder()
                    .realm(realm)
                    .serverUrl(serverURL)
                    .username(username)
                    .password(password)
                    .clientId(clientID)
                    .clientSecret(clientSecret)
                    .scope("openid")
                    .grantType(OAuth2Constants.PASSWORD)
                    .build();
        }
        return keycloak;
    }

    public KeycloakBuilder newKeycloakBuilderWithCredentials(String username, String password) {
        return KeycloakBuilder.builder()
                .realm(realm)
                .serverUrl(serverURL)
                .clientId(clientID)
                .clientSecret(clientSecret)
                .scope("openid")
                .username(username)
                .password(password)
                .grantType(OAuth2Constants.PASSWORD);
    }
}

