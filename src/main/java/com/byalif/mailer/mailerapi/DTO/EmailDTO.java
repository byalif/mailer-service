package com.byalif.mailer.mailerapi.DTO;

import java.util.List;

public class EmailDTO {
	String email;
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	List<ProductDetailsDTO> products;

	public List<ProductDetailsDTO> getProducts() {
		return products;
	}

	public void setProducts(List<ProductDetailsDTO> products) {
		this.products = products;
	}
    
}