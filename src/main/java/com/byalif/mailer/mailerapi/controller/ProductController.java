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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.byalif.mailer.mailerapi.entity.ProductDetails;
import com.byalif.mailer.mailerapi.repository.ProductDetailsRepo;

@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
    private ProductDetailsRepo productDetailsRepository;


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
}
