# WillBot
A bot to rule them all.

The main functionality of this project is crawling through the websites (in our case Willhaben and Immowelt) and scrapping all the relevant
data we would like to see. Scope of this project is to scrape basic information of each real estate based on the requirement person using this 
bot chooses (specific price, size, room count).

## Instructions for running the Project
We used Selenium to scrape the websites for the search results.
This is why you'll need to have Google Chrome installed and dowload the chromedriver for your Chrome version from [https://sites.google.com/a/chromium.org/chromedriver/downloads]

Once downloaded, move the chromedriver.exe from the ZIP into a directory that's in your PATH Variable.
f.e. in Windows, move it to C:\Windows\System32

If you just cloned the project you will need to import Gradle configs before you can build/run the project. You'll also need to install a plugin called "Lombok".
Once you ran WillBotApplication.java with IntelliJ you can access the UI with a Browser under [http://localhost:8080/willbot]

If you want to use the API directly you can look at WillBotController.java to see the available paramters and HTTP methods.
