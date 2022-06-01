# BrowserMob BrowserStack App Automate

## Using BrowserMob Proxy to intercept/capture Network Requests into HAR files for App Automate tests

Steps to run:

- upload `Wikipedia.apk` file to BrowserStack App-Automate:
    ```shell
    curl -u "BROWSERSTACK_USERNAME:BROWSERSTACK_ACCESS_KEY" -X POST "https://api-cloud.browserstack.com/app-automate/upload" -F "file=@Wikipedia.apk" -F "custom_id=WikipediaApp"
    ```
- `mvn clean install`
- `mvn compile exec:java`
