package fr.fortytwo.spring;

public class PrinterWithPrefixImpl implements Printer {

    private String prefix;

    final private Renderer renderer;

    public PrinterWithPrefixImpl(final Renderer renderer) {
        this.renderer = renderer;
    }

    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void print(final String message) {
        renderer.render(this.prefix + " " + message);
    }

}
