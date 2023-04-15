package org.cloudbus.cloudsim.express.examples.gits.library;

import com.google.gson.Gson;
import org.cloudbus.cloudsim.express.examples.gits.uml.Uml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class GITSLibrary {

    public Uml getTransformedJSONInformation(File jsonTemplate) throws FileNotFoundException {

        Gson jsonParser = new Gson();
        return jsonParser.fromJson(new FileReader(jsonTemplate), Uml.class);
    }
}
