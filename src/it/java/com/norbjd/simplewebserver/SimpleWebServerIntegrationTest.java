package com.norbjd.simplewebserver;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class SimpleWebServerIntegrationTest {

    @Test
    public void itemPresentInRepository() {
        try {
            URL url = new URL("http://localhost:4567/items/1");
            URLConnection connection = url.openConnection();
            InputStream response = connection.getInputStream();

            Scanner scanner = new Scanner(response);
            String responseBody = scanner.useDelimiter("\\A").next();

            Assert.assertEquals("item1", responseBody);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            Assert.fail();
        }
    }

}
