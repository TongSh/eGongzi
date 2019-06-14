package tong.lan.com.hyperledger.bean;


import tong.lan.com.hyperledger.domain.Employee;
import tong.lan.com.hyperledger.domain.Product;

public class RecdListBean {
    private String productInfo;//产品
    private String employeeName;//生产人
    private String recordDate;//生产日期
    private double wage;//工资
    private int recID;

    public RecdListBean(String productInfo, String employeeName, String recordDate, double wage) {
        this.productInfo = productInfo;
        this.employeeName = employeeName;
        this.recordDate = recordDate;
        this.wage = wage;
    }

    public RecdListBean(String productInfo, String employeeName, String recordDate, double wage, int recID) {
        this.productInfo = productInfo;
        this.employeeName = employeeName;
        this.recordDate = recordDate;
        this.wage = wage;
        this.recID = recID;
    }

    public RecdListBean() {
    }

    public String getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(String productInfo) {
        this.productInfo = productInfo;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public double getWage() {
        return wage;
    }

    public void setWage(double wage) {
        this.wage = wage;
    }

    public int getRecID() {
        return recID;
    }

    public void setRecID(int recID) {
        this.recID = recID;
    }
}