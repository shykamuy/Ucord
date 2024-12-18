package com.example.ucord_auth_service.controller;

import com.example.ucord_auth_service.DTO.AuthResponseDTO;
import com.example.ucord_auth_service.DTO.RefreshTokenDTO;
import com.example.ucord_auth_service.DTO.request.CreateUserRequest;
import com.example.ucord_auth_service.DTO.request.LoginRequest;
import com.example.ucord_auth_service.DTO.response.AuthResponse;
import com.example.ucord_auth_service.DTO.response.RefreshTokenResponse;
import com.example.ucord_auth_service.DTO.response.SimpleResponse;
import com.example.ucord_auth_service.exception.AlreadyExistsException;
import com.example.ucord_auth_service.model.RoleType;
import com.example.ucord_auth_service.repository.GroupAuthRepository;
import com.example.ucord_auth_service.repository.UserAuthRepository;
import com.example.ucord_auth_service.security.SecurityService;
import com.example.ucord_auth_service.security.UserDetailsServiceImpl;
import com.example.ucord_auth_service.security.jwt.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;




@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserAuthRepository userRepository;

    private final GroupAuthRepository groupRepository;
    private final SecurityService securityService;

    private final UserDetailsServiceImpl userDetailsService;

    private final JwtUtils jwtUtils;



    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> authUser(@RequestBody LoginRequest loginRequest) {
        AuthResponseDTO authResponse = securityService.authenticateUser(loginRequest);

/*
        ResponseCookie cookie = ResponseCookie.from("refreshToken", authResponse.getRefreshToken())
                .httpOnly(true)
                .secure(true) // Убедитесь, что это установлено в true в продакшене
                .path("/")
                .maxAge(60 * 60 * 24)
                .sameSite("None")
                .build();
*/

        return ResponseEntity.ok()
                //.header("Set-Cookie", cookie.toString())
                .body(new AuthResponse(
                        authResponse.getId(),
                        authResponse.getToken(),
                        authResponse.getRefreshToken(),
                        authResponse.getUsername(),
                        authResponse.getEmail(),
                        authResponse.getRoles()
                ));
    }

    @PostMapping("/register")
    public ResponseEntity<SimpleResponse> registerUser(@RequestBody CreateUserRequest createUserRequest) {
        if (userRepository.existsByUsername(createUserRequest.getUsername())) {
            throw new AlreadyExistsException("Username already exists!");
        }
        if (userRepository.existsByEmail(createUserRequest.getEmail())) {
            throw new AlreadyExistsException("Email already exists!");
        }
        if (groupRepository.findByName(createUserRequest.getGroupName()).isEmpty()
                && createUserRequest.getRole().equals(RoleType.ROLE_USER)) {
            throw new RuntimeException("Group is not found!");
        }
        securityService.register(createUserRequest);

        return ResponseEntity.ok(new SimpleResponse("User created!"));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestHeader(required = true) String refreshToken) {
        // Извлекаем refresh token из cookie
/*
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
*/



        RefreshTokenDTO refreshTokenResponse = securityService.refreshToken(refreshToken);
        return ResponseEntity.ok(new RefreshTokenResponse(refreshTokenResponse.getAccessToken()));
    }

    @GetMapping("/validate")
    @ResponseStatus(HttpStatus.OK)
    public Long validateToken(@RequestHeader("Authorization") String headerAuth) {

        if (!StringUtils.hasText(headerAuth) && !headerAuth.startsWith("Bearer")) {
            throw new RuntimeException("Invalid token");
        }
        String token = headerAuth.substring(7);
        Long userId = userRepository.findByEmail(jwtUtils.getEmail(token)).orElseThrow().getId();
        return userId;
    }

    @PostMapping("/logout")
    public ResponseEntity<SimpleResponse> logoutUser(@AuthenticationPrincipal UserDetails userDetails, HttpServletResponse response) {
        securityService.logout();

        // Удаляем cookie refresh token
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // Устанавливаем время жизни в 0, чтобы удалить cookie
        response.addCookie(cookie);

        return ResponseEntity.ok(new SimpleResponse("User logout. Username is: " + userDetails.getUsername()));
    }
}
