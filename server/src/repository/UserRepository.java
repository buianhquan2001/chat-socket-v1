package repository;

import model.Friend;
import model.Login;
import model.SignUp;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    // id, username, password, name
    private Connection con = null;
    private PreparedStatement statement = null;
    private ResultSet rs = null;

    public User findById(Long id) throws SQLException {
        try {
            con = DataSource.getConnection();
            String sql = "select * from user where id = ?";
            statement = con.prepareStatement(sql);
            statement.setLong(1, id);
            rs = statement.executeQuery();
            if (rs.next() == false) {
                return null;
            }
            User user = new User(rs.getInt("id"), rs.getNString("username"), rs.getNString("password"),
                    rs.getNString("name"));
            return user;
        } finally {
            DataSource.closeConnection(statement, rs, con);
        }
    }

    public User checkLogin(Login account) throws SQLException {
        try {
            con = DataSource.getConnection();
            String sql = "select * from user where username = ?";
            statement = con.prepareStatement(sql);
            statement.setString(1, account.getUsername());
            rs = statement.executeQuery();
            if (rs.next() == false) {
                return null;
            }
            if (!rs.getNString("password").equals(account.getPassword()))
                return null;
            User user = new User(rs.getInt("id"), rs.getNString("username"), rs.getNString("password"),
                    rs.getNString("name"));
            return user;
        } finally {
            DataSource.closeConnection(statement, rs, con);
        }
    }

    public boolean isExist(String username) throws SQLException {
        try {
            con = DataSource.getConnection();
            String sql = "select * from user where username = ?";
            statement = con.prepareStatement(sql);
            statement.setString(1, username);
            rs = statement.executeQuery();
            return rs.next() != false;
        } finally {
            DataSource.closeConnection(statement, rs, con);
        }
    }

    public List<Friend> searchByName(Integer myId, String username) throws SQLException {
        try {
            con = DataSource.getConnection();
            String sql = "select * from user where name like ? and id <> ?";
            statement = con.prepareStatement(sql);
            statement.setString(1, "%" + username + "%");
            statement.setInt(2, myId);
            rs = statement.executeQuery();
            List<Friend> users = new ArrayList<>();
            while (rs.next()) {
                Friend user = new Friend(-1, rs.getInt("id"), rs.getNString("name"));
                users.add(user);
            }
            return new FriendRepository().checkFriend(myId, users);
        } finally {
            DataSource.closeConnection(statement, rs, con);
        }
    }

    public boolean save(SignUp user) throws SQLException {
        try {
            if (isExist(user.getUsername()))
                return false;
            con = DataSource.getConnection();
            String sql = "insert into user (username, password, name) values (?, ?, ?)";
            statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getName());
            statement.executeUpdate();
            rs = statement.getGeneratedKeys();
            return rs.next() == true;
        } finally {
            DataSource.closeConnection(statement, rs, con);
        }
    }

}