package it.polito.ezshop.classes;

import java.util.ArrayList;
import java.util.List;

import it.polito.ezshop.data.ProductType;

public class ReturnTransactionClass {
	private Integer id;
	private Integer quantity;
	private Integer transactionId;
	private String state;
	private List<ProductType> productTypeList = new ArrayList<>();
	private Double returnValue;
	
	public ReturnTransactionClass (Integer id, Integer transactionId, Integer quantity, double returnValue, String state) {
		this.id = id;
		this.transactionId = transactionId;
		this.quantity = quantity;
		this.returnValue = returnValue;
		this.state = state;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
	}

	public double getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(double returnValue) {
		this.returnValue = returnValue;
	}
	
	public List<ProductType> getProductTypeList() {
		return productTypeList;
	}

	public void setProductTypeList(List<ProductType> productTypeList) {
		this.productTypeList = productTypeList;
	}

}
