package com.example.splendor.bustest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Splendor on 2017-05-29.
 */

public class BusInfoActivity extends AppCompatActivity {

    // 타이머도 쓰기 위해 전역변수로 설정
    TextView tvStationId;
    TextView tvBusStopName;
    TextView tvDirection;
    TextView tvRoute;
    TextView tvNextStop;
    TextView tvPred1;
    TextView tvLocNo1;
    TextView tvSeat1;
    TextView tvPredSeat1;
    TextView tvPred2;
    TextView tvLocNo2;
    TextView tvSeat2;
    TextView tvPredSeat2;
    TextView tvNonInfo;

    String route;
    String stationid;

    int temp_busArrivalTime_1;
    int temp_busPrev_1;
    int temp_busEmptySeat_1;
    int temp_busExpectedEmptySeat_1;
    int temp_busArrivalTime_2;
    int temp_busPrev_2;
    int temp_busEmptySeat_2;
    int temp_busExpectedEmptySeat_2;


    private DBHelper dbHelper = null;
    private Station station;
    private ArrayList<Station> stations = new ArrayList<Station>();
    private ArrayList<Station> stationArray;


    BusInfo[] busInfo = new BusInfo[2];
    ArrayList<BusInfo> busInfos = new ArrayList<BusInfo>();

    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);


        dbHelper = new DBHelper(getApplicationContext(), "Busb.db", null, 1);

        tvStationId = (TextView)findViewById(R.id.tvStationId);
        tvBusStopName = (TextView)findViewById(R.id.tvBusStopName);
        tvDirection = (TextView)findViewById(R.id.tvDirection);
        tvRoute= (TextView)findViewById(R.id.tvRoute);
        tvNextStop = (TextView)findViewById(R.id.tvNextStop);
        tvPred1 = (TextView)findViewById(R.id.tvPred1);
        tvLocNo1 = (TextView)findViewById(R.id.tvLocNo1);
        tvSeat1 = (TextView)findViewById(R.id.tvSeat1);
        tvPredSeat1 = (TextView)findViewById(R.id.tvPredSeat1);
        tvPred2 = (TextView)findViewById(R.id.tvPred2);
        tvLocNo2 = (TextView)findViewById(R.id.tvLocNo2);
        tvSeat2 = (TextView)findViewById(R.id.tvSeat2);
        tvPredSeat2 = (TextView)findViewById(R.id.tvPredSeat2);
        tvNonInfo = (TextView)findViewById(R.id.tvNonInfo);
        Intent intent = getIntent(); // 보내온 Intent를 얻는다

        tvBusStopName.setText(intent.getStringExtra("Stop_Name"));
        tvStationId.setText(intent.getStringExtra("Stop_ID"));
        tvRoute.setText(intent.getStringExtra("Bus_ID"));
        tvDirection.setText(intent.getStringExtra("Bus_Direction")+" 방향");
        tvNextStop.setText(intent.getStringExtra("Bus_Direction")+" 방향");

        route = intent.getStringExtra("Bus_ID").toString();
        stationid = intent.getStringExtra("Stop_ID");

        setEmptyInfo();

        //httpwebStop(route.toLowerCase());
        refreshHandler.sendEmptyMessage(0);
        findViewById(R.id.btn_refreshStop).setOnClickListener(mClickListener);

        /*
        tvFirstBusArrivalTime.setText(busInfo[0].getBusArrivalTime());

        tvFirstBusPrev.setText(busInfo[0].getBusPrev());
        tvFirstEmptySeat.setText(busInfo[0].getBusEmptySeat());
        tvFirstExpectedEmptySeat.setText(busInfo[0].getBusExpectedEmptySeat());
        tvSecondBusArrivalTime.setText(busInfo[1].getBusArrivalTime());
        tvSecondBusPrev.setText(busInfo[1].getBusPrev());
        tvSecondEmptySeat.setText(busInfo[1].getBusEmptySeat());
        tvSecondExpectedEmptySeat.setText(busInfo[1].getBusExpectedEmptySeat());
        */




        /*
        mHandler.sendEmptyMessage(0);
        BackThread thread = new BackThread();
        thread.setDaemon(true);
        thread.start();
        */
    }
    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //이곳에 버튼 클릭시 일어날 일을 적습니다.
            refreshHandler.removeMessages(0);
            refreshHandler.sendEmptyMessage(0);
        }
    };

    public void parseAry(JSONArray jAry){

        try{
            for(int i = 0; i < jAry.length(); i++){
                JSONObject obj = jAry.getJSONObject(i);
                if(obj.getString("stationid").equals(stationid)) {

                    station = new Station();
                    station.setRoute(obj.getString("route"));
                    station.setRouteid(obj.getString("routeid"));
                    station.setStationid(obj.getString("stationid"));
                    station.setPno1(obj.getString("pno1"));
                    station.setSeat1(obj.getInt("seat1"));
                    station.setLocno1(obj.getInt("locno1"));
                    station.setPred1(obj.getInt("pred1"));
                    station.setRound_pred1(obj.getString("round_pred1"));
                    if (!obj.getString("pno2").equals("null")) {
                        station.setPno2(obj.getString("pno2"));
                        station.setSeat2(obj.getInt("seat2"));
                        station.setLocno2(obj.getInt("locno2"));
                        station.setPred2(obj.getInt("pred2"));
                        station.setRound_pred2(obj.getString("round_pred2"));
                    }
                    System.out.println(station.toString());
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        if(station!=null){
            try {
                tvPred1.setVisibility(View.VISIBLE);
                tvLocNo1.setVisibility(View.VISIBLE);
                tvSeat1.setVisibility(View.VISIBLE);
                tvPredSeat1.setVisibility(View.VISIBLE);

                String str = Integer.toString(station.getPred1());
                if(str.equals("1"))
                    tvPred1.setText(" 곧 도착 ");
                else
                    tvPred1.setText(" "+str+"분후 ");
                tvLocNo1.setText(" "+Integer.toString(station.getLocno1()) + "번째전 ");

                str = " 현재 " + Integer.toString(station.getSeat1()) + "석 ";
                int color = Color.BLUE;
                SpannableStringBuilder builder = new SpannableStringBuilder(str);
                builder.setSpan(new ForegroundColorSpan(color), 4, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvSeat1.setText(builder);

                str = " 예상 " + Integer.toString(dbHelper.getPredict(route.toLowerCase(), stationid, station.getRound_pred1() + "+00").getPred_empty_seat()) + "석 ";
                color = Color.GREEN;
                builder = new SpannableStringBuilder(str);
                builder.setSpan(new ForegroundColorSpan(color), 4, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvPredSeat1.setText(builder);

            }catch(Exception e){
                setEmptyInfo();
                e.printStackTrace();
            }

            tvPred2.setVisibility(View.VISIBLE);
            tvPred2.setText("도착정보 없음");
            tvLocNo2.setVisibility(View.INVISIBLE);
            tvSeat2.setVisibility(View.INVISIBLE);
            tvPredSeat2.setVisibility(View.INVISIBLE);
            tvNonInfo.setVisibility(View.INVISIBLE);

            if(station.getPno2()!=null){
                try {
                    tvPred2.setVisibility(View.VISIBLE);
                    tvLocNo2.setVisibility(View.VISIBLE);
                    tvSeat2.setVisibility(View.VISIBLE);
                    tvPredSeat2.setVisibility(View.VISIBLE);

                    String str = Integer.toString(station.getPred2());
                    if(str.equals("1"))
                        tvPred2.setText(" 곧 도착 ");
                    else
                        tvPred2.setText(" "+str+"분후 ");
                    tvLocNo2.setText(" "+Integer.toString(station.getLocno2()) + "번째전 ");

                    str = " 현재 " + Integer.toString(station.getSeat2()) + "석 ";
                    int color = Color.BLUE;
                    SpannableStringBuilder builder = new SpannableStringBuilder(str);
                    builder.setSpan(new ForegroundColorSpan(color), 4, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tvSeat2.setText(builder);

                    str = " 예상 " + Integer.toString(dbHelper.getPredict(route.toLowerCase(), stationid, station.getRound_pred2() + "+00").getPred_empty_seat()) + "석 ";
                    color = Color.GREEN;
                    builder = new SpannableStringBuilder(str);
                    builder.setSpan(new ForegroundColorSpan(color), 4, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tvPredSeat2.setText(builder);

                }catch (Exception e){

                    tvPred2.setVisibility(View.VISIBLE);
                    tvPred2.setText("도착정보 없음");
                    tvLocNo2.setVisibility(View.INVISIBLE);
                    tvSeat2.setVisibility(View.INVISIBLE);
                    tvPredSeat2.setVisibility(View.INVISIBLE);
                    tvNonInfo.setVisibility(View.INVISIBLE);
                }
            }


        }
    }

    public void httpwebStop(String route){
        HttpUtilBusStop http = new HttpUtilBusStop(this);
        String url = "http://52.78.231.5:8080/webtest0528/GetRealtimeStation.do?busRoute="+route;
        http.execute(url);
    }

    Handler mHandler = new Handler(){
        public void handleMessage(Message msg){


            mHandler.sendEmptyMessageDelayed(0, 1000);
        }

    };


    Handler refreshHandler = new Handler(){
        public void handleMessage(Message msg) {
            httpwebStop(route.toLowerCase());
            refreshHandler.sendEmptyMessageDelayed(0, 15000);
        }

    };


    public void setEmptyInfo(){
        tvNonInfo.setVisibility(View.VISIBLE);
        tvPred1.setVisibility(View.INVISIBLE);
        tvLocNo1.setVisibility(View.INVISIBLE);
        tvSeat1.setVisibility(View.INVISIBLE);
        tvPredSeat1.setVisibility(View.INVISIBLE);
        tvPred2.setVisibility(View.INVISIBLE);
        tvLocNo2.setVisibility(View.INVISIBLE);
        tvSeat2.setVisibility(View.INVISIBLE);
        tvPredSeat2.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onPause() {
        super.onPause();

        System.out.println("핸들러종료");
        // Remove the activity when its off the screen
        refreshHandler.removeMessages(0);
    }


}
