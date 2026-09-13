# Exercise 01: Annotations - SOURCE

## Objective
The goal of this exercise is to create a custom Java annotation processor that reads metadata from specific annotations (`@HtmlForm` and `@HtmlInput`) during compilation and generates an HTML form file based on that metadata.

## How it works

To achieve this, the following components were implemented:

### 1. Annotations
We created two custom annotations with `@Retention(RetentionPolicy.SOURCE)` because they are only needed during compilation to generate the HTML file and should not be available at runtime.
- `@HtmlForm`: Applied to classes (`@Target(ElementType.TYPE)`). Contains metadata for the form such as `fileName`, `action`, and `method`.
- `@HtmlInput`: Applied to fields (`@Target(ElementType.FIELD)`). Contains metadata for the input fields such as `type`, `name`, and `placeholder`.

### 2. Annotation Processor
We created the `HtmlProcessor` class extending `javax.annotation.processing.AbstractProcessor`.
- It is annotated with `@AutoService(Processor.class)` from the Google AutoService library. This automatically generates the `META-INF/services/javax.annotation.processing.Processor` file so that the Java compiler can discover and run our processor.
- In the `process` method, the processor iterates over all elements annotated with `@HtmlForm`.
- It retrieves the metadata (action, method, file name) from the class.
- It then iterates over the class's enclosed elements (fields) and checks for the `@HtmlInput` annotation, extracting the input field metadata to generate `<input>` tags.
- Using `processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", fileName)`, the processor writes the constructed HTML string into the `target/classes` directory as instructed.

### 3. Maven Configuration
Since the annotation processor and the test class (`UserForm`) are in the same Maven module, the compilation needs to be carefully orchestrated. An annotation processor cannot process classes if it hasn't been compiled itself yet. 
To solve this, the `maven-compiler-plugin` in `pom.xml` is configured with two executions:
1. `default-compile`: First pass. This compiles the annotations and the `HtmlProcessor`. The `AutoService` processor is allowed to run here to register our processor, but our `HtmlProcessor` is NOT yet used to process anything.
2. `compile-userform`: Second pass. This execution explicitly specifies our compiled `HtmlProcessor` in its `<annotationProcessors>` configuration and compiles the `UserForm` test class. This triggers our processor to read the `UserForm` metadata and generate `user_form.html`.

## Usage
Running the standard command:
```bash
mvn clean compile
```
Will trigger the annotation processor and generate the `user_form.html` file inside the `target/classes` folder.
