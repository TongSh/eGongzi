package tong.lan.com.hyperledger.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.litepal.crud.DataSupport;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tong.lan.com.hyperledger.R;
import tong.lan.com.hyperledger.adapter.WageEmplAdapter;
import tong.lan.com.hyperledger.bean.WageEmplListBean;
import tong.lan.com.hyperledger.domain.Employee;
import tong.lan.com.hyperledger.domain.Record;
import tong.lan.com.hyperledger.domain.Product;
import tong.lan.com.hyperledger.utils.DateUtil;
import tong.lan.com.hyperledger.utils.SaveImg;

import static java.lang.String.format;
import static org.litepal.LitePalApplication.getContext;

@ContentView(R.layout.activity_wage_detail)
public class WageEmplDetailActivity extends AppCompatActivity{


    List<WageEmplListBean> mData;
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

    private int year;
    private int month;
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
        mToolbar.inflateMenu(R.menu.wage_detail_menu);//设置右上角的填充菜单
    }

    private void setDetail(){
        // 前者记录工资，后者记录出勤
        double wage = 0;
        Set<Date> workDay = new HashSet<>();

        year = Integer.parseInt(mYear.getText().toString());
        month = Integer.parseInt(mMonth.getText().toString());
        long startDay = DateUtil.date2stamp(DateUtil.getString(year,month,1));
        long endDay = DateUtil.date2stamp(DateUtil.getString(year,month+1,1));
        List<Record> recordList = DataSupport.
                where("date >= ? And date < ? And employee_id = ?",startDay+"",endDay+"",employee.getId()+"")
                .order("date asc").find(Record.class,true);

        mData = new ArrayList<>();
        for (Record record : recordList)
        {
            double singleWage = record.getAmount()* record.getProduct().getWage();
            wage += singleWage;
            workDay.add(record.getDate());
            Employee e = record.getEmployee();
            Product p = record.getProduct();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(record.getDate());
            String date = DateUtil.getString(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH));
            date = date.split("-")[1]+"-"+date.split("-")[2];
            mData.add(new WageEmplListBean(e.getId(),
                    e.getEmployeeName(),
                    p.getName(),
                    record.getAmount(),
                    date,
                    singleWage));
        }

        wageTotal.setText((int)wage+"");
        wageAverage.setText((int) (wage/workDay.size())+"");

        // 设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(WageEmplDetailActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);
        // 设置adapter
        WageEmplAdapter wageEmplAdapter = new WageEmplAdapter(getContext(),mData);
        mRecyclerView.setAdapter(wageEmplAdapter);
    }

    public void initListener(){
        // 选择工资显示月份
        mDateSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //时间选择器
                TimePickerView pvTime = new TimePickerBuilder(getApplicationContext(), new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date,View v) {//选中事件回调
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        mYear.setText(format("%d", calendar.get(Calendar.YEAR)));
                        int monthOfYear = calendar.get(Calendar.MONTH) + 1;
                        if(monthOfYear > 9)
                            mMonth.setText(format("%d", monthOfYear));
                        else
                            mMonth.setText(format("0%d", monthOfYear));
                        setDetail();
                    }
                }).setType(new boolean[]{true, true, false, false, false, false})//分别对应年月日时分秒，默认全部显示
                        .setCancelText("取消")//取消按钮文字
                        .setSubmitText("确定")//确认按钮文字
                        .setContentTextSize(24)//滚轮文字大小
                        .setTitleSize(24)//标题文字大小
                        .setTitleText("选择月份")//标题文字
                        .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                        .isCyclic(false)//是否循环滚动
                        .setTitleColor(Color.BLACK)//标题文字颜色
                        .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                        .setCancelColor(Color.BLUE)//取消按钮文字颜色
                        .setLabel("年","月","日","时","分","秒")
                        .isDialog(false)//是否显示为对话框样式
                        .build();
                pvTime.show();
            }
        });

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.action_share) {
                    String title = employee.getEmployeeName()+mYear.getText().toString()+"年"+mMonth.getText().toString()+"月工资";
                    Bitmap bitmap = SaveImg.getBitmapWageDetail(WageEmplDetailActivity.this,mRecyclerView,mData,title,wageTotal.getText().toString());
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
}
