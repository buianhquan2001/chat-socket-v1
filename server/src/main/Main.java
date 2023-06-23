package main;

import model.UserOnline;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static void main(String[] args) {
        System.out.println("Start Server !!!");
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(9996);
            Map<Integer, UserOnline> userOnline = new ConcurrentHashMap<>();
            for (int i = 0; i < 100000; i++)
                userOnline.put(i, new UserOnline(false));
            while (true) {
                // Server accept
                Socket socket = serverSocket.accept();
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                Thread thread = new RequestHandler(dis, dos, userOnline);
                thread.start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
