package tong.lan.com.hyperledger.bean;

import java.io.Serializable;

public class EmployeeBean implements Serializable{
    private Integer id;
    private String employeeName;//姓名
    private String employeePhone;
    private int mType;          // 正常显示的员工，还是字母分隔符

    public EmployeeBean(Integer id, String employeeName, String employeePhone, int type) {
        this.id = id;
        this.employeeName = employeeName;
        this.employeePhone = employeePhone;
        mType = type;
    }

    public EmployeeBean(String ch, int type){
        id = 0;
        employeeName = ch;
        employeePhone = "";
        mType = type;
    }

    public int getmType() {
        return mType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getEmployeePhone() {
        return employeePhone;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public void setEmployeePhone(String employeePhone) {
        this.employeePhone = employeePhone;
    }

}