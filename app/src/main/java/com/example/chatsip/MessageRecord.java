package com.example.chatsip;

import org.apache.commons.lang3.RandomStringUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MessageRecord {
    public String content;// 消息内容
    public String sender;// 发送方sip账号
    public Timestamp time;
    public MessageRecord(){};

    public MessageRecord(String content, Timestamp time, String sender){
        this.content=content;
        this.time=time;
        this.sender = sender;
    }

    public static List<MessageRecord> getMockData(){
        ArrayList<MessageRecord> records = new ArrayList<>();
        String sipFrom = "sipF";
        String sipTo = "sipT";
        for(int i=0;i<5;i++){
            String content = RandomStringUtils.randomAlphabetic(10);
            Timestamp t = new Timestamp(System.currentTimeMillis());
            records.add(new MessageRecord(content,t,sipFrom));
            String content_ = RandomStringUtils.randomAlphabetic(10);
            records.add(new MessageRecord(content_,t,sipTo));
        }
        return records;
    }
}