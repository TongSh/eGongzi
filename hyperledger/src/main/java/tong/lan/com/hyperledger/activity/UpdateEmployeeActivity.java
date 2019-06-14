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
import tong.lan.com.hyperledger.domain.Product;

public class UpdateEmployeeActivity extends AppCompatActivity {

    private EditText employeeName;
    private TextView save;
    private TextView cancel;

    int eID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_employee);

        employeeName = findViewById(R.id.update_employee_name);
        save = findViewById(R.id.update_employee_save);
        cancel = findViewById(R.id.update_employee_cancel);

        Intent intent = getIntent();
        eID = intent.getIntExtra("eID",0);
        employeeName.setText(intent.getStringExtra("eName"));

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
                if (mEmployee.update(eID) == 1) {
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
