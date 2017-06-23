package com.example.splendor.bustest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Splendor on 2017-03-26.
 */

public class BusActivity extends AppCompatActivity {

    private int flag=0;

    private ListView listview = null ;
    private ListViewAdapter mAdapter = null;
    private DBHelper dbHelper = null;
    private ArrayList<RunningBus> runningBuses = new ArrayList<RunningBus>();
    private ArrayList<Integer> runningArray;
    private String route = null;
    private String routeid = null;
    //private ArrayList<Predict> predicts;
    private ArrayList<Station> stations = new ArrayList<Station>();
    private ArrayList<Station> stationArray;


    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);



        Intent intent = getIntent(); // 보내온 Intent를 얻는다
        route = intent.getStringExtra("route");
        routeid = intent.getStringExtra("routeid");


        //httpwebRunning(route.toLowerCase());

        refreshHandler.sendEmptyMessage(0);
        findViewById(R.id.btn_refreshBus).setOnClickListener(mClickListener);
    }
    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //이곳에 버튼 클릭시 일어날 일을 적습니다.
            refreshHandler.removeMessages(0);
            refreshHandler.sendEmptyMessage(0);
        }
    };

    /*
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Back button pressed.", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }
    */

    private class ViewHolder {
        public TextView sName;
        public TextView sID;
        public TextView sSeat;
        public TextView sSeat2;
        public ImageView iBusTop;
        public ImageView iBusMid;
        public ImageView iBusBot;
        public TextView sSeatTop;
        public TextView sSeatMid;
        public TextView sSeatBot;
    }

    class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        // 원본 데이터
        private ArrayList<BusStop> mStop = new ArrayList<BusStop>();

        public ListViewAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        // Adapter에 사용되는 데이터의 개수를 리턴
        @Override
        public int getCount() {
            return mStop.size();
        }

        @Override
        public Object getItem(int position) {
            return mStop.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //System.out.println("여기가 마지막인데 여기를 못오나?");


            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listview_stop, null);

                holder.sName = (TextView) convertView.findViewById(R.id.mStopName);
                holder.sID = (TextView) convertView.findViewById(R.id.mStopID);
                holder.sSeat = (TextView) convertView.findViewById(R.id.mSeat);
                holder.sSeat2 = (TextView) convertView.findViewById(R.id.mSeat2);
                holder.iBusTop = (ImageView) convertView.findViewById(R.id.bus1);
                holder.iBusMid = (ImageView) convertView.findViewById(R.id.bus2);
                holder.iBusBot = (ImageView) convertView.findViewById(R.id.bus3);
                holder.sSeatTop = (TextView) convertView.findViewById(R.id.seat1);
                holder.sSeatMid = (TextView) convertView.findViewById(R.id.seat2);
                holder.sSeatBot = (TextView) convertView.findViewById(R.id.seat3);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            BusStop mData = mStop.get(position);
            holder.sName.setText(mData.sName);
            if(mData.sPassable.equals("true")) {
                holder.sID.setText("미정차");

                //색깔문제는 나중에 해결하자 20170526 2145
                //holder.sName.setTextColor(holder.sID.getTextColors());

                /*
                convertView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
                */

            }
            else
                holder.sID.setText(mData.sID);

            holder.iBusMid.setVisibility(View.INVISIBLE);
            holder.sSeat.setVisibility(View.INVISIBLE);
            holder.sSeat2.setVisibility(View.INVISIBLE);
            holder.iBusTop.setVisibility(View.INVISIBLE);
            holder.iBusBot.setVisibility(View.INVISIBLE);
            holder.sSeatTop.setVisibility(View.INVISIBLE);
            holder.sSeatMid.setVisibility(View.INVISIBLE);
            holder.sSeatBot.setVisibility(View.INVISIBLE);

            /*
            int idx = runningArray.indexOf(position);
            if(idx!=-1) {
                System.out.println(runningBuses.get(idx).seat);
                String strSeat = Integer.toString(runningBuses.get(idx).seat);
                holder.sSeatMid.setText(strSeat);
                //holder.sSeatMid.setText(runningBuses.get(idx).seat);
            }
            */


            for(int i=0;i<runningBuses.size();i++) {
                RunningBus tempBus = runningBuses.get(i);
                System.out.println(position+" "+mData.getsIndex() + " "+tempBus.sta_order);
                if (position == tempBus.sta_order-1){

                    holder.iBusMid.setVisibility(View.VISIBLE);
                    holder.sSeatMid.setVisibility(View.VISIBLE);

                    String str = tempBus.pno.substring(5) + " " + Integer.toString(tempBus.seat) + "석";
                    int color = Color.BLUE;
                    SpannableStringBuilder builder = new SpannableStringBuilder(str);
                    builder.setSpan(new ForegroundColorSpan(color), 5, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    holder.sSeatMid.setText(builder);

                    holder.sSeat.setVisibility(View.INVISIBLE);
                    holder.sSeat2.setVisibility(View.INVISIBLE);
                    holder.iBusTop.setVisibility(View.INVISIBLE);
                    holder.iBusBot.setVisibility(View.INVISIBLE);
                    holder.sSeatTop.setVisibility(View.INVISIBLE);
                    holder.sSeatBot.setVisibility(View.INVISIBLE);
                }
            }

            for(int i=0;i<stationArray.size();i++){
                if(mData.getsID().equals(stationArray.get(i).getStationid()) && mData.sPredictable.equals("true")){
                    Station tempStation = stationArray.get(i);
                    holder.sSeat.setVisibility(View.VISIBLE);
                    holder.sSeat2.setVisibility(View.VISIBLE);
                    //System.out.println(tempStation.getRoute());
                    //System.out.println(tempStation.getRound_pred1());
                    //System.out.println(tempStation.getStationid());

                    //System.out.println(dbHelper.toStringPredict());
                    //System.out.println(dbHelper.getPredict(tempStation.getRoute(), tempStation.getStationid(), tempStation.getRound_pred1()+"+00").getPred_empty_seat() + "석");
                    //System.out.println(dbHelper.getPredict(tempStation.getRoute(), tempStation.getRound_pred1(), tempStation.getStationid()).getPred_empty_seat() + "석");

                    try{
                        String str = tempStation.getPno1().substring(5) + " " + Integer.toString(dbHelper.getPredict(tempStation.getRoute(),tempStation.getStationid(),tempStation.getRound_pred1()+"+00").getPred_empty_seat()) + "석";
                        int color = Color.GREEN;
                        SpannableStringBuilder builder = new SpannableStringBuilder(str);
                        builder.setSpan(new ForegroundColorSpan(color), 5, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        holder.sSeat.setText(builder);
                    }catch (Exception e){
                        holder.sSeat.setVisibility(View.INVISIBLE);
                        e.printStackTrace();
                    }

                    try{
                        String str = tempStation.getPno2().substring(5) + " " + Integer.toString(dbHelper.getPredict(tempStation.getRoute(),tempStation.getStationid(),tempStation.getRound_pred2()+"+00").getPred_empty_seat()) + "석";
                        int color = Color.GREEN;
                        SpannableStringBuilder builder = new SpannableStringBuilder(str);
                        builder.setSpan(new ForegroundColorSpan(color), 5, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        holder.sSeat2.setText(builder);
                    }catch (Exception e){
                        holder.sSeat2.setVisibility(View.INVISIBLE);
                        e.printStackTrace();
                    }

                    //if(!tempStation.getRound_pred1().startsWith("00:") && !tempStation.getRound_pred1().startsWith("01:"))
                        //holder.sSeat.setText(Integer.toString(dbHelper.getPredict(tempStation.getRoute(),tempStation.getStationid(),tempStation.getRound_pred1()+"+00").getPred_empty_seat())+"석");

                }
            }

            //System.out.println("여기가 마지막인데 여기를 못오나?");
            return convertView;
        }

        public void addItem(String sName, String sID, String sPassable, String sPredictable, int sIndex) {
            BusStop addInfo = null;
            addInfo = new BusStop();
            addInfo.sName = sName;
            addInfo.sID = sID;
            addInfo.sPassable = sPassable;
            addInfo.sPredictable = sPredictable;
            addInfo.sIndex = sIndex;
            mStop.add(addInfo);
        }

        public void remove(int position) {
            mStop.remove(position);
            dataChange();
        }

        public void dataChange() {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void viewStopList(ArrayList<BusStop> stopList){
        try{
            for(int i=0;i<stopList.size();i++){
                BusStop busStop = stopList.get(i);
                System.out.println(busStop.getsName());
                mAdapter.addItem(busStop.getsName(),busStop.getsID(),busStop.getsPassable(),busStop.getsPredictable(),busStop.getsIndex());
                //System.out.println("뷰스탑리스트?");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void parseAry(JSONArray jAry){

        // arraylist 초기화
        if(runningBuses!=null && !runningBuses.isEmpty())
            runningBuses.clear();

        System.out.println("여기는? " + route);

        try{
            for(int i = 0; i < jAry.length(); i++){
                JSONObject obj = jAry.getJSONObject(i);
                RunningBus runningBus = new RunningBus();
                runningBus.routeid = obj.getString("routeid");
                runningBus.stationid = obj.getString("stationid");
                runningBus.pno = obj.getString("pno1");
                runningBus.seat = obj.getInt("seat1");
                runningBus.sta_order = obj.getInt("sta_order");
                runningBus.route = obj.getString("route");
                runningBuses.add(runningBus);
                System.out.println(runningBus.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        httpwebStation(route.toLowerCase());
    }

    public void parseStationAry(JSONArray jAry){
        // arraylist 초기화
        if(stations!=null && !stations.isEmpty())
            stations.clear();

        System.out.println("여기는2? " + route);

        try{
            for(int i = 0; i < jAry.length(); i++){
                JSONObject obj = jAry.getJSONObject(i);
                Station station = new Station();
                station.setRoute(obj.getString("route"));
                station.setRouteid(obj.getString("routeid"));
                station.setStationid(obj.getString("stationid"));
                station.setPno1(obj.getString("pno1"));
                station.setSeat1(obj.getInt("seat1"));
                station.setLocno1(obj.getInt("locno1"));
                station.setPred1(obj.getInt("pred1"));
                station.setRound_pred1(obj.getString("round_pred1"));
                if(!obj.getString("pno2").equals("null")) {
                    station.setPno2(obj.getString("pno2"));
                    station.setSeat2(obj.getInt("seat2"));
                    station.setLocno2(obj.getInt("locno2"));
                    station.setPred2(obj.getInt("pred2"));
                    station.setRound_pred2(obj.getString("round_pred2"));
                }
                stations.add(station);
                System.out.println(station.toString());
            }

            stationArray = new ArrayList<Station>();
            for(int i=0;i<stations.size();i++){
                for(int j=0;j<runningBuses.size();j++){
                    if(stations.get(i).getPno1().equals(runningBuses.get(j).pno)){
                        stationArray.add(stations.get(i));
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }



        if(flag==0) {


            listview = (ListView) findViewById(R.id.listview2);
            mAdapter = new ListViewAdapter(this);
            listview.setAdapter(mAdapter);


            // 디비오픈
            dbHelper = new DBHelper(getApplicationContext(), "Busb.db", null, 1);
            //viewStopList(dbHelper.getStopList(route));
            TextView tvID = (TextView) findViewById(R.id.textView2);
            TextView tvArea = (TextView) findViewById(R.id.textView3);

            tvID.setText(route);
            tvArea.setText(routeid);
            System.out.println("여기는 두번오면 안대 세팅?");
            viewStopList(dbHelper.getStopList(route));
            flag = 1;
        }



        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                BusStop busStop = mAdapter.mStop.get(position);
                // 버스 정류장 방향 전달해주기 위함.
                BusStop busDirection = mAdapter.mStop.get(position+1);

                System.out.println(busStop.getsName());
                System.out.println(busStop.getsID());
                System.out.println(busDirection.getsName());

                // 상세정보 화면으로 이동하기(인텐트 날리기)
                // 1. 다음화면을 만든다
                // 2. AndroidManifest.xml 에 화면을 등록한다
                // 3. Intent 객체를 생성하여 날린다
                Intent intent = new Intent(
                        getApplicationContext(), // 현재화면의 제어권자
                        BusInfoActivity.class); // 다음넘어갈 화면

                // intent 객체에 데이터를 실어서 보내기
                // 리스트뷰 클릭시 인텐트 (Intent) 생성하고 position 값을 이용하여 인텐트로 넘길값들을 넘긴다
                intent.putExtra("Stop_Name", busStop.getsName());
                intent.putExtra("Stop_ID", busStop.getsID());
                intent.putExtra("Bus_Direction", busDirection.getsName());
                intent.putExtra("Bus_ID", route);

                startActivity(intent);
            }
        });



        System.out.println("여기오면 액티비니오니?");
        mAdapter.notifyDataSetChanged();

    }

    public void httpwebRunning(String route){
        HttpUtilRunning http = new HttpUtilRunning(this);
        String url = "http://52.78.231.5:8080/webtest0528/GetRealtimeBusList.do?busRoute="+route;
        http.execute(url);
    }

    public void httpwebStation(String route){
        HttpUtilStation http = new HttpUtilStation(this);
        String url = "http://52.78.231.5:8080/webtest0528/GetRealtimeStation.do?busRoute="+route;
        http.execute(url);
    }

    Handler refreshHandler = new Handler(){
        public void handleMessage(Message msg) {
            httpwebRunning(route.toLowerCase());
            refreshHandler.sendEmptyMessageDelayed(0, 15000);
        }

    };

    @Override
    public void onPause() {
        super.onPause();

        System.out.println("핸들러종료");
        // Remove the activity when its off the screen
        refreshHandler.removeMessages(0);
    }


    @Override
    public void onRestart() {

        // Always call the superclass so it can restore the view hierarchy

        super.onRestart();
        System.out.println("핸들러재시작");
        refreshHandler.sendEmptyMessage(0);

    }

}
