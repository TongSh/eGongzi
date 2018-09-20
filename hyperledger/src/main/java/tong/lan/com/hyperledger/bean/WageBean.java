package tong.lan.com.hyperledger.bean;


public class WageBean {
    private int employeeID;
    private String employeeName;//生产人
    private double wage;//工资
    private int workDay;//出勤

    public WageBean(int employeeID, String employeeName, double wage, int workDay) {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.wage = wage;
        this.workDay = workDay;
    }

    public WageBean() {
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public int getWorkDay() {
        return workDay;
    }

    public void setWorkDay(int workDay) {
        this.workDay = workDay;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public double getWage() {
        return wage;
    }

    public void setWage(double wage) {
        this.wage = wage;
    }
}