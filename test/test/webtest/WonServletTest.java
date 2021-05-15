/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webtest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author szheng
 */
public class WonServletTest {

    private static final String pathWon = "jack/won";
    private static int port = 8080;
    private static String jsessionid = "";
    private static String body;
    private static String contentType = null;
 

    public WonServletTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void wonAfter2hitsTest() throws Exception {

        while (true) {
           
           if("text/plain".equals(contentType)){ 
               System.out.println("wonBody: " + body);
               System.out.print(contentType);
               assertTrue(body.contains("Winner"));
               break;
           }
           else { //start a new game 
                makeServiceRequestGetBody("jack/move/hit");
                //hit again 
                body = hitAgainGetBody(); 
           }
        }
    }

    private static URI constructStartGameUri(String path) throws Exception {

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

    public static String startGameGetSessionID() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {

            URI url = constructStartGameUri("jack/start");
            HttpGet get = new HttpGet(url);

            try (CloseableHttpResponse response = client.execute(get)) {

                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                    String cookie = response.getFirstHeader("Set-Cookie").getValue().split(";")[0];
                    //"jsessionid = xxxxxx"
                    return cookie;
                } else {
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

    private static String makeServiceRequestGetBody(String path) throws Exception {

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            jsessionid = startGameGetSessionID().split("=")[1];

            URI url = constructStartGameUri(path + ";jsessionid=" + jsessionid);
            HttpGet get = new HttpGet(url);

            try (CloseableHttpResponse response = client.execute(get)) {

                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                    String body = getResponseBody(response.getEntity().getContent());

                    return body;
                } else {
                    throw new Exception("Error response from service.");
                }
            }
        }
    }

    private static String hitAgainGetBody() throws Exception {

        try (CloseableHttpClient client = HttpClients.createDefault()) {

            URI url = constructStartGameUri("jack/move/hit;jsessionid=" + jsessionid);
            HttpGet get = new HttpGet(url);

            try (CloseableHttpResponse response = client.execute(get)) {

                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                    body = getResponseBody(response.getEntity().getContent());
                    contentType = response.getFirstHeader("Content-Type").getValue().split(";")[0];
                    
                    return body;
                } else {
                    return null;
                }
            }
        }
    }

   
}
