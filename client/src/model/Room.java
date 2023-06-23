package model;

import java.util.List;

public class Room {
    private Integer id;
    private String name;
    private Integer adminId;
    private Integer friendId;
    private List<UserIN> listUsers;
    private List<Chat> listChats;
    private boolean isGroup;

    public boolean isGroup() {
        return friendId == -1;
    }

    public void setGroup(boolean isGroup) {
            this.isGroup = isGroup;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Integer getFriendId() {
		return friendId;
	}

	public void setFriendId(Integer friendId) {
		if (friendId == -1) {
			this.isGroup = true;
		}
		this.friendId = friendId;
	}

	public List<UserIN> getListUsers() {
        return listUsers;
    }

    public void setListUsers(List<UserIN> listUsers) {
        this.listUsers = listUsers;
    }

    public List<Chat> getListChats() {
        return listChats;
    }

    public void setListChats(List<Chat> listChats) {
        this.listChats = listChats;
    }

    public boolean isIsGroup() {
        return isGroup;
    }

    public void setIsGroup(boolean isGroup) {
        this.isGroup = isGroup;
    }
    public Object[] toObject() {
        return new Object[]{this.name};
    }
}
