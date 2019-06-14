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
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import tong.lan.com.hyperledger.R;
import tong.lan.com.hyperledger.domain.Product;

public class AddProductActivity extends AppCompatActivity {

    private EditText mProductName;
    private EditText mProductWage;
    private TextView save;
    private TextView cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        mProductName = findViewById(R.id.add_product_name);
        mProductWage = findViewById(R.id.add_product_wage);
        save = findViewById(R.id.add_product_save);
        cancel = findViewById(R.id.add_product_cancel);

        addListener();
    }


    private void addListener()
    {
        // 确认提交
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                if (mProduct.save()) {
                    Toast.makeText(getApplicationContext(), "存储成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "存储失败", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });

        // 取消
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
