package tong.lan.com.hyperledger.bean;

public class ProdListBean {
    private Integer id;
    private String productName;//产品名称
    private double productWage;//工价
    private int productImg;     // 图片

    public ProdListBean(Integer id, String productName, double productWage, int img) {
        this.id = id;
        this.productName = productName;
        this.productWage = productWage;
        productImg = img;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductImg(){return productImg;}

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductWage() {
        return productWage;
    }

    public void setProductWage(double productWage) {
        this.productWage = productWage;
    }
}