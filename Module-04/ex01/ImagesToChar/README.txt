For Compiling:
javac -d target src/java/fr/fortytwo/printer/app/*.java src/java/fr/fortytwo/printer/logic/*.java


Then copy the image to target folder:
cp -d src/resources/image.bmp target/

For packaging:
jar cfm target/images-to-chars-printer.jar src/manifest.txt -C target .


For Running:

java -jar target/images-to-chars-printer.jar <white-color-character> <black-color-character>

ex: java -jar target/images-to-chars-printer.jar . 0