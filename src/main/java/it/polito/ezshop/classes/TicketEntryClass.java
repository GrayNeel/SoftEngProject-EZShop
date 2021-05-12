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
		// TODO Auto-generated method stub
		return this.barCode;
	}

	@Override
	public void setBarCode(String barCode) {
		// TODO Auto-generated method stub
		this.barCode = barCode;
		
	}

	@Override
	public String getProductDescription() {
		// TODO Auto-generated method stub
		return this.productDescription;
	}

	@Override
	public void setProductDescription(String productDescription) {
		// TODO Auto-generated method stub
		this.productDescription = productDescription;
		
	}

	@Override
	public int getAmount() {
		// TODO Auto-generated method stub
		return this.amount;
	}

	@Override
	public void setAmount(int amount) {
		// TODO Auto-generated method stub
		this.amount = amount;
	}

	@Override
	public double getPricePerUnit() {
		// TODO Auto-generated method stub
		return this.pricePerUnit;
	}

	@Override
	public void setPricePerUnit(double pricePerUnit) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return this.discountRate;
	}

	@Override
	public void setDiscountRate(double discountRate) {
		// TODO Auto-generated method stub
		this.discountRate = discountRate;
	}

}
