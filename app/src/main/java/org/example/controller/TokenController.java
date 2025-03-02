package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.entities.RefreshToken;
import org.example.request.AuthRequestDto;
import org.example.request.RefreshTokenDto;
import org.example.response.JwtResponseDto;
import org.example.service.JwtService;
import org.example.service.RefreshTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    //if both tokens expires the user has to login then returns both tokens
    @PostMapping("/auth/v1/login")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody AuthRequestDto authRequestDto) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDto.getUsername(), authRequestDto.getPassword()));
        if (authentication.isAuthenticated()) {
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequestDto.getUsername());
            return new ResponseEntity<>(JwtResponseDto.builder()
                    .accessToken(jwtService.createToken(authRequestDto.getUsername()))
                    .token(refreshToken.getToken())
                    .build(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Exception in User Service", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // it will be called when Jwt expires and generates new Jwt with Refresh token
    @PostMapping("/auth/v1/refreshToken")
    public JwtResponseDto refreshToken(@RequestBody RefreshTokenDto refreshTokenDto) {
        return refreshTokenService.findByToken(refreshTokenDto.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(userInfo -> {
                    String accessToken = jwtService.createToken(userInfo.getUsername());
                    return JwtResponseDto.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenDto.getToken()).build();
                }).orElseThrow(()-> new RuntimeException("Refresh Token is not in DB"));
    }
}