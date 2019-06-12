package tong.lan.com.hyperledger.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tong.lan.com.hyperledger.R;
import tong.lan.com.hyperledger.adapter.WageAdapter;
import tong.lan.com.hyperledger.bean.WageBean;
import tong.lan.com.hyperledger.domain.Employee;
import tong.lan.com.hyperledger.domain.Make;
import tong.lan.com.hyperledger.utils.DateUtil;
import tong.lan.com.hyperledger.utils.SaveImg;

public class TabWageFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private LinearLayout searchMonth;
    private TextView mYear;
    private TextView mMonth;
    private TextView wageToltal;
    private TextView wageAverage;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    List<WageBean> mDatas;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_wage,null);
        initUnit(view);
        initListener();
        setData();
        return view;
    }

    public void initUnit(View v){
        searchMonth = v.findViewById(R.id.id_tab_1_date);
        mYear = v.findViewById(R.id.id_tab_1_year);
        mMonth= v.findViewById(R.id.id_tab_1_month);
        wageToltal = v.findViewById(R.id.id_tab_1_wage_toltal);
        wageAverage= v.findViewById(R.id.id_tab_1_wage_average);
        recyclerView = v.findViewById(R.id.id_tab_1_wage_recycler);
        floatingActionButton = v.findViewById(R.id.fab_print_wage);

        // 初始化日期
        Calendar now = Calendar.getInstance();
        mYear.setText(now.get(Calendar.YEAR)+"");
        int m = now.get(Calendar.MONTH);
        if(m < 9)
            mMonth.setText("0"+(m+1));
        else
            mMonth.setText(""+(m+1));
    }

    public void initListener(){
        // 选择工资显示月份
        searchMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        TabWageFragment.this,
                        now.get(Calendar.YEAR), // Initial year selection
                        now.get(Calendar.MONTH), // Initial month selection
                        now.get(Calendar.DAY_OF_MONTH) // Inital day selection
                );
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });

        // 监听上划隐藏Fab
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && floatingActionButton.getVisibility() == View.VISIBLE) {
                    floatingActionButton.hide();
                } else if (dy < 0 && floatingActionButton.getVisibility() != View.VISIBLE) {
                    floatingActionButton.show();
                }
            }
        });

        // Fab监听导出照片
        floatingActionButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String title = mYear.getText().toString()+"年"+mMonth.getText().toString()+"月工资";
                Bitmap bitmap = SaveImg.getRecyclerItemsToBitmap(getActivity(),recyclerView,mDatas,title);
                Uri path = SaveImg.saveBitmap(bitmap,title);

                // 调用系统分享
                Intent share_intent = new Intent();
                share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
                share_intent.setType("image/*");  //设置分享内容的类型
                share_intent.putExtra(Intent.EXTRA_STREAM, path);
                //创建分享的Dialog
                share_intent = Intent.createChooser(share_intent, "分享图片");
                getActivity().startActivity(share_intent);
            }
        });

    }

    public void setData(){
        // 前者记录工资，后者记录出勤
        @SuppressLint("UseSparseArrays") Map<Integer,Double> wage = new HashMap<>();
        @SuppressLint("UseSparseArrays") Map<Integer,Set<Date>> workDay = new HashMap<>();

        int year = Integer.parseInt(mYear.getText().toString());
        int month = Integer.parseInt(mMonth.getText().toString());
        long startDay = DateUtil.date2stamp(DateUtil.getString(year,month,1));
        long endDay = DateUtil.date2stamp(DateUtil.getString(year,month+1,1));
        List<Make> makeList = DataSupport.
                where("makedate >= ? And makedate < ?",startDay+"",endDay+"").find(Make.class,true);

        for (Make make : makeList)
        {
            int eID = make.getEmployee().getId();
            double eWage = make.getMakeAmount()*make.getProduct().getWage();
            if(wage.containsKey(eID)) {
                wage.put(eID, wage.get(eID) + eWage);
                workDay.get(eID).add(make.getMakeDate());
            }
            else {
                wage.put(eID, eWage);
                Set<Date> days = new HashSet<>();
                days.add(make.getMakeDate());
                workDay.put(eID, days);
            }
        }

        double sum = 0;
        mDatas = new ArrayList<>();
        for(Integer eID : wage.keySet()){
            double w = wage.get(eID);
            int workdays = workDay.get(eID).size();
            Employee e = DataSupport.find(Employee.class,eID);
            mDatas.add(new WageBean(e.getId(),e.getEmployeeName(),w,workdays));
            // for SUM
            sum += wage.get(eID);
        }
        // 设置布局管理器
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        // 设置adapter
        final WageAdapter wageAdapter = new WageAdapter(getContext(),mDatas);
        // 匿名内部类，实现适配器里面定义的接口
        wageAdapter.setOnMyItemClickListener(new WageAdapter.OnMyItemClickListener(){
            @Override
            public void myClick(View v, int pos) {
                Intent intent = new Intent(getActivity(), WageDetailActivity.class);
                intent.putExtra("eID", wageAdapter.getEmployee(pos));
                intent.putExtra("year", mYear.getText().toString());
                intent.putExtra("month", mMonth.getText().toString());
                startActivity(intent);
            }

            @Override
            public void mLongClick(View v, int pos) {
                Toast.makeText(getActivity(),"onLongClick---"+pos,Toast.LENGTH_LONG).show();
            }
        });
        recyclerView.setAdapter(wageAdapter);
        wageToltal.setText((int) sum+"");
        wageAverage.setText((int) (sum/wage.size())+"");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        mYear.setText(year+"");
        if(monthOfYear > 8)
            mMonth.setText((monthOfYear+1)+"");
        else
            mMonth.setText(String.format("0%d", monthOfYear + 1));
        setData();
    }
}

