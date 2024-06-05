package com.byalif.mailer.mailerapi.repository;

import org.springframework.stereotype.Repository;

import com.byalif.mailer.mailerapi.DTO.EmailDTO;

@Repository
public interface EmailRepository {

	void save(EmailDTO emailDTO);

}
