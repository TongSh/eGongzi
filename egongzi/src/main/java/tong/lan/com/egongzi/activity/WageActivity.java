package tong.lan.com.egongzi.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import tong.lan.com.egongzi.R;
import tong.lan.com.egongzi.domain.Employee;
import tong.lan.com.egongzi.domain.Make;
import tong.lan.com.egongzi.utils.DateUtil;

import org.litepal.crud.DataSupport;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WageActivity extends AppCompatActivity {
    private SimpleAdapter sa; //创建一个SimpleAdapter
    private ListView wage_list;//总计
    private Spinner month;//月份下拉框
    private Spinner year; //年份下拉框
    private Integer mon;
    private Integer y;
    private DecimalFormat df = new DecimalFormat("#.0");
    private TextView total_wage;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wage);
        context = this;
        // Inflate the layout for this fragment
        Calendar calendar=Calendar.getInstance();
        y = calendar.get(Calendar.YEAR);
        mon = calendar.get(Calendar.MONTH)+1;
        wage_list = (ListView)findViewById(R.id.onefragment_content);
        total_wage = (TextView)findViewById(R.id.wage_total);
        month = (Spinner)findViewById(R.id.wage_month);
        year = (Spinner)findViewById(R.id.wage_year);
        getMonth();
        findWageByEmployee();
    }

    //按月查询工资，按员工id聚合
    private void findWageByEmployee()
    {
        @SuppressLint("UseSparseArrays") Map<Integer,Double> wage = new HashMap<>();

        long startDay = DateUtil.date2stamp(DateUtil.getString(y,mon,1));
        long endDay = DateUtil.date2stamp(DateUtil.getString(y,mon+1,1));
        List<Make> makeList = DataSupport.
                where("makedate >= ? And makedate < ?",startDay+"",endDay+"").find(Make.class,true);

        for(Make i : makeList){
            Log.i("date---",i.getMakeDate().toString());// 2018-8-23
        }

        for (Make make : makeList)
        {
            int eID = make.getEmployee().getId();
            double eWage = make.getMakeAmount()*make.getProduct().getProductWage();
            if(wage.containsKey(eID))
                wage.put(eID, wage.get(eID)+eWage);
            else
                wage.put(eID, eWage);
        }

        double sum = 0;
        List<Map<String,Object>> wageList = new ArrayList<>();
        for(Integer eID : wage.keySet()){
            Map<String,Object> item = new HashMap<String,Object>();
            item.put("id",eID);
            item.put("name",DataSupport.find(Employee.class,eID).getEmployeeName());
            item.put("wage",wage.get(eID));
            wageList.add(item);
            // for SUM
            sum += wage.get(eID);
        }

        sa=new SimpleAdapter(this,
                wageList,
                R.layout.statistics,
                new String[]{"name","wage"},
                new int[]{R.id.wage_name,R.id.wage_item});
        wage_list.setAdapter(sa);
        total_wage.setText("总计："+df.format(sum)+"元");
        //为listView对象进行监听：当点击子项目的时候触发
        wage_list.setOnItemClickListener(new ItemClickEvent());
    }

    private void getMonth()
    {
        // 建立数据源
        String[] mItems = {"1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"};
        String[] yItems = {"2017","2018","2019","2020","2021","2022"};
        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter_m=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
        ArrayAdapter<String> adapter_y=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, yItems);
        adapter_m.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_y.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        month.setAdapter(adapter_m);
        year.setAdapter(adapter_y);
        month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                mon = pos+1;
                findWageByEmployee();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                y = pos+2017;
                Log.i("year---",y+"");
                findWageByEmployee();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }

    //继承OnItemClickListener，当子项目被点击的时候触发
    private class ItemClickEvent implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //通过单击事件，获得单击选项的内容
            HashMap<String,Object> map = (HashMap<String, Object>) wage_list.getItemAtPosition(i);
            int eID = (int) map.get("id");

            Intent intent = new Intent(context, WageDetailActivity.class);
            intent.putExtra("employee", eID);
            intent.putExtra("year",y);
            intent.putExtra("month",mon);
            startActivity(intent);
        }
    }
}
