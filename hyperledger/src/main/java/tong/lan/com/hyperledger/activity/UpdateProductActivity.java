package tong.lan.com.hyperledger.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
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
import tong.lan.com.hyperledger.domain.Product;

@ContentView(R.layout.activity_update_product)
public class UpdateProductActivity extends AppCompatActivity {

    private EditText mProductName;
    private EditText mProductWage;

    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        mProductName = findViewById(R.id.update_product_name);
        mProductWage = findViewById(R.id.update_product_wage);

        Intent intent = getIntent();
        int pID = intent.getIntExtra("pID",0);
        product = DataSupport.find(Product.class,pID);
        mProductName.setText(product.getName());
        mProductWage.setText(((Double)product.getWage()).toString());
    }

    //确认提交
    @Event(value = {R.id.update_product_save},type = View.OnClickListener.class)
    private void submit(View v)
    {
        String product_name = mProductName.getText().toString();
        if (product_name.isEmpty()){
            Toast.makeText(getApplicationContext(), "请输入产品名称！", Toast.LENGTH_LONG).show();
            return;
        }

        if (mProductWage.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "请输入产品单价！", Toast.LENGTH_LONG).show();
            return;
        }

        double product_wage = Double.parseDouble(mProductWage.getText().toString());

        Product mProduct = new Product(product_name,product_wage);
        if (mProduct.update(product.getId()) == 1) {
            Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "更新失败", Toast.LENGTH_SHORT).show();
        }
        finish();

    }

    //返回键
    @Event(value = {R.id.update_product_cancel},type = View.OnClickListener.class)
    private void back(View v)
    {
        finish();
    }
}
