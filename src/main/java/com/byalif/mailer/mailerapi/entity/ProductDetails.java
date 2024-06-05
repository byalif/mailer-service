package com.byalif.mailer.mailerapi.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;


@Entity
@Data
public class ProductDetails {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;
    private String productName;
    private Long quantity;
    private Long amount;
	private String currency;

    
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receipt_id", referencedColumnName = "id")
    private Receipt receipt;
	
	
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

    
    public Receipt getReceipt() {
		return receipt;
	}
    
    public ProductDetails() {};
    
    public ProductDetails(String productName, Long quantity, Long amount, String currency) {
		this.productName = productName;
		this.quantity = quantity;
		this.amount = amount;
		this.currency = currency;
	}

	public void setReceipt(Receipt receipt) {
		this.receipt = receipt;
	}

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
