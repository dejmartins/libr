package africa.semicolon.library.service;

import africa.semicolon.library.config.keycloakConfig.KeycloakProvider;
import africa.semicolon.library.data.dto.request.RegisterRequest;
import africa.semicolon.library.data.dto.response.RegisterResponse;
import africa.semicolon.library.data.model.Librarian;
import africa.semicolon.library.data.model.Member;
import africa.semicolon.library.data.repository.UserRepository;
import africa.semicolon.library.exception.registrationException.DuplicateEmailRegistrationException;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${keycloak.realm}")
    public String realm;

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

//    public RegisterResponse registerMember(RegisterRequest request){
//        Member member = modelMapper.map(request, Member.class);
//        member.setDateOfMembership(new Date());
//
//        createKeycloakUser(request);
//
//        Member savedMember = userRepository.save(member);
//
//        return RegisterResponse.builder()
//                .firstName(savedMember.getFirstName())
//                .lastName(savedMember.getLastName())
//                .email(savedMember.getEmailAddress())
//                .build();
//    }

    private void createKeycloakUser(RegisterRequest request) {
        System.out.println(realm);
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
