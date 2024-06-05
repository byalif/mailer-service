package com.byalif.mailer.mailerapi.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Options {
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOption_text() {
		return option_text;
	}
	public void setOption_text(String option_text) {
		this.option_text = option_text;
	}
	public Question getQuestion() {
		return question;
	}
	public void setQuestion(Question question) {
		this.question = question;
	}
	public int id;
	public String option_text;
	public Options(int id, String option_text) {
		super();
		this.id = id;
		this.option_text = option_text;
	}
	public Question question;
	
}
