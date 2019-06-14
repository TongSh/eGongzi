package tong.lan.com.hyperledger.adapter;

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
import tong.lan.com.hyperledger.bean.RecdListBean;

import static java.lang.String.*;

public class RecdListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<RecdListBean> mRecords;

    public RecdListAdapter(Context context, List<RecdListBean> records) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mRecords = records;
    }

    private OnMyItemClickListener listener;
    public void setOnMyItemClickListener(OnMyItemClickListener listener){
        this.listener = listener;
    }
    public interface OnMyItemClickListener{
        void myClick(View v,int pos);
        void mLongClick(View v,int pos);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(mLayoutInflater.inflate(R.layout.item_home_record, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ((Holder) holder).employeeFirstName.setText(mRecords.get(position).getEmployeeName().charAt(0)+"");
        ((Holder) holder).employeeName.setText(mRecords.get(position).getEmployeeName());
        ((Holder) holder).productInfo.setText(mRecords.get(position).getProductInfo());
        ((Holder) holder).recorddWage.setText(format("%.1f", mRecords.get(position).getWage()));
        ((Holder) holder).recordDate.setText(mRecords.get(position).getRecordDate());

        if (listener!=null) {
            ((Holder) holder).linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.myClick(v,position);
                }
            });


            // set LongClick
            ((Holder) holder).linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
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

    public int getRecID(int pos){
        return mRecords.get(pos).getRecID();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView employeeFirstName;
        TextView employeeName;
        TextView productInfo;
        TextView recorddWage;
        TextView recordDate;
        LinearLayout linearLayout;

        Holder(View view) {
            super(view);
            employeeFirstName = view.findViewById(R.id.item_home_record_employee_first_name);
            employeeName = view.findViewById(R.id.item_home_record_employee);
            productInfo = view.findViewById(R.id.item_home_record_product);
            recorddWage = view.findViewById(R.id.item_home_record_wage);
            recordDate = view.findViewById(R.id.item_home_record_date);
            linearLayout = view.findViewById(R.id.recd_layout);

            GradientDrawable gd = (GradientDrawable) employeeFirstName.getBackground();
            gd.setColor(new RandomColor().randomColor());
        }
    }
}
