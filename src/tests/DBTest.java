package tests;

import bean.Students;
import utils.DBUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DBTest {

    public static void main(String[] args) {
        DBUtils db = DBUtils.getInstance();
        db.getCon();
        String sql = "select * from student";
        List<Students> reslist = new ArrayList<>();
        List<Object> list = new ArrayList<>();

        try {
            reslist = db.executeQueryByRef(sql,list,Students.class);
            for (Students stu:reslist){
                System.out.println(stu.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }finally {
            db.closeDB();
        }

    }

}
