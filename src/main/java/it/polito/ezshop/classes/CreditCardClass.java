package it.polito.ezshop.classes;

public class CreditCardClass {
	private String cardNumber;
	private Double balance;
	
	public CreditCardClass (String cardNumber, Double balance) {
		this.cardNumber = cardNumber;
		this.balance = balance;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public static boolean checkValidCard(String cardNumber) {
		int digits = cardNumber.length();
		 
	    int sum = 0;
	    boolean isSecond = false;
	    for(int i = digits - 1; i >= 0; i--)
	    {
	        int digit = cardNumber.charAt(i) - '0';
	        if (isSecond == true)
	            digit = digit * 2;
	        sum += digit / 10;
	        sum += digit % 10;
	 
	        isSecond = !isSecond;
	    }
	    return (sum % 10 == 0);
	}
}
