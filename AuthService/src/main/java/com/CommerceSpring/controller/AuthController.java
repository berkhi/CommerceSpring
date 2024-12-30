package com.CommerceSpring.controller;

import com.CommerceSpring.dto.request.*;
import com.CommerceSpring.dto.response.ResponseDto;
import com.CommerceSpring.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.CommerceSpring.constants.EndPoints.*;

@RestController
@RequestMapping(AUTH)
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {
    private final AuthService authService;

    @PostMapping(REGISTER)
    @Operation( summary = "Register a new auth",
            description = "Registers a new auth in the system. The user details must be provided in the request body.")
   public ResponseEntity<ResponseDto<Boolean>> register(@RequestBody RegisterRequestDto dto){
        return ResponseEntity.ok(ResponseDto.<Boolean>builder()
               .data(authService.register(dto))
               .code(200)
               .message("Succesfully registered")
               .build());

   }

   @PostMapping(LOGIN)
   @Operation(
           summary = "Login a user",
           description = "Logs in a user with the provided credentials. The credentials must be provided in the request body."
   )
   public ResponseEntity<ResponseDto<String>> login(@RequestBody LoginRequestDto dto){
       return ResponseEntity.ok(ResponseDto.<String>builder()
               .code(200)
               .message("Succesfully logged in")
               .data(authService.login(dto))
               .build());
   }

}
