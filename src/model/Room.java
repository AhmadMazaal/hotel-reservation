package model;

public class Room implements IRoom{

    private RoomType roomType;
    private String roomNumber;
    private boolean isRoomFree;
    private double price;

    public Room(String roomNumber, RoomType roomType, boolean isRoomFree, double price){
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.isRoomFree = isRoomFree;
        this.price = price;
    }

    @Override
    public RoomType getRoomType() {
        return this.roomType;
    }

    @Override
    public String getRoomNumber() {
        return this.roomNumber;
    }

    @Override
    public boolean isFree() {
        return this.isRoomFree;
    }

    @Override
    public double getRoomPrice() {
        return this.price;
    }
}
