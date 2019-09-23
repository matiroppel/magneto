package com.magneto.mutant;


import org.springframework.boot.SpringApplication;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MutantTest {

    @BeforeSuite
    public void initSpringBoot() throws InterruptedException {
        SpringApplication.run(MutantApplication.class);
        Thread.sleep(2000);
    }

    @DataProvider( name = "humanDNADataBase")
    public Object[][] humanDNADataBase(){
        return new Object[][]{
                //{"src/test/resources/DNA/dna1.json", true},
                {"src/test/resources/DNA/dna2.json", false}

        };
    }

    @Test( dataProvider = "humanDNADataBase")
    public void mutantTest(String mutantPath, boolean isMutant) throws IOException {
        String json = new String(Files.readAllBytes(Paths.get(mutantPath)));
        String result = sendAPIRequest("http://127.0.0.1:8080/mutant", "POST", json);
        Assert.assertNotNull(result);
        if (isMutant)
            Assert.assertEquals(result, "Mutant");
        else
            Assert.assertEquals(result, "No-mutant");
    }

    @Test
    public void mutantStatsTest() throws IOException {
        //String result = sendAPIRequest("http://127.0.0.1:8080/stats", "GET", "");
        StringBuilder result = new StringBuilder();
        URL url = new URL("http://127.0.0.1:8080/stats");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        System.out.println(result);
        Assert.assertNotNull(result);
    }

    private String sendAPIRequest(String targetURL, String type, String json) {
        HttpURLConnection connection = null;
        String responseString = null;
        try {
            //Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(type);
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = json.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                responseString = response.toString();
            }
        } catch (IOException e){
            if (e.getMessage().contains("Server returned HTTP response code: 403"))
                return "No-mutant";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return responseString;
    }

}