package com.mycompany.app;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class App
{
    public static void main( String[] args ) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet("https://www.bloomberg.com/markets/api/security/currency/cross-rates/AUD,CNY,EUR,GBP,HKD,IDR,JPY,KRW,MYR,PHP,SGD,THB,USD,VND,AED,CAD,INR,MVR,TRY,TWD");
            CloseableHttpResponse response1 = httpclient.execute(httpGet);
            try {
                System.out.println(response1);
            } finally {
                response1.close();
            }
        } finally {
            httpclient.close();
        }
    }
}
