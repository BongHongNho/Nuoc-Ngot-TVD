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
    private static final String CREATE_TABLE_TAI_KHOAN =
            "CREATE TABLE TaiKhoan (" +
                    "maTK INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "maKH INTEGER REFERENCES KhachHang(maKH)," +
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
                    "tenKH TEXT NOT NULL," +
                    "Email TEXT NOT NULL," +
                    "SDT TEXT NOT NULL," +
                    "diaChi TEXT NOT NULL," +
                    "capTV TEXT NOT NULL," +
                    "hinhAnh TEXT NOT NULL)";
    private static final String CREATE_TABLE_ADMIN =
            "CREATE TABLE Admin (" +
                    "maAdmin INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "maTK INTEGER REFERENCES TaiKhoan(maTK)," +
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
        db.execSQL("INSERT INTO TaiKhoan (maTK, maKH, tenDN, matKhau, Email, SDT, role) VALUES (1, 1, 'admin1', 'admin1pass', 'admin1@example.com', 1234567890, 'admin')");
        db.execSQL("INSERT INTO TaiKhoan (maTK, maKH, tenDN, matKhau, Email, SDT, role) VALUES (2, 2, 'user1', 'user1pass', 'user1@example.com', 1234567890, 'user')");

        db.execSQL(CREATE_TABLE_KHACH_HANG);
        db.execSQL("INSERT INTO KhachHang (maTK, tenKH, Email, SDT, diaChi, capTV, hinhAnh) VALUES (1, 'KhachHang1', 'kh1@example.com', '1234567890', 'Địa chỉ 1', 'Vip', 'https://tea-3.lozi.vn/v1/ship/resized/losupply-quan-tan-phu-quan-tan-phu-ho-chi-minh-1618467447167540212-nuoc-ngot-coca-cola-lon-320ml-0-1626403242?w=480&type=o')");
        db.execSQL("INSERT INTO KhachHang (maTK, tenKH, Email, SDT, diaChi, capTV, hinhAnh) VALUES (2, 'KhachHang2', 'kh2@example.com', '0987654321', 'Địa chỉ 2', 'Normal', 'https://tea-3.lozi.vn/v1/ship/resized/losupply-quan-tan-phu-quan-tan-phu-ho-chi-minh-1618467447167540212-nuoc-ngot-coca-cola-lon-320ml-0-1626403242?w=480&type=o')");

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

        db.execSQL(CREATE_TABLE_CHI_TIET_SAN_PHAM);
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Coca Cola', 1, 1, 10000, 420, 'https://tea-3.lozi.vn/v1/ship/resized/losupply-quan-tan-phu-quan-tan-phu-ho-chi-minh-1618467447167540212-nuoc-ngot-coca-cola-lon-320ml-0-1626403242?w=480&type=o', 'Việt Nam', 'Là loại nước ngọt được nhiều người yêu thích với hương vị thơm ngon, sảng khoái. Nước ngọt Coca Cola 320ml chính hãng nước ngọt Coca Cola với lượng gas lớn sẽ giúp bạn xua tan mọi cảm giác mệt mỏi, căng thẳng, đem lại cảm giác thoải mái sau khi hoạt động ngoài trời.')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('PepSi', 2, 2, 20000, 289, 'https://onelife.vn/_next/image?url=https%3A%2F%2Fstorage.googleapis.com%2Fonelife-public%2F8934588012112.jpg&w=3840&q=75', 'Việt Nam', 'Nước ngọt của thương hiệu Pepsi an toàn, chất lượng được nhà nhà lựa chọn tin dùng. Nước ngọt Pepsi Cola Sleek lon 245ml mang hương vị đặc trưng, thơm ngon hấp dẫn giúp bạn xua tan đi cơn khát ngay tức khắc. Nước ngọt Pepsi được chế biến theo công nghệ hiện đại, an toàn cho sức khỏe người dùng.')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Mirinda Soda Kem', 3, 5, 30000, 340, 'https://storage.googleapis.com/sc_pcm_product/prod/2023/4/28/2870-92735.jpg', 'Việt Nam', 'Nước ngọt giải khát từ thương hiệu nước ngọt Mirinda nổi tiếng được nhiều người ưa chuộng. Nước ngọt Mirinda Kem lon 320ml với hương vị cam đặc trưng, không chỉ giải khát, mà còn bổ sung thêm vitamin C giúp lấy lại năng lượng cho hoạt động hàng ngày. Cam kết chính hãng và an toàn')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Mirinda Cam', 4, 5, 40000, 460, 'https://www.lottemart.vn/media/catalog/product/cache/0x0/8/9/8934588882111.jpg.webp', 'Việt Nam', 'Nước ngọt giải khát từ thương hiệu nước ngọt Mirinda nổi tiếng được nhiều người ưa chuộng. Nước ngọt Mirinda cam lon 320ml với hương vị cam đặc trưng, không chỉ giải khát, mà còn bổ sung thêm vitamin C giúp lấy lại năng lượng cho hoạt động hàng ngày. Cam kết chính hãng và an toàn')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong,hinhAnh, xuatXu, thongTinSP) VALUES ('Mirinda Xá Xị', 5, 5, 30000, 560, 'https://bizweb.dktcdn.net/100/385/152/products/thung-24-lon-nuoc-ngot-mirinda-huong-xa-xi-320ml-202112181001420466.jpg?v=1682078117373', 'Việt Nam', 'Nước ngọt giải khát từ thương hiệu nước ngọt Mirinda nổi tiếng được nhiều người ưa chuộng. Nước ngọt Mirinda Xá Xị lon 320ml với hương vị cam đặc trưng, không chỉ giải khát, mà còn bổ sung thêm vitamin C giúp lấy lại năng lượng cho hoạt động hàng ngày. Cam kết chính hãng và an toàn')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Mirinda Việt Quất', 6, 5, 20000, 774, 'https://cdn.tgdd.vn/Products/Images/2443/277738/bhx/thung-24-lon-nuoc-ngot-co-ga-mirinda-vi-soda-kem-viet-quat-320ml-202204222257169111.jpg', 'Việt Nam', 'Nước ngọt Mirinda soda kem vị việt quất ngọt ngào tươi mới, vị soda kem bùng nổ cùng hương việt quất thơm ngon. Hãy mua thùng 24 lon nước ngọt có ga Mirinda vị soda kem việt quất 320ml để cảm nhận vị ngon khó cưỡng và cùng thưởng thức nước ngọt này với bạn bè, người thân nhé!')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('PepSi Không Đường', 7, 2, 10000, 644, 'https://cdn.tgdd.vn/Products/Images/2443/227312/bhx/nuoc-ngot-pepsi-khong-calo-lon-320ml-giao-mau-ngau-nhien-202403011338383885.jpg', 'Việt Nam', 'Là loại nước ngọt được nhiều người yêu thích đến từ thương hiệu nước ngọt Pepsi nổi tiếng thế giới với hương vị thơm ngon, sảng khoái. Nước ngọt Pepsi không calo lon 320ml với lượng gas lớn sẽ giúp bạn xua tan mọi cảm giác mệt mỏi, căng thẳng, sản phẩm không calo lành mạnh, tốt cho sức khỏe')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('PepSi Không Đường Vị Chanh', 8, 2, 15000, 324, 'https://cdn.tgdd.vn/Products/Images/2443/227309/bhx/nuoc-ngot-pepsi-khong-calo-vi-chanh-lon-320ml-202403141101085218.jpg', 'Việt Nam', 'Sự kết hợp hài hòa của vị chanh thanh mát, giải nhiệt và mang lại cảm giác sảng khoái và tốt cho sức khỏe. Nước ngọt Pepsi không calo vị chanh lon 320ml cực kỳ thích hợp cho những người thích uống nước ngọt nhưng vẫn muốn giữ lối sống ăn thanh đạm, ít đường. Sản phẩm chất lượng từ nước ngọt Pepsi')");
        db.execSQL("INSERT INTO ChiTietSanPham (tenSP, maSP, maDM, gia, soLuong, hinhAnh, xuatXu, thongTinSP) VALUES ('Coca Light', 9, 1, 10000, 931, 'https://cdn.tgdd.vn/Products/Images/2443/190309/bhx/nuoc-ngot-coca-cola-light-lon-330ml-202209111853245462.jpg', 'Việt Nam', 'Từ thương hiệu nước ngọt nổi tiếng thế giới được ưa chuộng tại nhiều nhiều quốc gia. Nước ngọt không đường Coca Cola Light lon 320ml chính hãng nước ngọt Coca Cola là dòng sản phẩm nước uống có ga không đường, dành cho người ăn kiêng, không lo tăng cân')");

        db.execSQL(CREATE_TABLE_DANH_MUC);
        db.execSQL("INSERT INTO DanhMuc (maDM, tenDM) VALUES (1, 'Coca-Cola')");
        db.execSQL("INSERT INTO DanhMuc (maDM, tenDM) VALUES (2, 'Pepsi')");
        db.execSQL("INSERT INTO DanhMuc (maDM, tenDM) VALUES (3, 'Sprite')");
        db.execSQL("INSERT INTO DanhMuc (maDM, tenDM) VALUES (4, 'Fanta')");
        db.execSQL("INSERT INTO DanhMuc (maDM, tenDM) VALUES (5, 'Mirinda')");

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


