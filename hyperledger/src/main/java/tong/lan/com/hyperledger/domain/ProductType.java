package tong.lan.com.hyperledger.domain;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class ProductType  extends DataSupport {
    private int id;
    private String type;//类别名称

    public ProductType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
