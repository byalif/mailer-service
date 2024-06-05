package com.byalif.mailer.mailerapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.byalif.mailer.mailerapi.entity.Token;
import com.byalif.mailer.mailerapi.repository.TokenRepository;

@RestController
@RequestMapping("/token")
public class TokenController {

    @Autowired
    private TokenRepository tokenRepository;

    @GetMapping("/invalidateToken")
    public void invalidateToken(@RequestParam String token) {
        Token newToken = new Token();
        newToken.setTokenValue(token);
        tokenRepository.save(newToken);
    }

    @GetMapping("/validateToken")
    public boolean validateToken(@RequestParam String token) {
        return tokenRepository.findByTokenValue(token).isPresent();
    }
}
