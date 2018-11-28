# JAVAFX-WEB-Browser 
You always dreamed how to make a WebBrowser in JavaFX? Well then you are in the right place . It took me 2 days to make it and it is here for you ready to test and improve it .


[![Latest Version](https://img.shields.io/github/release/goxr3plus/JavaFX-Web-Browser.svg?style=flat-square)](https://github.com/goxr3plus/JavaFX-Web-Browser/releases)
[![GitHub contributors][contributors-image]][contributors-url]
[![HitCount](http://hits.dwyl.io/goxr3plus/JavaFX-Web-Browser.svg)](http://hits.dwyl.io/goxr3plus/JavaFX-Web-Browser)
[![Total Downloads](https://img.shields.io/github/downloads/goxr3plus/JavaFX-Web-Browser/total.svg)](https://github.com/goxr3plus/JavaFX-Web-Browser/releases)

[contributors-url]: https://github.com/goxr3plus/JavaFX-Web-Browser/graphs/contributors
[contributors-image]: https://img.shields.io/github/contributors/goxr3plus/JavaFX-Web-Browser.svg
[browser-jitpack-url]: https://jitpack.io/#goxr3plus/JavaFX-Web-Browser

> Java 8 and 9 releases [here](https://github.com/goxr3plus/JavaFX-Web-Browser/wiki/Java-8-and-9-Releases)

### You can use this browser inside your JavaFX Application 

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


