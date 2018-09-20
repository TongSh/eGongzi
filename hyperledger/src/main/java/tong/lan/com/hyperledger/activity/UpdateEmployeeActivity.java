package tong.lan.com.hyperledger.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import tong.lan.com.hyperledger.R;
import tong.lan.com.hyperledger.domain.Employee;

@ContentView(R.layout.activity_update_employee)
public class UpdateEmployeeActivity extends AppCompatActivity {

    @ViewInject(R.id.update_employee_name)
    private EditText employeeName;
    @ViewInject(R.id.update_employee_phone)
    private EditText employeePhone;
    @ViewInject(R.id.update_employee_title)
    private TextView title;

    int eID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        Intent intent = getIntent();
        eID = intent.getIntExtra("eID",0);
        employeeName.setText(intent.getStringExtra("eName"));
        employeePhone.setText(intent.getStringExtra("ePhone"));
        title.setText("更新员工信息");
    }

    //确认提交
    @Event(value = {R.id.update_employee_save},type = View.OnClickListener.class)
    private void submit(View v)
    {
        String employee_name = employeeName.getText().toString();
        String employee_phone = employeePhone.getText().toString();
        if (!employee_name.isEmpty()) {
            Employee mEmployee = new Employee();
            mEmployee.setEmployeeName(employee_name);
            mEmployee.setEmployeePhone(employee_phone);
            if (mEmployee.update(eID) == 1) {
                Toast.makeText(this, "存储成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "存储失败", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(UpdateEmployeeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(this, "员工姓名未填写！", Toast.LENGTH_SHORT).show();
        }
    }

    //返回键
    @Event(value = {R.id.update_employee_cancel},type = View.OnClickListener.class)
    private void addEmployeeBack(View v)
    {
        Intent intent = new Intent(UpdateEmployeeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
