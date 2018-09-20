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
import java.util.Arrays;
import java.util.List;

import tong.lan.com.hyperledger.R;
import tong.lan.com.hyperledger.adapter.EmployeeAdapter;
import tong.lan.com.hyperledger.adapter.ProductAdapter;
import tong.lan.com.hyperledger.adapter.WageAdapter;
import tong.lan.com.hyperledger.bean.EmployeeBean;
import tong.lan.com.hyperledger.bean.ProductBean;
import tong.lan.com.hyperledger.domain.Employee;
import tong.lan.com.hyperledger.domain.Product;
import tong.lan.com.hyperledger.utils.LetterView;

public class TabThreeFragment extends Fragment{

    private RecyclerView mRecyclerView;
    private List<ProductBean> mDatas;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,  Bundle savedInstanceState) {
        // 通过View方法把布局加载到fragmnet中
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.tab_03,null);
        initData();
        // 使用加载的布局中的findViewById的方法找到控件
        mRecyclerView = view.findViewById(R.id.product_recycler);
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
                Toast.makeText(getActivity(),"onLongClick---"+pos,Toast.LENGTH_LONG).show();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    protected void initData()
    {
        mDatas = new ArrayList<>();
        List<Product> products = DataSupport.findAll(Product.class);
        int[] imgs = new int[] {R.mipmap.icon_1,
                R.mipmap.icon_2,
                R.mipmap.icon_3,
                R.mipmap.icon_4,
                R.mipmap.icon_5,
                R.mipmap.icon_6,
                R.mipmap.icon_7,
                R.mipmap.icon_8,
                R.mipmap.icon_9,
                R.mipmap.icon_10,
                R.mipmap.icon_11,
                R.mipmap.icon_12,
                R.mipmap.icon_13,
                R.mipmap.icon_14,
                R.mipmap.icon_15,
                R.mipmap.icon_16,
                R.mipmap.icon_17,
                R.mipmap.icon_18,
                R.mipmap.icon_19};

        for (Product product : products){
            ArrayList<String> spinnerData = new ArrayList<>(Arrays.asList("内衬", "护托", "耳朵", "商标",
                    "拖把", "其他"));
            int index = spinnerData.indexOf(product.getProductType());
            mDatas.add(new ProductBean(product.getId(),
                    product.getProductType()+product.getProductName(),
                    product.getProductWage(),
                    imgs[index]));
        }
    }

}

