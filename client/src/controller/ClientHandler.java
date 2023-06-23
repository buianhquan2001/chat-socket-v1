/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.gson.Gson;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import javax.swing.JOptionPane;
import model.Chat;
import model.ChatFile;
import model.FilePart;
import model.Friend;
import model.Login;
import model.RawData;
import model.Room;
import model.SignUp;
import model.UserIN;
import resources.Resources;
import view.RegisterForm;

/**
 *
 * @author Quan
 */
public class ClientHandler extends Observable {

    public static final String ip = "localhost";
    public static final int port = 9996;
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    public Gson gson;
    public String name;

    //Constructor chi de test, khong GUI(dung trong ham main)
    public ClientHandler() {
        try {
            this.socket = new Socket(ip, port);
            this.dos = new DataOutputStream(socket.getOutputStream());
            this.dis = new DataInputStream(socket.getInputStream());
            this.gson = new Gson();
            // Tao luong nhan du lieu rieng
            readThread();
        } catch (IOException ex) {
            exitConnect();
        }
    }

    public ClientHandler(Observer obs) {
        try {
            this.addObserver(obs);
            this.socket = new Socket(ip, port);
            this.dos = new DataOutputStream(socket.getOutputStream());
            this.dis = new DataInputStream(socket.getInputStream());
            this.gson = new Gson();
            // Tao luong nhan du lieu rieng
            readThread();
        } catch (IOException ex) {
            exitConnect();
        }
    }

    public void sendData(RawData rawData) {
        try {
            String json = gson.toJson(rawData);
            dos.writeUTF(json);
            System.out.println(this + " Send Data: " + rawData);
        } catch (IOException ex) {
            exitConnect();
        }
    }

    public RawData receiveData() {
        RawData rawData = null;
        try {
            String json = dis.readUTF();
            rawData = gson.fromJson(json, RawData.class);
            System.out.println(this + " Receive Data: " + rawData);
        } catch (IOException ex) {
            exitConnect();
        } finally {
            return rawData;
        }
    }

    //----Cac ham gui request len server----
    public void login(Login login) {
        this.name = login.getUsername();
        //Login: username + password
        RawData data = new RawData(Resources.LOGIN_REQUEST, gson.toJson(login));
        sendData(data);
    }

    public void register(SignUp signUp) {
        //SignUp: name + username + password
        RawData data = new RawData(Resources.SIGNUP_REQUEST, gson.toJson(signUp));
        sendData(data);
    }

    public void findUser(String username) {
        RawData data = new RawData(Resources.FIND_USER_REQUEST, gson.toJson(username));
        sendData(data);
    }

    public void findInvitation() {
        RawData data = new RawData(Resources.FIND_USER_REQUEST, null);
        sendData(data);
    }

    public void addFriend(Friend friend) {
        //Friend:  (Status + userId + userName)
        RawData data = new RawData(Resources.ADD_FRIEND_REQUEST, gson.toJson(friend));
        sendData(data);
    }

    public void deleteFriend(Friend friend) {
        //Friend:  (Status + userId + userName)
        RawData data = new RawData(Resources.DEL_FRIEND_REQUEST, gson.toJson(friend));
        sendData(data);
    }

    public void createRoom(Room room) {
        //Room: name + listUsers class userin
        RawData data = new RawData(Resources.CREATE_ROOM_REQUEST, gson.toJson(room));
        sendData(data);
    }

    public void deleteRoom(Integer id) {
        //Integer (ID phòng)
        RawData data = new RawData(Resources.DELETE_ROOM_REQUEST, gson.toJson(id));
        sendData(data);
    }

    public void getRoomInfo(Room room) {
        RawData data = new RawData(Resources.GET_ROOM_INFO_REQUEST, gson.toJson(room));
        sendData(data);
    }

    public void addFriendToRoom(Room room) {
        //Room (id + listUsers thêm) 
        RawData data = new RawData(Resources.ADD_USER_REQUEST, gson.toJson(room));
        sendData(data);
    }

    public void deleteFriendInRoom(Room room) {
        //Room (id + listUsers xoá) 
        RawData data = new RawData(Resources.DELETE_USER_REQUEST, gson.toJson(room));
        sendData(data);
    }

    public void getOlderChat(Chat chat) {
        RawData data = new RawData(Resources.GET_OLDER_CHAT_REQUEST, gson.toJson(chat));
        sendData(data);
    }

    public void getNewerChat(Chat chat) {
        RawData data = new RawData(Resources.GET_NEWER_CHAT_REQUEST, gson.toJson(chat));
        sendData(data);
    }

    public void chatToRoom(Chat chat) {
        //Chat (msg + chatType = TEXT + toRoomId)
        RawData data = new RawData(Resources.CHAT_MSG_ROOM_REQUEST, gson.toJson(chat));
        sendData(data);
    }

    public void getInvitation() {
        RawData data = new RawData(Resources.FIND_INVITE_REQUEST, null);
        sendData(data);
    }

    public void chatWithFile(File file, Integer id) {
        try {
            RawData rd = new RawData();
            rd.setCode(Resources.CHAT_FILE_ROOM_REQUEST);
            FileInputStream fis = new FileInputStream(file);
            Long fileSize = file.length();
            ChatFile chatFile = new ChatFile();
            chatFile.setChatType(1);
            chatFile.setToRoomId(id);
            FilePart filePart = new FilePart();
            filePart.setName(file.getName());
            filePart.setFileSize(file.length());
            Integer partIndex = 1;
            Integer partSize = 10000;
            if (partSize > fileSize) {
                partSize = (int) (long) fileSize;
            }
            Integer partCount = 1;
            if (fileSize % partSize != 0) {
                partCount = (int) (fileSize / partSize + 1);
            } else {
                partCount = (int) (fileSize / partSize);
            }
            filePart.setPartCount(partCount);
            Long totalSend = 0L;
            Integer percent = 0;
            byte[] data = new byte[partSize];
            while ((fis.read(data)) != -1) {
                filePart.setPartIndex(partIndex);
                filePart.setPartSize(partSize);
                totalSend += partSize;
                percent += (int) (totalSend / file.length());
                filePart.setData(data);
                chatFile.setFilePart(filePart);
                rd.setData(gson.toJson(chatFile));
                System.out.println(gson.toJson(rd));
                dos.writeUTF(gson.toJson(rd));
                if ((long) fileSize - totalSend < partSize) {
                    partSize = (int) (fileSize - totalSend);
                }
                System.out.print(partIndex + " " + (totalSend * 100 / fileSize) + " " + totalSend + " ");
                System.out.println(fileSize);
                partIndex++;
            }
            System.out.println("done!");
            fis.close();
        } catch (IOException e) {

        }
    }

    public void downloadFile(Integer chatId) {
        RawData data = new RawData(Resources.DOWNLOAD_FILE, gson.toJson(chatId));
        sendData(data);
    }

    //Luong doc du lieu
    public void readThread() {
        new Thread() {
            public void run() {
                while (true) {
                    //Doc du lieu
                    RawData receiveData = receiveData();
                    notifyObservers(receiveData);
                }
            }
        }.start();
    }

    @Override
    public void notifyObservers(Object arg) {
        super.setChanged();
        super.notifyObservers(arg);
    }

    public void exitConnect() {
        try {
            if (socket != null || !socket.isClosed()) {
                socket.close();
            }
            if (dos != null) {
                dos.close();
            }
            if (dis != null) {
                dis.close();
            }
        } catch (IOException ex) {
            System.out.println("Error!!!!");
        }
    }

    //Ham test gui, nhan k GUI
    public static void main(String[] args) throws IOException {
        ClientHandler clientHandler2 = new ClientHandler();
        ClientHandler clientHandler = new ClientHandler();
        ClientHandler clientHandler3 = new ClientHandler();
        clientHandler2.login(new Login("tester2", "123123"));
        clientHandler.login(new Login("tester", "123123"));
        clientHandler3.login(new Login("tester3", "123123"));

        Scanner sc = new Scanner(System.in);

        while (true) {
            int action = sc.nextInt();
            System.out.println("Action: " + action);
            Room room = null;
            List<UserIN> listUserIN = new ArrayList<>();
            UserIN userIN = null;
            Chat chat = null;
            switch (action) {
                case 1:  //Tao room
                    room = new Room();
                    room.setName("Quan");
                    clientHandler.createRoom(room);
                    break;
                case 2: // Add user
                    //Room (id + listUsers thêm) 
                    room = new Room();
                    room.setId(344);

                    userIN = new UserIN();
                    userIN.setId(2);

                    listUserIN.add(userIN);

                    room.setListUsers(listUserIN);

                    clientHandler3.addFriendToRoom(room);
                    break;
                case 3: //Xoa nguoi trong room
                    //Room (id + listUsers xoá) 
                    room = new Room();
                    room.setId(344);

                    userIN = new UserIN();
                    userIN.setId(2);

                    listUserIN.add(userIN);

                    room.setListUsers(listUserIN);

                    clientHandler.deleteFriendInRoom(room);

                    break;
                case 4: //Xem tin nhan room,
                    //Room (id + pageIndex) 
                    chat = new Chat();
                    chat.setId(-1);
                    chat.setToRoomId(345);

//                    clientHandler.getChat(chat);
                    break;
                case 5: //Chat Text Nhom
                    //Chat (msg + chatType + toRoomId)
                    chat = new Chat();
                    chat.setMsg("Hello Ban");
                    // 0 -> TEXT, 1 -> FILE;
                    chat.setChatType(0);
                    chat.setToRoomId(344);

                    clientHandler2.chatToRoom(chat);
                    break;
                case 6: //Chat text 1-1
                    //Chat (msg + chatType + toRoomId)
                    chat = new Chat();
                    chat.setMsg("Hello Ban 1");
                    // 0 -> TEXT, 1 -> FILE;
                    chat.setChatType(0);
                    chat.setToRoomId(-1);
                    chat.setUserId(1);
                    clientHandler2.chatToRoom(chat);
                    break;
                case 7: // Lấy info phòng
                    room = new Room();
                    room.setId(344);
                    clientHandler2.getRoomInfo(room);
            }
        }

    }

    public String toString() {
        return this.name;
    }

}
