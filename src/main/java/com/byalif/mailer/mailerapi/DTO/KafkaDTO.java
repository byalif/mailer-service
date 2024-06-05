package com.byalif.mailer.mailerapi.DTO;

public class KafkaDTO {
	EmailDTO emailDto;
	QuestionDTO questionDTO;
	public EmailDTO getEmailDto() {
		return emailDto;
	}
	public void setEmailDto(EmailDTO emailDto) {
		this.emailDto = emailDto;
	}
	public QuestionDTO getQuestionDTO() {
		return questionDTO;
	}
	public void setQuestionDTO(QuestionDTO questionDTO) {
		this.questionDTO = questionDTO;
	}
}
