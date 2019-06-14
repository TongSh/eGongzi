package tong.lan.com.hyperledger.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.lzyzsd.randomcolor.RandomColor;

import java.util.List;

import tong.lan.com.hyperledger.R;
import tong.lan.com.hyperledger.bean.WageMonListBean;

import static java.lang.String.*;

public class WageMonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<WageMonListBean> mRecords;

    private OnMyItemClickListener listener;
    public void setOnMyItemClickListener(OnMyItemClickListener listener){
        this.listener = listener;

    }

    public interface OnMyItemClickListener{
        void myClick(View v,int pos);
        void mLongClick(View v,int pos);
    }

    public int getEmployee(int pos){
        return mRecords.get(pos).getEmployeeID();
    }

    public WageMonAdapter(Context context, List<WageMonListBean> records) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mRecords = records;
    }

    public WageMonAdapter(){}



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductHolder(mLayoutInflater.inflate(R.layout.item_wage, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        ((ProductHolder) holder).employeeFirstName.setText(mRecords.get(position).getEmployeeName().charAt(0)+"");
        ((ProductHolder) holder).employeeName.setText(mRecords.get(position).getEmployeeName());
        ((ProductHolder) holder).recorddWage.setText(format("%.1f", mRecords.get(position).getWage()));
        ((ProductHolder) holder).recordWorkDay.setText("出勤"+mRecords.get(position).getWorkDay()+"天");

        if (listener!=null) {
            ((ProductHolder) holder).linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.myClick(v,position);
                }
            });


            // set LongClick
            ((ProductHolder) holder).linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.mLongClick(v,position);
                    return true;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mRecords == null ? 0 : mRecords.size();
    }

    public class ProductHolder extends RecyclerView.ViewHolder {
        TextView employeeFirstName;
        TextView employeeName;
        TextView recorddWage;
        TextView recordWorkDay;
        LinearLayout linearLayout;

        ProductHolder(View view) {
            super(view);
            employeeFirstName = view.findViewById(R.id.item_wage_first_name);
            employeeName = view.findViewById(R.id.item_wage_name);
            recorddWage = view.findViewById(R.id.item_wage_wage);
            recordWorkDay = view.findViewById(R.id.item_wage_work_day);
            linearLayout = view.findViewById(R.id.item_wage);
            GradientDrawable gd = (GradientDrawable) employeeFirstName.getBackground();
            gd.setColor(new RandomColor().randomColor());
        }
    }
}
