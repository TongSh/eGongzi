package tong.lan.com.egongzi.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;
import tong.lan.com.egongzi.R;
import tong.lan.com.egongzi.bean.EmployeeBean;
import tong.lan.com.egongzi.domain.Employee;
import tong.lan.com.egongzi.utils.CreateUserDialog;
import tong.lan.com.egongzi.utils.EmployeeAdapter;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListEmployeeActivity extends AppCompatActivity {
    private List<Map<String,Object>> list_map = new ArrayList<Map<String,Object>>(); //定义一个适配器对象
    private ListView listView;
    private EmployeeAdapter adapter;
    private List<EmployeeBean> lists;
    //dialog
    private CreateUserDialog createUserDialog;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = ListEmployeeActivity.this;
        setContentView(R.layout.activity_list_employee);
        listView = findViewById(R.id.employee_list);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //弹出修改对话框
                createUserDialog = new CreateUserDialog((Activity) context,"","",R.style.Theme_AppCompat_Dialog,new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.btn_save_pop:
                                String name = createUserDialog.text_name.getText().toString().trim();
                                String mobile = createUserDialog.text_mobile.getText().toString().trim();
                                System.out.println(name+"——"+mobile);
                                if (!name.isEmpty()) {
                                    //创建要保存的类对象
                                    Employee mEmployee = new Employee();
                                    mEmployee.setEmployeeName(name);
                                    mEmployee.setEmployeePhone(mobile);
                                    if (mEmployee.save()) {
                                        //存储成功
                                        lists.add(new EmployeeBean(mEmployee.getId(), name, mobile));
                                        adapter.notifyDataSetChanged();
                                    }
                                    createUserDialog.dismiss();
                                }else {
                                    Toast.makeText(context, "员工姓名未填写！", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case R.id.btn_cancel_pop:
                                createUserDialog.dismiss();
                                Log.i("点击================","取消");
                                break;
                            default:
                                createUserDialog.dismiss();
                                break;
                        }
                    }
                });
                createUserDialog.show();
            }
        });
        fillInList();
    }
    //填充首页的listView
    public void fillInList(){
        lists=new ArrayList<EmployeeBean>();
        List<Employee> myList = DataSupport.findAll(Employee.class);
        for (int i=0;i<myList.size();i++){
            lists.add(new EmployeeBean(myList.get(i).getId(),myList.get(i).getEmployeeName(),myList.get(i).getEmployeePhone()));
        }
        adapter=new EmployeeAdapter(ListEmployeeActivity.this,lists);
        listView.setAdapter(adapter);
        adapter.setMode(Attributes.Mode.Single);
    }
}
