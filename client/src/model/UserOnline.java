package model;

import java.io.DataOutputStream;

public class UserOnline {
    private DataOutputStream dos;
    private boolean online;

    public UserOnline() {
    }

    public UserOnline(boolean online) {
        this.online = online;
    }

    public DataOutputStream getDos() {
        return dos;
    }

    public void setDos(DataOutputStream dos) {
        this.dos = dos;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}
