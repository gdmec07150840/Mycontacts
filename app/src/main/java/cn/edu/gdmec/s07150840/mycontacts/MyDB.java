package cn.edu.gdmec.s07150840.mycontacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hasee on 2016/11/20.
 */
public class MyDB extends SQLiteOpenHelper{
    private static String DB_NAME = "MY_DB.db"; //数据库名称
    private static int DB_VERSION =2;   //版本号
    private SQLiteDatabase db;  //数据库操作对象
    //构造方法Context是获取数据文件存放位置
    public MyDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        //以写方式打开数据库，该方法是获得SQLite数据库的关键。
        //后面对数据库的操作可以直接使用SQLiteDatabase类中的方法。
        db = getWritableDatabase(); //创建或打开一个数据库，返回一个SQLite数据库对象
    }

    //创建数据库后，该方法被调用，但若数据库是此前创建，则该方法不会被执行
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        //TODO每次成功打开数据库后首先被执行
        super.onOpen(db);
    }

    //升级数据库版本，该方法被调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //更改数据库版本操作
    }
    //打开数据库
    public SQLiteDatabase openConnection()
    {
        if (!db.isOpen()) //判断数据库是否被打开
        {
            db = getWritableDatabase();
        }
        return db;
    }
    //关闭数据库
    public void closeConnection()
    {
        try {
            if (db != null && db.isOpen())
            {
                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //创建数据表
    public boolean createTable(String createTableSql)
    {
        try {
            openConnection();   //调用了上面的方法，目的是打开数据库
            db.execSQL(createTableSql);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    //保存数据
    public boolean save(String tableName, ContentValues values)
    {
        try {
            openConnection();
            db.insert(tableName, null, values);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeConnection();  //调用了上面所写的方法，目的是关闭数据库
        }
        return true;
    }
    //更新数据
    public boolean update(String table, ContentValues values, String whereClause, String[] whereArgs)
    {
        try {
            openConnection();
            db.update(table, values, whereClause, whereArgs); //更新数据
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeConnection();
        }
        return true;
    }
    //删除数据
    public boolean delete(String table, String deleteSql, String obj[])
    {
        try {
            openConnection();
            db.delete(table, deleteSql, obj);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeConnection();
        }
        return true;
    }
    //查找数据
    //不可以直接关闭数据库，Cursor返回显示的是一部分数据，随着游标的移动跟数据库交换数据
    //必须先关闭Cursor，再关闭数据库
    public Cursor find(String findSql, String obj[])
    {
        try {
            openConnection();
            Cursor cursor = db.rawQuery(findSql, obj);
            return cursor;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    //数据表是否存在：对表进行查询，如果出错表示数据库中不存在所要查询的表
    public boolean isTableExits(String tablename)
    {
        try {
            openConnection();
            String str = "select count(*)xcount from "+tablename;
            db.rawQuery(str, null).close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeConnection();
        }
        return true;
    }
}

