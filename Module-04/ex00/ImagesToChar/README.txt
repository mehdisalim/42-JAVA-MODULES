# Images to Characters - ex00

This project converts a monochrome BMP image into ASCII characters printed in the terminal.

## Project Structure

```
.
├── src/
│   └── java/
│       └── fr/
│           └── fortytwo/
│               └── printer/
│                   ├── app/
│                   └── logic/
├── target/
└── README.md
```

## Compilation

Compile all Java source files into the `target` directory:

```bash
javac -d target src/java/fr/fortytwo/printer/app/*.java src/java/fr/fortytwo/printer/logic/*.java
```

## Usage

Run the program using:

```bash
java -cp target fr.fortytwo.printer.app.Program <white-character> <black-character> <image-path>
```

### Arguments

| Argument | Description |
|----------|-------------|
| `<white-character>` | Character used to represent white pixels |
| `<black-character>` | Character used to represent black pixels |
| `<image-path>` | Path to the BMP image |

## Example

```bash
java -cp target fr.fortytwo.printer.app.Program . 0 it.bmp
```

This command prints the image using:

- `.` for white pixels
- `0` for black pixels

## Notes

- The input image must be a monochrome BMP file.
- Compiled `.class` files are generated inside the `target` directory.