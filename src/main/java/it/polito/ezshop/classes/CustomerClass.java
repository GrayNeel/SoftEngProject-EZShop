package it.polito.ezshop.classes;

import it.polito.ezshop.data.Customer;

public class CustomerClass implements Customer {
	private Integer id;
	private String customerName;
	private String customerCard;
	private Integer points;
	
	public CustomerClass (Integer id, String customerName, String customerCard, Integer points) {
		this.id = id;
		this.customerName = customerName;
		this.customerCard = customerCard;
		this.points = points;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerCard() {
		return customerCard;
	}

	public void setCustomerCard(String customerCard) {
		this.customerCard = customerCard;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}
	

}
