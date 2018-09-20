package tong.lan.com.hyperledger.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import tong.lan.com.hyperledger.R;
import tong.lan.com.hyperledger.utils.DbBackups;

public class TabFourFragment extends Fragment{

    private LinearLayout mProductType;
    private LinearLayout mDbBackup;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_04,null);
        initUnit(view);
        initListener();
        return view;
    }

    private void initUnit(View v){
        mProductType = v.findViewById(R.id.add_product_type);
        mDbBackup = v.findViewById(R.id.db_back_up);
    }

    private void initListener(){
        mProductType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),AddProductTypeActivity.class);
                startActivity(intent);
            }
        });

        mDbBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThreadDemo threadDemo = new ThreadDemo();
                threadDemo.run();
            }
        });
    }

    private class ThreadDemo extends Thread{
        public void run(){
            //编写自己的线程代码
            DbBackups backups = new DbBackups(getActivity());
            backups.doInBackground(DbBackups.COMMAND_BACKUP);
        }
    }
}

