
package org.cloudbus.cloudsim.express.examples.gits.uml;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Dc {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("DC_count")
    @Expose
    private Integer dCCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDCCount() {
        return dCCount;
    }

    public void setDCCount(Integer dCCount) {
        this.dCCount = dCCount;
    }

}
