import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.impl.client.HttpClients;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.model.ByteString;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.UnicodeString;

import org.apache.http.impl.client.CloseableHttpClient;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpResponse;
import org.apache.http.HttpHost;

public class JSONFetch {

        static CloseableHttpClient httpclient = null;

        public static void main(String[] args) throws  IOException {

        //String url = "http://13.126.207.211:8080/blocks";
        String url = "http://13.126.77.3:8008/blocks";
        try{
         HttpGet request = new HttpGet(url);

         HttpHost proxyHost = new HttpHost("proxy.esl.cisco.com", 80);
         httpclient = HttpClients.custom().setProxy(proxyHost).build();
         HttpResponse response = null;
		 response = httpclient.execute(request);
                 int responseCode = response.getStatusLine().getStatusCode();
        // }while(response.getStatusLine().getStatusCode()!=200);
         System.out.println("Processing Response ...."+response.getStatusLine().getStatusCode());
         if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 202 || response.getStatusLine().getStatusCode() == 204) {

                 BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

                 String output;
                 System.out.println("Output from Server ...." + response.getStatusLine().getStatusCode() + "\n");
                 while ((output = br.readLine()) != null) {
                         //System.out.println("----1---------"+output);
                         if(output!=null && output.contains("\"payload\":")){
                                 output=(output.substring(output.indexOf(':')+3,output.length()-1));
                                 //System.out.println(output);
                                 try{
                                                output = new String(Base64.getDecoder().decode((output.getBytes("UTF-8"))));
                                                //System.out.println("-----final--------"+output);
                                                if(output != null && output.contains("Value") && output.contains("{")){
                                                        output=(output.substring(output.indexOf('{'),output.length()));
                                                        System.out.println("--------Transactions---------"+output);
                                                }

                                }catch(Exception e){
                                         System.out.println("not a map"+e);
                                   }
                         }
                 }
         } else {

                 throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
         }
        }finally{

                if (httpclient != null) {
                        httpclient.getConnectionManager().shutdown();
                }

        }
    }
}
    
