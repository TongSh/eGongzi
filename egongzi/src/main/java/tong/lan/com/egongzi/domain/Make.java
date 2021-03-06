package tong.lan.com.egongzi.domain;
// Generated 2017-7-29 17:34:30 by Hibernate Tools 3.4.0.CR1

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Make generated by hbm2java
 */
public class Make extends DataSupport {

	private int id;
	private Employee employee;
	private Product product;
	private Date makeDate;
	private Integer makeAmount;

	public Make(Employee employee, Product product, Date makeDate, Integer makeAmount) {
		this.employee = employee;
		this.product = product;
		this.makeDate = makeDate;
		this.makeAmount = makeAmount;
	}

	public Make() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Date getMakeDate() {
		return this.makeDate;
	}

	public void setMakeDate(Date makeDate) {
		this.makeDate = makeDate;
	}

	public Integer getMakeAmount() {
		return this.makeAmount;
	}

	public void setMakeAmount(Integer makeAmount) {
		this.makeAmount = makeAmount;
	}

}
