package tong.lan.com.hyperledger.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tong.lan.com.hyperledger.R;
import tong.lan.com.hyperledger.adapter.WageAdapter;
import tong.lan.com.hyperledger.adapter.WageDetailAdapter;
import tong.lan.com.hyperledger.bean.RecordBean;
import tong.lan.com.hyperledger.bean.WageBean;
import tong.lan.com.hyperledger.bean.WageDetailBean;
import tong.lan.com.hyperledger.domain.Employee;
import tong.lan.com.hyperledger.domain.Make;
import tong.lan.com.hyperledger.domain.Product;
import tong.lan.com.hyperledger.utils.DateUtil;
import tong.lan.com.hyperledger.utils.SaveImg;

import static org.litepal.LitePalApplication.getContext;

@ContentView(R.layout.activity_wage_detail)
public class WageDetailActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {


    List<WageDetailBean> mData;
    @ViewInject(R.id.wage_detail_year)
    private TextView mYear;
    @ViewInject(R.id.wage_detail_month)
    private TextView mMonth;
    @ViewInject(R.id.wage_detail_toltal)
    private TextView wageTotal;
    @ViewInject(R.id.wage_detail_average)
    private TextView wageAverage;
    @ViewInject(R.id.wage_detail_toolbar)
    private Toolbar mToolbar;
    @ViewInject(R.id.wage_detail_recycler)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.wage_detail_date)
    private LinearLayout mDateSearch;
    private Intent intent;
    private Employee employee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        intent = getIntent();
        setInfo();
        setDetail();
        initListener();
    }

    private void setInfo(){
        mYear.setText(intent.getStringExtra("year"));
        mMonth.setText(intent.getStringExtra("month"));
        int eID = intent.getIntExtra("eID",0);
        employee = DataSupport.find(Employee.class,eID);
        mToolbar.setSubtitle(employee.getEmployeeName());
        mToolbar.inflateMenu(R.menu.wage_detailtoolbar_menu);//设置右上角的填充菜单
    }

    private void setDetail(){
        // 前者记录工资，后者记录出勤
        double wage = 0;
        Set<Date> workDay = new HashSet<>();

        int year = Integer.parseInt(mYear.getText().toString());
        int month = Integer.parseInt(mMonth.getText().toString());
        long startDay = DateUtil.date2stamp(DateUtil.getString(year,month,1));
        long endDay = DateUtil.date2stamp(DateUtil.getString(year,month+1,1));
        List<Make> makeList = DataSupport.
                where("makedate >= ? And makedate < ? And employee_id = ?",startDay+"",endDay+"",employee.getId()+"")
                .order("makedate asc").find(Make.class,true);

        mData = new ArrayList<>();
        for (Make make : makeList)
        {
            double singleWage = make.getMakeAmount()*make.getProduct().getProductWage();
            wage += singleWage;
            workDay.add(make.getMakeDate());
            Employee e = make.getEmployee();
            Product p = make.getProduct();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(make.getMakeDate());
            String date = DateUtil.getString(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH));
            date = date.split("-")[1]+"-"+date.split("-")[2];
            mData.add(new WageDetailBean(e.getId(),
                    e.getEmployeeName(),
                    p.getProductType()+" "+p.getProductName(),
                    make.getMakeAmount(),
                    date,
                    singleWage));
        }

        wageTotal.setText((int)wage+"");
        wageAverage.setText((int) (wage/workDay.size())+"");

        // 设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(WageDetailActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);
        // 设置adapter
        WageDetailAdapter wageDetailAdapter = new WageDetailAdapter(getContext(),mData);
        mRecyclerView.setAdapter(wageDetailAdapter);
    }

    public void initListener(){
        // 选择工资显示月份
        mDateSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        WageDetailActivity.this,
                        now.get(Calendar.YEAR), // Initial year selection
                        now.get(Calendar.MONTH), // Initial month selection
                        now.get(Calendar.DAY_OF_MONTH) // Inital day selection
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.action_share) {
                    String title = employee.getEmployeeName()+mYear.getText().toString()+"年"+mMonth.getText().toString()+"月工资";
                    Bitmap bitmap = SaveImg.getBitmapWageDetail(WageDetailActivity.this,mRecyclerView,mData,title,wageTotal.getText().toString());
                    Uri path = SaveImg.saveBitmap(bitmap,title);

                    // 调用系统分享
                    Intent share_intent = new Intent();
                    share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
                    share_intent.setType("image/*");  //设置分享内容的类型
                    share_intent.putExtra(Intent.EXTRA_STREAM, path);
                    //创建分享的Dialog
                    share_intent = Intent.createChooser(share_intent, "分享图片");
                    startActivity(share_intent);
                }
                return true;
            }
        });

        // toolbar返回按钮
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        mYear.setText(year+"");
        if(monthOfYear > 8)
            mMonth.setText((monthOfYear+1)+"");
        else
            mMonth.setText(String.format("0%d", monthOfYear + 1));
        setDetail();
    }
}
