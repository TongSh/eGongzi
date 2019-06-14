package tong.lan.com.hyperledger.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import tong.lan.com.hyperledger.R;
import tong.lan.com.hyperledger.bean.DayProductBean;
import tong.lan.com.hyperledger.utils.DateUtil;

public class DayProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<DayProductBean> mRecords;

    public DayProductAdapter(Context context, List<DayProductBean> records) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mRecords = records;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(mLayoutInflater.inflate(R.layout.item_home_day_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((Holder) holder).productInfo.setText(mRecords.get(position).getProductInfo());
        ((Holder) holder).margin.setText("记录数 "+ mRecords.get(position).getNrec());
        ((Holder) holder).amount.setText(mRecords.get(position).getAmount()+"");
        Calendar now = Calendar.getInstance();
        String d = DateUtil.getString(now.get(Calendar.YEAR),now.get(Calendar.MONTH)+1,now.get(Calendar.DAY_OF_MONTH));
        ((Holder) holder).date.setText(d.split("-")[1]+"-"+d.split("-")[2]);
    }

    @Override
    public int getItemCount() {
        return mRecords == null ? 0 : mRecords.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView productInfo;
        TextView margin;
        TextView amount;
        TextView date;

        Holder(View view) {
            super(view);
            productInfo = view.findViewById(R.id.item_day_prodct_name);
            margin = view.findViewById(R.id.item_day_prodct_margin);
            amount = view.findViewById(R.id.item_day_prduct_amount);
            date = view.findViewById(R.id.item_day_prduct_date);
        }
    }
}
