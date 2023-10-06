package africa.semicolon.library.service;

import africa.semicolon.library.config.keycloakConfig.KeycloakProvider;
import africa.semicolon.library.data.dto.request.RegisterRequest;
import africa.semicolon.library.data.model.Librarian;
import africa.semicolon.library.data.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private Keycloak keycloak;
    @Mock
    private KeycloakProvider keycloakProvider;
    @Mock
    private RealmResource realmResource;
    @Mock
    private UsersResource usersResource;
    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void canRegister() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmailAddress("johndoe@gmail.com");
        request.setPassword("john123@");

        ReflectionTestUtils
                .setField(authService, "realm", "Library");
        when(keycloakProvider.getInstance()).thenReturn(keycloak);
        when(keycloak.realm("Library")).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);

        Librarian librarian = modelMapper.map(request, Librarian.class);
        when(modelMapper.map(request, Librarian.class)).thenReturn(librarian);

        Librarian savedLibrarian = new Librarian();
        when(userRepository.save(librarian)).thenReturn(savedLibrarian);

        authService.register(request);

        ArgumentCaptor<Librarian> librarianArgumentCaptor = ArgumentCaptor.forClass(Librarian.class);
        verify(userRepository).save(librarianArgumentCaptor.capture());
    }
}