package africa.semicolon.library.config;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class KeycloakResolver {

    @Bean
    public KeycloakConfigResolver resolver(){
        return new KeycloakSpringBootConfigResolver();
    }
}
