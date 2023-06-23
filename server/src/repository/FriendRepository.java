package repository;

import model.Friend;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FriendRepository {
    private Connection con = null;
    private PreparedStatement statement = null;
    private ResultSet rs = null;

    // Huỷ lời mời = -2
    // Chưa kết bạn hoặc huỷ kết bạn= -1
    // Đã kết bạn = 1

    // Đang đợi kết bạn = 0
    // Người nhận lời mời là myId = 2
    // Người nhận lời mời là toId = 3


    public Friend invite(Integer myId, Integer toId) throws SQLException {
        try {
            con = DataSource.getConnection();
            String sql = "insert into friend (status, user_id1, user_id2) values (0, ?, ?)";
            statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, myId);
            statement.setInt(2, toId);
            statement.executeUpdate();
            rs = statement.getGeneratedKeys();
            return new Friend(3, toId);
        } finally {
            DataSource.closeConnection(statement, rs, con);
        }
    }

    public Friend accept(Integer myId, Integer toId, String username, Integer checkAccpet) throws SQLException {
        try {
            // Chưa kết bạn --> Gửi lời mời
            if (checkAccpet == -1)
                return invite(myId, toId);
            // Đã kết bạn
            if (checkAccpet == 1)
                return new Friend(1, toId);
            // Đã gửi lời mời
            if (checkAccpet == 3)
                return new Friend(3, toId);
            // Chấp nhận lời mời
            if (checkAccpet == 2) {
                con = DataSource.getConnection();
                String sql = "update friend set status = 1" +
                        " where (user_id1 = ? and user_id2 = ?)";
                statement = con.prepareStatement(sql);
                statement.setInt(1, toId);
                statement.setInt(2, myId);
                statement.executeUpdate();
                return new Friend(1, toId, username);
            }
            return new Friend(0, toId, username);
        } finally {
            DataSource.closeConnection(statement, rs, con);
        }
    }

    public Friend denied(Integer myId, Integer toId) throws SQLException {
        try {
            con = DataSource.getConnection();
            String sql = "delete from friend " +
                    " where (user_id1 = ? and user_id2 = ?) or (user_id2 = ? and user_id1 = ?)";
            statement = con.prepareStatement(sql);
            statement.setInt(1, myId);
            statement.setInt(2, toId);
            statement.setInt(3, myId);
            statement.setInt(4, toId);
            statement.executeUpdate();
            return new Friend(-1, toId);
        } finally {
            DataSource.closeConnection(statement, rs, con);
        }
    }

    public Integer canAccept(Integer myId, Integer toId) throws SQLException {
        try {
            con = DataSource.getConnection();
            String sql = "select * from friend" +
                    " where (user_id1 = ? and user_id2 = ?) or (user_id2 = ? and user_id1 = ?) ";
            statement = con.prepareStatement(sql);
            statement.setInt(1, myId);
            statement.setInt(2, toId);
            statement.setInt(3, myId);
            statement.setInt(4, toId);
            rs = statement.executeQuery();
            if (rs.next() == false) {
                return -1;
            }
            int status = rs.getInt("status");
            int userAccept = rs.getInt("user_id2");

            if (status == 1)
                return 1;
            if (status == 0 && userAccept == myId)
                return 2;
            if (status == 0 && userAccept == toId)
                return 3;
            return 10;
        } finally {
            DataSource.closeConnection(statement, rs, con);
        }
    }

    public List<Friend> checkFriend(Integer myId, List<Friend> friends) throws SQLException {
        try {
            con = DataSource.getConnection();

            String sql = "select * from friend" +
                    " where (user_id1 = ? and user_id2 = ?) or (user_id2 = ? and user_id1 = ?) ";

            for (Friend friend : friends) {
                statement = con.prepareStatement(sql);
                statement.setInt(1, myId);
                statement.setInt(2, friend.getUserId());
                statement.setInt(3, myId);
                statement.setInt(4, friend.getUserId());
                rs = statement.executeQuery();
                if (rs.next()) {
                    int status = rs.getInt("status");
                    int userAccept = rs.getInt("user_id2");
                    // Đã kết bạn
                    if (status == 1)
                        friend.setStatus(1);
                    // Chưa chấp nhận
                    if (status == 0 && userAccept == myId)
                        friend.setStatus(2);
                    // Đã gửi lời mời
                    if (status == 0 && userAccept != myId)
                        friend.setStatus(3);
                    // Default -1 : Không phải bạn
                }
            }
            return friends;
        } finally {
            DataSource.closeConnection(statement, rs, con);
        }
    }

    public List<Friend> getListFriend(Integer id) throws SQLException {
        try {
            con = DataSource.getConnection();
            String sql = "select * from friend" +
                    " join user on user.id =  user_id2 " +
                    " where user_id1 = ? and status = 1 ";
            statement = con.prepareStatement(sql);
            statement.setInt(1, id);
            rs = statement.executeQuery();
            List<Friend> result = new ArrayList<>();
            while (rs.next()) {
                result.add(new Friend(1, rs.getInt("user_id2"), rs.getNString("name")));
            }
            sql = "select * from friend" +
                    " join user on user.id = user_id1 " +
                    " where user_id2 = ? and status = 1 ";
            statement = con.prepareStatement(sql);
            statement.setInt(1, id);
            rs = statement.executeQuery();
            while (rs.next()) {
                result.add(new Friend(1, rs.getInt("user_id1"), rs.getNString("user.name")));
            }
            return result;
        } finally {
            DataSource.closeConnection(statement, rs, con);
        }
    }

    public List<Friend> getListInvite(Integer id) throws SQLException {
        try {
            con = DataSource.getConnection();
            String sql = "select * from friend" +
                    " join user on user.id = user_id1 " +
                    " where user_id2 = ? and status = 0";
            statement = con.prepareStatement(sql);
            statement.setInt(1, id);
            rs = statement.executeQuery();
            List<Friend> result = new ArrayList<>();
            while (rs.next()) {
                result.add(new Friend(2, rs.getInt("user_id1"), rs.getNString("name")));
            }
            return result;
        } finally {
            DataSource.closeConnection(statement, rs, con);
        }
    }
}
