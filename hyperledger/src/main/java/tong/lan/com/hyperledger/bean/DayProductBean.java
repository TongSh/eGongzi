package tong.lan.com.hyperledger.bean;


public class DayProductBean {
    private String productInfo;//产品
    private int amount;
    private int nrec; //记录数

public DayProductBean(String productInfo, int amount, int nrec) {
        this.productInfo = productInfo;
        this.amount = amount;
        this.nrec = nrec;
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

    public int getNrec() {
        return nrec;
    }

    public void setNrec(int nrec) {
        this.nrec = nrec;
    }
}