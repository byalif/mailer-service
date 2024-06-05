package com.byalif.mailer.mailerapi.entity;

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
public class Inquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
	@Column(name = "created_at", nullable = false)
	private Date createdAt;
	
	@PrePersist
	protected void onCreate() {
	        createdAt = new Date();
	 }
    
    

	@JsonManagedReference
	@OneToMany(mappedBy = "inquiry", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<ClientResults> clientResults;
	
	public Inquiry() {};
	
	private String email;
    
    
	public Date getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public Inquiry(List<ClientResults> results, String email) {
		this.clientResults = results;
		this.email = email;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public List<ClientResults> getClientResults() {
		return clientResults;
	}


	public void setClientResults(List<ClientResults> clientResults) {
		this.clientResults = clientResults;
	}

}
