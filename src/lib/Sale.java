package lib;

import java.io.Serializable;


/**
 * Sale class that implements Serializable. Has methods such as getters and setters that better help the buyer and the seller to make purchases and sells.
 */
public class Sale implements Serializable {

    private int quantity;
    private double value;
    private String sellerID;


    /**
     * Basic constructor that determines the seller's id, the quantity and a value.
     * @param sellerID selled id
     * @param quantity quantity to be sold
     * @param value2 value set
     */
    public Sale (String sellerID , int quantity , double value2){
        this.sellerID = sellerID;
        this.quantity = quantity;
        this.value = value2;
    }

    /**
     * Gets value of the product
     * @return value of product
     */
    public double getValue(){
        return value;
    }


    /**
     * Gets quantity of the product
     * @return quantity of product
     */
    public int getQuantity(){
        return quantity;
    }
    

    /**
     * Gets seller of the product
     * @return seller id
     */
    public String getSeller(){
        return sellerID;
    }


    
    /**
     * Returns all basic information from a sale, such as the seller id, the quantity and the value
     */
    public String toString() {
        return "Seller: " + sellerID + " | Quantity: " + quantity + " | Value: " + value;
    }

    /**
     * Sets new stock and new value to the product
     * @param quantity new quantity
     * @param value2 new product value
     */
	public void setStock(int quantity, double value2) {
        if (value2 == value){
            this.quantity += quantity;
        } else {
            this.value = value2;
            this.quantity = quantity;
        }
		
	}

    /**
     * Sets new quantity given a bought product
     * @param bought units bought from a given product
     */
    public void setQuantity(int bought){
        this.quantity = this.quantity - bought;
    }

}