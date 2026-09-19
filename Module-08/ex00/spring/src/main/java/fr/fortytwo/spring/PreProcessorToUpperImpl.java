package fr.fortytwo.spring.impl;

import fr.fortytwo.spring.PreProcessor;


public class PreProcessorToUpperImpl implements PreProcessor {

    @Override
    public String process(String message) {
        return message.toUpperCase();
    }

}
