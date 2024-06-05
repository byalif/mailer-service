package com.byalif.mailer.mailerapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.byalif.mailer.mailerapi.entity.Subscriber;

public interface SubscriberRepository extends JpaRepository<Subscriber, Integer>{

	boolean existsByEmail(String email);

}
