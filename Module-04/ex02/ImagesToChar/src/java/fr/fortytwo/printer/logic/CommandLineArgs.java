package fr.fortytwo.printer.logic;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(separators = "=")
public class CommandLineArgs {

    @Parameter(names = "--white", required = true)
    public String white;
    
    @Parameter(names = "--black", required = true)
    public String black;


    public void setWhite(final String white) {
        this.white = white;
    }


    public void setBlack(final String black) {
        this.black = black;
    }

    public String getWhite() {
        return this.white;
    }
    
    public String getBlack() {
        return this.black;
    }

}