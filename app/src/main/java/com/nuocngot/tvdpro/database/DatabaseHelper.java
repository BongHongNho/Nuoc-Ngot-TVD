package com.nuocngot.tvdpro.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "user_database";
    private static final int DATABASE_VERSION = 1;

    static final String CREATE_TABLE_TAI_KHOAN =
            "CREATE TABLE TaiKhoan (" +
                    "maTK INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "tenDN TEXT NOT NULL," +
                    "matKhau TEXT NOT NULL," +
                    "Email TEXT NOT NULL," +
                    "SDT INTEGER NOT NULL," +
                    "loaiTK TEXT NOT NULL)";
    private static final String CREATE_TABLE_KHACH_HANG =
            "CREATE TABLE KhachHang (" +
                    "maKH INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "loaiTK TEXT REFERENCES TaiKhoan(loaiTK)," +
                    "tenKH TEXT NOT NULL," +
                    "Email TEXT NOT NULL," +
                    "SDT INTEGER NOT NULL," +
                    "diaChi TEXT NOT NULL," +
                    "capTV TEXT NOT NULL," +
                    "hinhAnh INTEGER NOT NULL)";

    private static final String CREATE_TABLE_ADMIN =
            "CREATE TABLE Admin (" +
                    "maAdmin INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "loaiTK TEXT REFERENCES TaiKhoan(loaiTK)," +
                    "tenAdmin TEXT NOT NULL," +
                    "hinhAnh INTEGER NOT NULL)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TAI_KHOAN);
        db.execSQL("INSERT INTO TaiKhoan (tenDN, matKhau, Email, SDT, loaiTK) VALUES ('admin', 'admin123', 'admin@example.com', '123456789', 'admin')");
        db.execSQL("INSERT INTO TaiKhoan (tenDN, matKhau, Email, SDT, loaiTK) VALUES ('user', 'user123', 'user@example.com', '987654321', 'user')");
        db.execSQL(CREATE_TABLE_KHACH_HANG);
        db.execSQL(CREATE_TABLE_ADMIN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS TaiKhoan");
        db.execSQL("DROP TABLE IF EXISTS KhachHang");
        db.execSQL("DROP TABLE IF EXISTS Admin");
        onCreate(db);
    }
}
