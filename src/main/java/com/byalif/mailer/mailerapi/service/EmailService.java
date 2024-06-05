package com.byalif.mailer.mailerapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.byalif.mailer.mailerapi.DTO.EmailDTO;
import com.byalif.mailer.mailerapi.DTO.ProductDetailsDTO;
import com.byalif.mailer.mailerapi.DTO.QuestionDTO;
import com.byalif.mailer.mailerapi.entity.ClientResults;
import com.byalif.mailer.mailerapi.entity.Inquiry;
import com.byalif.mailer.mailerapi.entity.ProductDetails;
import com.byalif.mailer.mailerapi.entity.Receipt;
import com.byalif.mailer.mailerapi.entity.Subscriber;
import com.byalif.mailer.mailerapi.repository.InquiryRepository;
import com.byalif.mailer.mailerapi.repository.ProductDetailsRepo;
import com.byalif.mailer.mailerapi.repository.ReceiptRepository;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	private static final String UTF_8_ENCODING = "UTF-8";

	@Autowired
	JavaMailSender emailSender;
	
	@Autowired
	ProductDetailsRepo productRepository;
	
	@Autowired
	ReceiptRepository receiptRepository;
	
	@Autowired
	InquiryRepository inquiryRepository;

	@Autowired
	TemplateEngine templateEngine;

	@Value("${spring.mail.verify.host")
	String host;
	@Value("${spring.mail.username")
	String username;

	// For purchase receipts
	public void sendEmailAsync(EmailDTO emailDTO) {
		try {
			Context context = new Context();
			context.setVariable("email", emailDTO.getEmail());
			context.setVariable("products", emailDTO.getProducts());
			String text = templateEngine.process("receipt", context);
			
			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
			helper.setPriority(1);
			helper.setSubject("Receipt from sousaesthetic.com");
			helper.setFrom(username);
			helper.setTo("alifrahi22@gmail.com");
			helper.setText(text, true);
			
			emailSender.send(message);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
	
	
	// Additional email after receipt
	public void sendAddtional(String email) {
		try {
			Context context = new Context();
			context.setVariable("name", email);
			String text = templateEngine.process("email", context);
			
			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
			helper.setPriority(2);
			helper.setSubject("Welcome aboard: Your journey starts now");
			helper.setFrom(username);
			helper.setTo("alifrahi22@gmail.com");
			helper.setText(text, true);
			
			emailSender.send(message);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	// For new client forms
	public void sendEmailAsync(QuestionDTO questionDTO) {
		try {
			Context context = new Context();
			context.setVariable("questions", questionDTO.getQuestions());
			context.setVariable("email", questionDTO.getEmail());
			String text = templateEngine.process("questionEmail", context);
			
			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
			helper.setPriority(1);
			helper.setSubject("New Client Inquiry: Form Submission");
			helper.setFrom(username);
			helper.setTo("ginkgfx@gmail.com");
			helper.setText(text, true);
			
			emailSender.send(message);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}


	public Receipt saveInvoiceToDB(EmailDTO emailDto) {
		List<ProductDetails> products = emailDto.getProducts().stream()
                .map(dto -> new ProductDetails(dto.getProductName(), dto.getQuantity(), dto.getAmount(), dto.getCurrency()))
                .collect(Collectors.toList());
		Receipt receipt = new Receipt(products, emailDto.getEmail());
		
		for (ProductDetails product : products) {
	        product.setReceipt(receipt);
	    }
		
		return receiptRepository.save(receipt);
	}


	public Inquiry saveInquiryToDB(QuestionDTO questionDTO) {
		List<ClientResults> results = questionDTO.getQuestions()
				.stream().map(dto -> new ClientResults(dto.getQuestion(), dto.getAnswer())).collect(Collectors.toList());
		
		Inquiry inquiry = new Inquiry(results,questionDTO.getEmail());
		
		
		for (ClientResults result : results) {
			result.setInquiry(inquiry);
	    }
		
		return inquiryRepository.save(inquiry);
	}



}
