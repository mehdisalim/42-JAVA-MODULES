#!/bin/bash
rm -rf target
javac -cp "lib/*" -d target src/java/fr/fortytwo/printer/app/*.java src/java/fr/fortytwo/printer/logic/*.java
cp src/resources/image.bmp target/

cd target
jar -xf ../lib/jcommander-3.0.jar
jar -xf ../lib/JCDP-2.0.3.1.jar
cd ..

jar cfm target/images-to-chars-printer.jar src/manifest.txt -C target .

java -jar target/images-to-chars-printer.jar --white RED --black GREEN
