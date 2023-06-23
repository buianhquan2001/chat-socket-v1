package repository;

import model.Chat;

import java.sql.*;
import java.util.ArrayList;

public class ChatRepository {

    private Connection con = null;
    private PreparedStatement statement = null;
    private ResultSet rs = null;

    public boolean insert(Chat chat) throws SQLException {
        chat.setMsgTime(System.currentTimeMillis());
        try {
            con = DataSource.getConnection();
            String sql = "INSERT INTO chat (id, msg, msg_time, chat_type, user_id, room_id) VALUES (NULL, ?, ?, ?, ?, ?)";
            statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, chat.getMsg());
            statement.setLong(2, chat.getMsgTime());
            statement.setInt(3, chat.getChatType());
            statement.setInt(4, chat.getUserId());
            statement.setInt(5, chat.getToRoomId());
            int row = statement.executeUpdate();
            if (row == 1) {
                rs = statement.getGeneratedKeys();
                if (rs.next()) {
                    chat.setId(rs.getInt(1));
                }
                return true;
            }
        } finally {
            DataSource.closeConnection(statement, rs, con);
        }
        return false;
    }

    public ArrayList<Chat> olderWithRoomId(int roomId, int lastChatId) throws SQLException {
        ArrayList<Chat> chats = new ArrayList<Chat>();
        try {
            con = DataSource.getConnection();
            String sql = "SELECT chat.*, user.name FROM chat INNER JOIN user ON chat.user_id=user.id WHERE chat.room_id=? and (chat.id < ? or ?=-1) ORDER BY chat.id DESC LIMIT 10";
            statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, roomId);
            statement.setInt(2, lastChatId);
            statement.setInt(3, lastChatId);
            rs = statement.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    Chat chat = new Chat();
                    chat.setId(rs.getInt("id"));
                    chat.setMsg(rs.getString("msg"));
                    chat.setMsgTime(rs.getLong("msg_time"));
                    chat.setChatType(rs.getInt("chat_type"));
                    chat.setUserId(rs.getInt("user_id"));
                    chat.setToRoomId(rs.getInt("room_id"));
                    chat.setUserName(rs.getString("name"));
                    chats.add(chat);
                }
            }
        } finally {
            DataSource.closeConnection(statement, rs, con);
        }
        return chats;
    }

    public ArrayList<Chat> newerWithRoomId(int roomId, int lastChatId) throws SQLException {
        ArrayList<Chat> chats = new ArrayList<Chat>();
        try {
            con = DataSource.getConnection();
            String sql = "SELECT chat.*, user.name FROM chat INNER JOIN user ON chat.user_id=user.id WHERE chat.room_id=? and (chat.id > ? or ?=-1) ORDER BY chat.id DESC LIMIT 10";
            statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, roomId);
            statement.setInt(2, lastChatId);
            statement.setInt(3, lastChatId);
            rs = statement.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    Chat chat = new Chat();
                    chat.setId(rs.getInt("id"));
                    chat.setMsg(rs.getString("msg"));
                    chat.setMsgTime(rs.getLong("msg_time"));
                    chat.setChatType(rs.getInt("chat_type"));
                    chat.setUserId(rs.getInt("user_id"));
                    chat.setToRoomId(rs.getInt("room_id"));
                    chat.setUserName(rs.getString("name"));
                    chats.add(chat);
                }
            }
        } finally {
            DataSource.closeConnection(statement, rs, con);
        }
        return chats;
    }

    public Chat findById(Integer chatId, Integer clientId) throws SQLException {
        Chat chat = null;
        try {
            con = DataSource.getConnection();
            String sql = "SELECT * from chat where chat.id = ? " ;
            statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, chatId);
            rs = statement.executeQuery();
            if (rs.next()) {
                chat = new Chat();
                chat.setId(rs.getInt("id"));
                chat.setMsg(rs.getString("msg"));
                chat.setMsgTime(rs.getLong("msg_time"));
                chat.setChatType(rs.getInt("chat_type"));
                chat.setUserId(rs.getInt("user_id"));
                chat.setToRoomId(rs.getInt("room_id"));
            }
        } finally {
            DataSource.closeConnection(statement, rs, con);
        }
        return chat;
    }
}
