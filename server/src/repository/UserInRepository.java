package repository;

import model.UserIN;

import java.sql.*;
import java.util.ArrayList;


public class UserInRepository {
    private Connection con = null;
    private PreparedStatement statement = null;
    private ResultSet rs = null;

    public boolean insert(int roomId, int userId) throws SQLException {
        try {
            con = DataSource.getConnection();
            String sql = "INSERT INTO user_in_room (id, user_id, room_id) VALUES (NULL, ?, ?)";
            statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, userId);
            statement.setInt(2, roomId);
            int row = statement.executeUpdate();
            if (row == 1) {
                return true;
            }
        } finally {
            DataSource.closeConnection(statement, rs, con);
        }
        return false;
    }

    public boolean delete(int roomId, int userId) throws SQLException {
        try {
            con = DataSource.getConnection();
            String sql = "DELETE FROM user_in_room WHERE user_id=? and room_id=?";
            statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, userId);
            statement.setInt(2, roomId);
            int row = statement.executeUpdate();
            if (row == 1) {
                return true;
            }
        } finally {
            DataSource.closeConnection(statement, rs, con);
        }
        return false;
    }

    public UserIN with(int roomId, int userId) throws SQLException {
        try {
            con = DataSource.getConnection();
            String sql = "SELECT user_in_room.user_id, user.name FROM user_in_room INNER JOIN user ON user_in_room.user_id=user.id WHERE user_in_room.room_id=? and user_in_room.user_id=?";
            statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, roomId);
            statement.setInt(2, userId);
            rs = statement.executeQuery();
            if (rs != null && rs.next()) {
                return (new UserIN(rs.getInt("user_id"), rs.getString("name")));
            }
        } finally {
            DataSource.closeConnection(statement, rs, con);
        }
        return null;
    }

    public ArrayList<UserIN> withRoomId(int id) throws SQLException {
        ArrayList<UserIN> users = new ArrayList<UserIN>();
        try {
            con = DataSource.getConnection();
            String sql = "SELECT user_in_room.user_id, user.name FROM user_in_room INNER JOIN user ON user_in_room.user_id=user.id WHERE user_in_room.room_id=?";
            statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, id);
            rs = statement.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    users.add(new UserIN(rs.getInt("user_id"), rs.getString("name")));
                }
            }
        } finally {
            DataSource.closeConnection(statement, rs, con);
        }
        return users;
    }
}
