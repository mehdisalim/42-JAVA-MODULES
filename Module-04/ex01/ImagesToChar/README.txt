# Images to Characters - ex01

This project converts a monochrome BMP image into ASCII characters printed in the terminal.

Unlike **ex00**, the BMP image is packaged inside the executable JAR and loaded as a resource.

## Project Structure

```
.
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

Compile all Java source files into the `target` directory:

```bash
javac -d target src/java/fr/fortytwo/printer/app/*.java src/java/fr/fortytwo/printer/logic/*.java
```

## Copy Resources

Copy the resources into the `target` directory before creating the JAR:

```bash
cp -rf src/resources target/
```

## Package the Application

Create the executable JAR:

```bash
jar cfm target/images-to-chars-printer.jar src/manifest.txt -C target .
```

## Usage

Run the executable JAR:

```bash
java -jar target/images-to-chars-printer.jar <white-character> <black-character>
```

### Arguments

| Argument | Description |
|----------|-------------|
| `<white-character>` | Character used to represent white pixels |
| `<black-character>` | Character used to represent black pixels |

## Example

```bash
java -jar target/images-to-chars-printer.jar . 0
```

This command prints the embedded BMP image using:

- `.` for white pixels
- `0` for black pixels

## Notes

- The BMP image is embedded inside the JAR and loaded as a classpath resource.
- The executable JAR is generated in the `target` directory.
- The `manifest.txt` file specifies the application's entry point (`Main-Class`).