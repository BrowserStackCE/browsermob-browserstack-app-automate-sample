package com.browserstack;

import com.browserstack.local.Local;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.proxy.CaptureType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.FileOutputStream;
import java.net.URL;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {

    public static void main(String[] args) throws Exception {
        BrowserMobProxy proxy = new BrowserMobProxyServer();
        proxy.start(1234);
        System.out.println("Started proxy server at: " + proxy.getPort());

        String username = System.getenv("BROWSERSTACK_USERNAME");
        String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");

        Local l = new Local();
        Map<String, String> options = new HashMap<>();
        options.put("key", accessKey);
        options.put("v", "true");
        options.put("force", "true");
        options.put("forcelocal", "true");
        options.put("forceproxy", "true");
        options.put("localProxyHost", "localhost");
        options.put("localProxyPort", "1234");
        options.put("localIdentifier", "Test1");
        l.start(options);

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platform", "android");
        capabilities.setCapability("os_version", "11.0");
        capabilities.setCapability("device", "Samsung Galaxy S21");
        capabilities.setCapability("app", "WikipediaApp");
        capabilities.setCapability("name", "BrowserMob with BrowserStack App-Automate");
        capabilities.setCapability("browserstack.debug", "true");
        capabilities.setCapability("browserstack.networkLogs", "true");
        capabilities.setCapability("browserstack.acceptInsecureCerts", "true");
        capabilities.setCapability("browserstack.local", "true");
        capabilities.setCapability("browserstack.localIdentifier", "Test1");

        AndroidDriver<AndroidElement> driver = new AndroidDriver<>(
                new URL("http://" + username + ":" + accessKey + "@hub.browserstack.com/wd/hub"), capabilities);

        proxy.enableHarCaptureTypes(EnumSet.of(CaptureType.REQUEST_HEADERS, CaptureType.REQUEST_CONTENT,
                CaptureType.RESPONSE_HEADERS, CaptureType.RESPONSE_CONTENT));
        proxy.newHar("wikipedia.com");

        Thread.sleep(10000);

        System.out.println("Getting HAR file");

        List<HarEntry> entries = proxy.getHar().getLog().getEntries();
        for (HarEntry harEntry : entries) {
            System.out.println(harEntry.getRequest().getUrl());
        }
        FileOutputStream fos = new FileOutputStream("test.har");
        proxy.getHar().writeTo(fos);

        driver.quit();
        l.stop();
        proxy.stop();
    }

}
