package model;

public class FreeRoom extends Room{

    private int price;
    public FreeRoom(){
        super();
        this.price = 0;
    }

    @Override
    public String toString(){
        return super.toString() + "price: "+ this.price;
    }
}
