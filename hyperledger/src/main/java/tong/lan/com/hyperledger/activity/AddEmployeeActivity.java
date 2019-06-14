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

public class AddEmployeeActivity extends AppCompatActivity {

    private EditText employeeName;
    private TextView save;
    private TextView cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);
        employeeName = findViewById(R.id.add_employee_name);
        save = findViewById(R.id.add_employee_save);
        cancel = findViewById(R.id.add_employee_cancel);
        addListener();
    }

    private void addListener()
    {
        // 确认提交
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String employee_name = employeeName.getText().toString();
            if (employee_name.isEmpty()) {
                Toast.makeText(getApplicationContext(), "员工姓名为空！", Toast.LENGTH_SHORT).show();
                return;
            }

            Employee mEmployee = new Employee();
            mEmployee.setEmployeeName(employee_name);
            if (mEmployee.save()) {
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
