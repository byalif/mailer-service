package com.byalif.mailer.mailerapi.DTO;

public class ResponseDTO {
	public ResponseDTO(String string) {
		this.message = string;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String message;
}
