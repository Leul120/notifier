package com.safeStatusNotifier.safeStatusNotifier.controllers;

import com.safeStatusNotifier.safeStatusNotifier.repositories.UserRepository;
import com.safeStatusNotifier.safeStatusNotifier.requests.JwtAuthenticationRequest;
import com.safeStatusNotifier.safeStatusNotifier.requests.RefreshTokenRequest;
import com.safeStatusNotifier.safeStatusNotifier.requests.SignInRequest;
import com.safeStatusNotifier.safeStatusNotifier.requests.SignUpRequest;
import com.safeStatusNotifier.safeStatusNotifier.services.AuthenticationService;
import com.safeStatusNotifier.safeStatusNotifier.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {
    private UserRepository userRepository;
//    @Value("${spring.security.oauth2.client.registration.google.client-id}")
//    private String googleClientId;
private final AuthenticationService authenticationService;
    @PostMapping("/signup")
    public ResponseEntity<JwtAuthenticationRequest> signup(@RequestBody SignUpRequest signUpRequest){
            return ResponseEntity.ok(authenticationService.signup(signUpRequest));

    }
    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationRequest> signIn(@RequestBody SignInRequest signInRequest){
            return ResponseEntity.ok(authenticationService.signIn(signInRequest));

    }
    @GetMapping
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("working");

    }





//    @PostMapping("/google")
//    public ResponseEntity<ApiResponse> googleSignUp(@RequestBody TokenRequest request){
//        System.out.println(request);
//        System.out.println(googleClientId);
//        try{
//            GoogleIdTokenVerifier verifier=new GoogleIdTokenVerifier.Builder(
//                    new NetHttpTransport(),
//                    new GsonFactory()
//            ).setAudience(Collections.singletonList(googleClientId))
//                    .build();
//            GoogleIdToken idToken=verifier.verify(request.getCredential());
//            if(idToken!=null){
//                GoogleIdToken.Payload payload=idToken.getPayload();
//                String email=payload.getEmail();
//                String sub=payload.getSubject();
//                String firstName=(String) payload.get("given_name");
//                String lastName=(String) payload.get("family_name");
//                System.out.println(email+sub+firstName+lastName);
//                Optional<User> user=userRepository.findByEmail(email);
//                if(user.isEmpty()){
//                    SignUpRequest signUpRequest=new SignUpRequest();
//                    signUpRequest.setEmail(email);
//                    signUpRequest.setFirstName(firstName);
//                    signUpRequest.setLastName(lastName);
//                    signUpRequest.setPassword(sub);
//                    return ResponseEntity.ok(new ApiResponse("success",authenticationService.signup(signUpRequest)));
//                }else{
//                    SignInRequest signInRequest=new SignInRequest();
//                    signInRequest.setEmail(email);
//                    signInRequest.setPassword(sub);
//                    return ResponseEntity.ok(new ApiResponse("success",authenticationService.signIn(signInRequest)));
//                }
//            }else {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                        .body(new ApiResponse("Invalid ID token.",HttpStatus.UNAUTHORIZED));
//            }
//
//        }catch (Exception e){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ApiResponse("Error verifying token: " + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
//        }
//    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationRequest> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
            return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));

    }
}
