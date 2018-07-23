package utils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtils {

    //定义数据库的用户名
    private static final String USERNAME = "root";
    //定义数据库的密码
    private static final String PASSWORD = "Test1234";
    //定义数据库的驱动信息
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    //定义访问数据库的地址
    private static final String URL = "jdbc:mysql://localhost:3306/test1?useSSL=false";
    //定义数据库的链接
    private static Connection con = null;
    //定义SQL语句的执行对象
    private static PreparedStatement preparedStatement= null;
    //定义查询返回的结果集合
    private static ResultSet resultSet= null;

    private static DBUtils per = null;

    private DBUtils(){

    }

    /**
     *
     * 单例模式，获取工具类的一个对象
     *
     */
    public static DBUtils getInstance(){

        if (per == null){
            per = new DBUtils();
            per.registeredDriver();
        }
        return per;
    }

    private void registeredDriver() {
        try {
            Class.forName(DRIVER);
            System.out.println("注册驱动成功！");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * 获取数据库的连接
     *
     */
    public Connection getCon(){
        try {
            con = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("数据库连接成功！");
        return con;
    }

    /**
     *
     * 完成对数据库的表的添加删除和修改操作
     *
     */
    public Boolean executeUpdate(String sql, List<Object> params) throws SQLException {
        boolean flag = false;
        int result = -1;//表示当用户执行添加删除和修改的时候所影响数据库的行数
        preparedStatement = con.prepareStatement(sql);
        if (params != null && !params.isEmpty()){
            int index = 1;
            for (int i = 0;i<params.size();i++){
                preparedStatement.setObject(index++,i);
            }
        }
        result = preparedStatement.executeUpdate();
        flag = result > 0 ? true : false;
        return flag;
    }


    /**
     *
     * 从数据库中查询数据
     *
     */
    public List<Map<String,Object>> executeQuery(String sql,List<Object> params) throws SQLException {
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        int index= 1;
        preparedStatement = con.prepareStatement(sql);
        if (params != null && !params.isEmpty()){
            for (int i = 0;i<params.size();i++){
                preparedStatement.setObject(index++,params.get(i));
            }
        }
        resultSet = preparedStatement.executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int cols_len = metaData.getColumnCount();
        while (resultSet.next()){
            Map<String,Object> map = new HashMap<String,Object>();
            for (int i = 0;i < cols_len; i++){
                String cols_name = metaData.getColumnName(i+1);
                Object cols_value = resultSet.getObject(cols_name);
                if(cols_value == null){
                    cols_value = "";
                }
                map.put(cols_name,cols_value);
            }
            list.add(map);
        }
        return list;
    }


    /**
     *
     * jdbc的封装可以用反射机制来封装，把从数据库中获取的数据封装到一个类的对象里
     *
     */
    public <T> List<T> executeQueryByRef(String sql,List<Object> params,Class<T> cls)
            throws SQLException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        List<T> list = new ArrayList<T>();
        int index = 1;
        preparedStatement = con.prepareStatement(sql);
        if (params !=null && !params.isEmpty()){
            for (int i = 0;i<params.size();i++){
                preparedStatement.setObject(index++,params.get(i));
            }
        }
        resultSet = preparedStatement.executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int cols_len = metaData.getColumnCount();
        while (resultSet.next()){
            T resultObject = cls.newInstance();//通过反射机制创建实例
            for (int i = 0; i<cols_len; i++){
                String cols_name = metaData.getColumnName(i+1);
                Object cols_value = resultSet.getObject(cols_name);
                if (cols_value == null){
                    cols_value = "";
                }
                Field field = cls.getDeclaredField(cols_name);
                field.setAccessible(true);//打开JavaBean的访问private权限
                field.set(resultObject,cols_value);
            }
            list.add(resultObject);
        }
        return list;
    }


    /**
     *
     * 关闭数据库资源
     *
     */
    public void closeDB(){
        if (resultSet !=null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (preparedStatement !=null){
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (con != null){
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
