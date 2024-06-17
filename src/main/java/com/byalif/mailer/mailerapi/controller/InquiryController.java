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

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.byalif.mailer.mailerapi.DTO.QuestionDTO;
import com.byalif.mailer.mailerapi.DTO.ResponseDTO;
import com.byalif.mailer.mailerapi.entity.ClientResults;
import com.byalif.mailer.mailerapi.repository.ClientResultRepo;
import com.byalif.mailer.mailerapi.repository.InquiryRepository;
import com.byalif.mailer.mailerapi.service.EmailService;

@RestController
@RequestMapping("/inquiries")
public class InquiryController {

    @Autowired
    private InquiryRepository inquiryRepository;
    
    @Autowired    
    private ClientResultRepo clientResultRepository;
    
    @Autowired
    EmailService sendEmailService;
    
    @PostMapping("/newInquiry")
    public ResponseEntity<ResponseDTO> sendEmail(@RequestBody QuestionDTO questionDTO) {
        try {
            // Process the email request here
            // For example, call a service to send the email
            sendEmailService.sendEmailAsync(questionDTO);

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
}
