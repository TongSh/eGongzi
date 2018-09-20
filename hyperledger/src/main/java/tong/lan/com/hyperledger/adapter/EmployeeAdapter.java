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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.lzyzsd.randomcolor.RandomColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import tong.lan.com.hyperledger.R;
import tong.lan.com.hyperledger.bean.EmployeeBean;
import tong.lan.com.hyperledger.utils.Utils;

public class EmployeeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<EmployeeBean> mEmployees; // 联系人名称字符串数组
    private List<String> mEmployeeList; // 联系人名称List（转换成拼音）
    private List<EmployeeBean> resultList; // 最终结果（包含分组的字母）
    private List<String> characterList; // 字母List

    private String[] mColor;


    public enum ITEM_TYPE {
        ITEM_TYPE_CHARACTER,
        ITEM_TYPE_CONTACT
    }

    public EmployeeAdapter(Context context, List<EmployeeBean> employeeNames) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mEmployees = employeeNames;
        mColor = new String[]{"#4169E1",
                "#EE3B3B",
                "#C71585",
                "#B23AEE",
                "#EE6AA7",
                "#EE4000",
                "#512da8",
                "#cd3e3a",
                "#FF4081",
                "#43d28d",
                "red",
                "blue",
                "green"
        };
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

    public int getEmployee(int pos){
        return resultList.get(pos).getId();
    }

    private void handleData() {
        mEmployeeList = new ArrayList<>();
        Map<String, EmployeeBean> map = new HashMap<>();

        for (EmployeeBean mEmployee : mEmployees) {
            String pinyin = Utils.getPingYin(mEmployee.getEmployeeName());
            map.put(pinyin, mEmployee);
            mEmployeeList.add(pinyin);
        }
        Collections.sort(mEmployeeList, new ContactComparator());

        resultList = new ArrayList<>();
        characterList = new ArrayList<>();

        for (int i = 0; i < mEmployeeList.size(); i++) {
            String name = mEmployeeList.get(i);
            String character = (name.charAt(0) + "").toUpperCase(Locale.ENGLISH);
            if (!characterList.contains(character)) {
                if (character.hashCode() >= "A".hashCode() && character.hashCode() <= "Z".hashCode()) { // 是字母
                    characterList.add(character);
                    resultList.add(new EmployeeBean(character, ITEM_TYPE.ITEM_TYPE_CHARACTER.ordinal()));
                } else {
                    if (!characterList.contains("#")) {
                        characterList.add("#");
                        resultList.add(new EmployeeBean("#", ITEM_TYPE.ITEM_TYPE_CHARACTER.ordinal()));
                    }
                }
            }

            resultList.add(new EmployeeBean(map.get(name).getId(),
                    map.get(name).getEmployeeName(),
                    map.get(name).getEmployeePhone(),
                    ITEM_TYPE.ITEM_TYPE_CONTACT.ordinal()));
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM_TYPE_CHARACTER.ordinal()) {
            return new CharacterHolder(mLayoutInflater.inflate(R.layout.item_character, parent, false));
        } else {
            return new EmployeeHolder(mLayoutInflater.inflate(R.layout.item_employee, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CharacterHolder) {
            ((CharacterHolder) holder).mTextView.setText(resultList.get(position).getEmployeeName());
        } else if (holder instanceof EmployeeHolder) {
            ((EmployeeHolder) holder).mTextViewFirst.setText(String.format("%s", resultList.get(position).getEmployeeName().charAt(0)));
            ((EmployeeHolder) holder).mTextViewName.setText(resultList.get(position).getEmployeeName());
        }

        if (holder instanceof EmployeeHolder && listener!=null) {
            ((EmployeeAdapter.EmployeeHolder) holder).linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.myClick(v,position);
                }
            });


            // set LongClick
            ((EmployeeAdapter.EmployeeHolder) holder).linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.mLongClick(v,position);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return resultList.get(position).getmType();
    }

    @Override
    public int getItemCount() {
        return resultList == null ? 0 : resultList.size();
    }

    public class CharacterHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        @SuppressLint("ResourceAsColor")
        CharacterHolder(View view) {
            super(view);
            mTextView = view.findViewById(R.id.item_character);
            GradientDrawable gd = (GradientDrawable) mTextView.getBackground();
            int index = (int) (Math.random()*mColor.length);
            gd.setColor(Color.parseColor(mColor[index]));
        }
    }

    public class EmployeeHolder extends RecyclerView.ViewHolder {
        TextView mTextViewFirst;
        TextView mTextViewName;
        LinearLayout linearLayout;

        EmployeeHolder(View view) {
            super(view);
            mTextViewFirst = view.findViewById(R.id.item_employee_first_name);
            mTextViewName = view.findViewById(R.id.item_employee_name);
            linearLayout = view.findViewById(R.id.item_employee);
            GradientDrawable gd = (GradientDrawable) mTextViewFirst.getBackground();
            int index = (int) (Math.random()*mColor.length);
            gd.setColor(Color.parseColor(mColor[index]));
        }
    }

    public int getScrollPosition(String character) {
        // 找离点击的字母最近的一个
        for(char i = character.charAt(0); i <= 'Z'; ++i)
            if(characterList.contains(i+"")){
                character = i+"";
                break;
            }

        if (characterList.contains(character)) {
            for (int i = 0; i < resultList.size(); i++) {
                if (resultList.get(i).getEmployeeName().equals(character)) {
                    return i;
                }
            }
        }

        return resultList.size()-1; // -1不会滑动
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
