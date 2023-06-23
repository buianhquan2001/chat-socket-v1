package repository;

import java.sql.SQLException;

public class TestRepo {
    public static void main(String[] args) throws SQLException {
        UserRepository ur = new UserRepository();
        FriendRepository fr = new FriendRepository();
//        System.out.println(fr.accept(1, 2).toString());
//        System.out.println(fr.accept(2,1).toString());
//        System.out.println(fr.accept(3, 2).toString());
//        System.out.println(fr.accept(2,3).toString());
//        System.out.println(fr.accept(4, 2).toString());
        System.out.println(fr.denied(6, 2).toString());
//        System.out.println(fr.getListFriend(2));
        System.out.println(ur.searchByName(2, "t"));
    }
}

