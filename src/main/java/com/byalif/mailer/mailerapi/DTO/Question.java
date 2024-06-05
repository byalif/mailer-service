package com.byalif.mailer.mailerapi.DTO;

import java.util.*;


public class Question {

	public int id;
	
	public String question;
	
	public String answer;
	
	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public boolean customText;

	public List<Options> options;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public boolean isCustomText() {
		return customText;
	}

	public void setCustomText(boolean customText) {
		this.customText = customText;
	}

	public List<Options> getOptions() {
		return options;
	}

	public void setOptions(List<Options> options) {
		this.options = options;
	}
	
}
