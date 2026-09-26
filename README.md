# Internet Applications Course Exercises

Java lab exercises for an Internet Applications course at NTUA (National Technical University of Athens), written around 2018. There are three labs. Lab 1 is a small servlet web shop. Lab 2 parses XML with DOM and SAX and turns XML into HTML. Lab 3 is a SOAP web service built on Apache SOAP 2.3.1. The assignment handouts are included as PDFs.

## Contents

| Lab | Topic | What it does |
| --- | --- | --- |
| Lab 1 | Java servlets and cookies | A three-step order form (`form_A`, `form_B`, `form_C`). The user name is stored in a cookie and shown on the last page with a picture of the chosen item. |
| Lab 2 | XML with DOM, SAX and XSLT | `DOMNavigator` and `XMLEventsPresentor` print an XML tree level by level. `XSLTransformer` builds an HTML table of cars with SAX. `cars.xsl` does the same with XSLT. |
| Lab 3 | SOAP RPC with Apache SOAP | `BVCatalog` is a vehicle catalog service with `addV`, `getVehicleBean` and `listV`. `BVAdderLister` is a client that adds a vehicle and lists the catalog. Vehicles and manufacturers are sent as Java beans. |

## Tech stack

- Java (tested with OpenJDK 17)
- Servlet API (`javax.servlet`) on Apache Tomcat 9
- JAXP (DOM and SAX parsers from the JDK)
- Apache Xalan 2.7.3 for the XSLT step (optional)
- Apache SOAP 2.3.1 with JavaMail 1.4.7 and JavaBeans Activation Framework 1.1.1 for Lab 3

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

- A JDK (8 or newer). Verified with OpenJDK 17 on Ubuntu 20.04 under WSL.
- Apache Tomcat 9 for Labs 1 and 3. Tomcat 10 and newer use `jakarta.servlet` and will not run this code.
- For Lab 3, these jars:
  - `soap.jar` and `soap.war` from [soap-bin-2.3.1.zip](https://archive.apache.org/dist/ws/soap/version-2.3.1/soap-bin-2.3.1.zip)
  - [mail-1.4.7.jar](https://repo1.maven.org/maven2/javax/mail/mail/1.4.7/mail-1.4.7.jar)
  - [activation-1.1.1.jar](https://repo1.maven.org/maven2/javax/activation/activation/1.1.1/activation-1.1.1.jar)
- For the optional XSLT step, [xalan-2.7.3.jar](https://repo1.maven.org/maven2/xalan/xalan/2.7.3/xalan-2.7.3.jar) and [serializer-2.7.3.jar](https://repo1.maven.org/maven2/xalan/serializer/2.7.3/serializer-2.7.3.jar).

## Setup

On Ubuntu:

```sh
sudo apt-get install -y openjdk-17-jdk-headless unzip
```

Download Tomcat 9 and the jars listed above into one folder. The commands below use these variables:

```sh
TOMCAT=/path/to/apache-tomcat-9.0.98
SOAP=/path/to/soap-2_3_1          # unpacked soap-bin-2.3.1.zip
LIBS=$SOAP/lib/soap.jar:/path/to/mail-1.4.7.jar:/path/to/activation-1.1.1.jar
```

## Build and run

All commands below were run on Linux (WSL). On Windows the same steps work with a JDK and Tomcat 9, but use `;` instead of `:` as the classpath separator and `catalina.bat` instead of `catalina.sh`. The Windows variants were not tested.

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

Install the Apache SOAP web application in Tomcat, add the service classes to it and start Tomcat:

```sh
cp mail-1.4.7.jar activation-1.1.1.jar $TOMCAT/lib/
mkdir -p $TOMCAT/webapps/soap && (cd $TOMCAT/webapps/soap && unzip -q $SOAP/webapps/soap.war)
mkdir -p $TOMCAT/webapps/soap/WEB-INF/lib $TOMCAT/webapps/soap/WEB-INF/classes
cp $SOAP/lib/soap.jar $TOMCAT/webapps/soap/WEB-INF/lib/
cd Lab3
javac -cp $LIBS -d $TOMCAT/webapps/soap/WEB-INF/classes BVShop/*.java
$TOMCAT/bin/catalina.sh start
```

Deploy the service and run the client:

```sh
URL=http://localhost:8080/soap/servlet/rpcrouter
java -cp $LIBS org.apache.soap.server.ServiceManagerClient $URL deploy BVCatalogDD.xml
java -cp $LIBS org.apache.soap.server.ServiceManagerClient $URL list
java -cp $LIBS:$TOMCAT/webapps/soap/WEB-INF/classes BVShop.BVAdderLister $URL Avensis Toyota 1937 Japan 2008
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
- `Lab1/myAskisisDir.war` is the web application as it was submitted. Its classes were built with an old JDK and it is kept as is.
- `ManBean.getManYear()` now returns `Integer` instead of `String`. The old code did not compile because the field is an `Integer`. The SOAP bean serializer also needs the getter and setter types to match.
- `form_C` fails with an error if the request has no cookies at all. Go through `form_A` first so the `username` cookie is set.
- The pages load the Ubuntu font from Google Fonts and one background image from tinypic.com, which no longer exists.
- Apache SOAP 2.3.1 is long obsolete. It still runs on Tomcat 9 and Java 17.

## Author

Savvas Leousis ([sleousis](https://github.com/sleousis))
