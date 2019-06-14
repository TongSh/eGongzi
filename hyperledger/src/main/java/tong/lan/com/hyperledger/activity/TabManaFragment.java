package tong.lan.com.hyperledger.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.Calendar;

import tong.lan.com.hyperledger.R;
import tong.lan.com.hyperledger.domain.Employee;
import tong.lan.com.hyperledger.utils.DbBackups;

public class TabManaFragment extends Fragment{

    private RelativeLayout addRecord;
    private RelativeLayout addEmpl;
    private RelativeLayout addProd;

    private LinearLayout opAllRecs;
    private LinearLayout opWageTable;
    private LinearLayout opBackup;
    private LinearLayout opRecover;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_mana,null);
        initUnit(view);
        initListener();
        return view;
    }

    private void initUnit(View v){
        addRecord = v.findViewById(R.id.mana_1);
        addEmpl = v.findViewById(R.id.mana_2);
        addProd = v.findViewById(R.id.mana_3);

        opAllRecs = v.findViewById(R.id.mana_all_recs);
        opWageTable = v.findViewById(R.id.mana_wage_table);
        opBackup = v.findViewById(R.id.mana_data_backup);
        opRecover = v.findViewById(R.id.mana_data_recover);
    }

    private void initListener(){
        addRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(getActivity(), AddRecordActivity.class);
            startActivity(intent);
            }
        });
        addEmpl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(getActivity(), AddEmployeeActivity.class);
            startActivity(intent);
            }
        });

        addProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(getActivity(), AddProductActivity.class);
            startActivity(intent);
            }
        });

        opAllRecs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(),"Type",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(),RecordListActivity.class);
                startActivity(intent);
            }
        });

        opWageTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"敬请期待",Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(getActivity(),WageRankActivity.class);
//                startActivity(intent);
            }
        });

        opBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThreadDemo threadDemo = new ThreadDemo();
                threadDemo.run();
            }
        });

        opRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThreadRecover threadRecover = new ThreadRecover();
                threadRecover.run();
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

    private class ThreadRecover extends Thread{
        public void run(){
            //编写自己的线程代码
            DbBackups backups = new DbBackups(getActivity());
            backups.doInBackground(DbBackups.COMMAND_RESTORE);
        }
    }
}

