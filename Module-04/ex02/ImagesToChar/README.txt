# Images to Characters - ex02

This project converts a monochrome BMP image into colored output displayed in the terminal.

Unlike **ex01**, this version uses external libraries:

- **JCommander** for parsing command-line arguments.
- **JCDP** for printing colored text in the terminal.

## Project Structure

```
.
├── lib/
│   ├── jcommander-1.82.jar
│   └── JCDP-4.0.2.jar
├── src/
│   ├── java/
│   │   └── fr/
│   │       └── fortytwo/
│   │           └── printer/
│   │               ├── app/
│   │               └── logic/
│   ├── resources/
│   │   └── image.bmp
│   └── manifest.txt
├── target/
└── README.md
```

## Compilation

Compile the project and include the external libraries on the classpath:

```bash
javac -cp "lib/*" -d target src/java/fr/fortytwo/printer/app/*.java src/java/fr/fortytwo/printer/logic/*.java
```

## Copy Resources

Copy the application resources into the `target` directory:

```bash
cp -rf src/resources target/
```

## Extract External Libraries

Extract the required libraries into the `target` directory so they are included in the executable JAR:

```bash
cd target

jar xf ../lib/jcommander-1.82.jar
jar xf ../lib/JCDP-4.0.2.jar

cd ..
```

## Package the Application

Create the executable JAR:

```bash
jar cfm target/images-to-chars-printer.jar src/manifest.txt -C target .
```

## Usage

Run the application using named command-line parameters:

```bash
java -jar target/images-to-chars-printer.jar --white=<COLOR> --black=<COLOR>
```

### Parameters

| Parameter | Description |
|----------|-------------|
| `--white` | Color used for white pixels |
| `--black` | Color used for black pixels |

Supported colors depend on the JCDP library (for example: `BLACK`, `WHITE`, `RED`, `GREEN`, `BLUE`, `CYAN`, `MAGENTA`, `YELLOW`, ...).

## Example

```bash
java -jar target/images-to-chars-printer.jar --white=RED --black=GREEN
```

This command prints the embedded BMP image using:

- **Red** for white pixels.
- **Green** for black pixels.

## Notes

- The BMP image is embedded inside the executable JAR and loaded as a classpath resource.
- **JCommander** is used to parse the `--white` and `--black` command-line arguments.
- **JCDP** is used to render colored output in the terminal.
- The executable JAR is generated in the `target` directory.
- The `manifest.txt` file specifies the application's entry point (`Main-Class`).