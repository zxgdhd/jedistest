package com.cssiot.redis;

import java.io.Serializable;

public class Card implements Serializable{
	private String cardNumber;
	private String cardBank;
	private double balance;
	
	public Card() {
	}
	
	
	public Card(String cardNumber, String cardBank, double balance) {
		this.cardNumber = cardNumber;
		this.cardBank = cardBank;
		this.balance = balance;
	}


	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getCardBank() {
		return cardBank;
	}
	public void setCardBank(String cardBank) {
		this.cardBank = cardBank;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	
}
