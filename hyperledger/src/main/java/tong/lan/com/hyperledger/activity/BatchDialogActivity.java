package tong.lan.com.hyperledger.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;

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
import tong.lan.com.hyperledger.adapter.BatchDialogAdapter;
import tong.lan.com.hyperledger.adapter.BatchRecordAdapter;
import tong.lan.com.hyperledger.adapter.WageEmplAdapter;
import tong.lan.com.hyperledger.bean.BatchDialogBean;
import tong.lan.com.hyperledger.bean.WageEmplListBean;
import tong.lan.com.hyperledger.domain.Employee;
import tong.lan.com.hyperledger.domain.Product;
import tong.lan.com.hyperledger.domain.Record;
import tong.lan.com.hyperledger.utils.DateUtil;

import static java.lang.String.format;
import static org.litepal.LitePalApplication.getContext;

@ContentView(R.layout.dialog_batch_day)
public class BatchDialogActivity extends Activity {

    @ViewInject(R.id.batch_dialog_clear)
    private TextView clearAll;
    @ViewInject(R.id.batch_dialog_new)
    private TextView newRecd;
//    @ViewInject(R.id.batch_record_recycler)
    private RecyclerView mRecyclerview;
    @ViewInject(R.id.batch_dialog_cancel)
    private ImageView cancel;

    private Intent intent;
    private int year;
    private int month;
    private int day;
    private Employee employee;
    private List<Record> recordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_batch_day);
        x.view().inject(this);
        mRecyclerview = findViewById(R.id.batch_dialog_recyclerview);

        intent = getIntent();
        setRecyclerview();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRecyclerview();
    }

    private void setRecyclerview(){
        year =  intent.getIntExtra("year",0);
        month = intent.getIntExtra("month",0);
        day = intent.getIntExtra("day",0);
        int eID = intent.getIntExtra("eID",0);
        employee = DataSupport.find(Employee.class,eID);

        long timestamp = DateUtil.date2stamp(DateUtil.getString(year,month,day));
        recordList = DataSupport.
                where("date = ? And employee_id = ?",timestamp+"",eID+"")
                .find(Record.class,true);

        List<BatchDialogBean> dialogData;
        dialogData = new ArrayList<>();
        for (Record record : recordList){
            dialogData.add(new BatchDialogBean(record.getProduct().getName(),record.getAmount()));
        }

        // 设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(BatchDialogActivity.this);
        mRecyclerview.setLayoutManager(layoutManager);
        // 设置adapter
        BatchDialogAdapter batchRecordAdapter = new BatchDialogAdapter(getContext(),dialogData);
        mRecyclerview.setAdapter(batchRecordAdapter);
    }

    public void initListener() {
        // 选择工资显示月份
        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recordList.size() == 0)
                    return;
                AlertDialog.Builder builder = new AlertDialog.Builder(BatchDialogActivity.this);
                builder.setIcon(R.mipmap.delete);
                builder.setTitle("删除确认");
                builder.setMessage("删除"+employee.getEmployeeName()+month+"月"+day+"日的"+recordList.size()+"条记录？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
//                        Toast.makeText(BatchDialogActivity.this, "positive: " + which, Toast.LENGTH_SHORT).show();
                        long timestamp = DateUtil.date2stamp(DateUtil.getString(year,month,day));
                        DataSupport.deleteAll(Record.class,"date = "+timestamp+" And employee_id = "+employee.getId());
                        finish();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
//                        Toast.makeText(BatchDialogActivity.this, "negative: " + which, Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });

        newRecd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BatchDialogActivity.this, AddRecordBatchActivity.class);
                intent.putExtra("eID",employee.getId());
                intent.putExtra("sdate",DateUtil.getString(year,month,day));
                startActivity(intent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    };
}
