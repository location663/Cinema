package com.stylefeng.guns.rest.util;

import com.alibaba.fastjson.JSON;
import com.stylefeng.guns.rest.vo.order.SeatsVO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetSeats {
    public static SeatsVO getSeatsFromFront(String urlString) {
        String res = null;
        try {
//            URL url = new URL("http://localhost:1818/json/4dx.json");
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String lines;
            StringBuilder sb = new StringBuilder();
            while ((lines = bufferedReader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sb.append(lines);
            }
            res = sb.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            SeatsVO seatsVO = null;
            if (null != res) {
                seatsVO = JSON.parseObject(res, SeatsVO.class);
            }
            return seatsVO;
        }
    }
}
