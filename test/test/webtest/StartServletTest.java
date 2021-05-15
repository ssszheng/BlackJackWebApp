package webtest;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.http.impl.client.HttpClients;



/**
 *
 * @author szheng
 */
public class StartServletTest {
    private static final String path ="jack/start";
    private static int port = 8080; 
    
    public StartServletTest() {
    }
    
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    
    @Test
    public void sessionIDcreatedTest() throws Exception{ 
         assertTrue(getSessionID("").contains("JSESSIONID"));
        
    }
    
    @Test
    public void gamestartedTest() throws Exception{ 
        assertTrue(getGameState("").contains("\"GameStatus\":\"started\""));
    }
    //check two sessin id different
    @Test
    public void gameSessionIdDifferentTest() throws Exception{ 
        String sessionidA= getSessionID("");
        String sessionidB= getSessionID("");
        assertFalse(sessionidA.equals(sessionidB));
        
    }
       
    public String getGameState(String expression) throws Exception{     
         String jsessionid = getSessionID("").split("=")[1];
         System.out.print(jsessionid);
         URI stateURL = new URIBuilder()
                    .setScheme("http")
                    .setHost("localhost")
                    .setPort(port)
                    .setPath("jack/state"+";jsessionid="+jsessionid)
                    .build();
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(stateURL);
        CloseableHttpResponse response = client.execute(get);
        
        String body= getResponseBody(response.getEntity().getContent()); 

        return body;
    }
         
    
    
    
    
    public String getSessionID(String expression) throws Exception{ 
         try (CloseableHttpClient client = HttpClients.createDefault()) {
        
            URI url = constructUri(expression);
            HttpGet get = new HttpGet(url);
            
            try (CloseableHttpResponse response = client.execute(get)) {
                
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                  
                    String cookie = response.getFirstHeader("Set-Cookie").getValue().split(";")[0];
                    return cookie;
                }
                else{                
                    throw new Exception("Error response from service.");
		}
            }
        }}
    
       
     
     
    private static String makeServiceRequest(String expression) throws Exception {
        
        try (CloseableHttpClient client = HttpClients.createDefault()) {
        
            URI url = constructUri(expression);
            HttpGet get = new HttpGet(url);
            
            try (CloseableHttpResponse response = client.execute(get)) {
                
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                   
                   return getResponseBody(response.getEntity().getContent()); 
                }
                else{                
                    throw new Exception("Error response from service.");
		}
            }
        }
    }
    
     private static String getResponseBody(final InputStream stream) throws UnsupportedOperationException, IOException {
        
        StringBuilder builder = new StringBuilder();
        
        BufferedReader rd = new BufferedReader(new InputStreamReader(stream));
        String line = "";
        while ((line = rd.readLine()) != null) {
            builder.append(line);
        }
        
        return builder.toString();
    }

    private static URI constructUri(String expression) throws Exception {
        
        URI url = null;
        try {
            url = new URIBuilder()
                    .setScheme("http")
                    .setHost("localhost")
                    .setPort(port)
                    .setPath(path)
                    .build();
        } catch (URISyntaxException ex) {
            throw new Exception("Error creating service URI", ex);
        }
        return url;
    }    
}

  