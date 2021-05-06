package it.polito.ezshop.classes;

import it.polito.ezshop.data.ProductType;

public class ProductTypeClass implements ProductType {
	private Integer id;
	private Integer quantity;
	private String location;
	private String note;
	private String productDescription;
	private String barCode;
	private Double pricePerUnit;
	
	public ProductTypeClass (Integer id, Integer quantity, String location, String note, String productDescription, String barCode, Double pricePerUnit) {
		this.id = id;
		this.quantity = quantity;
		this.location = location;
		this.note = note;
		this.productDescription = productDescription;
		this.barCode = barCode;
		this.pricePerUnit = pricePerUnit;		
	}

	@Override
	public Integer getQuantity() { 
		return quantity; 
	}

	@Override
	public void setQuantity(Integer quantity) {		
		this.quantity = quantity;
	}

	@Override
	public String getLocation() {		
		return location;
	}

	@Override
	public void setLocation(String location) {
		this.location = location;
		
	}

	@Override
	public String getNote() {		
		return note;
	}

	@Override
	public void setNote(String note) {
		this.note = note;		
	}

	@Override
	public String getProductDescription() {		
		return productDescription;
	}

	@Override
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;		
	}

	@Override
	public String getBarCode() {
		return barCode;
	}

	@Override
	public void setBarCode(String barCode) {
		this.barCode = barCode;		
	}

	@Override
	public Double getPricePerUnit() {		
		return pricePerUnit;
	}

	@Override
	public void setPricePerUnit(Double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;		
	}

	@Override
	public Integer getId() {		
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;		
	}
}
