package it.polito.ezshop.classes;

import java.util.ArrayList;
import java.util.List;

import it.polito.ezshop.data.ProductType;

public class ReturnTransactionClass {
	private Integer id;
	private Integer quantity;
	private Integer transactionId;
	private List<ProductType> productTypeList = new ArrayList<>();
	private double returnedValue;
	
	public ReturnTransactionClass (Integer id, Integer transactionId, Integer quantity, double returnedValue) {
		this.id = id;
		this.transactionId = transactionId;
		this.quantity = quantity;
		this.returnedValue = returnedValue;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public double getReturnedValue() {
		return returnedValue;
	}

	public void setReturnedValue(double returnedValue) {
		this.returnedValue = returnedValue;
	}
	
	public List<ProductType> getProductTypeList() {
		return productTypeList;
	}

	public void setProductTypeList(List<ProductType> productTypeList) {
		this.productTypeList = productTypeList;
	}

}
