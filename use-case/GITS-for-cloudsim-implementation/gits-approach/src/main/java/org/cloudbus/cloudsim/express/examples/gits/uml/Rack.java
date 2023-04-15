
package org.cloudbus.cloudsim.express.examples.gits.uml;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Rack {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("HostingDC")
    @Expose
    private String hostingDC;
    @SerializedName("Rack_count")
    @Expose
    private Integer rackCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHostingDC() {
        return hostingDC;
    }

    public void setHostingDC(String hostingDC) {
        this.hostingDC = hostingDC;
    }

    public Integer getRackCount() {
        return rackCount;
    }

    public void setRackCount(Integer rackCount) {
        this.rackCount = rackCount;
    }

}
