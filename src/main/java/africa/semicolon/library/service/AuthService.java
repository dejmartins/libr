package africa.semicolon.library.service;

import africa.semicolon.library.config.keycloakConfig.KeycloakProvider;
import africa.semicolon.library.data.dto.request.LoginRequest;
import africa.semicolon.library.data.dto.request.RegisterRequest;
import africa.semicolon.library.data.dto.response.KeycloakTokenResponse;
import africa.semicolon.library.data.dto.response.LogoutError;
import africa.semicolon.library.data.dto.response.LogoutResponse;
import africa.semicolon.library.data.dto.response.RegisterResponse;
import africa.semicolon.library.data.model.Librarian;
import africa.semicolon.library.data.repository.UserRepository;
import africa.semicolon.library.exception.loginException.InvalidLoginDetailsException;
import africa.semicolon.library.exception.loginException.InvalidTokenException;
import africa.semicolon.library.exception.registrationException.DuplicateEmailRegistrationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ModelMapper modelMapper;

    private final KeycloakProvider kcProvider;

    private final UserRepository userRepository;

    @Value("${my-keycloak.token.url}")
    private String TOKEN_URL;

    @Value("${my-keycloak.logout}")
    private String LOGOUT;

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

    public LogoutResponse logout(String refreshToken) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        OkHttpClient okHttpClient = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder()
                .add("refresh_token", refreshToken)
                .add("client_id", kcProvider.clientID)
                .add("client_secret", kcProvider.clientSecret)
                .build();

        Request request = new Request.Builder()
                .url(LOGOUT)
                .post(requestBody)
                .build();

        String response = okHttpClient.newCall(request)
                .execute()
                .body()
                .string();

        if (!response.isEmpty()) {
            LogoutError logoutResponse = objectMapper.readValue(response, LogoutError.class);
            throw new InvalidTokenException(logoutResponse.getErrorDescription());
        }

        return LogoutResponse.builder()
                .logout(true).build();
    }

    public KeycloakTokenResponse getKeyCloakToken() throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();

        RequestBody requestBody = new FormBody.Builder()
                .add("grant_type", "password")
                .add("client_id", kcProvider.clientID)
                .add("username", kcProvider.username)
                .add("password", kcProvider.password)
                .add("client_secret", kcProvider.clientSecret)
                .add("scope", "openid")
                .build();

        Request request = new Request.Builder()
                .url(TOKEN_URL)
                .post(requestBody)
                .build();

        Response response = okHttpClient.newCall(request).execute();

        return objectMapper
                .readValue(response.body().string(), KeycloakTokenResponse.class);
    }

    private void createKeycloakUser(RegisterRequest request) {
        UsersResource usersResource = kcProvider.getInstance().realm(kcProvider.realm).users();
        CredentialRepresentation credentialRepresentation = createPasswordCredentials(request.getPassword());

        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(request.getEmailAddress());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setFirstName(request.getFirstName());
        kcUser.setLastName(request.getLastName());
        kcUser.setEmail(request.getEmailAddress());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);

        jakarta.ws.rs.core.Response response = usersResource.create(kcUser);
        addUserRole(response, usersResource, "MEMBER");

    }

    private void addUserRole(jakarta.ws.rs.core.Response response, UsersResource usersResource, String role) {
        String userId = CreatedResponseUtil.getCreatedId(response);

        RealmResource realmResource = kcProvider.getInstance().realm(kcProvider.realm);
        ClientRepresentation clientRepresentation = realmResource.clients()
                .findByClientId(kcProvider.clientID).get(0);

        RoleRepresentation userClientRole = realmResource.clients().get(clientRepresentation.getId())
                .roles().get(role).toRepresentation();

        UserResource userResource = usersResource.get(userId);

        userResource.roles()
                .clientLevel(clientRepresentation.getId()).add(Arrays.asList(userClientRole));
    }

    private static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }
}
