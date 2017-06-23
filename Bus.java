package com.example.splendor.bustest;

import java.text.Collator;
import java.util.Comparator;

/**
 * Created by Splendor on 2017-03-26.
 */

public class Bus {
    /**
     * 리스트 정보를 담고 있을 객체 생성
     */
    // 버스이름
    public String mID;

    // 지역
    public String mArea;

    // 번호판
    public String mNo;

    public String getID(){
        return this.mID;
    }
    public String getmArea(){
        return this.mArea;
    }
    public String getmNo() { return this.mNo; }
}
