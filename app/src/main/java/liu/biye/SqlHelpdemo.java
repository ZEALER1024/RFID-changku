package liu.biye;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlHelpdemo extends SQLiteOpenHelper {
    /*
     * 创建语句
     */
    // 创建用户表
    String createUserTable = "create table user_info(_id int auto_increment,username char(20),"
            + "password char(20),num char(20),primary key('_id'));";
    // 创建商品表
    String creatproductsTable = "create Table products(_id int auto_increment,"
            + "pname char(40),pguige char(20),pdanwei char(20),primary key('_id'));";
    // 创建顾客表
    String createguke = "create table guke(_id int auto_increment,"
            + "comname char(40),pername char(40),addr char(40),youbian char(20),tel char(20),primary key('_id'));";
    //创建出库表
    String createruku = "create table ruku(_id int auto_increment,"
            + "comname char(40),pername char(40),tel char(40),"
            + "products char(40),guige char(40),danwei char(20)," +
            "num int,date char(40),primary key('_id'));";
    //创建入库表
    String createchuku = "create table chuku(_id int auto_increment,"
            + "comname char(40),pername char(40),tel char(40),"
            + "products char(40),guige char(40),danwei char(20)," +
            "num int,date char(40),primary key('_id'));";
    //创建库存表
    String createkucun = "create Table kucun(_id int auto_increment,"
            + "pname char(40),pguige char(20),pdanwei char(20),num int,primary key('_id'));";
    //创建盘点表
    String createpandian = "create Table pandian(_id int auto_increment,pname char(40),num int,primary key('_id'));";
    // 定义用户表插入语句
    String insertStr = "insert into user_info(_id,username,password,num) values(?,?,?,?)";
    // 定义盘点表插入语句
    String insertPan = "insert into pandian(_id,pname,num) values(?,?,?)";
    // 定义商品表插入语句
    String insertproducts = "insert into products values(?,?,?,?);";
    // 定义顾客表插入语句
    String insertguke = "insert into guke values(?,?,?,?,?,?);";
    //定义入库表插入语句
    String insertruku="insert into ruku values(?,?,?,?,?,?,?,?,?);";
    //定义出库表插入语句
    String insertchuku="insert into chuku values(?,?,?,?,?,?,?,?,?);";
    //定义库存表插入语句
    String insertkucun = "insert into kucun values(?,?,?,?,?);";


    public SqlHelpdemo(Context context, String name, CursorFactory factory,
                       int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase arg0) {
        // 设置ID
        int _id = 0;
        // 创建用户表，用商品表，用户，入库表。出库表
        arg0.execSQL(createUserTable);
        arg0.execSQL(creatproductsTable);
        arg0.execSQL(createruku);
        arg0.execSQL(createchuku);
        arg0.execSQL(createkucun);
        arg0.execSQL(createguke);
        arg0.execSQL(createpandian);
        // 插入测试data
        String[] insertValue = {"1", "admin", "1", "001",};
        String[] insertValue1 = {"1", "三体", "10x15", "本"};
        String[] insertValue2 = {"2", "iPhone", "184x74", "部"};
        String[] insguke = {"1", "腾讯科技", "马花藤", "深圳高新区",  "641210", "18281685562"};
        String[] insguke1 = {"2", "阿里巴巴", "杰克马", "杭州开发区",  "641101", "1788900987"};
        arg0.execSQL(insertStr, insertValue);
        arg0.execSQL(insertproducts, insertValue1);
        arg0.execSQL(insertproducts, insertValue2);
        arg0.execSQL(insertguke, insguke);
        arg0.execSQL(insertguke, insguke1);



    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }

}