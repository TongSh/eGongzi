package tong.lan.com.hyperledger.activity;

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
import tong.lan.com.hyperledger.adapter.EmplListAdapter;
import tong.lan.com.hyperledger.adapter.WageMonAdapter;
import tong.lan.com.hyperledger.bean.EmplListBean;
import tong.lan.com.hyperledger.domain.Employee;
import tong.lan.com.hyperledger.domain.Record;
import tong.lan.com.hyperledger.utils.DateUtil;
import tong.lan.com.hyperledger.utils.LetterView;

public class TabEmplFragment extends Fragment{

    private RecyclerView mRecyclerView;
    private LetterView letterView;
    private List<EmplListBean> mDatas;
    private EmplListAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,  Bundle savedInstanceState) {
        // 通过View方法把布局加载到fragmnet中
        View view = inflater.inflate(R.layout.tab_empl,null);
        // 使用加载的布局中的findViewById的方法找到控件
        mRecyclerView = view.findViewById(R.id.employee_recycler);
        letterView = view.findViewById(R.id.letter_view);
        // 设置内容
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

    /**
     * 从数据库读员工信息
     */
    protected void readDb()
    {
        mDatas = new ArrayList<EmplListBean>();
        List<Employee> employees = DataSupport.findAll(Employee.class);
        for (Employee employee : employees){
            mDatas.add(new EmplListBean(employee.getId(),employee.getEmployeeName(),0));
        }
    }

    /**
     * 设置员工列表监听、右侧字母导航栏监听
     */
    private void setAdapter()
    {

        // 设置布局管理器
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        // 设置adapter
        mAdapter = new EmplListAdapter(getContext(),mDatas);
        // 匿名内部类，实现适配器里面定义的接口
        mAdapter.setOnMyItemClickListener(new WageMonAdapter.OnMyItemClickListener(){
            @Override
            public void myClick(View v, int pos) {
                Intent intent = new Intent(getActivity(), UpdateEmployeeActivity.class);
                int eID = mAdapter.getEmployee(pos);
                Employee employee = DataSupport.find(Employee.class,eID);
                intent.putExtra("eID", eID);
                intent.putExtra("eName",employee.getEmployeeName());
                startActivity(intent);
            }

            @Override
            public void mLongClick(View v, int pos) {
                final Employee employee = DataSupport.find(Employee.class,mAdapter.getEmployee(pos));
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setIcon(R.mipmap.delete);
                builder.setTitle("删除确认");
                builder.setMessage("删除"+employee.getEmployeeName()+"的全部信息？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        DataSupport.delete(Employee.class,employee.getId());
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
        // 为右边的字母导航设置监听
        letterView.setCharacterListener(new LetterView.CharacterClickListener() {
            @Override
            public void clickCharacter(String character) {
                layoutManager.scrollToPositionWithOffset(mAdapter.getScrollPosition(character), 0);
            }
        });
    }
}

