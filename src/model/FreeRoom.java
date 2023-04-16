package model;

public class FreeRoom extends Room{

    public FreeRoom(String roomNumber, RoomType roomType, boolean isRoomFree){
        super(roomNumber, roomType, isRoomFree, 0);
    }

    @Override
    public String toString(){
        return super.toString();
    }
}
