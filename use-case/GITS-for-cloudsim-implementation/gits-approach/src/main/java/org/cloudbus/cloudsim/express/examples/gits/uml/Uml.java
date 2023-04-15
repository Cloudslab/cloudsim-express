
package org.cloudbus.cloudsim.express.examples.gits.uml;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Uml {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("Objective")
    @Expose
    private Objective objective;
    @SerializedName("DC")
    @Expose
    private Dc dc;
    @SerializedName("Rack")
    @Expose
    private Rack rack;
    @SerializedName("Shelf")
    @Expose
    private Shelf shelf;
    @SerializedName("Server")
    @Expose
    private Server server;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Objective getObjective() {
        return objective;
    }

    public void setObjective(Objective objective) {
        this.objective = objective;
    }

    public Dc getDc() {
        return dc;
    }

    public void setDc(Dc dc) {
        this.dc = dc;
    }

    public Rack getRack() {
        return rack;
    }

    public void setRack(Rack rack) {
        this.rack = rack;
    }

    public Shelf getShelf() {
        return shelf;
    }

    public void setShelf(Shelf shelf) {
        this.shelf = shelf;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

}
