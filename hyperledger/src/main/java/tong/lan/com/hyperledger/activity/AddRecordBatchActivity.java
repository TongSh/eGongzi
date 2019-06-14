package tong.lan.com.hyperledger.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;

import org.angmarch.views.NiceSpinner;
import org.litepal.crud.DataSupport;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import tong.lan.com.hyperledger.R;
import tong.lan.com.hyperledger.domain.Employee;
import tong.lan.com.hyperledger.domain.Product;
import tong.lan.com.hyperledger.domain.Record;
import tong.lan.com.hyperledger.utils.DateUtil;

import static java.lang.String.format;


@ContentView(R.layout.activity_add_batch_record)
public class AddRecordBatchActivity extends AppCompatActivity {
    List<String> employeeNames;
    List<String> productNames;

    @ViewInject(R.id.batch_add_record_product)
    private TextView prodSelect;
    @ViewInject(R.id.batch_add_record_amount)
    private EditText amount;
    @ViewInject(R.id.batch_add_record_name)
    private TextView eName;
    @ViewInject(R.id.batch_add_record_date)
    private TextView date;

    private TextView save;
    private TextView cancel;

    private int prodID;
    private Intent intant;
    private Context context;
    private Employee employee;
    private Date datevalue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        intant = getIntent();
        context = this;
        setInfo();
        prodSelect();
        initView();
        addListener();
    }

    public void initView(){
        save = findViewById(R.id.batch_add_record_save);
        cancel = findViewById(R.id.batch_add_record_cancel);
    }

    public void setInfo(){
        employee = DataSupport.find(Employee.class,intant.getIntExtra("eID",0));
        eName.setText(employee.getEmployeeName());

        String sdate = intant.getStringExtra("sdate");
        datevalue = DateUtil.getDate(sdate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datevalue);
        date.setText(format("%d年%d月", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1));
    }

    public void prodSelect(){
        prodSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productNames = new ArrayList<>();
                final List<Product> prodList = DataSupport.findAll(Product.class);
                for(Product product : prodList){
                    productNames.add(product.getName());
                }

                //条件选择器
                OptionsPickerView pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                        //返回的分别是三个级别的选中位置
                        String tx = productNames.get(options1);
                        prodID = prodList.get(options1).getId();
                        prodSelect.setText(tx);
                    }
                }).setContentTextSize(25)
                        .build();
                pvOptions.setPicker(productNames);
                pvOptions.show();
            }
        });

    }

    private void addListener(){
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product = DataSupport.find(Product.class,prodID);

                if (product == null){
                    Toast.makeText(getApplicationContext(), "请选择产品！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (amount.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "请设置数量！", Toast.LENGTH_SHORT).show();
                    return;
                }

                int a = Integer.parseInt(amount.getText().toString());
                Record record = new Record(employee,product,datevalue,a);
                if (record.save()) {
                    Toast.makeText(getApplicationContext(), "存储成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "存储失败", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
