package fr.fortytwo.spring.impl;

import fr.fortytwo.spring.PreProcessor;
import fr.fortytwo.spring.Renderer;

public class RendererErrImpl implements Renderer{


    final private PreProcessor preProcessor;

    public RendererErrImpl(final PreProcessor preProcessor) {
        this.preProcessor =  preProcessor;
    }

    @Override
    public void render(String message) {
        final String processedString = preProcessor.process(message);
        System.err.println(processedString);
    }

}
