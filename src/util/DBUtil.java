package util;

import java.sql.*;

/**
 * 连接数据库工具类
 * @author dailiwen
 * @date 2018/05/17
 */
public class DBUtil {
    public static Connection getConnection() throws Exception {
        //com.mysql.jdbc.Driver为Mysql驱动名
        //用了最新的mysql连接驱动,按照最新官方提示支持将com.mysql.jdbc.Driver  改为  com.mysql.cj.jdbc.Driver,
        String driverName = "com.mysql.cj.jdbc.Driver";
        //你需要显式禁用SSL设置usessl = false提供服务器证书验证信任库
        String url = "jdbc:mysql://localhost:3306/oicq?useSSL=false";
        String userName = "root";
        String userPWD = "123456";

        //将驱动程序的类文件动态加载到内存中，并将其自动注册
        Class.forName(driverName);

        //要求传递数据库URL，用户名和密码：
        Connection connection = DriverManager.getConnection(url,userName,userPWD);

        return connection;
    }

    public static boolean execute(String sql) throws Exception {
        Connection connection = getConnection();

        //用于对数据库进行通用访问，在运行时使用静态SQL语句时很有用。 Statement接口不能接受参数。
        Statement statement =  connection.createStatement();

        return statement.execute(sql);
    }
}
