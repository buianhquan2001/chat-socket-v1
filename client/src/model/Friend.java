package model;

public class Friend {
    private Integer status;
    private Integer userId;
    private String userName;

    public Friend() {
    }

    public Friend(Integer status, Integer userId, String userName) {
        this.status = status;
        this.userId = userId;
        this.userName = userName;
    }

    public Friend(Integer status, Integer userId) {
        this.status = status;
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "status=" + status +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                '}';
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public Object[] toFriendInvitationObject() {
        return new Object[]{this.userName};
    }
    
    public Object[] toObject() {
        if (this.status == 1) {
            return new Object[]{this.userName, "x"};
        }
        return new Object[]{this.userName, "o"};
    }
    
    public Object[] toFriendManagerObject() {
        if (this.status.equals(-1)) {
            return new Object[]{this.userName, "Chưa kết bạn"};
        } else if (this.status.equals(0)) {
            return new Object[]{this.userName, "Đang đợi kết bạn"};
        } else if (this.status.equals(1)){
            return new Object[]{this.userName, "Bạn bè"};
        } else if (this.status.equals(2)) {
            return new Object[]{this.userName, "Đợi bạn chấp nhận kết bạn"};
        } else {
            return new Object[]{this.userName, "Đã gửi yêu cầu kết bạn"};
        }
    }
}
