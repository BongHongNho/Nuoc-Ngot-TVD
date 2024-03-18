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
    public Context context;


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
                    "hinhAnh TEXT NOT NULL)";

    private static final String CREATE_TABLE_ADMIN =
            "CREATE TABLE Admin (" +
                    "maAdmin INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "loaiTK TEXT REFERENCES TaiKhoan(loaiTK)," +
                    "tenAdmin TEXT NOT NULL," +
                    "hinhAnh TEXT NOT NULL)";

    private static final String CREATE_TABLE_SAN_PHAM =
            "create table SanPham (maSP INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "hinhAnh TEXT NOT NULL," +
                    "tenSP TEXT NOT NULL," +
                    "soLuongSP INTEGER NOT NULL," +
                    "giaSP INTEGER NOT NULL)";
    private static final String CREATE_TABLE_CHI_TIET_SAN_PHAM =
            "create table ChiTietSanPham (maCTSP INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "maSP INTEGER REFERENCES SanPham(maSP)," +
                    "maDM INTEGER REFERENCES DanhMuc(maDM)," +
                    "hinhAnh TEXT REFERENCES SanPham(hinhAnh)," +
                    "tenSP TEXT REFERENCES SanPham(tenSP)," +
                    "xuatXu TEXT NOT NULL," +
                    "thongTinSP TEXT  NOT NULL," +
                    "soLuongSP INTEGER REFERENCES SanPham(soLuongSP)," +
                    "giaSP INTEGER REFERENCES SanPham(giaSP))";
    private static final String CREATE_TABLE_YEU_THICH =
            "create table YeuThich (maYT INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "maKH INTEGER REFERENCES KhachHang(maKH)," +
                    "tenKH TEXT REFERENCES KhachHang(tenKH)," +
                    "maSP INTEGER REFERENCES SanPham(maSP)," +
                    "hinhAnh TEXT REFERENCES SanPham(hinhAnh)," +
                    "tenSP TEXT REFERENCES SanPham(tenSP)," +
                    "giaSP INTEGER REFERENCES SanPham(giaSP)," +
                    "soLuongSP INTEGER REFERENCES SanPham(soLuongSP)," +
                    "thoiGian DATE NOT NULL)";
    private static final String CREATE_TABLE_DANH_GIA =
            "create table DanhGia (maDG INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "maKH INTEGER REFERENCES KhachHang(maKH)," +
                    "tenKH TEXT REFERENCES KhachHang(tenKH)," +
                    "maSP INTEGER REFERENCES SanPham(maSP)," +
                    "hinhAnh TEXT REFERENCES SanPham(hinhAnh)," +
                    "tenSP TEXT REFERENCES SanPham(tenSP)," +
                    "giaSP INTEGER REFERENCES SanPham(giaSP)," +
                    "soLuongSP INTEGER REFERENCES SanPham(soLuongSP)," +
                    "binhLuon TEXT NOT NULL," +
                    "thoiGian DATE NOT NULL)";
    private static final String CREATE_TABLE_GIO_HANG =
            "create table GioHang (maGH INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "maKH INTEGER REFERENCES KhachHang(maKH)," +
                    "tenKH TEXT REFERENCES KhachHang(tenKH)," +
                    "maSP INTEGER REFERENCES SanPham(maSP)," +
                    "hinhAnh TEXT REFERENCES SanPham(hinhAnh)," +
                    "tenSP TEXT REFERENCES SanPham(tenSP)," +
                    "giaSP INTEGER REFERENCES SanPham(giaSP)," +
                    "soLuongSP INTEGER REFERENCES SanPham(soLuongSP)," +
                    "soLuong INTEGER NOT NULL," +
                    "tongTien INTEGER NOT NULL)";
    private static final String CREATE_TABLE_HOA_DON =
            "create table HoaDon (maHD INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "maKH INTEGER REFERENCES KhachHang(maKH)," +
                    "tenKH TEXT REFERENCES KhachHang(tenKH)," +
                    "maSP INTEGER REFERENCES SanPham(maSP)," +
                    "hinhAnh TEXT REFERENCES SanPham(hinhAnh)," +
                    "tenSP TEXT REFERENCES SanPham(tenSP)," +
                    "giaSP INTEGER REFERENCES SanPham(giaSP)," +
                    "soLuongSP INTEGER REFERENCES SanPham(soLuongSP)," +
                    "ngayTT DATE NOT NULL," +
                    "soLuong INTEGER NOT NULL," +
                    "tongTien TEXT NOT NULL," +
                    "diaChi TEXT NOT NULL)";
    private static final String CREATE_TABLE_THANH_TOAN =
            "create table ThanhToan (maTToan INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "diaChi TEXT REFERENCES KhachHang(diaChi)," +
                    "maSP INTEGER REFERENCES SanPham(maSP)," +
                    "hinhAnh TEXT REFERENCES SanPham(hinhAnh)," +
                    "tenSP TEXT REFERENCES SanPham(tenSP)," +
                    "giaSP INTEGER REFERENCES SanPham(giaSP)," +
                    "soLuongSP INTEGER REFERENCES SanPham(soLuongSP)," +
                    "phuongThucTT TEXT NOT NULL," +
                    "tongTienTT INTEGER NOT NULL," +
                    "tongTienPhiVC INTEGER NOT NULL," +
                    "tongThanhToan INTEGER NOT NULL," +
                    "tongTienThanhToan INTEGER NOT NULL)";
    private static final String CREATE_TABLE_DANH_MUC =
            "create table DanhMuc (maDM INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "tenDM TEXT NOT NULL)";

    private static final String CREATE_TABLE_DON_MUA =
            "create table DonMua (maDMUA INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "maSP INTEGER REFERENCES SanPham(maSP)," +
                    "hinhAnh TEXT REFERENCES SanPham(hinhAnh)," +
                    "tenSP TEXT REFERENCES SanPham(tenSP)," +
                    "giaSP INTEGER REFERENCES SanPham(giaSP))";
    private static final String COLUMN_HINH_ANH = "hinhAnh";
    private static final String TABLE_ADMIN = "Admin";

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
        db.execSQL("INSERT INTO Admin (tenAdmin, hinhAnh) VALUES ('Nguyen Huy Phuoc Tan', 'https://cdn.icon-icons.com/icons2/1378/PNG/512/avatardefault_92824.png')");
        db.execSQL(CREATE_TABLE_SAN_PHAM);
        db.execSQL("INSERT INTO SanPham (hinhAnh, tenSP, soLuongSP, giaSP) VALUES ('https://cdn.tgdd.vn/Products/Images/2443/83757/bhx/nuoc-ngot-coca-cola-235ml-202112141707442712.jpg', 'Coca Cola', 100, 10000)");
        db.execSQL("INSERT INTO SanPham (hinhAnh, tenSP, soLuongSP, giaSP) VALUES ('https://www.lottemart.vn/media/catalog/product/cache/0x0/8/9/8934588882111.jpg.webp', 'Mirinda Cam', 50, 12000)");
        db.execSQL("INSERT INTO SanPham (hinhAnh, tenSP, soLuongSP, giaSP) VALUES ('https://sieuthihoaba.com.vn/wp-content/uploads/2020/08/nuoc-ngot-mirinda-vi-soda-kem-330ml-201911261643077465.jpg', 'Mirinda Kem', 39, 15000)");
        db.execSQL("INSERT INTO SanPham (hinhAnh, tenSP, soLuongSP, giaSP) VALUES ('https://bizweb.dktcdn.net/100/436/111/products/6-lon-nuoc-ngot-co-ga-mirinda-vi-soda-kem-viet-quat-320ml-202204222251553206.jpg?v=1704185813763', 'Mirinda Việt Quất', 320, 12000)");
        db.execSQL("INSERT INTO SanPham (hinhAnh, tenSP, soLuongSP, giaSP) VALUES ('https://sieuthihoaba.com.vn/wp-content/uploads/2020/08/nuoc-ngot-mirinda-huong-xa-xi-330ml-202003101804043892.jpg', 'Mirinda Xá Xị', 224, 10000)");
        db.execSQL("INSERT INTO SanPham (hinhAnh, tenSP, soLuongSP, giaSP) VALUES ('https://thucphamsachgiatot.vn/image/cache/catalog/X-NUOC-NGOT-DA-ME-700x700.jpg', 'Mirinda Đá Me', 89, 12000)");
        db.execSQL(CREATE_TABLE_CHI_TIET_SAN_PHAM);
        db.execSQL("INSERT INTO ChiTietSanPham (maSP, hinhAnh, tenSP, soLuongSP, giaSP, xuatXu, thongTinSP) VALUES (1, 'https://cdn.tgdd.vn/Products/Images/2443/83757/bhx/nuoc-ngot-coca-cola-235ml-202112141707442712.jpg', 'Coca Cola', 100, 10000, '', 'Nước ngọt CocaCola hiện nay là sản phẩm được nhiều người tin dùng và ưa chuộng bởi chất lượng sản phẩm tuyệt đối an toàn cho sức khỏe người dùng. Mang hương vị ngọt thơm, kích thích vị giác giúp xua tan mệt mỏi, đem đến tinh thần sảng khoái, bổ sung năng lượng cho cơ thể');\n");
        db.execSQL("INSERT INTO ChiTietSanPham (maSP, hinhAnh, tenSP, soLuongSP, giaSP, xuatXu, thongTinSP) VALUES (2, 'https://www.lottemart.vn/media/catalog/product/cache/0x0/8/9/8934588882111.jpg.webp', 'Mirinda Cam', 50, 12000, 'Việt Nam', 'Nước ngọt Mirinda cam lon 325 ml với hương vị cam đặc trưng, không chỉ giải khát, mà còn bổ sung thêm vitamin C giúp lấy lại năng lượng cho hoạt động hàng ngày. Cam kết chính hãng và an toàn. Hương vị cam đặc trưng, không chỉ giải khát, mà còn giúp bạn lấy lại năng lượng cho hoạt động hàng ngày.')");
        db.execSQL("INSERT INTO ChiTietSanPham (maSP, hinhAnh, tenSP, soLuongSP, giaSP, xuatXu, thongTinSP) VALUES (3, 'https://sieuthihoaba.com.vn/wp-content/uploads/2020/08/nuoc-ngot-mirinda-vi-soda-kem-330ml-201911261643077465.jpg', 'Mirinda Kem', 39, 15000, 'Việt Nam', 'Mirinda soda kem không chỉ giúp bạn xua tan cơn khát tức thì mà còn giúp kích thích vị giác, cho bữa ăn thêm ngon miệng. Đây là thức uống không thể thiếu trong các buổi tiệc, hoạt động ngoài trời, giúp bạn tràn đầy năng lượng, căng tràn sức sống hoạt động dưới thời tiết mùa hè đầy nắng nóng.')");
        db.execSQL("INSERT INTO ChiTietSanPham (maSP, hinhAnh, tenSP, soLuongSP, giaSP, xuatXu, thongTinSP) VALUES (4, 'https://bizweb.dktcdn.net/100/436/111/products/6-lon-nuoc-ngot-co-ga-mirinda-vi-soda-kem-viet-quat-320ml-202204222251553206.jpg?v=1704185813763', 'Mirinda Việt Quất', 320, 12000, 'Việt Nam', 'Nước ngọt Mirinda Soda Kem Vị Việt Quất ngọt ngào tươi mới, vị soda kem bùng nổ cùng hương việt quất thơm ngon.\n" +
                "\n" +
                "Thành phần: Nước bão hòa CO2, đường, hỗn hợp vị soda kem và hương việt quất giống tự nhiên, chất điều chỉnh độ axit, chất ổn định, chất tạo ngọt tổng hợp, chất bảo quản, chất chống oxy hóa,...\n" +
                "\n" +
                "Điểm nổi bật: Nước uống có ga vị soda kem việt quất tươi mới, thơm ngon bất ngờ, giải khát tức thì.')");
        db.execSQL("INSERT INTO ChiTietSanPham (maSP, hinhAnh, tenSP, soLuongSP, giaSP, xuatXu, thongTinSP) VALUES (5, 'https://sieuthihoaba.com.vn/wp-content/uploads/2020/08/nuoc-ngot-mirinda-huong-xa-xi-330ml-202003101804043892.jpg', 'Mirinda Xá Xị', 224, 10000, 'Việt Nam', 'Nước Giải Khát Mirinda có hương xá xị tự nhiên, độc đáo giúp bạn giải nhanh cơn khát, với vị gas nhẹ là thức uống giải khát tuyệt vời dành cho mọi lứa tuổi.\n" +
                "\n" +
                "Được sản xuất trên dây chuyền công nghệ hiện đại, đảm bảo vệ sinh, an toàn cho sức khỏe người tiêu dùng.\n" +
                "\n" +
                "Sản phẩm được đóng lon dễ dàng sử dụng trong gia đình cũng như mang theo trong các chuyến dã ngoại.')");
        db.execSQL("INSERT INTO ChiTietSanPham (maSP, hinhAnh, tenSP, soLuongSP, giaSP, xuatXu, thongTinSP) VALUES (6, 'https://thucphamsachgiatot.vn/image/cache/catalog/X-NUOC-NGOT-DA-ME-700x700.jpg', 'Mirinda Đá Me', 89, 12000, 'Việt Nam', 'Nước ngọt Mirinda đá me chai 390ml hương đá me chua ngọt đê mê, ngon ngả nghiêng giúp bạn giải nhanh cơn khát, với vị gas nhẹ là thức uống giải khát tuyệt vời dành cho mọi lứa tuổi. Nước bão hòa CO2, đường, màu tổng hợp (Caramen nhóm IV 150d), chất điều chỉnh độ axit (330), hương me tổng hợp, chất bảo quản (211, 202).')");
        db.execSQL(CREATE_TABLE_YEU_THICH);
        db.execSQL(CREATE_TABLE_DANH_GIA);
        db.execSQL(CREATE_TABLE_GIO_HANG);
        db.execSQL(CREATE_TABLE_HOA_DON);
        db.execSQL(CREATE_TABLE_DANH_MUC);
        db.execSQL(CREATE_TABLE_DON_MUA);
        db.execSQL(CREATE_TABLE_THANH_TOAN);
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


