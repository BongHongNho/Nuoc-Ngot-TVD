package com.nuocngot.tvdpro.database;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nuocngot.tvdpro.adapter.Admin;
import com.nuocngot.tvdpro.adapter.CartItem;
import com.nuocngot.tvdpro.adapter.productAdapter;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "user_database";
    private static final int DATABASE_VERSION = 1;

    // Tạo bảng Tài khoản để lưu thông tin đăng nhập và phân quyền
    private static final String CREATE_TABLE_TAI_KHOAN =
            "CREATE TABLE TaiKhoan (" +
                    "maTK INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "maKH INTEGER REFERENCES KhachHang(maKH)," + // Thêm khóa ngoại để liên kết với bảng KhachHang
                    "tenDN TEXT NOT NULL," +
                    "matKhau TEXT NOT NULL," +
                    "Email TEXT NOT NULL," +
                    "SDT TEXT NOT NULL," +
                    "role TEXT NOT NULL," +
                    "userId INTEGER DEFAULT -1," +
                    "isLogin INTEGER DEFAULT 0)";
    private static final String CREATE_TABLE_KHACH_HANG =
            "CREATE TABLE KhachHang (" +
                    "maKH INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "maTK INTEGER REFERENCES TaiKhoan(maTK)," + // Thêm khóa ngoại để liên kết với bảng Tài Khoản
                    "tenKH TEXT NOT NULL," +
                    "Email TEXT NOT NULL," +
                    "SDT TEXT NOT NULL," +
                    "diaChi TEXT NOT NULL," +
                    "capTV TEXT NOT NULL," +
                    "hinhAnh TEXT NOT NULL)";

    // Tạo bảng Admin để lưu thông tin admin
    private static final String CREATE_TABLE_ADMIN =
            "CREATE TABLE Admin (" +
                    "maAdmin INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "maTK INTEGER REFERENCES TaiKhoan(maTK)," + // Thêm khóa ngoại để liên kết với bảng Tài Khoản
                    "tenAdmin TEXT NOT NULL," +
                    "hinhAnh TEXT NOT NULL)";

    private static final String CREATE_TABLE_SAN_PHAM =
            "CREATE TABLE SanPham (maSP INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "maDM INTEGER REFERENCES DanhMuc(maDM)," +
                    "hinhAnh TEXT NOT NULL," +
                    "tenSP TEXT NOT NULL," +
                    "soLuong INTEGER NOT NULL," +
                    "gia INTEGER NOT NULL)";

    private static final String CREATE_TABLE_DANH_MUC =
            "CREATE TABLE DanhMuc (maDM INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "tenDM TEXT NOT NULL)";

    private static final String CREATE_TABLE_CHI_TIET_SAN_PHAM =
            "CREATE TABLE ChiTietSanPham (maCTSP INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "tenSP TEXT NOT NULL," +
                    "maSP INTEGER REFERENCES SanPham(maSP)," +
                    "maDM INTEGER REFERENCES DanhMuc(maDM)," +
                    "hinhAnh TEXT REFERENCES SanPham(hinhAnh)," +
                    "gia INTEGER REFERENCES SanPham(gia)," +
                    "soLuong INTEGER REFERENCES SanPham(soLuong)," +
                    "xuatXu TEXT NOT NULL," +
                    "thongTinSP TEXT NOT NULL)";

    private static final String CREATE_TABLE_YEU_THICH =
            "CREATE TABLE YeuThich (maYT INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "maKH INTEGER REFERENCES KhachHang(maKH)," +
                    "maSP INTEGER REFERENCES SanPham(maSP)," +
                    "thoiGian TEXT NOT NULL)";

    private static final String CREATE_TABLE_DANH_GIA =
            "CREATE TABLE DanhGia (maDG INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "maKH INTEGER REFERENCES KhachHang(maKH)," +
                    "maSP INTEGER REFERENCES SanPham(maSP)," +
                    "binhLuan TEXT NOT NULL," +
                    "thoiGian TEXT NOT NULL)";

    private static final String CREATE_TABLE_GIO_HANG =
            "CREATE TABLE GioHang (maGH INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "tenSP TEXT NOT NULL," +
                    "hinhAnh TEXT NOT NULL," +
                    "maKH INTEGER REFERENCES KhachHang(maKH)," +
                    "maSP INTEGER REFERENCES SanPham(maSP)," +
                    "soLuong INTEGER NOT NULL," +
                    "tongTien INTEGER NOT NULL)";


    private static final String CREATE_TABLE_HOA_DON =
            "CREATE TABLE HoaDon (maHD INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "maKH INTEGER REFERENCES KhachHang(maKH)," +
                    "maSP INTEGER REFERENCES SanPham(maSP)," +
                    "ngayTT TEXT NOT NULL," +
                    "soLuong INTEGER NOT NULL," +
                    "tongTien TEXT NOT NULL," +
                    "diaChi TEXT NOT NULL)";

    private static final String CREATE_TABLE_PHUONGTHUC_THANHTOAN =
            "CREATE TABLE PhuongThucThanhToan (maPTT INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "tenPTT TEXT NOT NULL)";


    private static final String CREATE_TABLE_THANH_TOAN =
            "CREATE TABLE ThanhToan (maTToan INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "maKH INTEGER REFERENCES KhachHang(maKH)," +
                    "maSP INTEGER REFERENCES SanPham(maSP)," +
                    "phuongThucTT TEXT NOT NULL," +
                    "tongTienTT INTEGER NOT NULL," +
                    "tongTienPhiVC INTEGER NOT NULL," +
                    "tongThanhToan INTEGER NOT NULL)";

    private static final String CREATE_TABLE_DON_MUA =
            "CREATE TABLE DonMua (maDMUA INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "maKH INTEGER REFERENCES KhachHang(maKH)," +
                    "maSP INTEGER REFERENCES SanPham(maSP)," +
                    "soLuong INTEGER NOT NULL," +
                    "tongTien INTEGER NOT NULL)";

    //Bổ sung Chi tiết đơn hàng



//    private static String CREATE_TABLE_TRANGTHAI_DONHANG =
//            "CREATE TABLE TrangThaiDonHang (maTTHD INTERGER PRIMARY KEY AUTOINCREMENT, " +
//                    "maKH INTERGER REFERENCES KhachHang(maKH))";


    private static final String COLUMN_HINH_ANH = "hinhAnh";
    private static final String TABLE_ADMIN = "Admin";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_ADMIN);
        db.execSQL("INSERT INTO Admin (maTK, tenAdmin, hinhAnh) VALUES (1, 'Admin1', 'https://media.istockphoto.com/id/1223671392/vector/default-profile-picture-avatar-photo-placeholder-vector-illustration.jpg?s=612x612&w=0&k=20&c=s0aTdmT5aU6b8ot7VKm11DeID6NctRCpB755rA1BIP0=')");
        db.execSQL("INSERT INTO Admin (maTK, tenAdmin, hinhAnh) VALUES (2, 'Admin2', 'https://media.istockphoto.com/id/1223671392/vector/default-profile-picture-avatar-photo-placeholder-vector-illustration.jpg?s=612x612&w=0&k=20&c=s0aTdmT5aU6b8ot7VKm11DeID6NctRCpB755rA1BIP0=')");

        db.execSQL(CREATE_TABLE_TAI_KHOAN);
        db.execSQL("INSERT INTO TaiKhoan (maTK, maKH, tenDN, matKhau, Email, SDT, role) VALUES (1, 1, 'admin', 'admin', 'tannhpph28818@fpt.edu.vn', 0359762830, 'admin')");

        db.execSQL(CREATE_TABLE_KHACH_HANG);
        db.execSQL("INSERT INTO KhachHang (maTK, tenKH, Email, SDT, diaChi, capTV, hinhAnh) VALUES (1, 'Nguyen Huy Phuoc Tan', 'tannhpph28818@fpt.edu.vn', '0359762830', 'Phương Canh - Nam Từ Liêm - Hà Nội', 'VIP', 'https://tea-3.lozi.vn/v1/ship/resized/losupply-quan-tan-phu-quan-tan-phu-ho-chi-minh-1618467447167540212-nuoc-ngot-coca-cola-lon-320ml-0-1626403242?w=480&type=o')");

        db.execSQL(CREATE_TABLE_SAN_PHAM);
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (1, 1, 'https://tea-3.lozi.vn/v1/ship/resized/losupply-quan-tan-phu-quan-tan-phu-ho-chi-minh-1618467447167540212-nuoc-ngot-coca-cola-lon-320ml-0-1626403242?w=480&type=o', 'Coca Cola', 420, 15000)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (2, 2, 'https://onelife.vn/_next/image?url=https%3A%2F%2Fstorage.googleapis.com%2Fonelife-public%2F8934588012112.jpg&w=3840&q=75', 'PepSi', 289, 10000)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (3, 5, 'https://storage.googleapis.com/sc_pcm_product/prod/2023/4/28/2870-92735.jpg', 'Mirinda Soda Kem', 340, 12000)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (4, 5, 'https://www.lottemart.vn/media/catalog/product/cache/0x0/8/9/8934588882111.jpg.webp', 'Mirinda Cam', 460, 14000)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (5, 5, 'https://bizweb.dktcdn.net/100/385/152/products/thung-24-lon-nuoc-ngot-mirinda-huong-xa-xi-320ml-202112181001420466.jpg?v=1682078117373', 'Mirinda Xá Xị', 560, 18000)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (6, 5, 'https://cdn.tgdd.vn/Products/Images/2443/277738/bhx/thung-24-lon-nuoc-ngot-co-ga-mirinda-vi-soda-kem-viet-quat-320ml-202204222257169111.jpg', 'Mirinda Việt Quất', 774, 16000)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (7, 2, 'https://cdn.tgdd.vn/Products/Images/2443/227312/bhx/nuoc-ngot-pepsi-khong-calo-lon-320ml-giao-mau-ngau-nhien-202403011338383885.jpg', 'PepSi Không Đường', 644, 14000)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (8, 2, 'https://cdn.tgdd.vn/Products/Images/2443/227309/bhx/nuoc-ngot-pepsi-khong-calo-vi-chanh-lon-320ml-202403141101085218.jpg', 'PepSi Không Đường Vị Chanh', 324, 15000)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (9, 1, 'https://cdn.tgdd.vn/Products/Images/2443/190309/bhx/nuoc-ngot-coca-cola-light-lon-330ml-202209111853245462.jpg', 'Coca Light', 931, 12000)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (10, 7, 'https://cdn.tgdd.vn/Products/Images/3226/76519/bhx/nuoc-tang-luc-sting-sleek-huong-dau-320ml-202111061723498584.jpg', 'Sting Dâu', 342, 15000)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (11, 7, 'https://cdn.tgdd.vn/Products/Images/3226/91595/bhx/nuoc-tang-luc-sting-vang-lon-cao-330ml-1511201815591.jpg', 'Sting Vàng', 176, 17000)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (12, 6, 'https://cdn.tgdd.vn/Products/Images/3226/142215/bhx/nuoc-tang-luc-monster-energy-lon-355ml-2-org.jpg', 'Monster Energy Black', 546, 24000)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (13, 6, 'https://cdn.tgdd.vn/Products/Images/3226/204869/bhx/nuoc-tang-luc-monster-energy-ultra-355ml-201906241244328046.JPG', 'Monster Energy Ultra', 786, 26000)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (14, 6, 'https://cdn.tgdd.vn/Products/Images/3226/241566/bhx/nuoc-tang-luc-monster-energy-mango-vi-xoai-355ml-202106241424413157.jpg', 'Monster Energy Blue', 653, 25000)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (15, 6, 'https://cdn.tgdd.vn/Products/Images/3226/303442/bhx/nuoc-tang-luc-monster-energy-ultra-paradise-lon-355ml-202303160926153431.jpg', 'Monster Energy Ultra Paradise', 654, 28000)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (16, 10, 'https://cdn.tgdd.vn/Products/Images/2443/76453/bhx/nuoc-ngot-fanta-huong-cam-lon-320ml-202303161607549178.jpg', 'Fanta Hương Cam', 123, 10000)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (17, 10, 'https://cdn.tgdd.vn/Products/Images/2443/282390/bhx/nuoc-ngot-fanta-huong-nho-lon-320ml-202206111337410913.jpg', 'Fanta Hương Nho', 543, 10000)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (18, 10, 'https://cdn.tgdd.vn/Products/Images/2443/85627/bhx/nuoc-ngot-fanta-huong-sa-xi-330ml-201912091405553420.jpg', 'Fanta Hương Xá Xị', 231, 12000)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (19, 10, 'https://cdn.tgdd.vn/Products/Images/2443/143434/bhx/nuoc-ngot-fanta-huong-soda-kem-trai-cay-lon-320ml-202212181657498794.jpg', 'Fanta Hương Soda Kem Trái Cây', 64, 14000)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (20, 10, 'https://cdn.tgdd.vn/Products/Images/2443/140621/bhx/nuoc-ngot-fanta-vi-chanh-thung-24-lon-330ml-201811281413475747.JPG', 'Fanta +C Hương Chanh', 423, 15000)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (21, 9, 'https://cdn.tgdd.vn/Products/Images/3226/76513/bhx/nuoc-tang-luc-redbull-lon-250ml-15112018162747.JPG', 'Bò Húc', 443, 10800)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (22, 9, 'https://cdn.tgdd.vn/Products/Images/3226/274493/bhx/nuoc-tang-luc-redbull-vi-ca-phe-u-lanh-lon-250ml-202203191642118270.jpg', 'Bò Húc Cà Phê', 543, 13000)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (23, 9, 'https://cdn.tgdd.vn/Products/Images/3226/322544/bhx/nuoc-tang-luc-redbull-thai-kem-va-vitamin-250ml-202403091724043647.jpg', 'Redbull Thái kẽm', 342, 15000)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (24, 11, 'https://cdn.tgdd.vn/Products/Images/3226/255054/bhx/nuoc-tang-luc-warrior-huong-nho-lon-325ml-202306191606058109.jpg', 'Warrior hương nho', 442, 11000)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (25, 11, 'https://cdn.tgdd.vn/Products/Images/3226/209214/bhx/nuoc-tang-luc-warrior-huong-dau-lon-325ml-202306191530349145.jpg', 'Warrior hương dâu', 242, 11800)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (26, 1, 'https://cdn.tgdd.vn/Products/Images/3226/209435/bhx/nuoc-tang-luc-coca-cola-energy-250ml-201908151112279900.jpg', 'Coca Cola Energy', 392, 15000)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (27, 1, 'https://cdn.tgdd.vn/Products/Images/2443/203041/bhx/nuoc-ngot-coca-cola-plus-320ml-202012301042189433.jpg', 'Coca Cola Plus', 142, 12600)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (28, 2, 'https://cdn.tgdd.vn/Products/Images/2443/202450/bhx/6-lon-nuoc-ngot-pepsi-light-330ml-201908271538509630.jpg', 'Pepsi Light', 542, 18000)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (29, 2, 'https://cdn.tgdd.vn/Products/Images/2443/262234/bhx/nuoc-ngot-pepsi-cola-sleek-lon-245ml-202402011356459658.jpg', 'Pepsi Cola Sleek', 42, 8600)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (30, 3, 'https://cdn.tgdd.vn/Products/Images/2443/85146/bhx/nuoc-ngot-sprite-huong-chanh-lon-320ml-202306200909131864.jpg', 'Sprite hương chanh', 342, 6600)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (31, 4, 'https://cdn.tgdd.vn/Products/Images/2282/179222/bhx/nuoc-ep-len-men-strongbow-vi-tao-lon-cao-330ml-09112018135624.jpg', 'Strongbow táo', 212, 22600)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (32, 4, 'https://cdn.tgdd.vn/Products/Images/2282/179216/bhx/nuoc-ep-len-men-strongbow-vi-dau-lon-cao-330ml-0911201814930.jpg', 'Strongbow dâu', 542, 20600)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (33, 4, 'https://cdn.tgdd.vn/Products/Images/2282/258076/bhx/strongbow-vi-dao-lon-330ml-202112041759466368.jpg', 'Strongbow vị đào', 543, 24600)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (34, 4, 'https://cdn.tgdd.vn/Products/Images/2282/193599/bhx/nuoc-ep-len-men-strongbow-dark-fruit-lon-cao-330ml-15112018105829.jpg', 'Strongbow dâu đen', 654, 23400)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (35, 4, 'https://cdn.tgdd.vn/Products/Images/2282/179217/bhx/nuoc-ep-len-men-strongbow-vi-dau-loc-4-lon-1511201811447.jpg', 'Strongbow dâu', 142, 21600)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (36, 4, 'https://cdn.tgdd.vn/Products/Images/2282/179225/bhx/nuoc-ep-len-men-strongbow-vi-mat-ong-lon-cao-330ml-0911201814053.jpg', 'Strongbow mật ong', 653, 27000)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (37, 12, 'https://cdn.tgdd.vn/Products/Images/2443/297295/bhx/nuoc-ngot-7-up-vi-chanh-lon-245ml-202312281121487139.jpg', '7 Up vị chanh', 342, 7600)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (38, 12, 'https://cdn.tgdd.vn/Products/Images/2443/312727/bhx/nuoc-ngot-soda-chanh-7-up-lon-320ml-202308160842308679.jpg', 'soda chanh 7 Up', 642, 11600)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (39, 12, 'https://cdn.tgdd.vn/Products/Images/2443/262309/bhx/nuoc-ngot-7-up-it-calo-bo-sung-chat-xo-lon-320ml-202112040854414279.jpg', '7 Up ít calo bổ sung chất xơ', 322, 10600)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (40, 12, 'https://cdn.tgdd.vn/Products/Images/2443/204231/bhx/nuoc-giai-khat-7-up-mojito-huong-chanh-bac-ha-330ml-201906211601069401.jpg', '7 Up Mojito hương chanh bạc hà', 345, 12600)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (41, 13, 'https://cdn.tgdd.vn/Products/Images/2443/84440/bhx/soda-schweppes-lon-330ml-202401091732426460.jpg', 'Soda Schweppes', 522, 16600)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (42, 13, 'https://cdn.tgdd.vn/Products/Images/2443/248710/bhx/nuoc-soda-schweppes-dry-ginger-ale-huong-gung-lon-320ml-202111031005084697.jpg', 'soda Schweppes Dry Ginger Ale hương gừng', 312, 18600)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (43, 13, 'https://cdn.tgdd.vn/Products/Images/2443/79192/bhx/soda-schweppes-tonic-330ml-2-org.jpg', 'Schweppes Tonic', 522, 10600)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (44, 14, 'https://cdn.tgdd.vn/Products/Images/3226/194367/bhx/nuoc-tang-luc-wake-up-247-lon-250ml-1511201814751.JPG', 'Wake Up 247 vị cà phê', 422, 12600)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (45, 15, 'https://cdn.tgdd.vn/Products/Images/3226/214682/bhx/nuoc-tang-luc-carabao-250ml-202006151132180175.jpg', 'Carabao', 234, 16600)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (46, 16, 'https://cdn.tgdd.vn/Products/Images/3226/307642/bhx/nuoc-tang-luc-thums-up-charged-dau-rung-lon-320ml-202305291015130427.jpg', 'Thums Up Charged dâu rừng', 64, 9600)");
        db.execSQL("INSERT INTO SanPham (maSP, maDM, hinhAnh, tenSP, soLuong, gia) VALUES (47, 16, 'https://cdn.tgdd.vn/Products/Images/3226/307586/bhx/nuoc-tang-luc-thums-up-charged-kiwi-lon-320ml-202305291011033482.jpg', 'Thums Up Charged kiwi', 233, 12600)");


        db.execSQL(CREATE_TABLE_CHI_TIET_SAN_PHAM);
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Coca Cola', 1, 1, 10000, 420, 'https://tea-3.lozi.vn/v1/ship/resized/losupply-quan-tan-phu-quan-tan-phu-ho-chi-minh-1618467447167540212-nuoc-ngot-coca-cola-lon-320ml-0-1626403242?w=480&type=o', 'Việt Nam', 'Là loại nước ngọt được nhiều người yêu thích với hương vị thơm ngon, sảng khoái. Nước ngọt Coca Cola 320ml chính hãng nước ngọt Coca Cola với lượng gas lớn sẽ giúp bạn xua tan mọi cảm giác mệt mỏi, căng thẳng, đem lại cảm giác thoải mái sau khi hoạt động ngoài trời.')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('PepSi', 2, 2, 20000, 289, 'https://onelife.vn/_next/image?url=https%3A%2F%2Fstorage.googleapis.com%2Fonelife-public%2F8934588012112.jpg&w=3840&q=75', 'Việt Nam', 'Nước ngọt của thương hiệu Pepsi an toàn, chất lượng được nhà nhà lựa chọn tin dùng. Nước ngọt Pepsi Cola Sleek lon 245ml mang hương vị đặc trưng, thơm ngon hấp dẫn giúp bạn xua tan đi cơn khát ngay tức khắc. Nước ngọt Pepsi được chế biến theo công nghệ hiện đại, an toàn cho sức khỏe người dùng.')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Mirinda Soda Kem', 3, 5, 30000, 340, 'https://storage.googleapis.com/sc_pcm_product/prod/2023/4/28/2870-92735.jpg', 'Việt Nam', 'Nước ngọt giải khát từ thương hiệu nước ngọt Mirinda nổi tiếng được nhiều người ưa chuộng. Nước ngọt Mirinda Kem lon 320ml với hương vị cam đặc trưng, không chỉ giải khát, mà còn bổ sung thêm vitamin C giúp lấy lại năng lượng cho hoạt động hàng ngày. Cam kết chính hãng và an toàn')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Mirinda Cam', 4, 5, 40000, 460, 'https://www.lottemart.vn/media/catalog/product/cache/0x0/8/9/8934588882111.jpg.webp', 'Việt Nam', 'Nước ngọt giải khát từ thương hiệu nước ngọt Mirinda nổi tiếng được nhiều người ưa chuộng. Nước ngọt Mirinda cam lon 320ml với hương vị cam đặc trưng, không chỉ giải khát, mà còn bổ sung thêm vitamin C giúp lấy lại năng lượng cho hoạt động hàng ngày. Cam kết chính hãng và an toàn')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Mirinda Xá Xị', 5, 5, 30000, 560, 'https://bizweb.dktcdn.net/100/385/152/products/thung-24-lon-nuoc-ngot-mirinda-huong-xa-xi-320ml-202112181001420466.jpg?v=1682078117373', 'Việt Nam', 'Nước ngọt giải khát từ thương hiệu nước ngọt Mirinda nổi tiếng được nhiều người ưa chuộng. Nước ngọt Mirinda Xá Xị lon 320ml với hương vị cam đặc trưng, không chỉ giải khát, mà còn bổ sung thêm vitamin C giúp lấy lại năng lượng cho hoạt động hàng ngày. Cam kết chính hãng và an toàn')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Mirinda Việt Quất', 6, 5, 20000, 774, 'https://cdn.tgdd.vn/Products/Images/2443/277738/bhx/thung-24-lon-nuoc-ngot-co-ga-mirinda-vi-soda-kem-viet-quat-320ml-202204222257169111.jpg', 'Việt Nam', 'Nước ngọt Mirinda soda kem vị việt quất ngọt ngào tươi mới, vị soda kem bùng nổ cùng hương việt quất thơm ngon. Hãy mua thùng 24 lon nước ngọt có ga Mirinda vị soda kem việt quất 320ml để cảm nhận vị ngon khó cưỡng và cùng thưởng thức nước ngọt này với bạn bè, người thân nhé!')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('PepSi Không Đường', 7, 2, 10000, 644, 'https://cdn.tgdd.vn/Products/Images/2443/227312/bhx/nuoc-ngot-pepsi-khong-calo-lon-320ml-giao-mau-ngau-nhien-202403011338383885.jpg', 'Việt Nam', 'Là loại nước ngọt được nhiều người yêu thích đến từ thương hiệu nước ngọt Pepsi nổi tiếng thế giới với hương vị thơm ngon, sảng khoái. Nước ngọt Pepsi không calo lon 320ml với lượng gas lớn sẽ giúp bạn xua tan mọi cảm giác mệt mỏi, căng thẳng, sản phẩm không calo lành mạnh, tốt cho sức khỏe')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('PepSi Không Đường Vị Chanh', 8, 2, 15000, 324, 'https://cdn.tgdd.vn/Products/Images/2443/227309/bhx/nuoc-ngot-pepsi-khong-calo-vi-chanh-lon-320ml-202403141101085218.jpg', 'Việt Nam', 'Sự kết hợp hài hòa của vị chanh thanh mát, giải nhiệt và mang lại cảm giác sảng khoái và tốt cho sức khỏe. Nước ngọt Pepsi không calo vị chanh lon 320ml cực kỳ thích hợp cho những người thích uống nước ngọt nhưng vẫn muốn giữ lối sống ăn thanh đạm, ít đường. Sản phẩm chất lượng từ nước ngọt Pepsi')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Coca Light', 9, 1, 10000, 931, 'https://cdn.tgdd.vn/Products/Images/2443/190309/bhx/nuoc-ngot-coca-cola-light-lon-330ml-202209111853245462.jpg', 'Việt Nam', 'Từ thương hiệu nước ngọt nổi tiếng thế giới được ưa chuộng tại nhiều nhiều quốc gia. Nước ngọt không đường Coca Cola Light lon 320ml chính hãng nước ngọt Coca Cola là dòng sản phẩm nước uống có ga không đường, dành cho người ăn kiêng, không lo tăng cân')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Sting Dâu', 10, 7, 15000, 342, 'https://cdn.tgdd.vn/Products/Images/3226/76519/bhx/nuoc-tang-luc-sting-sleek-huong-dau-320ml-202111061723498584.jpg', 'Việt Nam', 'Nước tăng lực Sting với mùi vị thơm ngon, sảng khoái, cùng hương dâu dễ chịu. Sting giúp cơ thể bù đắp nước, bổ sung năng lượng, vitamin C và E, giúp xua tan cơn khát và cảm giác mệt mỏi cùng dâu cho nhẹ nhàng và dễ chịu. Cam kết chính hãng, chất lượng và an toàn')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('String Vàng', 11, 7, 17000, 176, 'https://cdn.tgdd.vn/Products/Images/3226/91595/bhx/nuoc-tang-luc-sting-vang-lon-cao-330ml-1511201815591.jpg', 'Việt Nam', 'Nước tăng lực Sting với thành phần tự nhiên kết hợp với hương vị nhân sâm tạo nên sự kết hợp độc đáo với mùi vị thơm ngon, sảng khoái. Sản phẩm giúp cơ thể bù đắp nước, bổ sung năng lượng, vitamin C và E, giúp xua tan cơn khát và cảm giác mệt mỏi, cho bạn cảm giác cuộn trào hứng khởi')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Monster Energy Black', 12, 6, 24000, 546, 'https://cdn.tgdd.vn/Products/Images/3226/142215/bhx/nuoc-tang-luc-monster-energy-lon-355ml-2-org.jpg', 'Việt Nam', 'Với thành phần chính bao gồm các loại vitamin và thảo dược sẽ cung cấp cho bạn vitamin B3, vitamin B6, vitamin B12, natri giúp cơ thể bạn khỏe mạnh. Sản phẩm với hương vị tươi mát sẽ lập tức đập tan cơn khát và giải tỏa cái nóng của mùa hè, đem đến cho người dùng sự tỉnh táo và sảng khoái.')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Monster Energy Ultra', 13, 6, 26000, 786, 'https://cdn.tgdd.vn/Products/Images/3226/204869/bhx/nuoc-tang-luc-monster-energy-ultra-355ml-201906241244328046.JPG', 'Việt Nam', 'Nước tăng lực thơm ngon sản xuất tại Hà Lan. Nước tăng lực Monster Energy Ultra 355ml chính hãng nước tăng lực Monster Energy giải khát nhanh chóng, cug cấp cho cơ thể nguồn năng lượng mạnh mẽ, thể hiện đẳng cấp, phong cách sống khác biệt của những người trẻ năng động')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Monster Energy Blue', 14, 6, 25000, 653, 'https://cdn.tgdd.vn/Products/Images/3226/241566/bhx/nuoc-tang-luc-monster-energy-mango-vi-xoai-355ml-202106241424413157.jpg', 'Việt Nam', 'Nước tăng lực dành cho thế hệ trẻ dám sống khác biệt và đương đầu với thử thách mới. Nước tăng lực Monster Energy Mango vị xoài 355ml kết hợp độc đáo cùng nước ép xoài chua ngọt thanh mát, mang lại trải nghiệm mới lạ đầy thích thú. Sản phẩm chất lượng chính hãng thương hiệu nước tăng lực Monster')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Monster Energy Ultra Paradise', 15, 6, 28000, 654, 'https://cdn.tgdd.vn/Products/Images/3226/303442/bhx/nuoc-tang-luc-monster-energy-ultra-paradise-lon-355ml-202303160926153431.jpg', 'Việt Nam', 'Nước tăng lực hương vị táo và kiwi thơm hấp dẫn, vị ga sảng khoái, dồi dào các thành phần dinh dưỡng giúp tăng lực hấp dẫn như vitamin, taurine, caffeine, inositol,...Nước tăng lực Monster Ultra Paradise 355ml chính hãng nước tăng lực Monster được sản xuất tại Malaysia cho bạn cảm giác sảng khoái')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Fanta Hương Cam', 16, 10, 10000, 123, 'https://cdn.tgdd.vn/Products/Images/2443/76453/bhx/nuoc-ngot-fanta-huong-cam-lon-320ml-202303161607549178.jpg', 'Việt Nam', 'Sản phẩm nước ngọt có gas của thương hiệu nước ngọt Fanta nổi tiếng giúp giải khát sau khi hoạt động ngoài trời, giải tỏa căng thẳng, mệt mỏi khi học tập, làm việc. Nước ngọt Fanta hương cam lon 320ml thơm ngon kích thích vị giác, chứa nhiều vitamin C sẽ cung cấp năng lượng cho cơ thể')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Fanta Hương Nho', 17, 10, 10000, 543, 'https://cdn.tgdd.vn/Products/Images/2443/282390/bhx/nuoc-ngot-fanta-huong-nho-lon-320ml-202206111337410913.jpg', 'Việt Nam', 'Sản phẩm nước ngọt có ga thơm ngon hấp dẫn đến từ thương hiệu nước ngọt Fanta nổi tiếng trên thế giới. Nước ngọt Fanta hương nho lon 320ml chua ngọt tươi mới, chai lớn phù hợp sử dụng cho gia đình, nhóm bạn bè mang đến cảm giác sảng khoái, hứng khởi')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Fanta Hương Xá Xị', 18, 10, 12000, 231, 'https://cdn.tgdd.vn/Products/Images/2443/85627/bhx/nuoc-ngot-fanta-huong-sa-xi-330ml-201912091405553420.jpg', 'Việt Nam', 'Là sản phẩm nước ngọt có gas của thương hiệu nước ngọt Fanta nổi tiếng giúp giải khát sau khi hoạt động ngoài trời, giải tỏa căng thẳng, mệt mỏi khi học tập, làm việc. Nước ngọt Fanta hương xá xị lon 320ml thơm ngon kích thích vị giác, cung cấp năng lượng cho cơ thể.')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Fanta Hương Soda Kem Trái Cây', 19, 10, 14000, 64, 'https://cdn.tgdd.vn/Products/Images/2443/143434/bhx/nuoc-ngot-fanta-huong-soda-kem-trai-cay-lon-320ml-202212181657498794.jpg', 'Việt Nam', 'Sản phẩm nước ngọt có gas của thương hiệu nước ngọt Fanta nổi tiếng giúp giải khát sau khi hoạt động ngoài trời, giải tỏa căng thẳng, mệt mỏi khi học tập, làm việc. Nước ngọt Fanta hương trái cây lon 320ml thơm ngon vị trái cây độc đáo giúp xua tan cơn khát và kích thích vị giác.')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Fanta +C Hương Chanh', 20, 10, 15000, 423, 'https://cdn.tgdd.vn/Products/Images/2443/140621/bhx/nuoc-ngot-fanta-vi-chanh-thung-24-lon-330ml-201811281413475747.JPG', 'Việt Nam', 'Là sản phẩm nước ngọt có gas của thương hiệu Fanta nổi tiếng. 24 lon nước ngọt Fanta +C hương chanh 330ml với hương chanh tự nhiên mang lại cảm giác thư thái, tràn đầy năng lượng.Nước ngọt Fanta +C hương chanh chứa nhiều vitamin C sẽ cung cấp năng lượng cho cơ thể trong một ngày dài năng động.')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Bò Húc', 21, 9, 10800, 443, 'https://cdn.tgdd.vn/Products/Images/3226/76513/bhx/nuoc-tang-luc-redbull-lon-250ml-15112018162747.JPG', 'Việt Nam', 'Nước tăng lực Redbull thành phần tự nhiên, mùi vị thơm ngon, sảng khoái. Nước tăng lực Redbull 250ml giúp cơ thể bù đắp nước, bổ sung năng lượng, vitamin và các khoáng chất, giúp xua tan cơn khát và cảm giác mệt mỏi. Nước tăng lực không có đường hóa học, không chứa hóa chất độc hại, đảm bảo an toàn')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Bò Húc Cà Phê', 22, 9, 13000, 543, 'https://cdn.tgdd.vn/Products/Images/3226/274493/bhx/nuoc-tang-luc-redbull-vi-ca-phe-u-lanh-lon-250ml-202203191642118270.jpg', 'Việt Nam', 'Nước tăng lực vị cà phê ủ lạnh hấp dẫn mới lạ của thương hiệu nước tăng lực Redbull rất được yêu thích. Thùng 24 lon nước tăng lực Redbull vị cà phê ủ lạnh lon 250ml giá tốt tiết kiệm phù hợp cho gia đình, quán nước, nhóm bạn bè,...mang đến nguồn năng lượng bứt phá')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Redbull Thái kẽm', 23, 9, 15000, 342, 'https://cdn.tgdd.vn/Products/Images/3226/322544/bhx/nuoc-tang-luc-redbull-thai-kem-va-vitamin-250ml-202403091724043647.jpg', 'Việt Nam', 'Nước tăng lực Redbull thành phần tự nhiên, thơm ngon, sảng khoái là thương hiệu nước tăng lực rất được ưa thích trên thế giới. Nước tăng lực Redbull Thái Kẽm Vitamin 250ml bổ sung thêm kẽm, vitamin và nhiều dinh dưỡng  giúp cơ thể bù đắp nước, bổ sung năng lượng cho bạn hoạt động dẻo dai')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Warrior hương nho', 24, 11, 11000, 442, 'https://cdn.tgdd.vn/Products/Images/3226/255054/bhx/nuoc-tang-luc-warrior-huong-nho-lon-325ml-202306191606058109.jpg', 'Việt Nam', 'Nước uống tăng lực chất lượng từ thương hiệu nước tăng lực Warrior. Nước tăng lực Warrior hương nho lon 325ml với thiết kế sang trọng dễ cầm, có hương vị nho thơm ngon, ngọt dịu, đồng thời bổ sung vitamin nhóm B (B3, B6, B12) mang đến nguồn năng lượng mạnh mẽ và bền bỉ')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Warrior hương dâu', 25, 11, 11800, 242, 'https://cdn.tgdd.vn/Products/Images/3226/209214/bhx/nuoc-tang-luc-warrior-huong-dau-lon-325ml-202306191530349145.jpg', 'Việt Nam', 'Nước tăng lực vị dâu thơm ngon, ngọt dịu mà không gắt cổ, Warrior hương dâu 325ml chính hãng nước tăng lực Warrior được mệnh danh là “chiến binh năng lượng” chứa vitamin B3, B6, B12 giúp thúc đẩy quá trình trao đổi chất, cung cấp năng lượng bền bỉ cho cả thể chất và não bộ, duy trì sự tỉnh táo')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Coca Cola Energy', 26, 1, 15000, 392, 'https://cdn.tgdd.vn/Products/Images/3226/209435/bhx/nuoc-tang-luc-coca-cola-energy-250ml-201908151112279900.jpg', 'Việt Nam', 'Nước tăng lực chất lượng đến từ thương hiệu nước tăng lực Coca Cola. Nước tăng lực Coca Cola 250ml chứa nhiều vitamin và khoáng chất mang lại nguồn năng lượng tỉnh táo tức thời, đồng thời giải khát nhanh cho cơ thể giúp bạn bừng năng lượng, sảng khoái tức thì.')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Coca Cola Plus', 27, 1, 12600, 142, 'https://cdn.tgdd.vn/Products/Images/2443/203041/bhx/nuoc-ngot-coca-cola-plus-320ml-202012301042189433.jpg', 'Việt Nam', 'Từ thương hiệu nước ngọt giải khát nổi tiếng thế giới được ưa chuộng tại nhiều nhiều quốc gia. Nước ngọt Coca Cola Plus 320ml chính hãng nước ngọt Coca Cola bổ sung thêm chất xơ dinh dưỡng, không đường không calo giúp cơ thể hạn chế hấp thu chất béo cho cơ thể sảng khoái nhẹ nhàng')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Pepsi Light', 28, 2, 18000, 542, 'https://cdn.tgdd.vn/Products/Images/2443/202450/bhx/6-lon-nuoc-ngot-pepsi-light-330ml-201908271538509630.jpg', 'Việt Nam', 'Từ thương hiệu nước ngọt Pepsi nổi tiếng toàn cầu có mùi vị thơm ngon với hỗn hợp hương tự nhiên giúp xua tan cơn khát và cảm giác mệt mỏi. 6 lon Pepsi Light không đường lon 330ml giúp bổ sung năng lượng làm việc mỗi ngày, không chứa đường phù hợp cho người tiểu đường hoặc kiêng đồ ngọt')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Pepsi Cola Sleek', 29, 2, 8600, 42, 'https://cdn.tgdd.vn/Products/Images/2443/262234/bhx/nuoc-ngot-pepsi-cola-sleek-lon-245ml-202402011356459658.jpg', 'Việt Nam', 'Nước ngọt của thương hiệu Pepsi an toàn, chất lượng được nhà nhà lựa chọn tin dùng. Nước ngọt Pepsi Cola Sleek lon 245ml mang hương vị đặc trưng, thơm ngon hấp dẫn giúp bạn xua tan đi cơn khát ngay tức khắc. Nước ngọt Pepsi được chế biến theo công nghệ hiện đại, an toàn cho sức khỏe người dùng.')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Sprite hương chanh', 30, 3, 6600, 342, 'https://cdn.tgdd.vn/Products/Images/2443/85146/bhx/nuoc-ngot-sprite-huong-chanh-lon-320ml-202306200909131864.jpg', 'Việt Nam', 'Nước ngọt có ga thơm ngon, được ưa chuộng tại hơn 190 quốc gia. Nước ngọt Sprite hương chanh lon 320ml vị chanh tươi mát cùng, vị ga bùng nổ sảng khoái, giúp bạn đập tan cơn khát, kích thích vị giác giúp bạn ăn ngon miệng hơn. Sản phẩm cam kết chính hãng nước ngọt Sprite chất lượng và an toàn')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Strongbow táo', 31, 4, 22600, 212, 'https://cdn.tgdd.vn/Products/Images/2282/179222/bhx/nuoc-ep-len-men-strongbow-vi-tao-lon-cao-330ml-09112018135624.jpg', 'Việt Nam', 'Nước táo lên men Strongbow là nước uống có cồn độ ngọt dịu và hậu vị sang trọng kéo dài. Strongbow táo lon 330ml vị táo nguyên bản sẽ làm cho bạn dễ chịu và khi thưởng thức sẽ không cảm nhận được nhiều vị cồn vì thức uống này được lên men trực tiếp từ trái cây')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Strongbow dâu', 32, 4, 20600, 542, 'https://cdn.tgdd.vn/Products/Images/2282/179216/bhx/nuoc-ep-len-men-strongbow-vi-dau-lon-cao-330ml-0911201814930.jpg', 'Việt Nam', 'Thức uống có cồn nổi tiếng có nguồn gốc từ châu Âu. Strongbow dâu lon 330ml là sự kết hợp tinh tế giữa hương quả lựu, mâm xôi, quả lý và dâu tây, nước táo lên men Strongbow là loại thức uống có vị ngọt của dâu đỏ hòa quyện với vị chua thanh từ táo.')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Strongbow vị đào', 33, 4, 24600, 543, 'https://cdn.tgdd.vn/Products/Images/2282/258076/bhx/strongbow-vi-dao-lon-330ml-202112041759466368.jpg', 'Việt Nam', 'Thức uống có cồn thơm ngon nguồn gốc từ Châu Âu, nước táo lên men Strongbow trở nên quen thuộc với giới trẻ. Được chế biến từ quá trình lên men táo tự nhiên, mang lại men say thuần khiết. Strongbow vị đào lon 330ml mang vị chua ngọt thơm hương đào tự nhiên cho bạn cảm giác hứng khởi, sảng khoái')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Strongbow dâu đen', 34, 4, 23400, 654, 'https://cdn.tgdd.vn/Products/Images/2282/193599/bhx/nuoc-ep-len-men-strongbow-dark-fruit-lon-cao-330ml-15112018105829.jpg', 'Việt Nam', 'Nước có cồn được chế biến đầy ấn tượng từ quá trình lên men táo tự nhiên mang đến men say thuần khiết, hài hòa và đầy cuốn hút, chính hãng nước táo lên men Strongbow. Strongbow dâu đen lon 330ml ngọt dịu, thanh mát và chát nhẹ đặc trưng của táo cùng độ men vừa phải đủ để lâng lâng hứng khởi.')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Strongbow dâu', 35, 4, 21600, 142, 'https://cdn.tgdd.vn/Products/Images/2282/179217/bhx/nuoc-ep-len-men-strongbow-vi-dau-loc-4-lon-1511201811447.jpg', 'Việt Nam', 'Nước táo lên men Strongbow là thức uống nổi tiếng có nguồn gốc từ châu Âu. Strongbow lon chai 330ml là cự kết hợp tinh tế giữa hương quả lựu, mâm xôi, quả lý và dâu tây, tạo nên loại thức uống có vị ngọt của dâu đỏ hòa quyện với vị chua thanh từ táo, trở thành thức uống hấp dẫn, thơm ngon.')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Strongbow mật ong', 36, 4, 27000, 653, 'https://cdn.tgdd.vn/Products/Images/2282/179225/bhx/nuoc-ep-len-men-strongbow-vi-mat-ong-lon-cao-330ml-0911201814053.jpg', 'Việt Nam', 'Táo được lên men tự nhiên mang đến men say thuần khiết, hài hòa và đầy cuốn hút. Strongbow mật ong lon 330ml mang một chút ngọt dịu, thanh mát kết hợp cùng vị chát nhẹ đặc trưng của táo cùng độ men vừa phải đủ để lâng lâng hứng khởi. Là một lựa chọn hấp dẫn cho những cuộc vui thêm trọn vẹn')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('7 Up vị chanh', 37, 12, 7600, 342, 'https://cdn.tgdd.vn/Products/Images/2443/297295/bhx/nuoc-ngot-7-up-vi-chanh-lon-245ml-202312281121487139.jpg', 'Việt Nam', 'Nước ngọt chính hãng thương hiệu nước ngọt 7 Up uy tín được ưa chuộng trên thế giới. Nước ngọt 7 Up vị chanh lon 235ml với thiết kế lon nhỏ gọn tiện dụng, hương vị chanh tươi mát và ga sảng khoái cho bạn giải khát, mang đến nguồn năng lượng dồi dào, bao bì trẻ trung năng động phù hợp với giới trẻ')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('soda chanh 7 Up', 38, 12, 11600, 642, 'https://cdn.tgdd.vn/Products/Images/2443/312727/bhx/nuoc-ngot-soda-chanh-7-up-lon-320ml-202308160842308679.jpg', 'Việt Nam', 'Nước ngọt chính hãng thương hiệu nước ngọt 7Up uy tín được nhiều người ưa chuộng. Nước ngọt soda chanh 7 Up lon 320ml chứa nước ép chanh thật, không đường không calo, cung cấp vitamin C và mang đến cảm giác sảng khoái, tràn đầy sức sống')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('7 Up ít calo bổ sung chất xơ', 39, 12, 10600, 322, 'https://cdn.tgdd.vn/Products/Images/2443/262309/bhx/nuoc-ngot-7-up-it-calo-bo-sung-chat-xo-lon-320ml-202112040854414279.jpg', 'Việt Nam', 'Nước ngọt thơm ngon bổ sung năng lượng suốt ngày dài của 7 up là sự lựa chọn tuyệt vời cho bạn. Nước ngọt 7 Up ít calo bổ sung chất xơ lon 320ml thơm ngon, ít calo và đặc biệt giàu chất xơ tốt cho sức khỏe. Thành phần an toàn đã được kiểm nghiệm nên bạn có thể yên tâm sử dụng.')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('7 Up Mojito hương chanh bạc hà', 40, 12, 12600, 345, 'https://cdn.tgdd.vn/Products/Images/2443/204231/bhx/nuoc-giai-khat-7-up-mojito-huong-chanh-bac-ha-330ml-201906211601069401.jpg', 'Việt Nam', 'Từ thương hiệu nước giải khát 7Up được nhiều người ưa chuộng. Nước giải khát 7 Up Mojito hương chanh bạc hà 330ml kết hợp bạc hà mát lạnh và hương chanh tự nhiên, giúp xua tan nhanh cơn khát, giảm cảm giác ngấy, kích thích vị giác giúp ăn ngon hơn, cung cấp năng lượng cho tinh thần tươi vui mỗi ngày')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Soda Schweppes', 41, 13, 16600, 522, 'https://cdn.tgdd.vn/Products/Images/2443/84440/bhx/soda-schweppes-lon-330ml-202401091732426460.jpg', 'Việt Nam', 'Nước ngọt sản xuất theo dây chuyền công nghệ hiện đại kiểm định nghiêm ngặt. Nước Soda Schweppes lon 320ml là thức uống giải khát giúp bổ sung vitamin và khoáng chất tốt cho cơ thể, giúp hanh chóng để bù nước cho cơ thể. Cam kết chất lượng an toàn từ thương hiệu nước ngọt Schweppes')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('soda Schweppes Dry Ginger Ale hương gừng',42, 13, 18600, 312, 'https://cdn.tgdd.vn/Products/Images/2443/248710/bhx/nuoc-soda-schweppes-dry-ginger-ale-huong-gung-lon-320ml-202111031005084697.jpg', 'Việt Nam', 'Nước ngọt Schweppes sử dụng những hương liệu chiết xuất từ thiên nhiên, công thức riêng biệt kết hợp gừng cay nhẹ, nước ngọt có ga với vị mạnh mẽ mang tới cảm giác sảng khoái. Nước Soda Schweppes Dry Ginger Ale lon 320ml có thể dùng ngay hoặc dùng để pha chế các loại đồ uống hấp dẫn')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Schweppes Tonic', 43, 13, 10600, 522, 'https://cdn.tgdd.vn/Products/Images/2443/79192/bhx/soda-schweppes-tonic-330ml-2-org.jpg', 'Việt Nam', 'Sản xuất theo dây chuyền công nghệ hiện đại kiểm định nghiêm ngặt. Nước giải khát có gas Schweppes Tonic 330ml có thể dùng pha với các loại rượu hay chế biến các loại thức uống thơm ngon, lạ miệng, giàu chất khoáng và Vitamin. Nước ngọt chất lượng an toàn từ thương hiệu nước ngọt Schweppes')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Wake Up 247 vị cà phê', 44, 14, 12600, 422, 'https://cdn.tgdd.vn/Products/Images/3226/194367/bhx/nuoc-tang-luc-wake-up-247-lon-250ml-1511201814751.JPG', 'Việt Nam', 'Với các thành phần tự nhiên an toàn cho sức khỏe, bổ sung thêm vitamin và dưỡng chất cần thiết cho cơ thể. Nước tăng lực Wake Up 247 vị cà phê 250ml có mùi vị thơm ngon, sảng khoái cùng hương cà phê thơm dễ chịu. Sản phẩm nước uống tăng lực chính hãng từ thương hiệu Wake up 247')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Carabao', 45, 15, 16600, 234, 'https://cdn.tgdd.vn/Products/Images/3226/214682/bhx/nuoc-tang-luc-carabao-250ml-202006151132180175.jpg', 'Việt Nam', 'Nước tăng lực đến từ Thái Lan với hương vị đặc trưng, chứa các thành phần taurine, vitamin B3, B6, B12 bổ sung năng lượng. Nước tăng lực Carabao 250ml sản xuất trên dây chuyền công nghệ hiện đại, trải qua kiểm tra khắt khe trước khi xuất khẩu, uy tín chất lượng từ nước tăng lực Carabao')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Thums Up Charged dâu rừng', 46, 16, 9600, 64, 'https://cdn.tgdd.vn/Products/Images/3226/307642/bhx/nuoc-tang-luc-thums-up-charged-dau-rung-lon-320ml-202305291015130427.jpg', 'Việt Nam', 'Nước tăng lực chính hãng nước tăng lực Thums Up Charged từ Coca Cola. Nước tăng lực Thums Up Charged dâu rừng 320ml bổ sung vitamin B3, kẽm và caffeine, cho bạn cuộn trào năng lượng, bừng tỉnh táo')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Thums Up Charged kiwi', 47, 16, 12600, 233, 'https://cdn.tgdd.vn/Products/Images/3226/307586/bhx/nuoc-tang-luc-thums-up-charged-kiwi-lon-320ml-202305291011033482.jpg', 'Việt Nam', 'Sản phẩm nước tăng lực thơm ngon hấp dẫn với hương vị kiwi tươi mát, mới mẻ chính hãng nước tăng lực Thums Up Charged. Nước tăng lực Thums Up Charged kiwi 320ml tiếp thêm sinh lực, bừng tỉnh táo, bổ sung caffein, vitamin B3 và khoáng chất kẽm')");



        db.execSQL(CREATE_TABLE_DANH_MUC);
        db.execSQL("INSERT INTO DanhMuc (maDM, tenDM) VALUES (1, 'Coca-Cola')");//
        db.execSQL("INSERT INTO DanhMuc (maDM, tenDM) VALUES (2, 'Pepsi')");//
        db.execSQL("INSERT INTO DanhMuc (maDM, tenDM) VALUES (3, 'Sprite')");//
        db.execSQL("INSERT INTO DanhMuc (maDM, tenDM) VALUES (4, 'Strong Bow')");//
        db.execSQL("INSERT INTO DanhMuc (maDM, tenDM) VALUES (5, 'Mirinda')");//
        db.execSQL("INSERT INTO DanhMuc (maDM, tenDM) VALUES (6, 'Monster')");//
        db.execSQL("INSERT INTO DanhMuc (maDM, tenDM) VALUES (7, 'Sting')");//
        db.execSQL("INSERT INTO DanhMuc (maDM, tenDM) VALUES (8, 'Moutain Dew')");
        db.execSQL("INSERT INTO DanhMuc (maDM, tenDM) VALUES (9, 'Bò Húc')");//
        db.execSQL("INSERT INTO DanhMuc (maDM, tenDM) VALUES (10, 'FanTa')");//
        db.execSQL("INSERT INTO DanhMuc (maDM, tenDM) VALUES (11, 'Warrior')");//
        db.execSQL("INSERT INTO DanhMuc (maDM, tenDM) VALUES (12, '7 Up')");//
        db.execSQL("INSERT INTO DanhMuc (maDM, tenDM) VALUES (13, 'Soda Schweppes')");//
        db.execSQL("INSERT INTO DanhMuc (maDM, tenDM) VALUES (14, 'Wake Up 247')");//
        db.execSQL("INSERT INTO DanhMuc (maDM, tenDM) VALUES (15, 'Carabao')");//
        db.execSQL("INSERT INTO DanhMuc (maDM, tenDM) VALUES (16, 'Thums Up Charged')");//


        db.execSQL(CREATE_TABLE_YEU_THICH);
        db.execSQL("INSERT INTO YeuThich (maKH, maSP, thoiGian) VALUES (1, 1, '2024-03-20')");
        db.execSQL("INSERT INTO YeuThich (maKH, maSP, thoiGian) VALUES (2, 2, '2024-03-20')");

        db.execSQL(CREATE_TABLE_DANH_GIA);
        db.execSQL("INSERT INTO DanhGia (maKH, maSP, binhLuan, thoiGian) VALUES (1, 1, 'Rất tốt', '2024-03-20')");
        db.execSQL("INSERT INTO DanhGia (maKH, maSP, binhLuan, thoiGian) VALUES (2, 2, 'Tốt', '2024-03-20')");

        db.execSQL(CREATE_TABLE_GIO_HANG);
        db.execSQL(CREATE_TABLE_HOA_DON);
//        db.execSQL("INSERT INTO HoaDon (maKH, maSP, ngayTT, soLuong, tongTien, diaChi) VALUES (1, 1, '2024-03-20', 2, 20000, 'Địa chỉ 1')");
//        db.execSQL("INSERT INTO HoaDon (maKH, maSP, ngayTT, soLuong, tongTien, diaChi) VALUES (2, 2, '2024-03-20', 1, 20000, 'Địa chỉ 2')");

        db.execSQL(CREATE_TABLE_DON_MUA);
//        db.execSQL("INSERT INTO DonMua (maSP, soLuong, tongTien) VALUES (1, 1, 10000)");
//        db.execSQL("INSERT INTO DonMua (maSP, soLuong, tongTien) VALUES (2, 2, 40000)");

        db.execSQL(CREATE_TABLE_PHUONGTHUC_THANHTOAN);
        db.execSQL("INSERT INTO PhuongThucThanhToan (maPTT, tenPTT) VALUES (1, 'Thanh toán khi nhận hàng')");
        db.execSQL("INSERT INTO PhuongThucThanhToan (maPTT, tenPTT) VALUES (2, 'Thanh toán qua ngân hàng')");



        db.execSQL(CREATE_TABLE_THANH_TOAN);
        db.execSQL("INSERT INTO ThanhToan (maKH, maSP, phuongThucTT, tongTienTT, tongTienPhiVC, tongThanhToan) VALUES (1, 1, 'Thanh toán khi nhận hàng', 20000, 5000, 25000)");
        db.execSQL("INSERT INTO ThanhToan (maKH, maSP, phuongThucTT, tongTienTT, tongTienPhiVC, tongThanhToan) VALUES (2, 2, 'Thanh toán online', 40000, 5000, 45000)");



    }

    public List<productAdapter> getAllProducts() {
        List<productAdapter> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM SanPham", null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int idSP = cursor.getInt(cursor.getColumnIndex("maSP"));
                    String hinhAnh = cursor.getString(cursor.getColumnIndex("hinhAnh"));
                    String tenSP = cursor.getString(cursor.getColumnIndex("tenSP"));
                    int soLuongSP = cursor.getInt(cursor.getColumnIndex("soLuongSP"));
                    int giaSP = cursor.getInt(cursor.getColumnIndex("giaSP"));
                    productList.add(new productAdapter(idSP, tenSP, giaSP, hinhAnh, soLuongSP));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return productList;
    }

    public Admin getAdminInfo() {
        Admin admin = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM Admin";
            cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                admin = new Admin();
                admin.setTenAdmin(cursor.getString(cursor.getColumnIndex("tenAdmin")));
                admin.setHinhAnh(cursor.getString(cursor.getColumnIndex("hinhAnh")));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while fetching admin info: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return admin;
    }

    public void updateAdminAvatar(String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HINH_ANH, imageUrl);
        int rowsAffected = db.update(TABLE_ADMIN, values, null, null);
        db.close();
        Log.d(TAG, "Number of rows affected: " + rowsAffected);
    }

    public void addCartItem(CartItem cartItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("maKH", cartItem.getCustomerId());
        values.put("tenKH", cartItem.getCustomerName());
        values.put("maSP", cartItem.getProductId());
        values.put("hinhAnh", cartItem.getProductImage());
        values.put("tenSP", cartItem.getProductName());
        values.put("giaSP", cartItem.getProductPrice());
        values.put("soLuong", cartItem.getQuantityInStock());
        values.put("soLuongSP", cartItem.getProductQuantity());
        int totalPrice = cartItem.getProductPrice() * cartItem.getProductQuantity();
        values.put("tongTien", totalPrice);
        db.insert("GioHang", null, values);
        db.close();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS TaiKhoan");
        db.execSQL("DROP TABLE IF EXISTS KhachHang");
        db.execSQL("DROP TABLE IF EXISTS Admin");
        db.execSQL("DROP TABLE IF EXISTS SanPham");
        db.execSQL("DROP TABLE IF EXISTS ChiTietSanPham");
        db.execSQL("DROP TABLE IF EXISTS YeuThich");
        db.execSQL("DROP TABLE IF EXISTS DanhGia");
        db.execSQL("DROP TABLE IF EXISTS GioHang");
        db.execSQL("DROP TABLE IF EXISTS HoaDon");
        db.execSQL("DROP TABLE IF EXISTS DanhMuc");
        db.execSQL("DROP TABLE IF EXISTS DonMua");
        db.execSQL("DROP TABLE IF EXISTS ThanhToan");
        onCreate(db);
    }

    public CartItem getCartItem(int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        CartItem cartItem = null;

        String[] projection = {
                "maSP",
                "tenSP",
                "hinhAnh",
                "giaSP",
                "soLuongSP"
        };

        String selection = "maSP = ?";
        String[] selectionArgs = {String.valueOf(productId)};

        Cursor cursor = db.query(
                "GioHang",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex("maSP"));
            String productName = cursor.getString(cursor.getColumnIndex("tenSP"));
            String productImage = cursor.getString(cursor.getColumnIndex("hinhAnh"));
            int productPrice = cursor.getInt(cursor.getColumnIndex("giaSP"));
            int productQuantity = cursor.getInt(cursor.getColumnIndex("soLuongSP"));
            cartItem = new CartItem(id, productName, productImage, productPrice, productQuantity);
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return cartItem;
    }

    public void updateCartItem(int productId, int currentQuantity, int totalPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("soLuongSP", currentQuantity);
        values.put("tongTien", totalPrice);
        String selection = "maSP = ?";
        String[] selectionArgs = {String.valueOf(productId)};
        db.update("GioHang", values, selection, selectionArgs);
        db.close();
    }

    public boolean isProductInCart(int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM GioHang WHERE maSP = ?", new String[]{String.valueOf(productId)});
        boolean isInCart = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isInCart;
    }

    public int getTotalPay() {
        int totalPay = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(tongTien) FROM GioHang", null);
        if (cursor.moveToFirst()) {
            totalPay = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return totalPay;
    }

    public int getProductQuantityInCart(Context context, int productId) {
        int quantity = 0;
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT soLuongSP FROM GioHang WHERE maSP = ?", new String[]{String.valueOf(productId)});
        if (cursor.moveToFirst()) {
            quantity = cursor.getInt(cursor.getColumnIndex("soLuongSP"));
        }
        cursor.close();
        db.close();
        return quantity;
    }

    public void deleteCartItem(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = "maSP = ?";
        String[] selectionArgs = {String.valueOf(productId)};
        db.delete("GioHang", selection, selectionArgs);
        db.close();
    }

    public String getAddressFromDatabase(Context context) {
        String diaChi = null;
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT diaChi FROM KhachHang LIMIT 1", null);
        if (cursor != null && cursor.moveToFirst()) {
            diaChi = cursor.getString(cursor.getColumnIndex("diaChi"));
            cursor.close();
        }
        db.close();
        return diaChi;
    }

}


