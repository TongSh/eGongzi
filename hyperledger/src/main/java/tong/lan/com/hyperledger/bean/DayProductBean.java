package tong.lan.com.hyperledger.bean;


public class DayProductBean {
    private String productInfo;//产品
    private int amount;
    private double margin;//利润

    public DayProductBean(String productInfo, int amount, double margin) {
        this.productInfo = productInfo;
        this.amount = amount;
        this.margin = margin;
    }

    public String getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(String productInfo) {
        this.productInfo = productInfo;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getMargin() {
        return margin;
    }

    public void setMargin(double margin) {
        this.margin = margin;
    }
}