package com.example.splendor.bustest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Splendor on 2017-05-29.
 */

public class IntroActivity extends Activity {

    private TextView tvStatus;
    private ImageView imgIcon;

    private DBHelper dbHelper = null;
    private String tempVersion = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        tvStatus = (TextView)findViewById(R.id.tvStatus);
        imgIcon = (ImageView)findViewById(R.id.imgIcon);
        imgIcon.setVisibility(View.VISIBLE);

        dbHelper = new DBHelper(getApplicationContext(), "Busb.db", null, 1);
        dbHelper.dropTable("TEMP_VERSION_TABLE");
        dbHelper.reOnCreate();

        httpwebGetVer();
        System.out.println("여기서멈춤?");
        System.out.println("초기 웹에서받아온 버전은? : "+dbHelper.getTempVersion());
        System.out.print("초기 디비에 저장된 버전은? : "+dbHelper.getVersion());

    }

    public void parseGetVerAry(String version){

        System.out.println("웹에서받아온 버전은? : "+version);
        System.out.println("디비에 저장된 버전은? : "+dbHelper.getVersion());
        tempVersion = version;

        // 버전 테이블 비어있음?
        if(isVersionEmpty()){

            System.out.println("여기는 비어있을때");
            //버스 리스트 삽입
            httpwebBus();

        }
        else
            httpwebVer(version);
    }

    public void parseVerAry(String response) {
        System.out.println("버전어떄?"+response);

        // 클라와 서버의 버스 버전이 다름?
        if(!response.equals("true")){
            System.out.println("여기는 다를때");

            dbHelper.dropTable("BUSSTOP_LIST");
            dbHelper.dropTable("VERSION_TABLE");
            dbHelper.dropTable("BUS_LIST");
            dbHelper.reOnCreate();

            //여기서 부턴 초기 생성시와 똑같음

            //버스 리스트 삽입
            httpwebBus();

            System.out.println("누가먼저생성되냐?");
        }
        else{
            System.out.println("여기는 버전이 같을 때");
            //ArrayList<Bus> buses = dbHelper.getBusList();
            sendIntent();
        }

    }

    /**
     * 버스 리스트를 서버에서 받아 데이터베이스에 넣는 메소드
     * @param jAry
     */
    public void parseAry(JSONArray jAry) {

        try {
            for (int i = 0; i < jAry.length(); i++) {
                JSONObject obj = jAry.getJSONObject(i);
                System.out.println(i+"번째 제이슨이다.");
                dbHelper.insert(obj.getString("routeName"), obj.getString("routeId"));
                System.out.print(dbHelper.gettemp());
                //mAdapter.addItem(obj.getString("route"), obj.getString("routeId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        dbHelper.createBusStopList();
        httpwebStop();
    }

    /**
     * 버스별 정류소를 테이블에 저장
     * @param jAry
     */
    public void parseStopAry(JSONArray jAry){
        String route=null;
        try{
            for(int i = 0; i < jAry.length() ; i++){
                JSONObject obj = jAry.getJSONObject(i);
                System.out.println(i+"번째 제이슨이다.");
                route = obj.getString("route");
                dbHelper.insertStop(obj.getString("busStopId"),obj.getInt("busStopIndex"),obj.getString("busStopName"),obj.getString("route"),
                        obj.getString("passable"),obj.getString("predictable"));
                //mAdapter.addItem(obj.getString("routeId"),obj.getString("route"));
            }
            System.out.print(dbHelper.getTempStop());
        }catch (Exception e){
            e.printStackTrace();
        }

        dbHelper.dropTable("PREDICT");
        dbHelper.createPredict();
        httpwebPredict(route.toLowerCase());
    }

    public void parsePredictAry(JSONArray jAry){
        try{
            for(int i = 0; i < jAry.length() ; i++){
                JSONObject obj = jAry.getJSONObject(i);
                //System.out.println(obj.getString("route")+" | "+obj.getString("stationid")+" | "+obj.getString("ten_minutes")+" | "+obj.getInt("pred_empty_seat"));
                dbHelper.insertPredict(obj.getString("route"),obj.getString("stationid"),obj.getString("ten_minutes"),obj.getInt("pred_empty_seat"));

                //mAdapter.addItem(obj.getString("routeId"),obj.getString("route"));
            }
            //System.out.println(dbHelper.toStringPredict());
        }catch (Exception e){
            e.printStackTrace();
        }

        //버전 삽입 -----  여기가 메인엑티비티 마지막이거든. 이거 나중에 인트로로 다 넘겨야대
        //System.out.println("웹에서받아온 버전은? : "+dbHelper.getTempVersion());
        dbHelper.insertVersion(tempVersion);
        System.out.println("디비에 저장된 버전은? : "+dbHelper.getVersion());
        sendIntent();
    }



    public void httpwebGetVer(){
        tvStatus.setText("버전을 받아오는 중입니다.");
        HttpUtilGetVersion http = new HttpUtilGetVersion(this);
        String url = "http://52.78.231.5:8080/webtest0528/GetSupportedVer.do";
        http.execute(url);
    }


    /**
     * 서버에 접속하여 가용버스 목록과 정류소 명단을
     */
    public void httpwebVer(String version){
        tvStatus.setText("버전을 확인 중입니다.");
        HttpUtilVersion http = new HttpUtilVersion(this);
        String url = "http://52.78.231.5:8080/webtest0528/IsSupportedVer.do?clientResourceVer=";
        url = url + version;
        http.execute(url);
    }

    public void httpwebBus() {
        tvStatus.setText("가용 버스를 받아오는 중입니다.");
        HttpUtil http = new HttpUtil(this);
        String url = "http://52.78.231.5:8080/webtest0528/GetAvailableBusList.do";
        http.execute(url);
        //http.cancel(true);
    }

    public void httpwebStop(){
        tvStatus.setText("정류장 목록을 받아오는 중입니다.");
        HttpUtilBus http = new HttpUtilBus(this);
        String url = "http://52.78.231.5:8080/webtest0528/GetBusStopList.do";
        http.execute(url);
    }

    public void httpwebPredict(String route){
        tvStatus.setText("예측 잔여 좌석 정보를 받아오는 중입니다.");
        HttpUtilPredict http = new HttpUtilPredict(this);
        String url = "http://52.78.231.5:8080/webtest0528/GetPredict.do?busRoute=";
        url = url + route;
        http.execute(url);
    }

    public boolean isVersionEmpty(){
        String version = dbHelper.getVersion();
        if(version==null || version.equals("null"))
            return true;
        return false;
    }

    public void sendIntent(){
        tvStatus.setText("구성 완료! 곧 실행됩니다.");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(intent);
                // 뒤로가기 했을경우 안나오도록 없애주기 >> finish!!
                finish();
            }
        }, 2000);

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Toast.makeText(IntroActivity.this, "로딩 중에는 종료할 수 없습니다.", Toast.LENGTH_SHORT).show();
    }

}
