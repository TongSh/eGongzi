package tong.lan.com.hyperledger.domain;
// Generated 2017-7-29 17:34:30 by Hibernate Tools 3.4.0.CR1

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Product generated by hbm2java
 */
public class Product extends DataSupport {
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private String productName;//产品名称
	private String productType;//产品类型
	private double productWage;//工人工价
	private double productMargin;//产品利润
	private List<Make> make;//一个产品对应多条生产记录

	public Product(String productName, String productType, double productWage, double productMargin) {
		this.productName = productName;
		this.productType = productType;
		this.productWage = productWage;
		this.productMargin = productMargin;
		make = new ArrayList<Make>();
	}

	public Product() {
	}

	public List<Make> getMake() {
		return make;
	}

	public void setMake(List<Make> make) {
		this.make = make;
	}

	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductType() {
		return this.productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public double getProductWage() {
		return this.productWage;
	}

	public void setProductWage(double productWage) {
		this.productWage = productWage;
	}

	public double getProductMargin() {
		return this.productMargin;
	}

	public void setProductMargin(double productMargin) {
		this.productMargin = productMargin;
	}
}
