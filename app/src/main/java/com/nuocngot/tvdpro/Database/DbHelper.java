package com.nuocngot.tvdpro.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME ="NuocNgotTVD";
    private static final int DB_VERSION = 2;

    public DbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    static final String CREATE_TABLE_TAI_KHOAN =
            "create table TaiKhoan (maTK INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "tenDN TEXT NOT NULL,"+
                    "matKhau TEXT NOT NULL,"+
                    "Email TEXT NOT NULL,"+
                    "SDT INTEGER NOT NULL,"+
                    "loaiTK TEXT NOT NULL)";
    static final String CREATE_TABLE_KHACH_HANG =
            "create table KhachHang (maKH INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "loaiTK TEXT REFERENCES TaiKhoan(loaiTK),"+
                    "tenKH TEXT NOT NULL,"+
                    "Email TEXT NOT NULL,"+
                    "SDT INTEGER NOT NULL,"+
                    "diaChi TEXT NOT NULL,"+
                    "capTV TEXT NOT NULL,"+
                    "hinhAnh TEXT NOT NULL)";
    static final String CREATE_TABLE_ADMIN =
            "create table Admin (maAdmin INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "loaiTK TEXT REFERENCES TaiKhoan(loaiTK),"+
                    "tenAdmin TEXT NOT NULL,"+
                    "hinhAnh TEXT NOT NULL)";
    static final String CREATE_TABLE_SAN_PHAM =
            "create table SanPham (maSP INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "hinhAnh TEXT NOT NULL,"+
                    "tenSP TEXT NOT NULL,"+
                    "giaSP INTEGER NOT NULL)";
    static final String CREATE_TABLE_CHI_TIET_SAN_PHAM =
            "create table ChiTietSanPham (maCTSP INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "maSP INTEGER REFERENCES SanPham(maSP),"+
                    "maDM INTEGER REFERENCES DanhMuc(maDM),"+
                    "xuatXu TEXT NOT NULL,"+
                    "thongTinSP  NOT NULL,"+
                    "soLuong INTEGER NOT NULL)";
    static final String CREATE_TABLE_YEU_THICH =
            "create table YeuThich (maYT INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "maSP INTEGER REFERENCES SanPham(maSP),"+
                    "maKH INTEGER REFERENCES KhachHang(maKH),"+
                    "thoiGian DATE NOT NULL)";
    static final String CREATE_TABLE_DANH_GIA =
            "create table DanhGia (maDG INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "maSP INTEGER REFERENCES SanPham(maSP),"+
                    "maKH INTEGER REFERENCES KhachHang(maKH),"+
                    "danhGiaSao INTEGER NOT NULL,"+
                    "binhLuon TEXT NOT NULL,"+
                    "thoiGian DATE NOT NULL)";
    static final String CREATE_TABLE_Gio_Hang =
            "create table GioHang (maGH INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "maSP INTEGER REFERENCES SanPham(maSP),"+
                    "maKH INTEGER REFERENCES KhachHang(maKH),"+
                    "soLuong INTEGER NOT NULL,"+
                    "tongTien TEXT NOT NULL)";
    static final String CREATE_TABLE_Hoa_DON =
            "create table HoaDon (maHD INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "maSP INTEGER REFERENCES SanPham(maSP),"+
                    "maKH INTEGER REFERENCES KhachHang(maKH),"+
                    "ngayTT DATE NOT NULL,"+
                    "soLuong INTEGER NOT NULL,"+
                    "tongTien TEXT NOT NULL,"+
                    "diaChi TEXT NOT NULL)";
    static final String CREATE_TABLE_DANH_MUC =
            "create table DanhMuc (maDM INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "tenDM TEXT NOT NULL)";

    static final String CREATE_TABLE_DON_MUA =
            "create table DonMua (maDMUA INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "maSP INTEGER REFERENCES SanPham(maSP))";
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Tao Bang Tai Khoan
            db.execSQL(CREATE_TABLE_TAI_KHOAN);
            db.execSQL(CREATE_TABLE_KHACH_HANG);
            db.execSQL(CREATE_TABLE_ADMIN);
            db.execSQL(CREATE_TABLE_SAN_PHAM);
            db.execSQL(CREATE_TABLE_CHI_TIET_SAN_PHAM);
            db.execSQL(CREATE_TABLE_YEU_THICH);
            db.execSQL(CREATE_TABLE_DANH_GIA);
            db.execSQL(CREATE_TABLE_Gio_Hang);
            db.execSQL(CREATE_TABLE_Hoa_DON);
            db.execSQL(CREATE_TABLE_DANH_MUC);
            db.execSQL(CREATE_TABLE_DON_MUA);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            String dropTableTaiKhoan ="drop table if exists TaiKhoan";
            db.execSQL(dropTableTaiKhoan);
            String dropTableKhachHang ="drop table if exists TaiKhoan";
            db.execSQL(dropTableKhachHang);
            String dropTableAdmin ="drop table if exists TaiKhoan";
            db.execSQL(dropTableAdmin);
            String dropTableSanPham ="drop table if exists SanPham";
            db.execSQL(dropTableSanPham);
            String dropTableChiTietSanPham ="drop table if exists ChiTietSanPham";
            db.execSQL(dropTableChiTietSanPham);
            String dropTableYeuThich ="drop table if exists YeuThich";
            db.execSQL(dropTableYeuThich);
            String dropTableDanhGia ="drop table if exists DanhGia";
            db.execSQL(dropTableDanhGia);
            String dropTableGioHang ="drop table if exists GioHang";
            db.execSQL(dropTableGioHang);
            String dropTableHoaDon ="drop table if exists HoaDon";
            db.execSQL(dropTableHoaDon);
            String dropTableDanhMuc ="drop table if exists DanhMuc";
            db.execSQL(dropTableDanhMuc);
            String dropTableDonMua ="drop table if exists DonMua";
            db.execSQL(dropTableDonMua);
        onCreate(db);
    }
}
