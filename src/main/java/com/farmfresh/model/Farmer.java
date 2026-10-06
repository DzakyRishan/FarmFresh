package com.farmfresh.model;

public class Farmer extends User {

    public Farmer() {
        this.role = "FARMER";
    }

    // FARMER tidak boleh cancel
    @Override
    public boolean canCancelOrder(String status) {
        return false;
    }

    // FARMER tidak boleh complete
    @Override
    public boolean canCompleteOrder(String status) {
        return false;
    }

    // FARMER hanya boleh:
    // PENDING → PROCESSED
    // PROCESSED → SHIPPED
    @Override
    public boolean canUpdateOrderStatus(String from, String to) {

        if ("PENDING".equals(from) && "PROCESSED".equals(to)) {
            return true;
        }

        if ("PROCESSED".equals(from) && "SHIPPED".equals(to)) {
            return true;
        }

        return false;
    }
}
