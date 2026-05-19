/*
 * Entry point aplikasi Keranjang Belanja.
 * FILE INI DIMODIFIKASI untuk menerapkan pola MVC dan koneksi MySQL.
 */
package com.pbo.responsi;

import com.pbo.responsi.config.DatabaseConfig;
import com.pbo.responsi.controller.CartController;
import com.pbo.responsi.model.CartRepository;
import com.pbo.responsi.model.MysqlCartRepository;
import com.pbo.responsi.service.DiscountStrategy;
import com.pbo.responsi.service.EventDiscountStrategy;
import com.pbo.responsi.view.CartView;

import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Responsi {

    public static void main(String[] args) {
        try {
            Connection connection = DatabaseConfig.getConnection();
            DatabaseConfig.initializeTable(connection);
            CartRepository repository = new MysqlCartRepository(connection);
            DiscountStrategy discountStrategy = new EventDiscountStrategy();
            CartView view = new CartView();
            new CartController(repository, discountStrategy, view);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                null,
                "Gagal terhubung ke database MySQL.\n"
                + "Pastikan MySQL berjalan dan konfigurasi di DatabaseConfig.java sudah benar.\n\n"
                + "Detail: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}