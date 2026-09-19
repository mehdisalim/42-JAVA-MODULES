package fr.fortytwo.spring;

import java.time.LocalDateTime;

public class PrinterWithDateTimeImpl implements Printer {
    final private Renderer renderer;

    public PrinterWithDateTimeImpl(final Renderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void print(final String message) {
        renderer.render(LocalDateTime.now() + ": " + message);
    }

}
