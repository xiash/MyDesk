package com.example.xiash.mydesk;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;

    //id 应用id
    //appName 应用名称
    //packageName 应用包名
    //versionName 应用版本
    //versionCode 应用版本内部识别号
    //showType 显示类型，0隐藏，1显示
    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DbHelper(Context context, String name, int version){
        this(context,name,null,version);
    }

    public DbHelper(Context context, String name){
        this(context,name,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="";
        //showType int
        sql="create table if not exists app(id int, appName varchar(50),packageName varchar(200),versionName varchar(20),versionCode int,showType int)";
        db.execSQL(sql);

//		//Ver2 增加字段
//    	if(!checkColumnExist(db, "a", "b"))
//    		db.execSQL("ALTER TABLE a ADD b VARCHAR(20) DEFAULT '' "); //往表中增加一列
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {



//		//Ver2  两个地方都要写
//    	if(!checkColumnExist(db, "a", "b"))
//    		db.execSQL("ALTER TABLE a ADD b VARCHAR(20) DEFAULT '' "); //往表中增加一列
    }

    /**
     * 方法1：检查某表列是否存在
     * @param db
     * @param tableName 表名
     * @param columnName 列名
     * @return -1代表不存在该列
     */
    private boolean checkColumnExist(SQLiteDatabase db, String tableName, String columnName) {
        boolean result = false ;
        Cursor cursor = null ;

        try{
            cursor = db.rawQuery( "select * from sqlite_master where name = ? and sql like ?", new String[]{tableName , "%" + columnName + "%"} );
            result = null != cursor && cursor.moveToFirst() ;
        }catch (Exception e){
        }finally{
            if(null != cursor && !cursor.isClosed()){
                cursor.close() ;
            }
        }
        return result ;
    }

}
