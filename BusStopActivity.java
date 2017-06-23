package com.example.splendor.bustest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Splendor on 2017-04-09.
 */

public class BusStopActivity extends AppCompatActivity {

    // 타이머도 쓰기 위해 전역변수로 설정
    TextView tvStopName;
    TextView tvStopId;
    TextView tvBusId;
    TextView tvBusDirection;
    TextView tvFirstBusArrivalTime;
    TextView tvFirstBusPrev;
    TextView tvFirstEmptySeat;
    TextView tvFirstExpectedEmptySeat;
    TextView tvSecondBusArrivalTime;
    TextView tvSecondBusPrev;
    TextView tvSecondEmptySeat;
    TextView tvSecondExpectedEmptySeat;
    String busno;
    int temp_busArrivalTime_1;
    int temp_busPrev_1;
    int temp_busEmptySeat_1;
    int temp_busExpectedEmptySeat_1;
    int temp_busArrivalTime_2;
    int temp_busPrev_2;
    int temp_busEmptySeat_2;
    int temp_busExpectedEmptySeat_2;
    BusInfo[] busInfo = new BusInfo[2];
    ArrayList<BusInfo> busInfos = new ArrayList<BusInfo>();

    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop);


        tvStopName = (TextView)findViewById(R.id.textView4);
        tvStopId = (TextView)findViewById(R.id.textView5);
        tvBusId = (TextView)findViewById(R.id.textView6);
        tvBusDirection= (TextView)findViewById(R.id.textView7);
        tvFirstBusArrivalTime = (TextView)findViewById(R.id.textView8);
        tvFirstBusPrev = (TextView)findViewById(R.id.textView12);
        tvFirstEmptySeat = (TextView)findViewById(R.id.textView14);
        tvFirstExpectedEmptySeat = (TextView)findViewById(R.id.textView10);
        tvSecondBusArrivalTime = (TextView)findViewById(R.id.textView9);
        tvSecondBusPrev = (TextView)findViewById(R.id.textView13);
        tvSecondEmptySeat = (TextView)findViewById(R.id.textView32);
        tvSecondExpectedEmptySeat = (TextView)findViewById(R.id.textView11);
        Intent intent = getIntent(); // 보내온 Intent를 얻는다

        tvStopName.setText(intent.getStringExtra("Stop_Name"));
        tvStopId.setText(intent.getStringExtra("Stop_ID"));
        tvBusId.setText(intent.getStringExtra("Bus_ID"));
        tvBusDirection.setText(intent.getStringExtra("Bus_Direction")+" 방향");
        busno = intent.getStringExtra("Bus_ID").toString();


        //httpwebStop(busno);


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


        refreshHandler.sendEmptyMessage(0);
        mHandler.sendEmptyMessage(0);

        /*
        mHandler.sendEmptyMessage(0);
        BackThread thread = new BackThread();
        thread.setDaemon(true);
        thread.start();
        */
        findViewById(R.id.btn_refresh).setOnClickListener(mClickListener);
    }
    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //이곳에 버튼 클릭시 일어날 일을 적습니다.
            refreshHandler.removeMessages(0);
            refreshHandler.sendEmptyMessage(0);
        }
    };


    /*

    class BackThread extends Thread{
        @Override
        public void run() {

            while(true){

                // 메인스레드에 있던 handler겍체를 사용하여
                // Runnable 객체를 보내고 (post)
                mHandler.post(new Runnable(){
                    @Override
                    public void run() {  // Runnable 의 Run() 메소드에서 UI 접근
                        int temp_busArrivalTime_1 = Integer.parseInt(tvFirstBusArrivalTime.getText().toString());
                        int temp_busPrev_1 = Integer.parseInt(tvFirstBusPrev.getText().toString());
                        int temp_busEmptySeat_1 = Integer.parseInt(tvFirstEmptySeat.getText().toString());
                        int temp_busExpectedEmptySeat_1 = Integer.parseInt(tvFirstExpectedEmptySeat.getText().toString());
                        int temp_busArrivalTime_2 = Integer.parseInt(tvSecondBusArrivalTime.getText().toString());
                        int temp_busPrev_2 = Integer.parseInt(tvSecondBusPrev.getText().toString());
                        int temp_busEmptySeat_2 = Integer.parseInt(tvSecondEmptySeat.getText().toString());
                        int temp_busExpectedEmptySeat_2 = Integer.parseInt(tvSecondExpectedEmptySeat.getText().toString());

                        tvFirstBusArrivalTime.setText(temp_busArrivalTime_1--);
                        tvFirstBusPrev.setText(temp_busPrev_1--);
                        tvFirstEmptySeat.setText(temp_busEmptySeat_1--);
                        tvFirstExpectedEmptySeat.setText(temp_busExpectedEmptySeat_1--);
                        tvSecondBusArrivalTime.setText(temp_busArrivalTime_2--);
                        tvSecondBusPrev.setText(temp_busPrev_2--);
                        tvSecondEmptySeat.setText(temp_busEmptySeat_2--);
                        tvSecondExpectedEmptySeat.setText(temp_busExpectedEmptySeat_2--);
                    }
                });
                try {
                    Thread.sleep(1000);  // 1초 간격으로
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
*/

    public void parseAry(JSONArray jAry){

        try{
            // 상위 두개 버스만 필요하다.

                JSONObject obj = jAry.getJSONObject(0);
                tvFirstBusArrivalTime.setText(obj.getString("arrivalTime")+" 초");
                tvFirstBusPrev.setText(obj.getString("nextStop")+ " 번째 전");
                tvFirstEmptySeat.setText(obj.getString("emptySeat")+" 석");
                tvFirstExpectedEmptySeat.setText(obj.getString("expectedEmptySeat")+" 석");

            temp_busArrivalTime_1=Integer.parseInt(obj.getString("arrivalTime").replace(" 초",""));
            temp_busPrev_1=Integer.parseInt(obj.getString("nextStop").replace(" 번째 전",""));
            temp_busEmptySeat_1=Integer.parseInt(obj.getString("emptySeat").replace(" 석",""));
            temp_busExpectedEmptySeat_1=Integer.parseInt(obj.getString("expectedEmptySeat").replace(" 석",""));

                 obj = jAry.getJSONObject(1);
                tvSecondBusArrivalTime.setText(obj.getString("arrivalTime")+" 초");
                tvSecondBusPrev.setText(obj.getString("nextStop")+ " 번째 전");
                tvSecondEmptySeat.setText(obj.getString("emptySeat")+" 석");
                tvSecondExpectedEmptySeat.setText(obj.getString("expectedEmptySeat")+" 석");

            temp_busArrivalTime_2=Integer.parseInt(obj.getString("arrivalTime").replace(" 초",""));
            temp_busPrev_2=Integer.parseInt(obj.getString("nextStop").replace(" 번째 전",""));
            temp_busEmptySeat_2=Integer.parseInt(obj.getString("emptySeat").replace(" 석",""));
            temp_busExpectedEmptySeat_2=Integer.parseInt(obj.getString("expectedEmptySeat").replace(" 석",""));
                /*
                busInfo[i] = new BusInfo(obj.getString("arrivalTime"),obj.getString("nextStop"),
                        obj.getString("emptySeat"),obj.getString("expectedEmptySeat"));
                        */

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void httpwebStop(String bn){
        HttpUtilBusStop http = new HttpUtilBusStop(this);
        String url = "http://52.78.231.5:8080/webtest/getcurrentbuslist?bn="+bn;
        http.execute(url);
    }

    Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            //httpwebStop(busno);
            /*

            int temp_busArrivalTime_1 = Integer.parseInt(tvFirstBusArrivalTime.getText().toString());
            int temp_busPrev_1 = Integer.parseInt(tvFirstBusPrev.getText().toString());
            int temp_busEmptySeat_1 = Integer.parseInt(tvFirstEmptySeat.getText().toString());
            int temp_busExpectedEmptySeat_1 = Integer.parseInt(tvFirstExpectedEmptySeat.getText().toString());
            int temp_busArrivalTime_2 = Integer.parseInt(tvSecondBusArrivalTime.getText().toString());
            int temp_busPrev_2 = Integer.parseInt(tvSecondBusPrev.getText().toString());
            int temp_busEmptySeat_2 = Integer.parseInt(tvSecondEmptySeat.getText().toString());
            int temp_busExpectedEmptySeat_2 = Integer.parseInt(tvSecondExpectedEmptySeat.getText().toString());
*/
            tvFirstBusArrivalTime.setText(Integer.toString(temp_busArrivalTime_1--)+" 초");
            tvFirstBusPrev.setText(Integer.toString(temp_busPrev_1)+ " 번째 전");
            tvFirstEmptySeat.setText(Integer.toString(temp_busEmptySeat_1)+" 석");
            tvFirstExpectedEmptySeat.setText(Integer.toString(temp_busExpectedEmptySeat_1)+" 석");
            tvSecondBusArrivalTime.setText(Integer.toString(temp_busArrivalTime_2--)+" 초");
            tvSecondBusPrev.setText(Integer.toString(temp_busPrev_2)+ " 번째 전");
            tvSecondEmptySeat.setText(Integer.toString(temp_busEmptySeat_2)+" 석");
            tvSecondExpectedEmptySeat.setText(Integer.toString(temp_busExpectedEmptySeat_2)+" 석");


            mHandler.sendEmptyMessageDelayed(0, 1000);
        }

    };


    Handler refreshHandler = new Handler(){
        public void handleMessage(Message msg) {
            httpwebStop(busno);
            refreshHandler.sendEmptyMessageDelayed(0, 15000);
        }

    };
}
