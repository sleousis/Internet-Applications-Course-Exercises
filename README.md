# Internet Applications Course Exercises

Java lab exercises for an Internet Applications course at NTUA (National Technical University of Athens), written around 2018. There are three labs. Lab 1 is a small servlet web shop. Lab 2 parses XML with DOM and SAX and turns XML into HTML. Lab 3 is a SOAP web service built on Apache SOAP 2.3.1. The assignment handouts are included as PDFs.

## Contents

| Lab | Topic | What it does |
| --- | --- | --- |
| Lab 1 | Java servlets and cookies | A three-step order form (`form_A`, `form_B`, `form_C`). The user name is stored in a cookie and shown on the last page with a picture of the chosen item. |
| Lab 2 | XML with DOM, SAX and XSLT | `DOMNavigator` and `XMLEventsPresentor` print an XML tree level by level. `XSLTransformer` builds an HTML table of cars with SAX. `cars.xsl` does the same with XSLT. |
| Lab 3 | SOAP RPC with Apache SOAP | `BVCatalog` is a vehicle catalog service with `addV`, `getVehicleBean` and `listV`. `BVAdderLister` is a client that adds a vehicle and lists the catalog. Vehicles and manufacturers are sent as Java beans. |

## Tech stack

| Technology | Version used | Notes |
| --- | --- | --- |
| Java (Eclipse Temurin JDK) | 27 (27+35) | Latest release as of September 2026 |
| Apache Tomcat | 11.0.26 | Servlet 6.1, `jakarta.servlet` |
| JAXP (DOM, SAX) | built into the JDK | Lab 2 |
| Apache Xalan and serializer | 2.7.3 | Latest release. Optional XSLT step in Lab 2 |
| Apache SOAP | 2.3.1 | Last release (2002). Runs on Tomcat 11 through Tomcat's Java EE to Jakarta EE migration |
| JavaMail (`com.sun.mail:javax.mail`) | 1.6.2 | Latest release that keeps the `javax.mail` package that Apache SOAP needs |
| JavaBeans Activation (`com.sun.activation:jakarta.activation`) | 1.2.2 | Latest release that keeps the `javax.activation` package |

## Repository layout

```
Diadiktyo_Feb2012.pdf          Course material
Lab1/
  internet-applications-1.pdf  Assignment
  myAskisisDir/                Web application folder (index.html, style.css, images/, WEB-INF/)
    WEB-INF/classes/*.java     Servlets form_A, form_B, form_C
    WEB-INF/web.xml            Servlet mappings
  myAskisisDir.war             The packaged web application as submitted
Lab2/
  Internet___Applications_2.pdf  Assignment
  cars.xml, cars.xsl           Input and stylesheet for the XSLT step
  test.txt                     The Xalan command used originally
  JAVA DOM/                    DOMNavigator.java, generalXML.xml, outputDOM.txt (sample output)
  SAX PARSER/                  XMLEventsPresentor.java, generalXML.xml, outputSAX.txt (sample output)
  XSLTranformer/               XSLTransformer.java, cars.xml, cars2.html (sample output)
Lab3/
  Ask3_SOAP_2018.pdf, Internet___Applications_3.pdf  Assignment
  BVShop/*.java                Service class, beans and client (package BVShop)
  BVCatalogDD.xml              Apache SOAP deployment descriptor
  soap_request.txt             A captured SOAP request (TCP monitor output)
```

## Prerequisites

Download these into one folder (called `DEPS` below). These exact versions were tested:

- JDK 27 from [Adoptium](https://adoptium.net/temurin/releases/?version=27), for example `OpenJDK27U-jdk_x64_linux_hotspot_27_35.tar.gz` or `OpenJDK27U-jdk_x64_windows_hotspot_27_35.zip`
- [Apache Tomcat 11.0.26](https://tomcat.apache.org/download-11.cgi) (`.tar.gz` for Linux, `.zip` for Windows)
- For Lab 3:
  - `soap.jar` and `soap.war` from [soap-bin-2.3.1.zip](https://archive.apache.org/dist/ws/soap/version-2.3.1/soap-bin-2.3.1.zip)
  - [javax.mail-1.6.2.jar](https://repo1.maven.org/maven2/com/sun/mail/javax.mail/1.6.2/javax.mail-1.6.2.jar)
  - [jakarta.activation-1.2.2.jar](https://repo1.maven.org/maven2/com/sun/activation/jakarta.activation/1.2.2/jakarta.activation-1.2.2.jar)
- For the optional XSLT step, [xalan-2.7.3.jar](https://repo1.maven.org/maven2/xalan/xalan/2.7.3/xalan-2.7.3.jar) and [serializer-2.7.3.jar](https://repo1.maven.org/maven2/xalan/serializer/2.7.3/serializer-2.7.3.jar)

## Setup

Unpack the JDK, Tomcat and `soap-bin-2.3.1.zip`, and put the JDK `bin` folder on your `PATH`. The Linux commands below use these variables:

```sh
export JAVA_HOME=/path/to/jdk-27+35 PATH=/path/to/jdk-27+35/bin:$PATH
TOMCAT=/path/to/apache-tomcat-11.0.26
SOAP=$DEPS/soap-2_3_1
LIBS=$SOAP/lib/soap.jar:$DEPS/javax.mail-1.6.2.jar:$DEPS/jakarta.activation-1.2.2.jar
```

On Windows (PowerShell):

```powershell
$env:JAVA_HOME = "$DEPS\jdk-27+35"; $env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
$TOMCAT = "$DEPS\apache-tomcat-11.0.26"
$SOAP = "$DEPS\soap-2_3_1"
$LIBS = "$SOAP\lib\soap.jar;$DEPS\javax.mail-1.6.2.jar;$DEPS\jakarta.activation-1.2.2.jar"
```

## Build and run

The commands below were run on Linux (JDK 27 and Tomcat 11 under WSL Ubuntu 20.04). The same steps were also run on Windows 11 with PowerShell. On Windows use `;` as the classpath separator, backslashes in paths and `catalina.bat` instead of `catalina.sh`.

### Lab 1: servlets

Compile the servlets into the web application folder and copy it into Tomcat:

```sh
cp -r Lab1/myAskisisDir $TOMCAT/webapps/
javac -cp $TOMCAT/lib/servlet-api.jar -d $TOMCAT/webapps/myAskisisDir/WEB-INF/classes \
    Lab1/myAskisisDir/WEB-INF/classes/*.java
$TOMCAT/bin/catalina.sh start
```

Open http://localhost:8080/myAskisisDir/index.html in a browser. The same flow with curl:

```sh
curl -c cj -d "username=Savvas&product_service=products" http://localhost:8080/myAskisisDir/form_A
curl -b cj -d "form_A=toys" http://localhost:8080/myAskisisDir/form_B
curl -b cj --data-urlencode "form_B=a Doll" http://localhost:8080/myAskisisDir/form_C
```

On Windows start Tomcat with `& "$TOMCAT\bin\catalina.bat" run` in its own window and use `curl.exe` with the same arguments.

The last page says "Transaction Complete!", "Dear Savvas," and "You have selected a Doll." and shows `images/a Doll.png`.

### Lab 2: DOM, SAX and XSLT

Each program writes its output into the current folder (`output.txt` or `cars.html`).

```sh
cd "Lab2/JAVA DOM"
javac -d build src/DOMNavigator.java
java -cp build DOMNavigator generalXML.xml          # writes output.txt

cd "../SAX PARSER"
javac -d build src/XMLEventsPresentor.java
java -cp build XMLEventsPresentor generalXML.xml    # writes output.txt

cd ../XSLTranformer
javac -d build src/XSLTransformer.java
java -cp build XSLTransformer cars.xml              # writes cars.html
```

The output of each program is identical to the committed samples `outputDOM.txt`, `outputSAX.txt` and `cars2.html`. The generated `output.txt` and `build/` folders are ignored by git.

The XSLT version (from `Lab2/test.txt`) with Xalan:

```sh
cd Lab2
java -cp xalan-2.7.3.jar:serializer-2.7.3.jar org.apache.xalan.xslt.Process \
    -IN cars.xml -XSL cars.xsl -OUT cars.html
```

You can also open `Lab2/cars.xml` in a browser that applies the linked stylesheet.

### Lab 3: SOAP service

Apache SOAP was written for `javax.servlet`. Tomcat 11 only has `jakarta.servlet`, but it converts old web applications on the fly. A `.war` placed in `webapps-javaee` is migrated with the Apache Tomcat Migration Tool for Jakarta EE and deployed to `webapps`. So build a `soap.war` that holds the Apache SOAP web application, its jars and the service classes, and drop it there:

```sh
W=/tmp/soapwar
mkdir -p $W && (cd $W && unzip -q $SOAP/webapps/soap.war)
mkdir -p $W/WEB-INF/lib $W/WEB-INF/classes
cp $SOAP/lib/soap.jar $DEPS/javax.mail-1.6.2.jar $DEPS/jakarta.activation-1.2.2.jar $W/WEB-INF/lib/
cd Lab3
javac -cp $LIBS -d $W/WEB-INF/classes BVShop/*.java
mkdir -p $TOMCAT/webapps-javaee && jar cf $TOMCAT/webapps-javaee/soap.war -C $W .
$TOMCAT/bin/catalina.sh start
```

The Tomcat log shows `Migration completed successfully` and then deploys `/soap`. The client side runs the original unmigrated `soap.jar`.

Deploy the service and run the client:

```sh
URL=http://localhost:8080/soap/servlet/rpcrouter
java -cp $LIBS org.apache.soap.server.ServiceManagerClient $URL deploy BVCatalogDD.xml
java -cp $LIBS org.apache.soap.server.ServiceManagerClient $URL list
java -cp $LIBS:$W/WEB-INF/classes BVShop.BVAdderLister $URL Avensis Toyota 1937 Japan 2008
```

Output of the client:

```
Adding vehicle model 'Avensis' by Toyota
Server reported NO FAULT while adding vehicle
  'Avensis' by Toyota(Japan 1937), year 2008
  'Buick' by General Motors(USA 1908), year 1948
  'Jeep' by General Motors(USA 1908), year 1942
  '4CV' by Citroen(France 1919), year 1950
  'Mustang' by Ford(USA 1903), year 1960
  'Beatle' by Volkswagen(Germany 1937), year 1938
```

The Tomcat log shows `Addition at server side: ...` for each vehicle added.

## Notes and known limitations

- Compiled classes and IntelliJ project files are no longer in the repository. Build from the `.java` sources as shown above.
- Changes made to run on current versions:
  - `ManBean.getManYear()` now returns `Integer` instead of `String`. The old code did not compile because the field is an `Integer`. The SOAP bean serializer also needs the getter and setter types to match.
  - The Lab 1 servlets import `jakarta.servlet` instead of `javax.servlet`. `web.xml` uses the Jakarta EE `web-app_6_1.xsd` schema instead of the Servlet 2.2 DTD.
- `Lab1/myAskisisDir.war` is the web application exactly as it was submitted. It still uses `javax.servlet` and old class files, so it does not run on Tomcat 11 as is. Build the folder version as shown above instead.
- Apache SOAP 2.3.1 is dead and has no newer release. It is kept because the lab is about it. It runs on Tomcat 11 only through the automatic migration described above. It needs the `javax.mail` and `javax.activation` packages, so the newest releases in those packages are used rather than Jakarta Mail 2.x.
- Compiling Lab 3 prints deprecation and unchecked warnings. They are harmless.
- Lab 2 writes lines with `\n` inside the text, so on Windows `output.txt` has mixed line endings. The text itself matches the samples.
- `form_C` fails with an error if the request has no cookies at all. Go through `form_A` first so the `username` cookie is set.
- The pages load the Ubuntu font from Google Fonts and one background image from tinypic.com, which no longer exists.

## Author

Savvas Leousis ([sleousis](https://github.com/sleousis))
