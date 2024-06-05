package com.byalif.mailer.mailerapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.byalif.mailer.mailerapi.entity.Receipt;

public interface ReceiptRepository extends JpaRepository<Receipt, Long>{

}
