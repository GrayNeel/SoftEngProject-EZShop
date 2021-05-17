package it.polito.ezshop.classes;

import it.polito.ezshop.data.TicketEntry;

public class TicketEntryClass implements TicketEntry {
	private Integer id;
	private String barCode;
	private String productDescription;
	private Integer amount;
	private Double pricePerUnit;
	private Integer transactionId;
	private Double discountRate;
	
	public TicketEntryClass (Integer id, String barCode, String productDescription,
			Integer amount, Double pricePerUnit, Integer transactionId, Double discountRate) {
		this.id = id;
		this.barCode = barCode;
		this.productDescription = productDescription;
		this.amount = amount;
		this.pricePerUnit = pricePerUnit;
		this.transactionId = transactionId;
		this.discountRate = discountRate;		
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String getBarCode() {
		
		return this.barCode;
	}

	@Override
	public void setBarCode(String barCode) {
		
		this.barCode = barCode;
		
	}

	@Override
	public String getProductDescription() {
		
		return this.productDescription;
	}

	@Override
	public void setProductDescription(String productDescription) {
		
		this.productDescription = productDescription;
		
	}

	@Override
	public int getAmount() {
		
		return this.amount;
	}

	@Override
	public void setAmount(int amount) {
		
		this.amount = amount;
	}

	@Override
	public double getPricePerUnit() {
	
		return this.pricePerUnit;
	}

	@Override
	public void setPricePerUnit(double pricePerUnit) {
		
		this.pricePerUnit = pricePerUnit;
	}
	
	public Integer getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
	}


	@Override
	public double getDiscountRate() {
		
		return this.discountRate;
	}

	@Override
	public void setDiscountRate(double discountRate) {
		
		this.discountRate = discountRate;
	}

}
