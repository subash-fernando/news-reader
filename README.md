# News Reader



### Requirements

- [JDK 1.8+](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)


### Configurations

**Images download directory**

`download.file.directory   `

ex :- C:\Users\Subash\Desktop\images\

**Max Download file size in MB**

`download.file.max.size.mb=4`

**Max download retry count**

`download.file.retry.max.limit=4`

**Image downloading Scheduler rate in milli seconds**

`scheduler.image.downloader.fixed.rate.milliseconds=10000`

**Image downloading Scheduler initially delay in milli seconds**

`scheduler.image.downloader.fixed.delay.milliseconds=10000`

**News feed reding max messages per pol**

`news.feed.reading.max.messages.per.poll=10`

**News feed reading rate in milli seconds**

`news.feed.reading.period=300000`


### Commands

**Build Command**

`mvn clean build`


**Run Command**

`mvn spring-boot:run`

### Application Url

http://localhost:8080/graphiql

