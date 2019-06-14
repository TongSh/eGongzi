package tong.lan.com.hyperledger.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
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
import tong.lan.com.hyperledger.domain.Record;
import tong.lan.com.hyperledger.domain.Product;
import tong.lan.com.hyperledger.utils.DateUtil;

@ContentView(R.layout.activity_add_record)
public class AddRecordActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    List<String> employeeNames;
    List<String> productNames;

    @ViewInject(R.id.add_record_product)
    private TextView prodText;
    @ViewInject(R.id.add_record_employee)
    private TextView emplText;
    @ViewInject(R.id.add_record_date)
    private TextView mDate;
    @ViewInject(R.id.add_record_amount)
    private EditText mAmount;

    Employee employee;
    Product product;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        context = this;
    }

    @Event(value = {R.id.add_record_product},type = View.OnClickListener.class)
    private void prodSelect(View v)
    {
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

    @Event(value = {R.id.add_record_employee},type = View.OnClickListener.class)
    private void emplSelect(View v)
    {
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
//        int employeeID = Integer.parseInt(employeeNames.get(mEmployee.getSelectedIndex()).split(" ")[0]);
//        Employee e = DataSupport.find(Employee.class,employeeID);
//
//        int productID = Integer.parseInt(productNames.get(mProduct.getSelectedIndex()).split(" ")[0]);
//        Product p = DataSupport.find(Product.class,productID);

        Date recordDate = DateUtil.getDate(mDate.getText().toString());
        int amount = Integer.parseInt(mAmount.getText().toString());

        Record record = new Record(employee,product,recordDate,amount);
        if (record.save()) {
            Toast.makeText(this, "存储成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "存储失败", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    //返回键
    @Event(value = {R.id.add_record_cancel},type = View.OnClickListener.class)
    private void Back(View v)
    {
        finish();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = DateUtil.getString(year,monthOfYear+1,dayOfMonth);
        mDate.setText(date);
    }
}
