package com.byalif.mailer.mailerapi.DTO;

public class ProductDetailsDTO {
    public ProductDetailsDTO(String productName, Long quantity, Long amount, String currency) {
		this.productName = productName;
		this.quantity = quantity;
		this.amount = amount;
		this.currency = currency;
	}
    ProductDetailsDTO(){};

	private String productName;
    private Long quantity;
    private Long amount;
    private String currency;
    public void setProductName(String productName) {
		this.productName = productName;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	// Getters
    public String getProductName() {
        return productName;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Long getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }
}
