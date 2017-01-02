package cn.edu.gdmec.s07150840.mycontacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.Vector;

/**
 * Created by hasee on 2016/11/20.
 */
public class ContactsTable {
    private final  static String TABLENAME="contactsTable";
    private MyDB db;
    public ContactsTable(Context context)
    {
        //创建MyDB对象，context很很重要
        db = new MyDB(context);
        //如果数据表不存在就新建数据表
        if (!db.isTableExits(TABLENAME)){
            String createTableSql = "create table if not exists " + TABLENAME +
                    "(id_DB integer " + "primary key AUTOINCREMENT,"+
                    User.NAME + " VARCHAR," +
                    User.MOBILE + " VARCHAR," +
                    User.QQ + " VARCHAR," +
                    User.DANWEI + " VARCHAR," +
                    User.ADDRESS + " VARCHAR)";
            //创建表
            db.createTable(createTableSql);
        }
    }
    //增加数据
    public boolean addData(User user)
    {
        //创建ContenValues对象用于保存数据
        ContentValues values = new ContentValues();
        //contentvalue 赋值
        values.put(User.NAME, user.getName());
        values.put(User.MOBILE, user.getMobile());
        values.put(User.DANWEI, user.getQq());
        values.put(User.ADDRESS, user.getAddress());
        //保存数据
        return db.save(TABLENAME,values);
    }

    //获取联系人表数据
    public User[] getAllUser(){
        Vector<User> v = new Vector<User>();
        Cursor cursor = null;
        try{
            cursor = db.find("select * from " + TABLENAME, null);
            while (cursor.moveToNext()){
                User temp = new User();
                temp.setName(cursor.getString(cursor.getColumnIndex(User.NAME)));
                temp.setId_DB(cursor.getInt(cursor.getColumnIndex("id_DB")));
                temp.setMobile(cursor.getString(cursor.getColumnIndex(User.MOBILE)));
                temp.setDanwei(cursor.getString(cursor.getColumnIndex(User.DANWEI)));
                temp.setAddress(cursor.getString(cursor.getColumnIndex(User.ADDRESS)));
                temp.setQq(cursor.getString(cursor.getColumnIndex(User.QQ)));
                v.add(temp);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null){
                cursor.close();
            }
            db.closeConnection();
        }
        return null;
    }

    //根据数据库改变主键ID来获取联系人
    public User getUserByID(int id){
        Cursor cursor = null;
        User temp = new User();
        try{
            cursor = db.find("select * from " + TABLENAME + " where "+ " id_DB=?", new String[]{id+""});
            //游标一开始指向-1位置，moveToNext方法将游标移动到了下一行，即第一行
            cursor.moveToNext();
            temp.setId_DB(cursor.getInt(cursor.getColumnIndex("id_DB")));
            temp.setName(cursor.getString(cursor.getColumnIndex(User.NAME)));
            temp.setMobile(cursor.getString(cursor.getColumnIndex(User.MOBILE)));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null){
                cursor.close();
            }
            db.closeConnection();
        }
        return null;
    }

    //修改联系人信息
    public boolean updateUser(User user){
        //创建ContentValues对象用于保存数据
        ContentValues values = new ContentValues();
        //contentvalue赋值
        values.put("id_DB", user.getId_DB());
        values.put(User.NAME, user.getName());
        values.put(User.MOBILE, user.getMobile());
        values.put(User.DANWEI, user.getDanwei());
        values.put(User.QQ, user.getQq());
        values.put(User.ADDRESS, user.getAddress());
        //执行数据更新操作
        return db.update(TABLENAME, values, " id_DB=? ", new String[]{user.getId_DB()+""});
    }

    public User[] findUserByKey(String key){
        Vector<User> v = new Vector<User>();
        Cursor cursor = null;
        try{
            cursor = db.find(" select * from " + TABLENAME + " where "
                            + User.NAME + " like '%" + key + "%' " +
                            " or "+User.MOBILE+" like '%"+key+"%' " +
                            " or "+User.QQ+" like '%"+key+"%' "
                    ,null);
            while (cursor.moveToNext()){
                User temp = new User();
                temp.setId_DB(cursor.getInt(
                        cursor.getColumnIndex("id_DB")
                ));
                temp.setName(cursor.getString(
                        cursor.getColumnIndex(User.NAME)
                ));
                temp.setMobile(cursor.getString(
                        cursor.getColumnIndex(User.MOBILE)
                ));
                temp.setDanwei(cursor.getString(
                        cursor.getColumnIndex(User.DANWEI)
                ));
                temp.setQq(cursor.getString(
                        cursor.getColumnIndex(User.QQ)
                ));
                temp.setAddress(cursor.getString(
                        cursor.getColumnIndex(User.DANWEI)
                ));
                //将符合查询条件的User对象添加到向量集合中
                v.add(temp);
            }
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            if (cursor != null){
                cursor.close();
            }
            db.closeConnection();
        }
        if (v.size() > 0){
            //将vector中的数据，强制以User数组形式返回
            return v.toArray(new User[] {});
        }else{
            User[] users = new User[1];
            User user = new User();
            user.setName("无结果");
            users[0] = user;
            return users;
        }
    }

    //删除联系人
    public  boolean deleteByUser(User user){
        return db.delete(TABLENAME, " id_DB=?", new String[]{user.getId_DB() + ""});
    }
}
