package com.abhay.userservice.resource;

import com.abhay.userservice.domain.Confirmation;
import com.abhay.userservice.domain.HttpResponse;
import com.abhay.userservice.domain.User;
import com.abhay.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserResource {
    private final UserService userService;

    @Operation(
            summary = "Save user",
            description = "Creates a new user in the system and sends a welcome email. " +
            "The format of the email can be specified via the 'emailType' parameter."
    )
    @PostMapping
    public ResponseEntity<HttpResponse> createUser(@RequestBody User user,
                                                   @Parameter(
                                                           description = "Here are the options for the email type:\n" +
                                                                   "1. simpleEmail\n2. emailWithAttachment\n3. emailWithEmbeddedImages\n4. HTMLEmail\n5. htmlEmailWithAttachment",
                                                           example = "HTML"
                                                   )
                                                   @RequestParam(value = "emailType",required = false) String emailType) {
        User newUser = userService.saveUser(user,emailType);
    return ResponseEntity.created(URI.create("")).body(
            HttpResponse.builder()
                    .timeStamp(LocalDateTime.now().toString())
                    .data(Map.of("user",newUser))
                    .message("User Created")
                    .status(HttpStatus.CREATED)
                    .statusCode(HttpStatus.CREATED.value())
                    .build()
    );
    }

    @GetMapping
    public ResponseEntity<HttpResponse> confirmUserAccount(@RequestParam("token") String token) {
        Boolean isSuccess = userService.verifyToken(token);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("Success",isSuccess))
                        .message("Account Verified")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
}
