package it.polito.ezshop.classes;

import java.util.ArrayList;
import java.util.List;

import it.polito.ezshop.data.SaleTransaction;
import it.polito.ezshop.data.TicketEntry;
import it.polito.ezshop.data.User;

public class SaleTransactionClass implements SaleTransaction {
	private Integer transactionId;
	private String date;
	private String time;
	private double price;
	private double discountRate;
	private String paymentType;
	private List<TicketEntry> ticketList = new ArrayList<>();
	private String state;
	
	public SaleTransactionClass (Integer transactionId, String date, String time, 
			double price, String paymentType, double discountRate, List<TicketEntry> ticketList,
			String state) {
		this.transactionId = transactionId;
		this.date = date;
		this.time = time;
		this.price = price;
		this.paymentType = paymentType;
		this.discountRate = discountRate;
		this.ticketList = ticketList;
		this.state = state;
	}
	
	@Override
	public Integer getTicketNumber() {
		return transactionId;
	}

	@Override
	public void setTicketNumber(Integer transactionId) {
		this.transactionId = transactionId;
		
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}

	public void setDiscountRate(double discountRate) {
		this.discountRate = discountRate;
	}
	
	public double getDiscountRate() {
		return discountRate;
	}

	@Override
	public List<TicketEntry> getEntries() {
		return ticketList;
	}

	@Override
	public void setEntries(List<TicketEntry> entries) {
		this.ticketList = entries;
		
	}
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}


}
