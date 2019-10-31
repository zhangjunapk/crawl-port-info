package crow.util;

import com.alibaba.druid.pool.DruidDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @BelongsProject: crow
 * @BelongsPackage: org.zj.crow.dao
 * @Author: ZhangJun
 * @CreateTime: 2018/12/24
 * @Description: ${Description}
 */
public class DBUtil {
Connection connection;
    DruidDataSource druidDataSource;

    public void runSql(String sql) {
        System.out.println(sql);
        if (druidDataSource == null) {
            initDataSource();
        }
        try {
            getStatement().execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initDataSource() {
        druidDataSource = new DruidDataSource();
        druidDataSource.setUrl("jdbc:mysql://localhost:3306/crawl_truck?serverTimezone=UTC");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("zhangjun249");
        druidDataSource.setDriverClassName("com.mysql.jdbc.Driver");
    }

    private Connection getConnection() {
        if (druidDataSource != null) {
            try {
                return druidDataSource.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private Statement getStatement() {
        if(connection==null){
            connection = getConnection();
        }
        if (connection != null) {
            try {
                return connection.createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
