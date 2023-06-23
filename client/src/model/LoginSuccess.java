package model;

import java.util.List;

public class LoginSuccess {
    private UserIN user;
    private List<Friend> listFriends;
    private List<Room> listRooms;

    public List<Room> getListRooms() {
        return listRooms;
    }

    public LoginSuccess(UserIN user, List<Friend> listFriends, List<Room> listRooms) {
        this.user = user;
        this.listFriends = listFriends;
        this.listRooms = listRooms;
    }

    public void setListRooms(List<Room> listRooms) {
        this.listRooms = listRooms;
    }

    public UserIN getUser() {
        return user;
    }

    public void setUser(UserIN user) {
        this.user = user;
    }

    public List<Friend> getListFriends() {
        return listFriends;
    }

    public void setListFriends(List<Friend> listFriends) {
        this.listFriends = listFriends;
    }
}
