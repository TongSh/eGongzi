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

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.angmarch.views.NiceSpinner;
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
import tong.lan.com.hyperledger.domain.Make;
import tong.lan.com.hyperledger.domain.Product;
import tong.lan.com.hyperledger.utils.DateUtil;

@ContentView(R.layout.activity_add_record)
public class AddRecordActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    List<String> employeeNames;
    List<String> productNames;

    @ViewInject(R.id.add_record_product)
    private NiceSpinner mProduct;
    @ViewInject(R.id.add_record_employee)
    private NiceSpinner mEmployee;
    @ViewInject(R.id.add_record_date)
    private TextView mDate;
    @ViewInject(R.id.add_record_amount)
    private EditText mAmount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initSpinner();
    }

    void initSpinner(){
        employeeNames = new ArrayList<>();
        for(Employee employee : DataSupport.findAll(Employee.class)){
            employeeNames.add(employee.getId()+" "+employee.getEmployeeName());
        }
        mEmployee.attachDataSource(employeeNames);
        mEmployee.setTextColor(Color.BLACK);
        mEmployee.setTextSize(18);

        productNames = new ArrayList<>();
        for(Product product : DataSupport.findAll(Product.class)){
            productNames.add(product.getId()+" "+product.getProductType()+" "+product.getProductName());
        }
        mProduct.attachDataSource(productNames);
        mProduct.setTextColor(Color.BLACK);
        mProduct.setTextSize(18);



        mEmployee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        mProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    // 日期
    @Event(value = {R.id.add_record_date},type = View.OnClickListener.class)
    private void getDate(View v)
    {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                AddRecordActivity.this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Inital day selection
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    //确认提交
    @Event(value = {R.id.add_record_save},type = View.OnClickListener.class)
    private void submit(View v)
    {
        int employeeID = Integer.parseInt(employeeNames.get(mEmployee.getSelectedIndex()).split(" ")[0]);
        Employee e = DataSupport.find(Employee.class,employeeID);

        int productID = Integer.parseInt(productNames.get(mProduct.getSelectedIndex()).split(" ")[0]);
        Product p = DataSupport.find(Product.class,productID);

        Date recordDate = DateUtil.getDate(mDate.getText().toString());
        int amount = Integer.parseInt(mAmount.getText().toString());

        Make record = new Make(e,p,recordDate,amount);
        if (record.save()) {
            Toast.makeText(this, "存储成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "存储失败", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(AddRecordActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //返回键
    @Event(value = {R.id.add_record_cancel},type = View.OnClickListener.class)
    private void Back(View v)
    {
        Intent intent = new Intent(AddRecordActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = DateUtil.getString(year,monthOfYear+1,dayOfMonth);
        mDate.setText(date);
    }
}
