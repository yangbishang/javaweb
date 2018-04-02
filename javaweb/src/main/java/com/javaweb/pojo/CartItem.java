package com.javaweb.pojo;

public class CartItem {
    private Product product;
    private int buyNum;
    private double subtotal;

    public CartItem(){

    }
    public CartItem(Product product, int buyNum, double subtotal) {
        this.product = product;
        this.buyNum = buyNum;
        this.subtotal = subtotal;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "product=" + product +
                ", buyNum=" + buyNum +
                ", subtotal=" + subtotal +
                '}';
    }
}
