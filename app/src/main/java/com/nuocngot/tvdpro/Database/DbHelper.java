package com.nuocngot.tvdpro.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME ="NuocNgotTVD";
    private static final int DB_VERSION = 3;

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
                    "soLuongSP INTEGER NOT NULL,"+
                    "giaSP INTEGER NOT NULL)";
    static final String CREATE_TABLE_CHI_TIET_SAN_PHAM =
            "create table ChiTietSanPham (maCTSP INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "maSP INTEGER REFERENCES SanPham(maSP),"+
                    "maDM INTEGER REFERENCES DanhMuc(maDM),"+
                    "hinhAnh TEXT REFERENCES SanPham(hinhAnh),"+
                    "tenSP TEXT REFERENCES SanPham(tenSP),"+
                    "xuatXu TEXT NOT NULL,"+
                    "thongTinSP TEXT  NOT NULL,"+
                    "soLuongSP INTEGER REFERENCES SanPham(soLuongSP),"+
                    "giaSP INTEGER REFERENCES SanPham(giaSP))";
    static final String CREATE_TABLE_YEU_THICH =
            "create table YeuThich (maYT INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "maKH INTEGER REFERENCES KhachHang(maKH),"+
                    "tenKH TEXT REFERENCES KhachHang(tenKH),"+
                    "maSP INTEGER REFERENCES SanPham(maSP),"+
                    "hinhAnh TEXT REFERENCES SanPham(hinhAnh),"+
                    "tenSP TEXT REFERENCES SanPham(tenSP),"+
                    "giaSP INTEGER REFERENCES SanPham(giaSP),"+
                    "soLuongSP INTEGER REFERENCES SanPham(soLuongSP),"+
                    "thoiGian DATE NOT NULL)";
    static final String CREATE_TABLE_DANH_GIA =
            "create table DanhGia (maDG INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "maKH INTEGER REFERENCES KhachHang(maKH),"+
                    "tenKH TEXT REFERENCES KhachHang(tenKH),"+
                    "maSP INTEGER REFERENCES SanPham(maSP),"+
                    "hinhAnh TEXT REFERENCES SanPham(hinhAnh),"+
                    "tenSP TEXT REFERENCES SanPham(tenSP),"+
                    "giaSP INTEGER REFERENCES SanPham(giaSP),"+
                    "soLuongSP INTEGER REFERENCES SanPham(soLuongSP),"+
                    "binhLuon TEXT NOT NULL,"+
                    "thoiGian DATE NOT NULL)";
    static final String CREATE_TABLE_GIO_HANG =
            "create table GioHang (maGH INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "maKH INTEGER REFERENCES KhachHang(maKH),"+
                    "tenKH TEXT REFERENCES KhachHang(tenKH),"+
                    "maSP INTEGER REFERENCES SanPham(maSP),"+
                    "hinhAnh TEXT REFERENCES SanPham(hinhAnh),"+
                    "tenSP TEXT REFERENCES SanPham(tenSP),"+
                    "giaSP INTEGER REFERENCES SanPham(giaSP),"+
                    "soLuongSP INTEGER REFERENCES SanPham(soLuongSP),"+
                    "soLuong INTEGER NOT NULL,"+
                    "tongTien TEXT NOT NULL)";
    static final String CREATE_TABLE_HOA_DON =
            "create table HoaDon (maHD INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "maKH INTEGER REFERENCES KhachHang(maKH),"+
                    "tenKH TEXT REFERENCES KhachHang(tenKH),"+
                    "maSP INTEGER REFERENCES SanPham(maSP),"+
                    "hinhAnh TEXT REFERENCES SanPham(hinhAnh),"+
                    "tenSP TEXT REFERENCES SanPham(tenSP),"+
                    "giaSP INTEGER REFERENCES SanPham(giaSP),"+
                    "soLuongSP INTEGER REFERENCES SanPham(soLuongSP),"+
                    "ngayTT DATE NOT NULL,"+
                    "soLuong INTEGER NOT NULL,"+
                    "tongTien TEXT NOT NULL,"+
                    "diaChi TEXT NOT NULL)";
    static final String CREATE_TABLE_THANH_TOAN =
            "create table ThanhToan (maTToan INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "diaChi TEXT REFERENCES KhachHang(diaChi),"+
                    "maSP INTEGER REFERENCES SanPham(maSP),"+
                    "hinhAnh TEXT REFERENCES SanPham(hinhAnh),"+
                    "tenSP TEXT REFERENCES SanPham(tenSP),"+
                    "giaSP INTEGER REFERENCES SanPham(giaSP),"+
                    "soLuongSP INTEGER REFERENCES SanPham(soLuongSP),"+
                    "phuongThucTT TEXT NOT NULL,"+
                    "tongTienTT TEXT NOT NULL,"+
                    "tongTienPhiVC TEXT NOT NULL,"+
                    "tongThanhToan TEXT NOT NULL,"+
                    "tongTienThanhToan TEXT NOT NULL)";
    static final String CREATE_TABLE_DANH_MUC =
            "create table DanhMuc (maDM INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "tenDM TEXT NOT NULL)";

    static final String CREATE_TABLE_DON_MUA =
            "create table DonMua (maDMUA INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "maSP INTEGER REFERENCES SanPham(maSP),"+
                    "hinhAnh TEXT REFERENCES SanPham(hinhAnh),"+
                    "tenSP TEXT REFERENCES SanPham(tenSP),"+
                    "giaSP INTEGER REFERENCES SanPham(giaSP))";
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
        db.execSQL(CREATE_TABLE_GIO_HANG);
        db.execSQL(CREATE_TABLE_HOA_DON);
        db.execSQL(CREATE_TABLE_DANH_MUC);
        db.execSQL(CREATE_TABLE_DON_MUA);
        db.execSQL(CREATE_TABLE_THANH_TOAN);

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
        String dropTableThanhToan ="drop table if exists DonMua";
        db.execSQL(dropTableThanhToan);
        onCreate(db);
    }


}
