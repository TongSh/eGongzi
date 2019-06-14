package tong.lan.com.hyperledger.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;

import org.litepal.crud.DataSupport;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import tong.lan.com.hyperledger.R;
import tong.lan.com.hyperledger.adapter.BatchRecordAdapter;
import tong.lan.com.hyperledger.bean.BatchRecordBean;
import tong.lan.com.hyperledger.domain.Employee;
import tong.lan.com.hyperledger.domain.Record;
import tong.lan.com.hyperledger.utils.DateUtil;

import static java.lang.String.format;
import static org.litepal.LitePalApplication.getContext;

@ContentView(R.layout.activity_batch_record)
public class BatchRecordActivity extends AppCompatActivity {
    private List<String> employeeNames;

    private List<BatchRecordBean> mData;
//    @ViewInject(R.id.batch_record_date)
//    private TextView mDate;
    @ViewInject(R.id.batch_record_toolbar)
    private Toolbar mToolbar;
    @ViewInject(R.id.batch_record_recycler)
    private RecyclerView batchTable;

    private Intent intent;
    private Employee employee;
    private int year;
    private int month;
    private Context context;
    private boolean firstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_record);
        x.view().inject(this);
        context = this;
        intent = getIntent();
        firstTime = true;
        getIntentValue();
        setInfo();
        setDetail();
        setMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setInfo();
        setDetail();
    }

    private void setMenu(){
        mToolbar.inflateMenu(R.menu.batch_record_menu);//设置右上角的填充菜单
        // toolbar返回按钮
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.action_set_empl) {
//                    Toast.makeText(BatchRecordActivity.this , "选择员工" , Toast.LENGTH_SHORT).show();
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
                            onResume();
                        }
                    }).setContentTextSize(25)
                            .build();
                    pvOptions.setPicker(employeeNames);
                    pvOptions.show();
                } else if (menuItemId == R.id.action_set_mon) {
//                    Toast.makeText(BatchRecordActivity.this , "选择月份" , Toast.LENGTH_SHORT).show();
                     //时间选择器
                    TimePickerView pvTime = new TimePickerBuilder(context, new OnTimeSelectListener() {
                        @Override
                        public void onTimeSelect(Date date, View v) {//选中事件回调
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date);
                            year = calendar.get(Calendar.YEAR);
                            month = calendar.get(Calendar.MONTH)+1;
                            mToolbar.setSubtitle(format("%s %d年%d月", employee.getEmployeeName(),year, month));
                            onResume();
                        }
                    }).setType(new boolean[]{true, true, false, false, false, false})//分别对应年月日时分秒，默认全部显示
                            .setContentTextSize(25)//滚轮文字大小
                            .isCyclic(false)//是否循环滚动
                            .build();
                    pvTime.show();
                }
                return true;
            }
        });
    }

    private void getIntentValue(){
        int eID = intent.getIntExtra("eID",0);
        employee = DataSupport.find(Employee.class,eID);
        year = intent.getIntExtra("year",0);
        month = intent.getIntExtra("month",0);
        return;
    }

    private void setInfo(){
        mToolbar.setSubtitle(format("%s %d年%d月", employee.getEmployeeName(),year, month));
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        return;
    }

    private void setDetail(){
        int days = DateUtil.getDaysByYearMonth(year,month);
        int[] recs;
        double[] wage;
        recs = new int[days];
        wage = new double[days];

        long startDay = DateUtil.date2stamp(DateUtil.getString(year,month,1));
        long endDay = DateUtil.date2stamp(DateUtil.getString(year,month+1,1));
        List<Record> recordList = DataSupport.
                where("date >= ? And date < ? And employee_id = ?",startDay+"",endDay+"",employee.getId()+"")
                .order("date asc").find(Record.class,true);

        mData = new ArrayList<>();
        for (Record record : recordList)
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(record.getDate());
            int mday = calendar.get(Calendar.DAY_OF_MONTH)-1;

            recs[mday] += 1;
            wage[mday] += record.getAmount() * record.getProduct().getWage();
        }

        for (int i = 0; i < days; ++i){
            mData.add(new BatchRecordBean(i+1,recs[i],wage[i]));
        }

        // 设置布局管理器
        GridLayoutManager layoutManager = new GridLayoutManager(BatchRecordActivity.this,4);
        batchTable.setLayoutManager(layoutManager);
        // 设置adapter
        BatchRecordAdapter batchRecordAdapter = new BatchRecordAdapter(getContext(),mData);
        // 匿名内部类，实现适配器里面定义的接口
        batchRecordAdapter.setOnMyItemClickListener(new BatchRecordAdapter.OnMyItemClickListener(){
            @Override
            public void myClick(View v, int pos) {
                Intent intent = new Intent(BatchRecordActivity.this, BatchDialogActivity.class);
                intent.putExtra("eID", employee.getId());
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("day",pos+1);
                startActivity(intent);
            }

            @Override
            public void mLongClick(View v, int pos) {
//                Toast.makeText(getContext(),"onLongClick---"+pos,Toast.LENGTH_LONG).show();
            }
        });
        batchTable.setAdapter(batchRecordAdapter);

        int itemWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,85,getResources().getDisplayMetrics());

        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int screenWidth = dm.widthPixels;

        batchTable.addItemDecoration(new SpaceItemDecoration((screenWidth/4-itemWidth)/2,firstTime));
        firstTime = false;
    }

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private int space;  //位移间距
        private boolean ftime;
        public SpaceItemDecoration(int space, boolean firstTime) {
            this.space = space;
            this.ftime = firstTime;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int span = 4;
            if (ftime) {
                outRect.left = space;
                if (parent.getChildAdapterPosition(view) >= span) {
                    float scale = getContext().getResources().getDisplayMetrics().density;
                    outRect.top = (int) (10 * scale + 0.5f);
                } else {
                    outRect.top = 0;
                }
            }else {
                outRect.left = 0;
                outRect.top = 0;
            }
            return;
        }
    }
}
