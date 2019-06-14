package tong.lan.com.hyperledger.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tong.lan.com.hyperledger.R;
import tong.lan.com.hyperledger.bean.WageEmplListBean;

import static java.lang.String.*;

public class WageEmplAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<WageEmplListBean> mRecords;

    public WageEmplAdapter(Context context, List<WageEmplListBean> records) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mRecords = records;
    }

    public WageEmplAdapter(){}



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductHolder(mLayoutInflater.inflate(R.layout.item_wage_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        ((ProductHolder) holder).productName.setText(mRecords.get(position).getProduct());
        ((ProductHolder) holder).recordAmount.setText("数量 "+ mRecords.get(position).getAmount());
        ((ProductHolder) holder).recordDate.setText(mRecords.get(position).getDate());
        ((ProductHolder) holder).recordWage.setText(format("%.1f", mRecords.get(position).getWage()));
    }

    @Override
    public int getItemCount() {
        return mRecords == null ? 0 : mRecords.size();
    }

    public class ProductHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView recordAmount;
        TextView recordWage;
        TextView recordDate;

        ProductHolder(View view) {
            super(view);
            productName = view.findViewById(R.id.item_wage_detail_prodct);
            recordAmount = view.findViewById(R.id.item_wage_detail_amount);
            recordWage = view.findViewById(R.id.item_wage_detail_wage);
            recordDate = view.findViewById(R.id.item_wage_detail_date);
        }
    }
}
