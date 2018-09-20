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

    List<String> spinnerData;
    @ViewInject(R.id.update_product_type)
    private NiceSpinner mProductType;
    @ViewInject(R.id.update_product_name)
    private EditText mProductName;
    @ViewInject(R.id.update_product_wage)
    private EditText mProductWage;
    @ViewInject(R.id.update_product_margin)
    private EditText mProductMargin;
    @ViewInject(R.id.update_product_title)
    private TextView mProductTitle;

    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initSpinner();

        Intent intent = getIntent();
        int pID = intent.getIntExtra("pID",0);
        product = DataSupport.find(Product.class,pID);
        mProductType.setSelectedIndex(spinnerData.indexOf(product.getProductType()));
        mProductName.setText(product.getProductName());
        mProductWage.setText(((Double)product.getProductWage()).toString());
        mProductMargin.setText(((Double)product.getProductMargin()).toString());
    }

    void initSpinner(){
        spinnerData = new ArrayList<>(Arrays.asList("内衬", "护托", "耳朵", "商标",
                "拖把", "其他"));
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
    @Event(value = {R.id.update_product_save},type = View.OnClickListener.class)
    private void submit(View v)
    {
        String product_type = spinnerData.get(mProductType.getSelectedIndex());
        String product_name = mProductName.getText().toString();
        double product_wage = Double.parseDouble(mProductWage.getText().toString());
        double product_margin = Double.parseDouble(mProductMargin.getText().toString());
        if (!product_name.isEmpty()) {
            Product mProduct = new Product(product_name,product_type,product_wage,product_margin);
            if (mProduct.update(product.getId()) == 1) {
                Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "更新失败", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(UpdateProductActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(this, "产品名称未填写！", Toast.LENGTH_SHORT).show();
        }
    }

    //返回键
    @Event(value = {R.id.update_product_cancel},type = View.OnClickListener.class)
    private void back(View v)
    {
        Intent intent = new Intent(UpdateProductActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
