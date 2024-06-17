package com.byalif.mailer.mailerapi.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.byalif.mailer.mailerapi.DTO.EmailDTO;
import com.byalif.mailer.mailerapi.DTO.QuestionDTO;
import com.byalif.mailer.mailerapi.DTO.ResponseDTO;
import com.byalif.mailer.mailerapi.entity.ClientResults;
import com.byalif.mailer.mailerapi.entity.Inquiry;
import com.byalif.mailer.mailerapi.entity.ProductDetails;
import com.byalif.mailer.mailerapi.entity.Receipt;
import com.byalif.mailer.mailerapi.entity.Subscriber;
import com.byalif.mailer.mailerapi.entity.Token;
import com.byalif.mailer.mailerapi.repository.ClientResultRepo;
import com.byalif.mailer.mailerapi.repository.InquiryRepository;
import com.byalif.mailer.mailerapi.repository.ProductDetailsRepo;
import com.byalif.mailer.mailerapi.repository.SubscriberRepository;
import com.byalif.mailer.mailerapi.repository.TokenRepository;
import com.byalif.mailer.mailerapi.service.EmailService;

@RestController
@RequestMapping("/mail")
public class InquiryController {


    private static final Logger logg = LoggerFactory.getLogger(SubscriberController.class);

    @Autowired
    private SubscriberRepository subscriberRepository;

    
    @Autowired    
    private ClientResultRepo clientResultRepository;
    
	@Autowired
    private ProductDetailsRepo productDetailsRepository;
    
    @Autowired
    EmailService sendEmailService;
    
    //INQUIRIES
    
    @PostMapping("/newInquiry")
    public ResponseEntity<ResponseDTO> sendEmail(@RequestBody QuestionDTO questionDTO) {
        try {
            // Process the email request here
            // For example, call a service to send the email
            sendEmailService.sendEmailAsync(questionDTO);
            
	        Inquiry inquiry = sendEmailService.saveInquiryToDB(questionDTO);
	        
	        logg.info(String.format("Inquiry id: ", inquiry.getId()));

            // Return a success response
            return ResponseEntity.ok().body(new ResponseDTO("Email sent successfully to: " + questionDTO.getEmail()));
        } catch (Exception e) {
            // Handle exception and log error
//            log.error("Failed to send email", e);
            return ResponseEntity.ok().body(new ResponseDTO("Email did not send successfully to: " + questionDTO.getEmail()));
        }
    }

    @GetMapping("/allResultsGroupedByInquiryId")
    public List<Map<String, Object>> getAllResultsGroupedByInquiryId() {
        List<ClientResults> allResults = clientResultRepository.findAll();

        // Group ClientResults by Inquiry Ids into a HashMap
        Map<Integer, List<ClientResults>> groupedResults = allResults.stream()
        		.collect(Collectors.groupingBy(clientResult ->  clientResult.getInquiry().getId()));
        
        
        groupedResults = groupedResults.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.comparing(p -> p.get(0).getInquiry().getCreatedAt(), Comparator.reverseOrder())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new));
       
        // Create JSON array from grouped data
        List<Map<String, Object>> jsonArray = groupedResults.entrySet().stream()
                .map(entry -> {
                	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                	String createdAtString = dateFormat.format(entry.getValue().get(0).getInquiry().getCreatedAt());
                	SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                	SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM d, yyyy hh:mm a");
                	Date date = null;
					try {
						date = inputFormat.parse(createdAtString);
					} catch (ParseException e) {
						e.printStackTrace();
					}
                	 String formattedDate = outputFormat.format(date);

                    Map<String, Object> result = new HashMap<>();
                    result.put("email", entry.getValue().get(0).getInquiry().getEmail());
                	result.put("date", formattedDate);
                    result.put("inquiryId", entry.getKey());
                    result.put("results", entry.getValue().stream()
                            .map(this::mapToResultDto)
                            .collect(Collectors.toList()));
                    return result;
                }).collect(Collectors.toList());

        return jsonArray;
    }

    // Helper method to map ClientResult entity to DTO
    private Map<String, Object> mapToResultDto(ClientResults clientResult) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("question", clientResult.getQuestion());
        dto.put("answer", clientResult.getAnswer());
        return dto;
    }
    
    
    //PRODUCTS
    
    @PostMapping("/newClientProducts")
    public ResponseEntity<ResponseDTO> sendEmail(@RequestBody EmailDTO emailDTO) {
        try {
            // Process the email request here
            // For example, call a service to send the email
            sendEmailService.sendEmailAsync(emailDTO);
            sendEmailService.sendAddtional(emailDTO.getEmail());
            
            Receipt receipt = sendEmailService.saveInvoiceToDB(emailDTO);
          
            logg.info(String.format("Transaction id: %d", receipt.getId()));
            // Return a success response
            return ResponseEntity.ok().body(new ResponseDTO("Email sent successfully to: " + emailDTO.getEmail()));
        } catch (Exception e) {
            // Handle exception and log error
            logg.error("Failed to send email", e);
            return ResponseEntity.ok().body(new ResponseDTO("Email did not send successfully to: " + emailDTO.getEmail()));
        }
    }

    @GetMapping("/allProductsByReceiptId")
    public List<Map<String, Object>> getAllProductsGroupedByReceiptId() {
        List<ProductDetails> allProductDetails = productDetailsRepository.findAll();

        // Group ProductDetails by Receipt Ids into a HashMap
        Map<Long, List<ProductDetails>> groupedProducts = allProductDetails.stream()
                .collect(Collectors.groupingBy(product -> product.getReceipt().getId()));
        
        
        groupedProducts = groupedProducts.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.comparing(p -> p.get(0).getReceipt().getCreatedAt(), Comparator.reverseOrder())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new));


        // Create JSON array from grouped data
        List<Map<String, Object>> jsonArray = groupedProducts.entrySet().stream()
                .map(entry -> {
                	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                	String createdAtString = dateFormat.format(entry.getValue().get(0).getReceipt().getCreatedAt());
                	SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                	SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM d, yyyy hh:mm a");
                	Date date = null;
					try {
						date = inputFormat.parse(createdAtString);
					} catch (ParseException e) {
						e.printStackTrace();
					}
                	 String formattedDate = outputFormat.format(date);


                    Map<String, Object> receiptMap = new HashMap<>();
                    receiptMap.put("email", entry.getValue().get(0).getReceipt().getEmail()); 
                    receiptMap.put("date", formattedDate);
                    receiptMap.put("receiptId", entry.getKey());
                    receiptMap.put("products", entry.getValue().stream()
                            .map(this::mapToProductDto)
                            .collect(Collectors.toList()));
                    return receiptMap;
                })
                .collect(Collectors.toList());

        return jsonArray;
    }

    // Helper method to map ProductDetails entity to DTO
    private Map<String, Object> mapToProductDto(ProductDetails productDetails) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", productDetails.getId());
        dto.put("productName", productDetails.getProductName());
        dto.put("quantity", productDetails.getQuantity());
        dto.put("amount", productDetails.getAmount());
        dto.put("currency", productDetails.getCurrency());
        return dto;
    }
    
    
    //SUBSCRIBERS 
    
    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribe(@RequestBody Subscriber request) {
        try {
            // Check if email is already subscribed
            if (subscriberRepository.existsByEmail(request.getEmail())) {
                logg.warn("Email '{}' is already subscribed.", request.getEmail());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is already subscribed.");
            }

            // Create new subscriber entity
            Subscriber subscriber = new Subscriber(request.getEmail());
            subscriberRepository.save(subscriber);

            logg.info("New subscriber added: {}", subscriber.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body("Successfully subscribed.");
        } catch (Exception e) {
            logg.error("Failed to subscribe email '{}': {}", request.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to subscribe.");
        }
    }
    
    @GetMapping("/getAllSubscribers")
    public ResponseEntity<List<Subscriber>> getAllSubscribers() {
            return ResponseEntity.status(HttpStatus.OK).body(subscriberRepository.findAll());
     }
    
    //token
    

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
