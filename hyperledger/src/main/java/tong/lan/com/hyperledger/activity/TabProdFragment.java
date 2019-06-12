package tong.lan.com.hyperledger.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import tong.lan.com.hyperledger.R;
import tong.lan.com.hyperledger.adapter.ProductAdapter;
import tong.lan.com.hyperledger.adapter.WageAdapter;
import tong.lan.com.hyperledger.bean.ProductBean;
import tong.lan.com.hyperledger.domain.Product;

public class TabProdFragment extends Fragment{

    private RecyclerView mRecyclerView;
    private List<ProductBean> mDatas;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,  Bundle savedInstanceState) {
        // 通过View方法把布局加载到fragmnet中
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.tab_prod,null);
        // 使用加载的布局中的findViewById的方法找到控件
        mRecyclerView = view.findViewById(R.id.product_recycler);
        // 更新内容
        readDb();
        setAdapter();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 更新内容
        readDb();
        setAdapter();
    }

    protected void readDb()
    {
        mDatas = new ArrayList<>();
        List<Product> products = DataSupport.findAll(Product.class);
        int prodIcon = R.mipmap.helmet;

        for (Product product : products){
            mDatas.add(new ProductBean(product.getId(),
                    product.getName(),
                    product.getWage(),
                    prodIcon));
        }
    }

    private void setAdapter()
    {
        // 设置布局管理器
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        // 设置adapter
        final ProductAdapter mAdapter = new ProductAdapter(getContext(),mDatas);
        // 匿名内部类，实现适配器里面定义的接口
        mAdapter.setOnMyItemClickListener(new WageAdapter.OnMyItemClickListener(){
            @Override
            public void myClick(View v, int pos) {
                Intent intent = new Intent(getActivity(), UpdateProductActivity.class);
                int pID = mAdapter.getProduct(pos);
                intent.putExtra("pID", pID);
                startActivity(intent);
            }

            @Override
            public void mLongClick(View v, int pos) {
                Toast.makeText(getActivity(),"长按---"+pos,Toast.LENGTH_LONG).show();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

}

