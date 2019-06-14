package tong.lan.com.hyperledger.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import tong.lan.com.hyperledger.R;
import tong.lan.com.hyperledger.bean.BatchRecordBean;
import tong.lan.com.hyperledger.bean.DayProductBean;
import tong.lan.com.hyperledger.utils.DateUtil;

import static java.lang.String.*;

public class BatchRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<BatchRecordBean> mRecords;

    public BatchRecordAdapter(Context context, List<BatchRecordBean> records) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mRecords = records;
    }

    private BatchRecordAdapter.OnMyItemClickListener listener;
    public void setOnMyItemClickListener(BatchRecordAdapter.OnMyItemClickListener listener){
        this.listener = listener;

    }
    public interface OnMyItemClickListener{
        void myClick(View v,int pos);
        void mLongClick(View v,int pos);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(mLayoutInflater.inflate(R.layout.item_batch_record, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ((Holder) holder).recs.setText(format("%d", mRecords.get(position).getRecs()));
        ((Holder) holder).date.setText(format("%d", mRecords.get(position).getDate()));
        ((Holder) holder).wage.setText(format("%.1f", mRecords.get(position).getWage()));

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

    public class Holder extends RecyclerView.ViewHolder {
        TextView recs;
        TextView date;
        TextView wage;
        LinearLayout linearLayout;

        Holder(View view) {
            super(view);
            recs = view.findViewById(R.id.item_batch_recs);
            date = view.findViewById(R.id.item_batch_date);
            wage = view.findViewById(R.id.item_batch_wage);
            linearLayout = view.findViewById(R.id.item_batch_layout);
        }
    }
}
