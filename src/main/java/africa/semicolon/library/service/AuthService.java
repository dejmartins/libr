package africa.semicolon.library.service;

import africa.semicolon.library.config.keycloakConfig.KeycloakProvider;
import africa.semicolon.library.data.dto.request.LoginRequest;
import africa.semicolon.library.data.dto.request.RegisterRequest;
import africa.semicolon.library.data.dto.response.RegisterResponse;
import africa.semicolon.library.data.model.Librarian;
import africa.semicolon.library.data.repository.UserRepository;
import africa.semicolon.library.exception.loginException.InvalidLoginDetailsException;
import africa.semicolon.library.exception.registrationException.DuplicateEmailRegistrationException;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${keycloak.realm}")
    private String realm;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private KeycloakProvider kcProvider;

    @Autowired
    private UserRepository userRepository;

    public RegisterResponse register(RegisterRequest request) {
        boolean emailExists = userRepository.existsByEmailAddress(request.getEmailAddress());

        if (emailExists) {
            throw new DuplicateEmailRegistrationException("A librarian with email " + request.getEmailAddress() + " already exists");
        }

        Librarian librarian = modelMapper.map(request, Librarian.class);

        createKeycloakUser(request);

        Librarian savedLibrarian = userRepository.save(librarian);

        return RegisterResponse.builder()
                .firstName(savedLibrarian.getFirstName())
                .lastName(savedLibrarian.getLastName())
                .email(savedLibrarian.getEmailAddress())
                .build();
    }

    public AccessTokenResponse login(LoginRequest loginRequest) throws InvalidLoginDetailsException {
        Keycloak keycloak = kcProvider
                .newKeycloakBuilderWithCredentials(loginRequest.getEmailAddress(), loginRequest.getPassword())
                .build();

        AccessTokenResponse accessTokenResponse;
        accessTokenResponse = keycloak.tokenManager().getAccessToken();

        if (accessTokenResponse == null){
            throw new InvalidLoginDetailsException("Invalid Login Details");
        }

        return accessTokenResponse;
    }

    private void createKeycloakUser(RegisterRequest request) {
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

        usersResource.create(kcUser);
    }

    private static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }
}
