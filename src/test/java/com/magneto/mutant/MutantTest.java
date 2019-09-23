package com.magneto.mutant;


import org.springframework.boot.SpringApplication;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.*;
import java.net.HttpURLConnection;
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

    @DataProvider( name = "humanDNADataBase", parallel = true)
    public Object[][] humanDNADataBase(){
        return new Object[][]{
                {"src/test/resources/DNA/dna1.json", true}
        };
    }

    @Test( dataProvider = "humanDNADataBase")
    public void mutanteTest(String mutantPath, boolean isMutant) throws IOException {
        String json = new String(Files.readAllBytes(Paths.get(mutantPath)));
        String result = sendAPIRequest("http://127.0.0.1:8080/mutant", "POST", json);
        Assert.assertNotNull(result);
        if (isMutant)
            Assert.assertEquals(result, "Mutante");
        else
            Assert.assertEquals(result, "No-Mutante");

    }

    private String sendAPIRequest(String targetURL, String type, String json) {
        HttpURLConnection connection = null;
        String responseString;
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