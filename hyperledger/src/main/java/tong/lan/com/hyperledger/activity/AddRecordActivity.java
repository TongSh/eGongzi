package tong.lan.com.hyperledger.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.litepal.crud.DataSupport;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import tong.lan.com.hyperledger.R;
import tong.lan.com.hyperledger.domain.Employee;
import tong.lan.com.hyperledger.domain.Record;
import tong.lan.com.hyperledger.domain.Product;
import tong.lan.com.hyperledger.utils.DateUtil;

public class AddRecordActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    List<String> employeeNames;
    List<String> productNames;

    private TextView prodText;
    private TextView emplText;
    private TextView dateText;
    private EditText amountText;

    private TextView save;
    private TextView cancel;

    Employee employee;
    Product product;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        context = this;
        initView();
        addListener();
    }

    public void initView(){
        save = findViewById(R.id.add_record_save);
        cancel = findViewById(R.id.add_record_cancel);
        prodText = findViewById(R.id.add_record_product);
        emplText = findViewById(R.id.add_record_employee);
        dateText = findViewById(R.id.add_record_date);
        amountText = findViewById(R.id.add_record_amount);
    }

    public void addListener(){
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (employee == null){
                    Toast.makeText(getApplicationContext(), "请选择员工！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (product == null){
                    Toast.makeText(getApplicationContext(), "请选择产品！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (dateText.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "请选设置日期！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (amountText.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "请选设置生产数量！", Toast.LENGTH_SHORT).show();
                    return;
                }

                Date recordDate = DateUtil.getDate(dateText.getText().toString());
                int amount = Integer.parseInt(amountText.getText().toString());
                Record record = new Record(employee,product,recordDate,amount);
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

        prodText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productNames = new ArrayList<>();
                final List<Product> prodList = DataSupport.findAll(Product.class);
                for(Product prod : prodList){
                    productNames.add(prod.getName());
                }

                //条件选择器
                OptionsPickerView pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                        //返回的分别是三个级别的选中位置
                        String tx = productNames.get(options1);
                        product = prodList.get(options1);
                        prodText.setText(tx);
                    }
                }).setContentTextSize(25)
                        .build();
                pvOptions.setPicker(productNames);
                pvOptions.show();
            }
        });

        emplText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                employeeNames = new ArrayList<>();
                final List<Employee> emplList = DataSupport.findAll(Employee.class);
                for(Employee empl : emplList){
                    employeeNames.add(empl.getEmployeeName());
                }

                //条件选择器
                OptionsPickerView pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                        //返回的分别是三个级别的选中位置
                        String tx = employeeNames.get(options1);
                        employee = emplList.get(options1);
                        emplText.setText(tx);
                    }
                }).setContentTextSize(25)
                        .build();
                pvOptions.setPicker(employeeNames);
                pvOptions.show();
            }
        });

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        AddRecordActivity.this,
                        now.get(Calendar.YEAR), // Initial year selection
                        now.get(Calendar.MONTH), // Initial month selection
                        now.get(Calendar.DAY_OF_MONTH) // Inital day selection
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = DateUtil.getString(year,monthOfYear+1,dayOfMonth);
        dateText.setText(date);
    }
}
