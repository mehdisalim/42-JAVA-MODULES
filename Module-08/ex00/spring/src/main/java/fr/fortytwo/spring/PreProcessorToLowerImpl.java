package fr.fortytwo.spring.impl;

import fr.fortytwo.spring.PreProcessor;


public class PreProcessorToLowerImpl implements PreProcessor {

    @Override
    public String process(String message) {
        return message.toLowerCase();
    }

}
