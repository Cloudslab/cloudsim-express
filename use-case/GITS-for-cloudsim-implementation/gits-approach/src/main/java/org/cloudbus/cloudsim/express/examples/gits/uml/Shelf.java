
package org.cloudbus.cloudsim.express.examples.gits.uml;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Shelf {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("HostingRack")
    @Expose
    private String hostingRack;
    @SerializedName("Shelf_count")
    @Expose
    private Integer shelfCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHostingRack() {
        return hostingRack;
    }

    public void setHostingRack(String hostingRack) {
        this.hostingRack = hostingRack;
    }

    public Integer getShelfCount() {
        return shelfCount;
    }

    public void setShelfCount(Integer shelfCount) {
        this.shelfCount = shelfCount;
    }

}
