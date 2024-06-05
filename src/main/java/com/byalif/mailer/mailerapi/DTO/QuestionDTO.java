package com.byalif.mailer.mailerapi.DTO;

import java.util.List;

public class QuestionDTO {
	List<Question> questions;
	String email;
	
	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
}
