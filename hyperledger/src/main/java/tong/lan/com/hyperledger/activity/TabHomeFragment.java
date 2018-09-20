package tong.lan.com.hyperledger.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tong.lan.com.hyperledger.R;
import tong.lan.com.hyperledger.adapter.DayProductAdapter;
import tong.lan.com.hyperledger.adapter.RecordAdapter;
import tong.lan.com.hyperledger.bean.DayProductBean;
import tong.lan.com.hyperledger.bean.RecordBean;
import tong.lan.com.hyperledger.domain.Make;
import tong.lan.com.hyperledger.domain.Product;
import tong.lan.com.hyperledger.utils.DateUtil;

public class TabHomeFragment extends Fragment{

    private LinearLayout addRecord;
    private RecyclerView recentRecord;
    private RecyclerView dayProduct;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_home,null);
        initUnit(view);
        initButton();
        setRecentRecord();
        setDayProduct();
        return view;
    }

    public void initUnit(View v){
        addRecord = v.findViewById(R.id.home_page_record);
        recentRecord = v.findViewById(R.id.recent_record_recycler);
        dayProduct = v.findViewById(R.id.day_product_recycler);
    }

    public void initButton(){
        addRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddRecordActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    public void setRecentRecord(){
        List<Make> makes = DataSupport.order("id desc").find(Make.class,true);
        List<RecordBean> recordBeans = new ArrayList<>();
        for(int i = 0; i < 3 && i < makes.size(); ++i){
            Make make = makes.get(i);
            String productInfo = make.getProduct().getProductType()
                    +" "+make.getProduct().getProductName()
                    +"  *  "+make.getMakeAmount();//产品及数量信息
            String employeeName = make.getEmployee().getEmployeeName();//生产人
            Calendar cal = Calendar.getInstance();
            cal.setTime(make.getMakeDate());
            String recordDate = (cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DAY_OF_MONTH);//生产日期
            double wage = make.getMakeAmount() * make.getProduct().getProductWage();//工资

            recordBeans.add(new RecordBean(productInfo,employeeName,recordDate,wage));
        }
        // 设置布局管理器
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recentRecord.setLayoutManager(layoutManager);
        // 设置adapter
        recentRecord.setAdapter(new RecordAdapter(getContext(),recordBeans));
    }

    public void setDayProduct(){
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH)+1;
        int day = now.get(Calendar.DAY_OF_MONTH);
        long timeStamp = DateUtil.date2stamp(DateUtil.getString(year,month,day));

        List<Make> makes = DataSupport.where("makedate = ?",timeStamp+"").find(Make.class,true);
        List<DayProductBean> dayProductBeans = new ArrayList<>();

        Map<Integer,Integer> map = new HashMap<>();
        for(Make make : makes){
            int pID = make.getProduct().getId();
            if(map.keySet().contains(pID)){
                map.put(pID, map.get(pID)+make.getMakeAmount());
            }else {
                map.put(pID, make.getMakeAmount());
            }
        }

        for(int key : map.keySet()){
            Product product = DataSupport.find(Product.class,key);
            String productInfo = product.getProductType()+" "+product.getProductName();//产品信息
            int amount = map.get(key);
            double margin = product.getProductMargin()*amount;
            dayProductBeans.add(new DayProductBean(productInfo,amount,margin));
        }
        // 设置布局管理器
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        dayProduct.setLayoutManager(layoutManager);
        // 设置adapter
        dayProduct.setAdapter(new DayProductAdapter(getContext(),dayProductBeans));
    }
}

