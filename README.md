> PS: Currently i am focused on developing another browser based on Chromium , so i am not actively supporting this Browser anymore except if Oracle decides to improve the JavaFX Browser engine because it is lacking a lot of things .

# JAVAFX-WEB-Browser 

[![Latest Version](https://img.shields.io/github/release/goxr3plus/JavaFX-Web-Browser.svg?style=flat-square)](https://github.com/goxr3plus/JavaFX-Web-Browser/releases)
[![GitHub contributors][contributors-image]][contributors-url]
[![HitCount](http://hits.dwyl.io/goxr3plus/JavaFX-Web-Browser.svg)](http://hits.dwyl.io/goxr3plus/JavaFX-Web-Browser)
[![Total Downloads](https://img.shields.io/github/downloads/goxr3plus/JavaFX-Web-Browser/total.svg)](https://github.com/goxr3plus/JavaFX-Web-Browser/releases)

[contributors-url]: https://github.com/goxr3plus/JavaFX-Web-Browser/graphs/contributors
[contributors-image]: https://img.shields.io/github/contributors/goxr3plus/JavaFX-Web-Browser.svg
[browser-jitpack-url]: https://jitpack.io/#goxr3plus/JavaFX-Web-Browser

>[Java 8 Pre-build runnable jar file](https://github.com/goxr3plus/JavaFX-Web-Browser/releases/download/V3.5.1/JavaFXWebBrowser-3.5.1-bin.zip) 

>[Java 9 Pre-build runnable jar file](https://github.com/goxr3plus/JavaFX-Web-Browser/releases/download/V9.1.0/JavaFXWebBrowser-9.1.0-bin.zip) 

### Keep the bro alive :)

<a href="https://patreon.com/preview/8adae1b75d654b2899e04a9e1111f0eb" title="Donate to this project using Patreon"><img src="https://img.shields.io/badge/patreon-donate-yellow.svg" alt="Patreon donate button" /></a>
<a href="https://www.paypal.me/GOXR3PLUSCOMPANY" title="Donate to this project using Paypal"><img src="https://img.shields.io/badge/paypal-donate-yellow.svg" alt="PayPal donate button" /></a>


### Add it to your project using JitPack :

[Take me a trip to JitPack][browser-jitpack-url]

### Step 1. Add the JitPack repository to your build file
``` XML
<repositories>
	<repository>
	   <id>jitpack.io</id>
	   <url>https://jitpack.io</url>
        </repository>
</repositories>
```

### Step 1. Add it as a dependency

* JavaFX-Web-Browser for Java 9 - (9.x.x)

``` XML
<dependency>
	 <groupId>com.github.goxr3plus</groupId>
	 <artifactId>JavaFX-Web-Browser</artifactId>
	 <version>V9.1.0</version>
</dependency>
```

* JavaFX-Web-Browser for Java 8 - (3.x.x)

``` XML
<dependency>
	 <groupId>com.github.goxr3plus</groupId>
	 <artifactId>JavaFX-Web-Browser</artifactId>
	 <version>V3.5.1</version>
</dependency>
```

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

![javafxwebbrowser](https://user-images.githubusercontent.com/20374208/32561528-18323d74-c4b5-11e7-9552-f7aeba4f9728.jpg)

---

## --Maven Build--

### Maven Clean Package [ With Javadocs produced ]

```mvn clean package``` 

### Maven Clean Package [ No Javadocs produced ]

```mvn -Dmaven.javadoc.skip=true clean package``` 

---

