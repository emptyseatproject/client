package com.example.splendor.bustest;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listviewBus = null;
    //private ListView listviewStop = null;
    private ListViewAdapter mAdapter = null;
    private ListViewAdapter sAdapter = null;
    private DBHelper dbHelper = null;

    // ArrayList<Bus> busList = new ArrayList<Bus>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        listviewBus = (ListView) findViewById(R.id.listview1);

        mAdapter = new ListViewAdapter(this);
        sAdapter = new ListViewAdapter(this);
        listviewBus.setAdapter(mAdapter);

        dbHelper = new DBHelper(getApplicationContext(), "Busb.db", null, 1);
        ArrayList<Bus> buses = dbHelper.getBusList();
        viewBusList(buses);

        listviewBus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Bus bus = mAdapter.filteredItemList.get(position);
                Toast.makeText(MainActivity.this, bus.mID, Toast.LENGTH_SHORT).show();

                // 상세정보 화면으로 이동하기(인텐트 날리기)
                // 1. 다음화면을 만든다
                // 2. AndroidManifest.xml 에 화면을 등록한다
                // 3. Intent 객체를 생성하여 날린다
                Intent intent = new Intent(
                        getApplicationContext(), // 현재화면의 제어권자
                        BusActivity.class); // 다음넘어갈 화면

                // intent 객체에 데이터를 실어서 보내기
                // 리스트뷰 클릭시 인텐트 (Intent) 생성하고 position 값을 이용하여 인텐트로 넘길값들을 넘긴다
                intent.putExtra("route", bus.mID);
                intent.putExtra("routeid", bus.mArea);

                startActivity(intent);
            }
        });


        EditText editTextFilter = (EditText) findViewById(R.id.editTextFilter);
        editTextFilter.addTextChangedListener(new TextWatcher() {
              @Override
              public void afterTextChanged(Editable edit) {
                  String filterText = edit.toString();
                  if (filterText.length() > 0) {
                      listviewBus.setFilterText(filterText);
                  } else {
                      listviewBus.clearTextFilter();
                  }
              }

              @Override
              public void beforeTextChanged(CharSequence s, int start, int count, int after) {
              }

              @Override
              public void onTextChanged(CharSequence s, int start, int before, int count) {
              }
          }
        );

    }

    private class ViewHolder {
        public TextView mID;
        public TextView mArea;
    }

    class ListViewAdapter extends BaseAdapter implements Filterable {
        private Context mContext = null;
        // 원본 데이터
        private ArrayList<Bus> mBus = new ArrayList<Bus>();

        // 필터링된 결과 데이터를 저장하기 위한 ArrayList. 최초에는 전체 리스트 보유
        private ArrayList<Bus> filteredItemList = mBus;

        Filter listFilter;

        public ListViewAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        // Adapter에 사용되는 데이터의 개수를 리턴
        @Override
        public int getCount() {
            return filteredItemList.size();
        }

        @Override
        public Object getItem(int position) {
            return filteredItemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            System.out.println("버스 목록 표시");

            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listview_item, null);

                holder.mID = (TextView) convertView.findViewById(R.id.mID);
                holder.mArea = (TextView) convertView.findViewById(R.id.mArea);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Bus mData = filteredItemList.get(position);

            holder.mID.setText(mData.mID);
            holder.mArea.setText(mData.mArea);

            return convertView;
        }

        public void addItem(String mID, String mArea) {
            Bus addInfo = null;
            addInfo = new Bus();
            addInfo.mID = mID;
            addInfo.mArea = mArea;
            mBus.add(addInfo);
        }

        public void remove(int position) {
            mBus.remove(position);
            dataChange();
        }

        public void dataChange() {
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public Filter getFilter() {
            if (listFilter == null) {
                listFilter = new ListFilter();
            }
            return listFilter;
        }

        private class ListFilter extends Filter {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    results.values = mBus;
                    results.count = mBus.size();
                } else {
                    ArrayList<Bus> itemList = new ArrayList<Bus>();
                    for (Bus item : mBus) {
                        if (item.getID().toUpperCase().contains(constraint.toString().toUpperCase()) ||
                                item.getmArea().toUpperCase().contains(constraint.toString().toUpperCase())) {
                            itemList.add(item);
                        }
                    }
                    results.values = itemList;
                    results.count = itemList.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                // update listview by filtered data list.
                filteredItemList = (ArrayList<Bus>) results.values;

                // notify
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        }
    }

    /**
     * 데이터베이스에 있는 버스 리스틀 mAdapter에 넣는 메소드이다.
     * @param busList
     */
    public void viewBusList(ArrayList<Bus> busList){
        try{
            for(int i=0;i<busList.size();i++){
                Bus bus = busList.get(i);
                mAdapter.addItem(bus.getID(), bus.getmArea());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}