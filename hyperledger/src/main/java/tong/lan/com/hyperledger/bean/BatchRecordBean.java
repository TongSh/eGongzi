package tong.lan.com.hyperledger.bean;

public class BatchRecordBean {
    private int date;
    private int recs;
    private double wage;

    public BatchRecordBean(int date, int recs, double wage) {
        this.date = date;
        this.recs = recs;
        this.wage = wage;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getRecs() {
        return recs;
    }

    public void setRecs(int recs) {
        this.recs = recs;
    }

    public double getWage() {
        return wage;
    }

    public void setWage(double wage) {
        this.wage = wage;
    }
}
