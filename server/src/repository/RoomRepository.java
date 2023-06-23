package repository;

import model.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomRepository {

    private Connection con = null;
    private PreparedStatement statement = null;
    private ResultSet rs = null;

    public Room create(String name, int adminId, int friendId) throws SQLException {
        if (name == null || name.length() > 45) {
            return null;
        }
        Room r = null;
        try {
            con = DataSource.getConnection();
            String sql = "INSERT INTO room (id, name, admin_id, friend_id) VALUES (NULL, ?, ?, ?)";
            statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.setInt(2, adminId);
            statement.setInt(3, friendId);
            int row = statement.executeUpdate();
            if (row == 1) {
                rs = statement.getGeneratedKeys();
                if (rs.next()) {
                    r = new Room();
                    r.setId(rs.getInt(1));
                    r.setName(name);
                    r.setAdminId(adminId);
                }
            }
        } finally {
            DataSource.closeConnection(statement, rs, con);
        }
        return r;
    }

    public Room withChatPrivate(int adminId, int friendId) throws SQLException {
        Room r = null;
        try {
            con = DataSource.getConnection();
            String sql = "SELECT * FROM room WHERE (friend_id=? and admin_id=?) or (admin_id=? and friend_id=?) LIMIT 1";
            statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, adminId);
            statement.setInt(2, friendId);
            statement.setInt(3, adminId);
            statement.setInt(4, friendId);
            rs = statement.executeQuery();
            if (rs != null && rs.next()) {
                r = new Room();
                r.setId(rs.getInt("id"));
                r.setName(rs.getString("name"));
                r.setFriendId(rs.getInt("friend_id"));
                r.setAdminId(rs.getInt("admin_id"));
            }
        } finally {
            DataSource.closeConnection(statement, rs, con);
        }
        return r;
    }

    public Room withId(int id) throws SQLException {
        Room r = null;
        try {
            con = DataSource.getConnection();
            String sql = "SELECT * FROM room WHERE id=? LIMIT 1";
            statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, id);
            rs = statement.executeQuery();
            if (rs != null && rs.next()) {
                r = new Room();
                r.setId(rs.getInt("id"));
                r.setName(rs.getString("name"));
                r.setFriendId(rs.getInt("friend_id"));
                r.setAdminId(rs.getInt("admin_id"));
            }
        } finally {
            DataSource.closeConnection(statement, rs, con);
        }
        return r;
    }

    public Room withFriendId(int clientId, int friendId) throws SQLException {
        Room r = null;
        try {
            con = DataSource.getConnection();
            String sql = "SELECT * FROM room WHERE (admin_id = ? and friend_id=?) or (friend_id=? and admin_id=?) LIMIT 1";
            statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, clientId);
            statement.setInt(2, friendId);
            statement.setInt(3, clientId);
            statement.setInt(4, friendId);
            rs = statement.executeQuery();
            if (rs != null && rs.next()) {
                r = new Room();
                r.setId(rs.getInt("id"));
                r.setName(rs.getString("name"));
                r.setFriendId(rs.getInt("friend_id"));
                r.setAdminId(rs.getInt("admin_id"));
            }
        } finally {
            DataSource.closeConnection(statement, rs, con);
        }
        return r;
    }

    public List<Room> getListRoom(Integer clientId) throws SQLException {
        List<Room> rooms = new ArrayList<>();
        try {
            con = DataSource.getConnection();
            String sql = "SELECT * FROM room " +
                    " JOIN user_in_room on user_in_room.room_id = room.id " +
                    " WHERE user_in_room.user_id = ?" +
                    " AND room.friend_id = -1";
            statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, clientId);
            rs = statement.executeQuery();
            while (rs.next()) {
                rooms.add(new Room(rs.getInt("room.id"), rs.getString("room.name")));
            }
        } finally {
            DataSource.closeConnection(statement, rs, con);
        }
        return rooms;
    }
}
