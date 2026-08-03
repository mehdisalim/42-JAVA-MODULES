For Compiling:
javac -cp "lib/*" -d target src/java/fr/fortytwo/printer/app/*.java src/java/fr/fortytwo/printer/logic/*.java


Then copy the image to target folder:
cp src/resources/image.bmp target/


jar xf lib/jcommander-3.0.jar -C target
jar xf lib/JCDP-2.0.3.1.jar -C target

For packaging:
jar cfm target/images-to-chars-printer.jar src/manifest.txt -C target .


For Running:

java -jar target/images-to-chars-printer.jar <white-color-character> <black-color-character>

ex: java -jar target/images-to-chars-printer.jar . 0

java -jar target/images-to-chars-printer.jar --white=RED --black=GREEN



