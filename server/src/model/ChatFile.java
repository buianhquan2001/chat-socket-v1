package model;

public class ChatFile {
    FilePart filePart;
    private Integer id;
    private long msgTime;
    private Integer chatType;
    private int userId;
    private String userName;
    private Integer toRoomId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public FilePart getFilePart() {
        return filePart;
    }

    public void setFilePart(FilePart filePart) {
        this.filePart = filePart;
    }

    public long getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(long msgTime) {
        this.msgTime = msgTime;
    }

    public Integer getChatType() {
        return chatType;
    }

    public void setChatType(Integer chatType) {
        this.chatType = chatType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getToRoomId() {
        return toRoomId;
    }

    public void setToRoomId(Integer toRoomId) {
        this.toRoomId = toRoomId;
    }
}
