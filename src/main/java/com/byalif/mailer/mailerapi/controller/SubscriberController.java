package com.byalif.mailer.mailerapi.controller;

import java.util.List;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.byalif.mailer.mailerapi.entity.Subscriber;
import com.byalif.mailer.mailerapi.repository.SubscriberRepository;



@RestController
@RequestMapping("/mail/subscriber")
public class SubscriberController {

    private static final Logger logger = LoggerFactory.getLogger(SubscriberController.class);

    @Autowired
    private SubscriberRepository subscriberRepository;

    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribe(@RequestBody Subscriber request) {
        try {
            // Check if email is already subscribed
            if (subscriberRepository.existsByEmail(request.getEmail())) {
                logger.warn("Email '{}' is already subscribed.", request.getEmail());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is already subscribed.");
            }

            // Create new subscriber entity
            Subscriber subscriber = new Subscriber(request.getEmail());
            subscriberRepository.save(subscriber);

            logger.info("New subscriber added: {}", subscriber.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body("Successfully subscribed.");
        } catch (Exception e) {
            logger.error("Failed to subscribe email '{}': {}", request.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to subscribe.");
        }
    }
    
    @GetMapping("/getAllSubscribers")
    public ResponseEntity<List<Subscriber>> getAllSubscribers() {
            return ResponseEntity.status(HttpStatus.OK).body(subscriberRepository.findAll());
     }

//    @PostMapping("/sendEmailToAllSubscribers")
//    public ResponseEntity<String> sendEmailToAllSubscribers(@RequestBody String message) {
//        try {
//            // Get all subscribers
//            List<Subscriber> subscribers = subscriberRepository.findAll();
//
//            // Send email to each subscriber using Kafka
//            for (Subscriber subscriber : subscribers) {
//                kafkaTemplate.send(new ProducerRecord<>("email-topic", subscriber.getEmail(), message));
//            }
//
//            return ResponseEntity.ok("Email sent to all subscribers.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email.");
//        }
//    }
}
