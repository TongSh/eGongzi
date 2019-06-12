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
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import tong.lan.com.hyperledger.R;
import tong.lan.com.hyperledger.domain.Product;

@ContentView(R.layout.activity_add_product)
public class AddProductActivity extends AppCompatActivity {

    @ViewInject(R.id.add_product_name)
    private EditText mProductName;
    @ViewInject(R.id.add_product_wage)
    private EditText mProductWage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }

    //确认提交
    @Event(value = {R.id.add_product_save},type = View.OnClickListener.class)
    private void submit(View v)
    {
        String product_name = mProductName.getText().toString();
        double product_wage = Double.parseDouble(mProductWage.getText().toString());
        if (!product_name.isEmpty()) {
            Product mProduct = new Product(product_name,product_wage);
            if (mProduct.save()) {
                Toast.makeText(this, "存储成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "存储失败", Toast.LENGTH_SHORT).show();
            }
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
        finish();
    }
}
