package tong.lan.com.hyperledger.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import tong.lan.com.hyperledger.adapter.ProdListAdapter;
import tong.lan.com.hyperledger.adapter.WageMonAdapter;
import tong.lan.com.hyperledger.bean.ProdListBean;
import tong.lan.com.hyperledger.domain.Employee;
import tong.lan.com.hyperledger.domain.Product;

public class TabProdFragment extends Fragment{

    private RecyclerView mRecyclerView;
    private List<ProdListBean> mDatas;

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
            mDatas.add(new ProdListBean(product.getId(),
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
        final ProdListAdapter mAdapter = new ProdListAdapter(getContext(),mDatas);
        // 匿名内部类，实现适配器里面定义的接口
        mAdapter.setOnMyItemClickListener(new WageMonAdapter.OnMyItemClickListener(){
            @Override
            public void myClick(View v, int pos) {
                Intent intent = new Intent(getActivity(), UpdateProductActivity.class);
                int pID = mAdapter.getProduct(pos);
                intent.putExtra("pID", pID);
                startActivity(intent);
            }

            @Override
            public void mLongClick(View v, int pos) {
                final Product product = DataSupport.find(Product.class,mAdapter.getProduct(pos));
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setIcon(R.mipmap.delete);
                builder.setTitle("删除确认");
                builder.setMessage("删除"+product.getName()+"的全部信息？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        DataSupport.delete(Product.class,product.getId());
                        onResume();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
//                        Toast.makeText(BatchDialogActivity.this, "negative: " + which, Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

}

