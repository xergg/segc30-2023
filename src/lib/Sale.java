package lib;

import java.io.Serializable;

public class Sale implements Serializable {

    private int quantity;
    private double value;
    private String sellerID;

    public Sale (String sellerID , int quantity , double value2){
        this.sellerID = sellerID;
        this.quantity = quantity;
        this.value = value2;
    }

    public double getValue(){
        return value;
    }

    public int getQuantity(){
        return quantity;
    }
    
    public String getSeller(){
        return sellerID;
    }

    public String toString() {
        return "Seller: " + sellerID + " | Quantity: " + quantity + " | Value: " + value;
    }

	public void setStock(int quantity, double value2) {
		this.quantity += quantity;
		this.value += value2;
	}

}