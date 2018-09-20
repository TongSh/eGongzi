package tong.lan.com.hyperledger.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import tong.lan.com.hyperledger.R;
import tong.lan.com.hyperledger.bean.EmployeeBean;
import tong.lan.com.hyperledger.bean.ProductBean;
import tong.lan.com.hyperledger.domain.Product;
import tong.lan.com.hyperledger.utils.Utils;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<ProductBean> mProducts;
    private List<String> mProductList; // 联系人名称List（转换成拼音）
    private Map<String, ProductBean> map = new HashMap<>();

    public ProductAdapter(Context context, List<ProductBean> products) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mProducts = products;
        handleData();
    }

    private WageAdapter.OnMyItemClickListener listener;
    public void setOnMyItemClickListener(WageAdapter.OnMyItemClickListener listener){
        this.listener = listener;

    }

    public interface OnMyItemClickListener{
        void myClick(View v,int pos);
        void mLongClick(View v,int pos);
    }

    public int getProduct(int pos){
        return map.get(mProductList.get(pos)).getId();
    }

    private void handleData() {
        mProductList = new ArrayList<>();


        for (ProductBean mProduct : mProducts) {
            String pinyin = Utils.getPingYin(mProduct.getProductName());
            map.put(pinyin, mProduct);
            mProductList.add(pinyin);
        }
        Collections.sort(mProductList, new ContactComparator());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductHolder(mLayoutInflater.inflate(R.layout.item_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ((ProductHolder) holder).productImg.setImageResource(map.get(mProductList.get(position)).getProductImg());
        ((ProductHolder) holder).productName.setText(map.get(mProductList.get(position)).getProductName());
        ((ProductHolder) holder).productWage.setText(map.get(mProductList.get(position)).getProductWage()+"");
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
        return mProductList == null ? 0 : mProductList.size();
    }

    public class ProductHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView productWage;
        ImageView productImg;
        LinearLayout linearLayout;

        ProductHolder(View view) {
            super(view);
            productName = view.findViewById(R.id.item_product_name);
            productWage = view.findViewById(R.id.item_product_wage);
            productImg = view.findViewById(R.id.item_product_img);
            linearLayout = view.findViewById(R.id.item_product);
        }
    }

    private class ContactComparator  implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            int c1 = (o1.charAt(0) + "").toUpperCase().hashCode();
            int c2 = (o2.charAt(0) + "").toUpperCase().hashCode();

            boolean c1Flag = (c1 < "A".hashCode() || c1 > "Z".hashCode()); // 不是字母
            boolean c2Flag = (c2 < "A".hashCode() || c2 > "Z".hashCode()); // 不是字母
            if (c1Flag && !c2Flag) {
                return 1;
            } else if (!c1Flag && c2Flag) {
                return -1;
            }

            return c1 - c2;
        }

    }
}
