package tong.lan.com.hyperledger.activity;

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
import tong.lan.com.hyperledger.adapter.EmployeeAdapter;
import tong.lan.com.hyperledger.adapter.WageAdapter;
import tong.lan.com.hyperledger.bean.EmployeeBean;
import tong.lan.com.hyperledger.domain.Employee;
import tong.lan.com.hyperledger.utils.LetterView;

public class TabTwoFragment extends Fragment{

    private RecyclerView mRecyclerView;
    private LetterView letterView;
    private List<EmployeeBean> mDatas;
    private EmployeeAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,  Bundle savedInstanceState) {
        // 通过View方法把布局加载到fragmnet中
        View view = inflater.inflate(R.layout.tab_02,null);
        initData();
        // 使用加载的布局中的findViewById的方法找到控件
        mRecyclerView = view.findViewById(R.id.employee_recycler);
        letterView = view.findViewById(R.id.letter_view);
        // 设置布局管理器
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        // 设置adapter
        mAdapter = new EmployeeAdapter(getContext(),mDatas);
        // 匿名内部类，实现适配器里面定义的接口
        mAdapter.setOnMyItemClickListener(new WageAdapter.OnMyItemClickListener(){
            @Override
            public void myClick(View v, int pos) {
                Intent intent = new Intent(getActivity(), UpdateEmployeeActivity.class);
                int eID = mAdapter.getEmployee(pos);
                Employee employee = DataSupport.find(Employee.class,eID);
                intent.putExtra("eID", eID);
                intent.putExtra("eName",employee.getEmployeeName());
                intent.putExtra("ePhone",employee.getEmployeePhone());
                startActivity(intent);
            }

            @Override
            public void mLongClick(View v, int pos) {
                Toast.makeText(getActivity(),"onLongClick---"+pos,Toast.LENGTH_LONG).show();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        // 为右边的字母导航设置监听
        letterView.setCharacterListener(new LetterView.CharacterClickListener() {
            @Override
            public void clickCharacter(String character) {
                layoutManager.scrollToPositionWithOffset(mAdapter.getScrollPosition(character), 0);
            }
        });
        return view;
    }

    protected void initData()
    {
        mDatas = new ArrayList<EmployeeBean>();
        List<Employee> employees = DataSupport.findAll(Employee.class);
        for (Employee employee : employees){
            mDatas.add(new EmployeeBean(employee.getId(),employee.getEmployeeName(),employee.getEmployeePhone(),0));
        }
    }
}
