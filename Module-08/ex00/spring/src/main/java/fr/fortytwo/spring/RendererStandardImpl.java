package fr.fortytwo.spring;

public class RendererStandardImpl implements Renderer {

    final private PreProcessor preProcessor;

    public RendererStandardImpl(final PreProcessor preProcessor) {
        this.preProcessor = preProcessor;
    }

    @Override
    public void render(String message) {
        final String processedString = preProcessor.process(message);
        System.out.println(processedString);
    }

}
