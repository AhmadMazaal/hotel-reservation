package model;

public interface IRoom {
    public RoomType getRoomType();
    public String getRoomNumber();
    public boolean isFree();
    public double getRoomPrice();
}
