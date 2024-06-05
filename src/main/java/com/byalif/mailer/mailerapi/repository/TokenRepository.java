package com.byalif.mailer.mailerapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.byalif.mailer.mailerapi.entity.Token;

public interface TokenRepository extends JpaRepository<Token, Long>{

	Optional<Token> findByTokenValue(String token);

}
