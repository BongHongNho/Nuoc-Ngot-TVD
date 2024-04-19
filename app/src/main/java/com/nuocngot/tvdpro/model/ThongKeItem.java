package com.nuocngot.tvdpro.model;

public class ThongKeItem {

        private String tenSPDH;
        private int soLuong;
        private double tongTien; // Tổng tiền của đơn hàng

        public ThongKeItem(String tenSPDH, int soLuong, double tongTien) {
            this.tenSPDH = tenSPDH;
            this.soLuong = soLuong;
            this.tongTien = tongTien;
        }

        public String getTenSPDH() {
            return tenSPDH;
        }

        public int getSoLuong() {
            return soLuong;
        }

        public double getTongTien() {
            return tongTien;
        }

}
