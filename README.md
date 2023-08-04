# BrowserMob BrowserStack App Automate

## Using BrowserMob Proxy to intercept/capture Network Requests into HAR files for App Automate tests

Steps to run:

- upload `Wikipedia.apk` file to BrowserStack App-Automate:
    ```shell
    curl -u "BROWSERSTACK_USERNAME:BROWSERSTACK_ACCESS_KEY" -X POST "https://api-cloud.browserstack.com/app-automate/upload" -F "file=@Wikipedia.apk" -F "custom_id=WikipediaApp"
    ```
- `mvn clean install`
- `mvn compile exec:java`

## Explanation

1. Add the BrowserMob Proxy dependency in the pom.xml.
```
<dependency>
  <groupId>net.lightbody.bmp</groupId>
  <artifactId>browsermob-core</artifactId>
  <version>2.1.5</version>
</dependency>
```

2. Create the BrowserMobProxy instance with a port number.
```
BrowserMobProxy proxy = new BrowserMobProxyServer();
proxy.start(1234);
```

3. Start BrowserStack Local instance with Proxy Details.
```
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
```

4. Instantiate the driver with relevant Desired Capabilities including `browserstack.networkLogs`, `browserstack.acceptInsecureCerts`, `browserstack.local` and `browserstack.localIdentifier`
```
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
```
5. Enable HAR capture for the BrowserMobProxy instance 
```
proxy.enableHarCaptureTypes(EnumSet.of(CaptureType.REQUEST_HEADERS, CaptureType.REQUEST_CONTENT,
        CaptureType.RESPONSE_HEADERS, CaptureType.RESPONSE_CONTENT));
proxy.newHar("wikipedia.com");
```
6. Save the HAR to a file
```
FileOutputStream fos = new FileOutputStream("test.har");
proxy.getHar().writeTo(fos);
```
