package repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//Connect Database
public class DataSource {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/btlltm";
    private static final String USER = "root";
    private static final String PASS = "123";
    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource ds;

    static {
        config.setJdbcUrl(DB_URL);
        config.setUsername(USER);
        config.setPassword(PASS);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("maximumPoolSize", "40");
        config.setConnectionTimeout(20000);
        config.setIdleTimeout(300000);
        config.setMinimumIdle(15);
        config.setMaximumPoolSize(30);
        ds = new HikariDataSource(config);
    }

    public DataSource() {
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public static void closeConnection(PreparedStatement statement,
                                       ResultSet rs, Connection con) throws SQLException {
        if (statement != null) {
            statement.close();
        }
        if (rs != null) {
            rs.close();
        }
        if (con != null) {
            con.close();
        }
    }
}
