package main;

import com.google.gson.Gson;
import model.*;
import repository.*;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequestHandler extends Thread {
    private final DataInputStream dis;
    private final DataOutputStream dos;
    private final Map<Integer, UserOnline> userOnline;
    private Integer clientId;
    private String clientName;
    private ByteArrayOutputStream bos;

    public RequestHandler(DataInputStream dis, DataOutputStream dos, Map<Integer, UserOnline> userOnline) {
        super();
        clientId = -1;
        this.dis = dis;
        this.dos = dos;
        this.userOnline = userOnline;
    }

    @Override
    public void run() {
        Gson gson = new Gson();
        UserRepository userRepository = new UserRepository();
        FriendRepository friendRepository = new FriendRepository();
        RoomRepository roomRepository = new RoomRepository();
        UserInRepository userInRepository = new UserInRepository();
        ChatRepository chatRepository = new ChatRepository();
        boolean isLogin = true;
        Room r, r2;
        Chat chat;
        ChatFile chatFile;
        FilePart filePart;
        try {
            while (isLogin) {
                System.out.println("Login: " + dis.toString());
                // dos.writeUtf
                RawData rd = gson.fromJson(dis.readUTF(), RawData.class);
                System.out.println(rd.getCode() + " | " + rd.getData());
                switch (rd.getCode()) {
                    case "LOGIN":
                        Login login = gson.fromJson(rd.getData(), Login.class);
                        User user = userRepository.checkLogin(login);
                        if (user != null) {
                            clientId = user.getId();
                            clientName = user.getName();
                            userOnline.get(clientId).setDos(dos);
                            userOnline.get(clientId).setOnline(true);

                            List<Friend> friends = friendRepository.getListFriend(clientId);

                            LoginSuccess ok = new LoginSuccess(new UserIN(user.getId(), user.getName()), friends, roomRepository.getListRoom(clientId));
                            for (Friend friend : friends) {
                                if (!userOnline.get(friend.getUserId()).isOnline())
                                    friend.setStatus(0);
                            }
                            rd.setCode("LOGIN_SUCCESS");
                            rd.setData(gson.toJson(ok));
                            dos.writeUTF(gson.toJson(rd));

                            rd.setCode("ONLINE");
                            rd.setData(gson.toJson(clientId));
                            for (Friend friend : friends) {
                                System.out
                                        .println(friend.getUserId() + " " + userOnline.get(friend.getUserId()).isOnline());
                                if (userOnline.get(friend.getUserId()).isOnline())
                                    userOnline.get(friend.getUserId()).getDos().writeUTF(gson.toJson(rd));
                            }
                            isLogin = false;
                        } else {
                            rd.setCode("LOGIN_FAILED");
                            dos.writeUTF(gson.toJson(rd));
                        }
                        break;
                    case "SIGNUP":
                        SignUp signUp = gson.fromJson(rd.getData(), SignUp.class);
                        System.out.println(rd.getData());
                        if (userRepository.save(signUp))
                            rd.setCode("SIGN_UP_SUCCESS");
                        else
                            rd.setCode("SIGN_UP_FAILED");
                        dos.writeUTF(gson.toJson(rd));
                        break;
                    default:
                        break;
                }
            }

            while (true) {
                RawData rd = gson.fromJson(dis.readUTF(), RawData.class);
                System.out.println(rd.getCode());
                switch (rd.getCode()) {
                    // ThÃªm báº¡n / cháº¥p nháº­n
                    case "ADD_FRIEND":
                        Friend requestAddFriend = gson.fromJson(rd.getData(), Friend.class);
                        Integer checkAccept = friendRepository.canAccept(clientId, requestAddFriend.getUserId());
                        if (checkAccept == 1 || checkAccept == 3)
                            break;
                        Friend responseAddFriend = friendRepository.accept(clientId, requestAddFriend.getUserId(), requestAddFriend.getUserName(),
                                checkAccept);
                        rd.setData(gson.toJson(responseAddFriend));
                        dos.writeUTF(gson.toJson(rd));

                        responseAddFriend.setUserId(clientId);
                        responseAddFriend.setUserName(clientName);
                        rd.setCode("RECEIVE_ADD_FRIEND");
                        rd.setData(gson.toJson(responseAddFriend));
                        // Online
                        if (userOnline.get(requestAddFriend.getUserId()).isOnline()) {
                            userOnline.get(requestAddFriend.getUserId()).getDos().writeUTF(gson.toJson(rd));
                        } else {

                        }

                        // add new room with to chat private
                        r = roomRepository.withChatPrivate(clientId, requestAddFriend.getUserId());
                        if (r == null) {
                            System.out.println("Create Room Private");
                            r = roomRepository.create("", clientId, requestAddFriend.getUserId());
							// Gui them trang online/offline
                        }

                        break;
                    case "DEL_FRIEND":
                        Friend requestDelFriend = gson.fromJson(rd.getData(), Friend.class);
                        Integer checkDel = friendRepository.canAccept(clientId, requestDelFriend.getUserId());
                        if (checkDel == -1)
                            break;
                        Friend responseDelFriend = friendRepository.denied(clientId, requestDelFriend.getUserId());
                        if (checkDel == 1)
                            responseDelFriend.setStatus(-1);
                        else
                            responseDelFriend.setStatus(-2);
                        rd.setData(gson.toJson(responseDelFriend));
                        dos.writeUTF(gson.toJson(rd));

                        responseDelFriend.setUserId(clientId);
                        responseDelFriend.setUserName(clientName);
                        rd.setCode("RECEIVE_DEL_FRIEND");
                        rd.setData(gson.toJson(responseDelFriend));
                        // Online
                        if (userOnline.get(requestDelFriend.getUserId()).isOnline()) {
                            userOnline.get(requestDelFriend.getUserId()).getDos().writeUTF(gson.toJson(rd));
                        } else { // Offline

                        }

                        break;
                    case "FIND_USER":
                        String name = gson.fromJson(rd.getData(), String.class);
                        List<Friend> responseFindUser = userRepository.searchByName(clientId, name);
                        rd.setData(gson.toJson(responseFindUser));
                        dos.writeUTF(gson.toJson(rd));
                        break;
                    case "FIND_INVITE":
                        List<Friend> responseFriend = friendRepository.getListInvite(clientId);
                        rd.setData(gson.toJson(responseFriend));
                        dos.writeUTF(gson.toJson(rd));
                        break;

                    case "CREATE_ROOM":
                        r = gson.fromJson(rd.getData(), Room.class);
                        r = roomRepository.create(r.getName(), clientId, -1);
                        if (r != null) {
                            userInRepository.insert(r.getId(), clientId);
                            rd.setCode("CREATE_ROOM_SUCCESS");
                            rd.setData(gson.toJson(r));
                            dos.writeUTF(gson.toJson(rd));
                            System.out.println("Create room success");
                        } else {
                            System.out.println("Create room error");
                        }
                        break;
                    case "ADD_USER":
                        r = gson.fromJson(rd.getData(), Room.class);
                        r2 = roomRepository.withId(r.getId());
                        if (r2 != null && r.getListUsers().size() == 1) {
                            var userIn = r.getListUsers().get(0);
                            if (userInRepository.with(r.getId(), userIn.getId()) == null) {
                                userInRepository.insert(r2.getId(), userIn.getId());

                                rd.setCode("ADD_USER_SUCCESS");
                                dos.writeUTF(gson.toJson(rd));

                                if (userOnline.get(userIn.getId()).isOnline()) {
                                    rd.setCode("RECEIVE_ADD_USER");
                                    userOnline.get(userIn.getId()).getDos().writeUTF(gson.toJson(rd));
                                }
                                System.out.println("Add user success");
                            } else {
                                System.out.println("User is exist");
                            }
                        } else {
                            System.out.println("Not find room");
                        }
                        break;
                    case "DELETE_USER":
                        r = gson.fromJson(rd.getData(), Room.class);
                        r2 = roomRepository.withId(r.getId());
                        if (r2 != null) {
                            boolean flag = true;
                            if (r.getListUsers().size() == 1 && r.getListUsers().get(0).getId() == clientId) {
                                userInRepository.delete(r.getId(), r.getListUsers().get(0).getId());
                            } else if (r2.getAdminId() == clientId) {
                                userInRepository.delete(r.getId(), r.getListUsers().get(0).getId());
                            } else {
                                flag = false;
                                System.out.println("Delete User Error");
                            }
                            if (flag) {
                                rd.setCode("DELETE_USER_SUCCESS");
                                dos.writeUTF(gson.toJson(rd));
                                var arr = userInRepository.withRoomId(r.getId());
                                rd.setCode("RECEIVE_DELETE_USER");
                                for (var user : arr) {
                                    if (userOnline.get(user.getId()).isOnline()) {
                                        userOnline.get(user.getId()).getDos().writeUTF(gson.toJson(rd));
                                    }
                                }

                                if (userOnline.get(r.getListUsers().get(0).getId()).isOnline()) {
                                    rd.setCode("RECEIVE_DELETE_USER");
                                    userOnline.get(r.getListUsers().get(0).getId()).getDos().writeUTF(gson.toJson(rd));
                                }

                                System.out.println("Delete user success");
                            }
                        }
                        break;
                    case "CHAT_MSG":
                        chat = gson.fromJson(rd.getData(), Chat.class);
                        if (chat.getToRoomId() == -1) { // chat private
                            int toUserId = chat.getUserId();
                            r = roomRepository.withChatPrivate(clientId, toUserId);
                            if (r == null) {
                                System.out.println("Create Room Private");
                                r = roomRepository.create("", clientId, toUserId);
                            }
                            if (r != null) {
                                chat.setToRoomId(r.getId());
                                chat.setUserId(clientId);
                                chat.setUserName(clientName);

                                chatRepository.insert(chat);

                                rd.setCode("CHAT_MSG");
                                rd.setData(gson.toJson(chat));

                                dos.writeUTF(gson.toJson(rd));

                                if (userOnline.get(toUserId).isOnline()) {
                                    userOnline.get(toUserId).getDos().writeUTF(gson.toJson(rd));
                                }

                            } else {
                                System.out.println("Room private create false");
                            }
                        } else { // chat group
                            int roomId = chat.getToRoomId();
                            var room = roomRepository.withId(roomId);
                            if (room != null) {
                                var userIn = userInRepository.with(roomId, clientId);
                                if (userIn != null) {
                                    chat.setUserId(clientId);
                                    chat.setUserName(clientName);
                                    chatRepository.insert(chat);

                                    var arr = userInRepository.withRoomId(roomId);
                                    rd.setCode("CHAT_MSG");
                                    rd.setData(gson.toJson(chat));
                                    for (var user : arr) {
                                        if (userOnline.get(user.getId()).isOnline()) {
                                            userOnline.get(user.getId()).getDos().writeUTF(gson.toJson(rd));
                                        }
                                    }
                                    System.out.println("Chat group success");

                                } else {
                                    System.out.println("User not in room");
                                }
                            } else {
                                System.out.println("Room not found");
                            }
                        }
                        break;
                    case "GET_ROOM_INFO":
                        r = gson.fromJson(rd.getData(), Room.class);
                        Room room = null;
                        if (r.getFriendId() == null || r.getFriendId().equals(-1)) {
                            room = roomRepository.withId(r.getId());
                        } else {
                            room = roomRepository.withFriendId(clientId, r.getFriendId());
                        }
                        if (room != null) {
                            room.setListUsers(userInRepository.withRoomId(room.getId()));
                            rd.setCode("GET_ROOM_INFO");
                            rd.setData(gson.toJson(room));
                            dos.writeUTF(gson.toJson(rd));
                        } else {
                            System.out.println("Not find room id");
                        }
                        break;
                    case "GET_OLDER_CHAT":
                        chat = gson.fromJson(rd.getData(), Chat.class);
                        r = roomRepository.withId(chat.getToRoomId());
                        if (r != null) {
                            var arr = chatRepository.olderWithRoomId(r.getId(), chat.getId());
                            System.out.println("kq: " + arr);
                            rd.setCode("GET_CHAT");
                            rd.setData(gson.toJson(arr));
                            dos.writeUTF(gson.toJson(rd));
                        } else {
                            System.out.println("Room not exist");
                        }
                        break;
                    case "GET_NEWER_CHAT":
                        chat = gson.fromJson(rd.getData(), Chat.class);
                        r = roomRepository.withId(chat.getToRoomId());
                        if (r != null) {
                            var arr = chatRepository.newerWithRoomId(r.getId(), chat.getId());
                            rd.setCode("GET_CHAT");
                            rd.setData(gson.toJson(arr));
                            dos.writeUTF(gson.toJson(rd));
                        } else {
                            System.out.println("Room not exist");
                        }
                        break;
                    case "CHAT_FILE":
                        System.out.println(rd.getData());
                        chatFile = gson.fromJson(rd.getData(), ChatFile.class);
                        int roomId = chatFile.getToRoomId();
                        filePart = chatFile.getFilePart();
                        if (filePart.getPartIndex() == 1) {
                            System.out.println("start");
                            bos = new ByteArrayOutputStream();
                        }
                        bos.write(filePart.getData(), 0, filePart.getPartSize());
                        int index = filePart.getPartIndex();
                        int count = filePart.getPartCount();
                        if (index == count) {
                            System.out.println("ok");
                            byte[] fileFull = bos.toByteArray();
                            //Upload file to disk
                            File file = new File("D:\\1CaiServer\\" + clientId + "." +
                                    roomId + "." + filePart.getName());
                            System.out.println(file.getName());
                            FileOutputStream fos = new FileOutputStream(file);
                            fos.write(fileFull);
                            fos.close();
                            System.out.println("upload done" + filePart.getName());
                        }
                        break;
                    case "DOWN_FILE":
                        Integer chatId = gson.fromJson(rd.getData(), Integer.class);
                        Chat chatDownFile = chatRepository.findById(chatId, clientId);
                        if (chatDownFile != null && chatDownFile.getChatType() == 1) {
                            String path = "D:\\1CaiServer\\" + chatDownFile.getUserId()
                                    + "." + chatDownFile.getToRoomId() + "." + chatDownFile.getMsg();
                            System.out.println(path);
                            File file = new File(path);
                            rd = new RawData();
                            rd.setCode("DOWN_FILE");
                            FileInputStream fis = new FileInputStream(file);
                            Long fileSize = file.length();
                            chatFile = new ChatFile();
                            chatFile.setChatType(1);
                            filePart = new FilePart();
                            filePart.setName(chatDownFile.getMsg());
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
                                filePart.setPercent(percent);
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
                        }
                        break;
//                    case "CALL":
//                        break;
//                    case "VIDEO_CALL":
//                        break;
                }
            }
        } catch (IOException e) {
            if (clientId != -1) {
                userOnline.get(clientId).setOnline(false);
                // offline
                List<Friend> friends = new ArrayList<>();
                try {
                    friends = friendRepository.getListFriend(clientId);
                } catch (SQLException ex) {
                    e.printStackTrace();
                }
                RawData rd = new RawData();
                rd.setData(gson.toJson(clientId));
                rd.setCode("OFFLINE");

                for (Friend friend : friends) {
                    if (userOnline.get(friend.getUserId()).isOnline())
                        try {
                            userOnline.get(friend.getUserId()).getDos().writeUTF(gson.toJson(rd));
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                }
            }
            System.out.println("ClienId out: " + clientId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
