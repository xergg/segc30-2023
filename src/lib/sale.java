package lib;

public class sale {

    private int quantity;
    private int value;
    private String sellerID;

    public sale (String sellerID , int quantity , int value){
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

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public void setValue(int value){
        this.value=value;
    }
    
    public String getSeller(){
        return sellerID;
    }

}
