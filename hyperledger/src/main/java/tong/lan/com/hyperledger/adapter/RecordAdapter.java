package tong.lan.com.hyperledger.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lzyzsd.randomcolor.RandomColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tong.lan.com.hyperledger.R;
import tong.lan.com.hyperledger.bean.ProductBean;
import tong.lan.com.hyperledger.bean.RecordBean;
import tong.lan.com.hyperledger.utils.Utils;

public class RecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<RecordBean> mRecords;

    public RecordAdapter(Context context, List<RecordBean> records) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mRecords = records;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductHolder(mLayoutInflater.inflate(R.layout.item_home_record, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ProductHolder) holder).employeeFirstName.setText(mRecords.get(position).getEmployeeName().charAt(0)+"");
        ((ProductHolder) holder).employeeName.setText(mRecords.get(position).getEmployeeName());
        ((ProductHolder) holder).productInfo.setText(mRecords.get(position).getProductInfo());
        ((ProductHolder) holder).recorddWage.setText((int) mRecords.get(position).getWage()+"");
        ((ProductHolder) holder).recordDate.setText(mRecords.get(position).getRecordDate());
    }

    @Override
    public int getItemCount() {
        return mRecords == null ? 0 : mRecords.size();
    }

    public class ProductHolder extends RecyclerView.ViewHolder {
        TextView employeeFirstName;
        TextView employeeName;
        TextView productInfo;
        TextView recorddWage;
        TextView recordDate;

        ProductHolder(View view) {
            super(view);
            employeeFirstName = view.findViewById(R.id.item_home_record_employee_first_name);
            employeeName = view.findViewById(R.id.item_home_record_employee);
            productInfo = view.findViewById(R.id.item_home_record_product);
            recorddWage = view.findViewById(R.id.item_home_record_wage);
            recordDate = view.findViewById(R.id.item_home_record_date);

            GradientDrawable gd = (GradientDrawable) employeeFirstName.getBackground();
            gd.setColor(new RandomColor().randomColor());
        }
    }
}
