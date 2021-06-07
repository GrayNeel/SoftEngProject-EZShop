package it.polito.ezshop.classes;

public class ProductClass {
	String RFID;
	Integer id;
	
	public ProductClass(Integer id, String RFID) {
		this.id = id;
		this.RFID = RFID;
	}
	
	public String getRFID() {
		return RFID;
	}
	public void setRFID(String rFID) {
		RFID = rFID;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
}
