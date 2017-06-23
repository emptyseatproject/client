package com.example.splendor.bustest;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Splendor on 2017-04-30.
 */

public class DBHelper extends SQLiteOpenHelper {
    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        /* 이름은 BUS이고, 자동으로 값이 증가하는 _id 정수형 기본키 컬럼과
        item 문자열 컬럼, price 정수형 컬럼, create_at 문자열 컬럼으로 구성된 테이블을 생성. */
        db.execSQL("CREATE TABLE VERSION_TABLE (busVer TEXT);");
        // 서버에서 받아와서 테이블에 넣어놓고 클라이언트의 테이블과 비교하기 위함(http 리퀘스트가 너무빨라)
        db.execSQL("CREATE TABLE TEMP_VERSION_TABLE (busVer TEXT);");
        db.execSQL("CREATE TABLE BUS_LIST (_id INTEGER PRIMARY KEY AUTOINCREMENT, routeName TEXT, routeId TEXT);");
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void reOnCreate(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS VERSION_TABLE (busVer TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS TEMP_VERSION_TABLE (busVer TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS BUS_LIST (_id INTEGER PRIMARY KEY AUTOINCREMENT, routeName TEXT, routeId TEXT);");

    }

    public void createtBusTable(String busNo){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS " + busNo + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, stopId TEXT, stopName TEXT);");
        db.close();
    }

    public void createBusStopList(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS BUSSTOP_LIST (busStopId TEXT, busStopIndex INTEGER, " +
                "busStopName TEXT, route TEXT, passable TEXT, predictable TEXT);");
        db.close();
    }

    public void createPredict(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS PREDICT (route TEXT, stationid TEXT, ten_minutes TEXT, pred_empty_seat INTEGER);");
        db.close();
    }

    public void insertStop(String busStopId, int busStopIndex, String busStopName, String route, String passable, String predictable){
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO BUSSTOP_LIST VALUES('" + busStopId + "', '" + busStopIndex + "', '" + busStopName + "', '" + route+ "'" +
                ", '" + passable + "', '" + predictable + "');");
        db.close();
    }

    public void insertPredict(String route, String stationid, String ten_minutes, int pred_empty_seat){
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO PREDICT VALUES('" + route + "', '" + stationid + "', '" + ten_minutes + "', '" + pred_empty_seat + "');");
        db.close();
    }

    // 서버 API 변경으로 인해 임시 주석 처리 20170526 1828
    /*
    public void insertStop(String busNo, String stopId, String stopName){
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO "+busNo+" VALUES(null, '" + stopId + "', '" + stopName + "');");
        db.close();
    }
    */

    public void insertVersion(String busVer){
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO VERSION_TABLE VALUES('" + busVer + "');");
        db.close();
    }

    public void insertTempVersion(String busVer){
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO TEMP_VERSION_TABLE VALUES('" + busVer + "');");
        db.close();
    }

    public void insert(String routeName, String routeId) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO BUS_LIST VALUES(null, '" + routeName + "', '" + routeId + "');");
        db.close();
    }

    public String gettemp(){
        SQLiteDatabase db = getReadableDatabase();
        String result = "";
        Cursor cursor = db.rawQuery("SELECT * FROM BUS_LIST", null);
        while (cursor.moveToNext()) {
            result += cursor.getString(0)
                    + " | "
                    + cursor.getString(1)
                    + " | "
                    + cursor.getString(2)
                    + " | "
                    + "\n";
        }

        return result;

    }

    public String getTempStop(){
        SQLiteDatabase db = getReadableDatabase();
        String result = "";
        Cursor cursor = db.rawQuery("SELECT * FROM BUSSTOP_LIST", null);
        while (cursor.moveToNext()) {
            result += cursor.getString(0)
                    + " | "
                    + cursor.getString(1)
                    + " | "
                    + cursor.getString(2)
                    + " | "
                    + cursor.getString(3)
                    + " | "
                    + cursor.getString(4)
                    + " | "
                    + cursor.getString(5)
                    + " | "
                    + "\n";
        }

        return result;

    }

    /*
    public void update(String item, int price) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE MONEYBOOK SET price=" + price + " WHERE item='" + item + "';");
        db.close();
    }
    */

    public void dropTable(String tableName){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + tableName + ";");
        db.close();
    }

    public void deleteVersion() {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM VERSION_TABLE;");
        db.close();
    }

    public void deleteBusList() {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM BUS_LIST;");
        db.close();
    }

    public void deleteBus(String busNo) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM " + busNo + ";");
        db.close();
    }

    public String getVersion(){
        SQLiteDatabase db = getReadableDatabase();
        String version = null;
        Cursor cursor = db.rawQuery("SELECT * FROM VERSION_TABLE", null);
        while(cursor.moveToNext()) {
            version = cursor.getString(0);
        }
        return version;
    }

    public String getTempVersion(){
        SQLiteDatabase db = getReadableDatabase();
        String version = null;
        Cursor cursor = db.rawQuery("SELECT * FROM TEMP_VERSION_TABLE", null);
        while(cursor.moveToNext()) {
            version = cursor.getString(0);
        }
        return version;
    }

    public String gettempver(){
        SQLiteDatabase db = getReadableDatabase();
        String result = "";
        Cursor cursor = db.rawQuery("SELECT * FROM VERSION_TABLE", null);
        while (cursor.moveToNext()) {
            result += cursor.getString(0)
                    + " | "
                    + "\n";
        }

        return result;
    }

    public ArrayList<Bus> getBusList(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Bus> busList = new ArrayList<Bus>();

        Cursor cursor = db.rawQuery("SELECT * FROM BUS_LIST", null);
        while (cursor.moveToNext()) {
            Bus bus = null;
            bus = new Bus();
            bus.mID = cursor.getString(1);
            bus.mArea = cursor.getString(2);
            busList.add(bus);
        }
        return busList;
    }

    public ArrayList<BusStop> getStopList(String busStopName){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<BusStop> stopList = new ArrayList<BusStop>();
        Cursor cursor = db.rawQuery("SELECT * FROM BUSSTOP_LIST WHERE route='"+busStopName+"'", null);
        while (cursor.moveToNext()) {
            BusStop busStop = null;
            busStop = new BusStop();
            busStop.sID = cursor.getString(0);
            busStop.sName = cursor.getString(2);
            busStop.sPassable = cursor.getString(4);
            busStop.sPredictable = cursor.getString(5);
            busStop.sIndex = cursor.getInt(1);
            stopList.add(busStop);
        }
        return stopList;
    }

    public Predict getPredict(String route, String stationid, String ten_minutes){
        SQLiteDatabase db = getReadableDatabase();
        Predict predict = null;
        Cursor cursor = db.rawQuery("SELECT * FROM PREDICT WHERE ten_minutes='"+ten_minutes+"' AND route='"+route+"' AND stationid='"+stationid+"'", null);
        //System.out.println("여기와?");
        while(cursor.moveToNext()){
            predict = new Predict(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3));
            System.out.println(predict.getPred_empty_seat());
        }

        return predict;
    }

    public String toStringPredict(){
        SQLiteDatabase db = getReadableDatabase();
        String str = "";
        Cursor cursor = db.rawQuery("SELECT * FROM PREDICT", null);
        while(cursor.moveToNext()){
            str += cursor.getString(0)
                    + " | "
                    + cursor.getString(1)
                    + " | "
                    +  cursor.getString(2)
                    + " | "
                    + cursor.getInt(3)
                    + "\n";
        }

        return str;
    }

    /*
    public String getResult() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM MONEYBOOK", null);
        while (cursor.moveToNext()) {
            result += cursor.getString(0)
                    + " : "
                    + cursor.getString(1)
                    + " | "
                    + cursor.getInt(2)
                    + "원 "
                    + cursor.getString(3)
                    + "\n";
        }

        return result;
    }
    */
}
