### PS Java 1.8.0_141 ++ Required ! Download Java 8 here : ( https://www.java.com/en/ )

# JAVAFX-WEB-Browser 

[![Latest Version](https://img.shields.io/github/release/goxr3plus/JavaFX-Web-Browser.svg?style=flat-square)](https://github.com/goxr3plus/JavaFX-Web-Browser/releases)
[![GitHub contributors][contributors-image]][contributors-url]
[![HitCount](http://hits.dwyl.io/goxr3plus/JavaFX-Web-Browser.svg)](http://hits.dwyl.io/goxr3plus/JavaFX-Web-Browser)
[![Total Downloads](https://img.shields.io/github/downloads/goxr3plus/JavaFX-Web-Browser/total.svg)](https://github.com/goxr3plus/JavaFX-Web-Browser/releases)

[contributors-url]: https://github.com/goxr3plus/JavaFX-Web-Browser/graphs/contributors
[contributors-image]: https://img.shields.io/github/contributors/goxr3plus/JavaFX-Web-Browser.svg
[browser-jitpack-url]: https://jitpack.io/#goxr3plus/JavaFX-Web-Browser/V3.5.0

# Hey friends follow the newest NetBeans tutorial here ->

[![video](http://img.youtube.com/vi/5Y0ZUS7q_lI/0.jpg)](https://www.youtube.com/watch?v=5Y0ZUS7q_lI)

Repository -> https://github.com/goxr3plus/Embedded-JavaFX-Web-Browser-Example-with-Maven

----------------------------------------------------------------------------

The below tutorial is Eclipse Version , older tutorial

# Youtube tutorial 
[![video](http://img.youtube.com/vi/F1yxsH8qyuI/0.jpg)](https://www.youtube.com/watch?v=F1yxsH8qyuI)

Repository of tutorial -> https://github.com/goxr3plus/JavaFXBrowserOverlay

-----------------------------------------------------------------------------

# You can use this browser inside your JavaFX Application 

What that means ? Well you can download the jar file with Maven , Gradle etc ( the depencities will come along ) and use it inside your application . 

--> You don't believe me ?? ( I am already doing this with [XR3Player](https://github.com/goxr3plus/XR3Player) ) 

--> How you can embed it inside your application ? ( Use [JitPack.io][browser-jitpack-url] )

### Add it to your project using JitPack :

[Link][browser-jitpack-url]

### Step 1. Add the JitPack repository to your build file
``` XML
<repositories>
	<repository>
	   <id>jitpack.io</id>
	   <url>https://jitpack.io</url>
        </repository>
</repositories>
```

###  Step 2. Add the dependency
``` XML
<dependency>
	 <groupId>com.github.goxr3plus</groupId>
	 <artifactId>JavaFX-Web-Browser</artifactId>
	 <version>V3.5.0</version>
</dependency>
```

Then from inside your code you can create an intance or multiple instances of Browser like this:

``` JAVA
public WebBrowserController webBrowser = new WebBrowserController();
```

and add it for example inside a BorderPane :

```JAVA
BorderPane borderPane = new BorderPane( webBrowser );
```

Happy :) ?  Cause i am ...

---

# Description

You always dreamed how to make a WebBrowser in JavaFX? Well then you are in the right place . It took me 2 days to make it and it is here for you ready to test and improve it .

This project is part of XR3Player java Media Player , so i will continue to improve it in every update :) 

Donwload latest prebuild zip folder V3.5.0 [here](https://github.com/goxr3plus/JavaFX-Web-Browser/releases/download/V3.5.0/JavaFXWebBrowser-3.5.0-bin.zip)

![javafxwebbrowser](https://user-images.githubusercontent.com/20374208/32561528-18323d74-c4b5-11e7-9552-f7aeba4f9728.jpg)

---

## --Maven Build--

### Maven Clean Package [ With Javadocs produced ]

```mvn clean package``` 

### Maven Clean Package [ No Javadocs produced ]

```mvn -Dmaven.javadoc.skip=true clean package``` 

---

