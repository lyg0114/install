package com.install.global.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {

	private final CustomErrorCode errorCode;
	private final String errorMessage;

	public ErrorResponse(CustomException e) {
		this.errorCode = e.getErrorCode();
		this.errorMessage = e.getErrorMessage();
	}

	public ErrorResponse(CustomErrorCode errorCode) {
		this.errorCode = errorCode;
		this.errorMessage = errorCode.getErrorMessage();
	}
}
