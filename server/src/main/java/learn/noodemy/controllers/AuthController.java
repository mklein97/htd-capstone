package learn.noodemy.controllers;

import learn.noodemy.domain.AuthService;
import learn.noodemy.domain.Result;
import learn.noodemy.model.authentication.LoginRequestDTO;
import learn.noodemy.model.authentication.LoginResponseDTO;
import learn.noodemy.model.authentication.RegisterRequestDTO;
import learn.noodemy.model.authentication.RegisterResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/api/authenticate")
    public ResponseEntity<Object> authenticate(@RequestBody LoginRequestDTO credentials) {

        Result<LoginResponseDTO> result = authService.login(credentials);
        if (result.getPayload() == null) return ErrorResponse.build(result);

        return new ResponseEntity<>(result.getPayload(), HttpStatus.OK);
    }

    @PostMapping("/api/register")
    public ResponseEntity<Object> register(@RequestBody RegisterRequestDTO registerRequestDTO) {

        Result<RegisterResponseDTO> result = authService.register(registerRequestDTO);
        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }

        return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
    }

}


