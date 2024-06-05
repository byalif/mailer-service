package com.byalif.mailer.mailerapi.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import lombok.Data;

@Entity
@Data
public class Receipt {
	
	@Column(name = "created_at", nullable = false)
	private Date createdAt;
	
	@PrePersist
	protected void onCreate() {
	        createdAt = new Date();
	 }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    

    
    
	@JsonManagedReference
	@OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ProductDetails> productDetails;
    
    private String email;
    
    public Receipt(List<ProductDetails> productDetails, String email) {
        this.productDetails = productDetails;
        this.email = email;
    }
    
    public Receipt() {}
    
	public Date getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}



    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ProductDetails> getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(List<ProductDetails> productDetails) {
        this.productDetails = productDetails;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
