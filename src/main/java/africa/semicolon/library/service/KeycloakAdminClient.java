package africa.semicolon.library.service;

import africa.semicolon.library.config.KeycloakProvider;
import africa.semicolon.library.data.dto.RegisterRequest;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakAdminClient {

    @Value("${keycloak.realm}")
    public String realm;

    @Autowired
    private KeycloakProvider kcProvider;

    public Response createKeycloakUser(RegisterRequest request) {
        UsersResource usersResource = kcProvider.getInstance().realm(realm).users();
        CredentialRepresentation credentialRepresentation = createPasswordCredentials(request.getPassword());

        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(request.getEmailAddress());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setFirstName(request.getFirstName());
        kcUser.setLastName(request.getLastName());
        kcUser.setEmail(request.getEmailAddress());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);

        return usersResource.create(kcUser);

    }

    private static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }
}
