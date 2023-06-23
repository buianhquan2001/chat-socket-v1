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
}
