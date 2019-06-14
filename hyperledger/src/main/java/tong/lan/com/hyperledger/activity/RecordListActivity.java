package tong.lan.com.hyperledger.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tong.lan.com.hyperledger.R;
import tong.lan.com.hyperledger.adapter.RecdListAdapter;
import tong.lan.com.hyperledger.bean.RecdListBean;
import tong.lan.com.hyperledger.domain.Employee;
import tong.lan.com.hyperledger.domain.Record;

public class RecordListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);
        toolbar = findViewById(R.id.recd_list_toolbar);
        recyclerView = findViewById(R.id.recd_list_recycler);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        context = this;

        setRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRecyclerView();
    }

    public void setRecyclerView(){
        List<Record> records = DataSupport.order("id desc").find(Record.class,true);
        List<RecdListBean> recdListBeans = new ArrayList<>();
        for(Record record : records){
            String productInfo = record.getProduct().getName()
                    +"  *  "+ record.getAmount();//产品及数量信息
            String employeeName = record.getEmployee().getEmployeeName();//生产人
            Calendar cal = Calendar.getInstance();
            cal.setTime(record.getDate());
            String recordDate = (cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DAY_OF_MONTH);//生产日期
            double wage = record.getAmount() * record.getProduct().getWage();//工资

            recdListBeans.add(new RecdListBean(productInfo,employeeName,recordDate,wage,record.getId()));
        }
        // 设置布局管理器
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // 设置adapter
        final RecdListAdapter recdListAdapter = new RecdListAdapter(this, recdListBeans);
        // 匿名内部类，实现适配器里面定义的接口
        recdListAdapter.setOnMyItemClickListener(new RecdListAdapter.OnMyItemClickListener(){
            @Override
            public void myClick(View v, int pos) {
            }

            @Override
            public void mLongClick(View v, int pos) {
//                Toast.makeText(getApplicationContext(),"Long Click",Toast.LENGTH_LONG).show();
                final Record record = DataSupport.find(Record.class,recdListAdapter.getRecID(pos),true);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.mipmap.delete);
                builder.setTitle("删除确认");
                builder.setMessage("删除"+record.getEmployee().getEmployeeName()+"的一条记录？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        DataSupport.delete(Record.class,record.getId());
                        onResume();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {}
                });
                builder.show();
            }
        });
        recyclerView.setAdapter(recdListAdapter);
    }
}
