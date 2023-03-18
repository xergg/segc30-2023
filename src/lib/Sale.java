package lib;

public class Sale {

    private int quantity;
    private int value;
    private String sellerID;

    public Sale (String sellerID , int quantity , int value){
        this.sellerID = sellerID;
        this.quantity = quantity;
        this.value = value;
    }

    public int getValue(){
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

	public void setStock(int quantity, int value) {
		this.quantity += quantity;
		this.value += value;
	}

}