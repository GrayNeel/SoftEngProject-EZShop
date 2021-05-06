package it.polito.ezshop.classes;

import it.polito.ezshop.data.Order;

public class OrderClass implements Order {
	private Integer balanceId;
	private String productCode;
	private Double pricePerUnit;
	private Integer quantity;
	private String status;
	private Integer orderId;
	
	public OrderClass (Integer balanceId, String productCode, Double pricePerUnit, Integer quantity, String status, Integer orderId) {
		this.balanceId = balanceId;
		this.productCode = productCode;
		this.pricePerUnit = pricePerUnit;
		this.quantity = quantity;
		this.status = status;
		this.orderId = orderId;			
	}
	
	@Override
	public Integer getBalanceId() {		
		return balanceId;
	}

	@Override
	public void setBalanceId(Integer balanceId) {
		this.balanceId= balanceId;
	}

	@Override
	public String getProductCode() {		
		return productCode;
	}

	@Override
	public void setProductCode(String productCode) {
		this.productCode = productCode;		
	}

	@Override
	public double getPricePerUnit() {		
		return pricePerUnit;
	}

	@Override
	public void setPricePerUnit(double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;		
	}

	@Override
	public int getQuantity() {		
		return quantity;
	}

	@Override
	public void setQuantity(int quantity) {		
		this.quantity = quantity;
	}

	@Override
	public String getStatus() {	
		return status;
	}

	@Override
	public void setStatus(String status) {
		this.status = status;		
	}

	@Override
	public Integer getOrderId() {		
		return orderId;
	}

	@Override
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;		
	}
}
