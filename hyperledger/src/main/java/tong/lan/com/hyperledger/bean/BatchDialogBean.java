package tong.lan.com.hyperledger.bean;

public class BatchDialogBean {
    private String prod;
    private int amount;

    public BatchDialogBean(String prod, int amount) {
        this.prod = prod;
        this.amount = amount;
    }

    public String getProd() {
        return prod;
    }

    public void setProd(String prod) {
        this.prod = prod;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
