package com.byalif.mailer.mailerapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.byalif.mailer.mailerapi.DTO.KafkaDTO;
import com.byalif.mailer.mailerapi.entity.Inquiry;
import com.byalif.mailer.mailerapi.entity.Receipt;

@Service
public class KafkaConsumer {
	final static Logger log = LoggerFactory.getLogger(KafkaConsumer.class);
	
	@Autowired
	private EmailService emailService;

	
	@KafkaListener(topics = {"transaction_requests"}, groupId = "email")
	public void transactionListener(KafkaDTO kafkaDTO) {
		log.info(String.format("New Transaction from: ", kafkaDTO.getEmailDto().getEmail()));
		
        // Send email asynchronously
        emailService.sendEmailAsync(kafkaDTO.getEmailDto());
        
        emailService.sendAddtional(kafkaDTO.getEmailDto().getEmail());       

        //Save the transaction to DB
       
        Receipt receipt = emailService.saveInvoiceToDB(kafkaDTO.getEmailDto());
        
		log.info(String.format("Transaction id: %d", receipt.getId()));
    }
	
	@KafkaListener(topics = {"inquiry_requests"}, groupId = "email")
	public void inquiryListener(KafkaDTO kafkaDTO) {
		log.info(String.format("New client Inquiry: ", kafkaDTO.getQuestionDTO().getEmail()));
		
        // Send email asynchronously
        emailService.sendEmailAsync(kafkaDTO.getQuestionDTO());
        
        
        Inquiry inquiry = emailService.saveInquiryToDB(kafkaDTO.getQuestionDTO());
        
        log.info(String.format("Inquiry id: ", inquiry.getId()));
        }
	

}
