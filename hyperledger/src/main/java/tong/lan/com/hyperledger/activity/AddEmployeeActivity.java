package tong.lan.com.hyperledger.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import tong.lan.com.hyperledger.R;
import tong.lan.com.hyperledger.domain.Employee;

@ContentView(R.layout.activity_add_employee)
public class AddEmployeeActivity extends AppCompatActivity {

    @ViewInject(R.id.add_employee_name)
    private EditText employeeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }

    //确认提交
    @Event(value = {R.id.add_employee_save},type = View.OnClickListener.class)
    private void submit(View v)
    {
        String employee_name = employeeName.getText().toString();
        if (!employee_name.isEmpty()) {
            Employee mEmployee = new Employee();
            mEmployee.setEmployeeName(employee_name);
            if (mEmployee.save()) {
                Toast.makeText(this, "存储成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "存储失败", Toast.LENGTH_SHORT).show();
            }
//            Intent intent = new Intent(AddEmployeeActivity.this, MainActivity.class);
//            startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(this, "员工姓名未填写！", Toast.LENGTH_SHORT).show();
        }
    }

    //返回键
    @Event(value = {R.id.add_employee_cancel},type = View.OnClickListener.class)
    private void addEmployeeBack(View v)
    {
//        Intent intent = new Intent(AddEmployeeActivity.this, MainActivity.class);
//        startActivity(intent);
        finish();
    }
}
