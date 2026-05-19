package com.pbo.responsi.controller;

import com.pbo.responsi.dto.CartItemDTO;
import com.pbo.responsi.model.CartRepository;
import com.pbo.responsi.service.DiscountStrategy;
import com.pbo.responsi.view.CartView;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class CartController {

    private final CartRepository repository;
    private final DiscountStrategy discountStrategy;
    private final CartView view;

    public CartController(CartRepository repository, DiscountStrategy discountStrategy, CartView view) {
        this.repository = repository;
        this.discountStrategy = discountStrategy;
        this.view = view;
        bindEvents();
        SwingUtilities.invokeLater(this::refreshView);
    }

    private void bindEvents() {
        view.onAdd(e -> handleAdd());
        view.onUpdate(e -> handleUpdate());
        view.onDelete(e -> handleDelete());
        view.onTableSelect(e -> {
            if (!e.getValueIsAdjusting()) {
                handleTableSelect();
            }
        });
    }

    private void refreshView() {
        List<CartItemDTO> items = repository.findAll();
        double subtotal = items.stream()
            .mapToDouble(i -> i.getPrice() * i.getQuantity())
            .sum();
        double discountAmount = discountStrategy.calculateDiscount(subtotal);
        double grandTotal = subtotal - discountAmount;
        view.showCartItems(items, subtotal, discountAmount, grandTotal, discountStrategy.getDiscountName());
    }

    private void handleAdd() {
        String name     = view.getNameInput();
        String priceStr = view.getPriceInput();
        String qtyStr   = view.getQtyInput();

        if (name.isEmpty() || priceStr.isEmpty() || qtyStr.isEmpty()) {
            view.showMessage("Semua field input wajib diisi!");
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            int qty = Integer.parseInt(qtyStr);
            repository.save(new CartItemDTO(name, price, qty));
            refreshView();
            view.clearForm();
        } catch (NumberFormatException ex) {
            view.showMessage("Harga dan Qty harus berupa angka!");
        }
    }

    private void handleUpdate() {
        String selectedName = view.getSelectedRowItemName();
        if (selectedName == null) {
            view.showMessage("Pilih barang di tabel terlebih dahulu!");
            return;
        }
        String inputStr = JOptionPane.showInputDialog(
            null,
            "Masukkan Jumlah (Qty) Baru untuk " + selectedName + ":"
        );
        if (inputStr != null) {
            try {
                int newQty = Integer.parseInt(inputStr);
                repository.updateQuantity(selectedName, newQty);
                refreshView();
                view.clearForm();
            } catch (NumberFormatException ex) {
                view.showMessage("Qty harus diisi angka!");
            }
        }
    }

    private void handleDelete() {
        String selectedName = view.getSelectedRowItemName();
        if (selectedName == null) {
            view.showMessage("Pilih barang di tabel terlebih dahulu!");
            return;
        }
        repository.delete(selectedName);
        refreshView();
        view.clearForm();
    }

    private void handleTableSelect() {
        String selectedName = view.getSelectedRowItemName();
        if (selectedName == null) return;
        repository.findAll().stream()
            .filter(item -> item.getName().equals(selectedName))
            .findFirst()
            .ifPresent(item -> view.setForm(
                item.getName(),
                item.getPrice(),
                item.getQuantity()
            ));
    }
}