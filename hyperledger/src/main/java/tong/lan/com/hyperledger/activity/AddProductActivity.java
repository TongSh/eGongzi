package tong.lan.com.hyperledger.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import org.angmarch.views.NiceSpinner;
import org.litepal.crud.DataSupport;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tong.lan.com.hyperledger.R;
import tong.lan.com.hyperledger.domain.Employee;
import tong.lan.com.hyperledger.domain.Product;
import tong.lan.com.hyperledger.domain.ProductType;

@ContentView(R.layout.activity_add_product)
public class AddProductActivity extends AppCompatActivity {

    List<String> spinnerData;
    @ViewInject(R.id.add_product_type)
    private NiceSpinner mProductType;
    @ViewInject(R.id.add_product_name)
    private EditText mProductName;
    @ViewInject(R.id.add_product_wage)
    private EditText mProductWage;
    @ViewInject(R.id.add_product_margin)
    private EditText mProductMargin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initSpinner();
    }

    void initSpinner(){
        spinnerData = new ArrayList<>();
        spinnerData.add("其他");
        List<ProductType> mData = DataSupport.findAll(ProductType.class);
        for(ProductType item : mData){
            spinnerData.add(item.getType());
        }
        mProductType.attachDataSource(spinnerData);
        mProductType.setTextColor(Color.BLACK);
        mProductType.setTextSize(18);

        mProductType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    //确认提交
    @Event(value = {R.id.add_product_save},type = View.OnClickListener.class)
    private void submit(View v)
    {
        String product_type = spinnerData.get(mProductType.getSelectedIndex());
        String product_name = mProductName.getText().toString();
        double product_wage = Double.parseDouble(mProductWage.getText().toString());
        double product_margin = Double.parseDouble(mProductMargin.getText().toString());
        if (!product_name.isEmpty()) {
            Product mProduct = new Product(product_name,product_type,product_wage,product_margin);
            if (mProduct.save()) {
                Toast.makeText(this, "存储成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "存储失败", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(AddProductActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(this, "产品名称未填写！", Toast.LENGTH_SHORT).show();
        }
    }

    //返回键
    @Event(value = {R.id.add_product_cancel},type = View.OnClickListener.class)
    private void addEmployeeBack(View v)
    {
        Intent intent = new Intent(AddProductActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
