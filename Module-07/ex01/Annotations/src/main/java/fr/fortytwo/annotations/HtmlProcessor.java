package fr.fortytwo.annotations;

import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Set;

@SupportedAnnotationTypes("fr.fortytwo.annotations.HtmlForm")
@AutoService(Processor.class)
public class HtmlProcessor extends AbstractProcessor {

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(HtmlForm.class)) {
            HtmlForm htmlForm = element.getAnnotation(HtmlForm.class);
            String fileName = htmlForm.fileName();
            String action = htmlForm.action();
            String method = htmlForm.method();

            try {
                FileObject fileObject = processingEnv.getFiler().createResource(
                        StandardLocation.CLASS_OUTPUT, "", fileName);

                try (BufferedWriter writer = new BufferedWriter(fileObject.openWriter())) {
                    writer.write(String.format("<form action = \"%s\" method = \"%s\">\n", action, method));

                    for (Element enclosedElement : element.getEnclosedElements()) {
                        HtmlInput htmlInput = enclosedElement.getAnnotation(HtmlInput.class);
                        if (htmlInput != null) {
                            writer.write(String.format("<input type = \"%s\" name = \"%s\" placeholder = \"%s\">\n",
                                    htmlInput.type(), htmlInput.name(), htmlInput.placeholder()));
                        }
                    }
                    writer.write("<input type = \"submit\" value = \"Send\">\n");
                    writer.write("</form>\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
