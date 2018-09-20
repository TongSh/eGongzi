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
import java.util.Arrays;
import java.util.List;

import tong.lan.com.hyperledger.R;
import tong.lan.com.hyperledger.domain.Product;
import tong.lan.com.hyperledger.domain.ProductType;

@ContentView(R.layout.activity_add_product_type)
public class AddProductTypeActivity extends AppCompatActivity {

    @ViewInject(R.id.add_product_type_text)
    private EditText mProductType;
    @ViewInject(R.id.title_text)
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("新增产品类别");
    }


    //确认提交
    @Event(value = {R.id.title_save},type = View.OnClickListener.class)
    private void submit(View v)
    {
        String type = mProductType.getText().toString();
        if (!type.isEmpty()) {
            ProductType mProduct = new ProductType(type);
            if (mProduct.save()) {
                Toast.makeText(this, "存储成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "存储失败", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
        else {
            Toast.makeText(this, "类型名称未填写！", Toast.LENGTH_SHORT).show();
        }
    }

    //返回键
    @Event(value = {R.id.title_cancel},type = View.OnClickListener.class)
    private void addEmployeeBack(View v)
    {
        finish();
    }
}
