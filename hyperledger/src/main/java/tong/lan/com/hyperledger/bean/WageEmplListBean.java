package tong.lan.com.hyperledger.bean;


public class WageEmplListBean {
    private int employeeID;
    private String employeeName;//生产人
    private String product;
    private int amount;
    private String date;
    private double wage;//工资

    public WageEmplListBean(int employeeID, String employeeName, String product, int amount, String date, double wage) {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.product = product;
        this.amount = amount;
        this.date = date;
        this.wage = wage;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getWage() {
        return wage;
    }

    public void setWage(double wage) {
        this.wage = wage;
    }
}