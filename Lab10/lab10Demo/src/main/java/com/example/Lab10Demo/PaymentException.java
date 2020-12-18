package com.example.Lab10Demo;

@SuppressWarnings("serial")
public class PaymentException extends RuntimeException {
	public static enum PaymentErrors {
		USER_NOT_FOUND,
		BAD_CREDENTIALS,
		USER_HAS_NO_ACCOUNT_FOR_CURRENCY,
		ACCOUNT_HAS_NOT_ENOUGH_AMOUNT_FOR_PAYMENT,
		PAYMENT_COULD_NOT_BE_PROCESSED
	}
	
	private PaymentErrors error;
	
	private PaymentException(PaymentErrors error) {
		this.error = error;
	}
	
	public PaymentErrors getError() {
		return error;
	}
	
	public static PaymentException userNotFound() {
		return new PaymentException(PaymentErrors.USER_NOT_FOUND);
	}
	
	public static PaymentException badCredentials() {
		return new PaymentException(PaymentErrors.BAD_CREDENTIALS);
	}
	
	public static PaymentException userHasNoAccountForCurrency() {
		return new PaymentException(PaymentErrors.USER_HAS_NO_ACCOUNT_FOR_CURRENCY);
	}
	
	public static PaymentException accountHasNotEnoughAmountForPayment() {
		return new PaymentException(PaymentErrors.ACCOUNT_HAS_NOT_ENOUGH_AMOUNT_FOR_PAYMENT);
	}
	
	public static PaymentException paymentCouldNotBeProcessed() {
		return new PaymentException(PaymentErrors.PAYMENT_COULD_NOT_BE_PROCESSED);
	}
}
