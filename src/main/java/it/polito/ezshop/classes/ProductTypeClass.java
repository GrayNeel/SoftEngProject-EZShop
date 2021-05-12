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
	
	public static boolean validateProductCode(String productCode) {
		Integer barCodeLength = productCode.length();
		
		//Verifying that the string is a number
    	try {
    		Double.parseDouble(productCode);
    	}catch(NumberFormatException e) {
    		return false;    		
    	}
    	
    	//Only GTIN-8, GTIN-12, GTIN-13, GTIN-14, GSIN and SSCC are allowed 
    	if(barCodeLength != 8 && barCodeLength != 12 && barCodeLength != 13 && barCodeLength != 14 && barCodeLength != 17 && barCodeLength != 18) {
    		return false;
    	}
    	
    	//Check digit to be verified (last number of the barcode)
    	Integer checkDigitToBeVerified = Character.getNumericValue(productCode.charAt(barCodeLength-1));
    	
    	//Calculation of the "Check digit"
    	Integer accumulator = 0;
    	for(Integer i=0; i<barCodeLength-1;i++) {
    		int n = Character.getNumericValue(productCode.charAt(i));
    		
    		if((i%2) == 0) {
    			//multiply by 1
    			accumulator+=n;
    		}else {
    			//multiply by 3
    			accumulator+=n*3;
    		}
    	}
    	
    	Integer checkDigitCalculated = 0;
    	while(accumulator%10 != 0) {
    		accumulator++;
    		checkDigitCalculated++;
    	}
    	
    	//If the calculated check digit does not correspond to the one to be verified, it is invalid
    	if(checkDigitCalculated != checkDigitToBeVerified)
    		return false;
    	
		return true;
	}
}
