package it.polito.ezshop.classes;

public class ProductReturnClass {
	private Integer id;
	private Integer returnId;
	private String productCode;
	private Integer quantity;
	private Double returnValue;
	
	public ProductReturnClass (Integer id, Integer returnId, String productCode,
			Integer quantity, Double returnValue) {
		this.id = id;
		this.returnId = returnId;
		this.productCode = productCode;
		this.quantity = quantity;
		this.returnValue = returnValue;	
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getReturnId() {
		return returnId;
	}

	public void setReturnId(Integer returnId) {
		this.returnId = returnId;
	}
	
	public String getProductCode() {
		return this.productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
		
	}

	public int getQuantity() {
		return this.quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getReturnValue() {
		return this.returnValue;
	}

	public void setReturnValue(double returnValue) {
		this.returnValue = returnValue;
	}

}
