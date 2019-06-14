package tong.lan.com.hyperledger.activity;

import android.content.Intent;
import android.os.Bundle;
import android.sax.RootElement;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tong.lan.com.hyperledger.R;
import tong.lan.com.hyperledger.adapter.DayProductAdapter;
import tong.lan.com.hyperledger.adapter.RecdListAdapter;
import tong.lan.com.hyperledger.bean.DayProductBean;
import tong.lan.com.hyperledger.bean.RecdListBean;
import tong.lan.com.hyperledger.domain.Employee;
import tong.lan.com.hyperledger.domain.Record;
import tong.lan.com.hyperledger.domain.Product;
import tong.lan.com.hyperledger.utils.DateUtil;

import static org.litepal.LitePalApplication.getContext;

public class TabHomeFragment extends Fragment{

    private LinearLayout addRecord;
    private RelativeLayout batchRecord;
    private RelativeLayout addEmpl;
    private RelativeLayout addProd;

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

    @Override
    public void onResume() {
        super.onResume();
        setRecentRecord();
        setDayProduct();
    }

    public void initUnit(View v){
        addRecord = v.findViewById(R.id.home_add_record);
        batchRecord = v.findViewById(R.id.home_batch_record);
        recentRecord = v.findViewById(R.id.recent_record_recycler);
        dayProduct = v.findViewById(R.id.day_product_recycler);
        addEmpl = v.findViewById(R.id.home_add_empl);
        addProd = v.findViewById(R.id.home_add_prod);
    }

    public void initButton(){
        addRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddRecordActivity.class);
                startActivity(intent);
            }
        });
        batchRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BatchRecordActivity.class);
                intent.putExtra("eID", DataSupport.findFirst(Employee.class).getId());
                Calendar calendar = Calendar.getInstance();
                intent.putExtra("year", calendar.get(Calendar.YEAR));
                intent.putExtra("month", calendar.get(Calendar.MONTH)+1);
                startActivity(intent);
            }
        });
        addEmpl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddEmployeeActivity.class);
                startActivity(intent);
            }
        });

        addProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddProductActivity.class);
                startActivity(intent);
            }
        });
    }

    public void setRecentRecord(){
        List<Record> records = DataSupport.order("id desc").find(Record.class,true);
        List<RecdListBean> recdListBeans = new ArrayList<>();
        for(int i = 0; i < 3 && i < records.size(); ++i){
            Record record = records.get(i);
            String productInfo = record.getProduct().getName()
                    +"  *  "+ record.getAmount();//产品及数量信息
            String employeeName = record.getEmployee().getEmployeeName();//生产人
            Calendar cal = Calendar.getInstance();
            cal.setTime(record.getDate());
            String recordDate = (cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DAY_OF_MONTH);//生产日期
            double wage = record.getAmount() * record.getProduct().getWage();//工资

            recdListBeans.add(new RecdListBean(productInfo,employeeName,recordDate,wage));
        }
        // 设置布局管理器
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recentRecord.setLayoutManager(layoutManager);
        // 设置adapter
        recentRecord.setAdapter(new RecdListAdapter(getContext(), recdListBeans));
    }

    public void setDayProduct(){
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH)+1;
        int day = now.get(Calendar.DAY_OF_MONTH);
        long timeStamp = DateUtil.date2stamp(DateUtil.getString(year,month,day));

        List<Record> records = DataSupport.where("date = ?",timeStamp+"").find(Record.class,true);
        List<DayProductBean> dayProductBeans = new ArrayList<>();

        Map<Integer,Integer> map = new HashMap<>();
        Map<Integer,Integer> nrecMap = new HashMap<>();
        for(Record record : records){
            int pID = record.getProduct().getId();
            if(map.keySet().contains(pID)){
                map.put(pID, map.get(pID)+ record.getAmount());
                nrecMap.put(pID, nrecMap.get(pID)+1);
            }else {
                map.put(pID, record.getAmount());
                nrecMap.put(pID,1);
            }
        }

        for(int key : map.keySet()){
            Product product = DataSupport.find(Product.class,key);
            String productInfo = product.getName();//产品信息
            int amount = map.get(key);
            int nrec = nrecMap.get(key);
            dayProductBeans.add(new DayProductBean(productInfo,amount,nrec));
        }
        // 设置布局管理器
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        dayProduct.setLayoutManager(layoutManager);
        // 设置adapter
        dayProduct.setAdapter(new DayProductAdapter(getContext(),dayProductBeans));
    }
}

