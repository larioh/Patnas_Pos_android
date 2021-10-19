package com.example.mobilebanking.myactivities;

public class P25ConnectionException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String error = "";
	
	public P25ConnectionException(String msg) {
		super(msg);
		
		error = msg;
	}
	
	public String getError() {
		return error;
	}

}