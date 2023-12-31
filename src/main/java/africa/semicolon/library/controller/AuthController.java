package africa.semicolon.library.controller;

import africa.semicolon.library.config.keycloakConfig.KeycloakProvider;
import africa.semicolon.library.data.dto.request.LoginRequest;
import africa.semicolon.library.data.dto.request.RegisterRequest;
import africa.semicolon.library.data.dto.response.SuccessResponse;
import africa.semicolon.library.service.AuthService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final KeycloakProvider kcProvider;

    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    @PostMapping
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        SuccessResponse response = SuccessResponse.builder()
                .data(authService.register(request))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) throws IOException {
        SuccessResponse response = SuccessResponse.builder()
                .data(authService.login(loginRequest))
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("X-Refresh-Token") String refreshToken) throws IOException {

        SuccessResponse response = SuccessResponse.builder()
                .data(authService.logout(refreshToken))
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/token")
    public ResponseEntity<?> getKeycloakToken() throws IOException {
        SuccessResponse response = SuccessResponse.builder()
                .data(authService.getKeyCloakToken())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
