package com.farmfresh.model;

public class Customer extends User {

    public Customer() {
        this.role = "CUSTOMER";
    }

    // CUSTOMER boleh cancel jika masih PENDING
    @Override
    public boolean canCancelOrder(String status) {
        return "PENDING".equals(status);
    }

    // CUSTOMER boleh complete jika sudah SHIPPED
    @Override
    public boolean canCompleteOrder(String status) {
        return "SHIPPED".equals(status);
    }

    // CUSTOMER TIDAK boleh update status manual
    @Override
    public boolean canUpdateOrderStatus(String from, String to) {
        return false;
    }
}
