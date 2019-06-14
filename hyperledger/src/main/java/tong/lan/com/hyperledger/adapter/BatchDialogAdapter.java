package tong.lan.com.hyperledger.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import tong.lan.com.hyperledger.R;
import tong.lan.com.hyperledger.bean.BatchDialogBean;

import static java.lang.String.format;

public class BatchDialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<BatchDialogBean> dataList;
    private LayoutInflater mLayoutInflater;

    public BatchDialogAdapter(Context context, List<BatchDialogBean> dataList) {
        this.dataList = dataList;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BatchDialogAdapter.Holder(mLayoutInflater.inflate(R.layout.item_batch_dialog, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((Holder) holder).prod.setText(format(dataList.get(position).getProd()));
        ((Holder) holder).amount.setText(format("%d", dataList.get(position).getAmount()));
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView prod;
        TextView amount;

        Holder(View view) {
            super(view);
            prod = view.findViewById(R.id.item_dialog_prod);
            amount = view.findViewById(R.id.item_dialog_amount);
        }
    }
}
