package africa.semicolon.library.service;

import africa.semicolon.library.config.KeycloakProvider;
import africa.semicolon.library.data.dto.request.RegisterRequest;
import africa.semicolon.library.data.model.Member;
import africa.semicolon.library.data.model.User;
import africa.semicolon.library.data.repository.UserRepository;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class KeycloakAdminClient {

    @Value("${keycloak.realm}")
    public String realm;

    @Autowired
    private KeycloakProvider kcProvider;

    private final UserRepository userRepository;

    public void createMember(RegisterRequest request){
        User user = new Member();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmailAddress(request.getEmailAddress());
        userRepository.save(user);
    }

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

        createMember(request);

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
