package tong.lan.com.hyperledger.bean;

import java.io.Serializable;

public class EmplListBean implements Serializable{
    private Integer id;
    private String employeeName;//姓名
    private int mType;          // 正常显示的员工，还是字母分隔符

    public EmplListBean(Integer id, String employeeName, int type) {
        this.id = id;
        this.employeeName = employeeName;
        mType = type;
    }

    public EmplListBean(String ch, int type){
        id = 0;
        employeeName = ch;
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

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
}