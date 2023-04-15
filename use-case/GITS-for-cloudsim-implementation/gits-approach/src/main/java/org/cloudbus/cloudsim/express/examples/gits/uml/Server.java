
package org.cloudbus.cloudsim.express.examples.gits.uml;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Server {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("Resources")
    @Expose
    private List<String> resources;
    @SerializedName("Server_count")
    @Expose
    private Integer serverCount;
    @SerializedName("HostingShelf")
    @Expose
    private String hostingShelf;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getResources() {
        return resources;
    }

    public void setResources(List<String> resources) {
        this.resources = resources;
    }

    public Integer getServerCount() {
        return serverCount;
    }

    public void setServerCount(Integer serverCount) {
        this.serverCount = serverCount;
    }

    public String getHostingShelf() {
        return hostingShelf;
    }

    public void setHostingShelf(String hostingShelf) {
        this.hostingShelf = hostingShelf;
    }

}
