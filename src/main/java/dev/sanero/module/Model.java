package dev.sanero.module;

import dev.sanero.util.StringUtils;

public class Model {
    private String name;
    private String lowerFirstCharName;
    private String lowerName;
    private String upperName;
    private String upperFirstCharName;

    public Model(String name) {
        this.name = name;
        lowerFirstCharName = StringUtils.lowerFirstChar(name);
        lowerName = name.toLowerCase();
        upperName = name.toUpperCase();
        upperFirstCharName = StringUtils.upperFirstChar(name);

    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLowerFirstCharName() {
        return lowerFirstCharName;
    }

    public void setLowerFirstCharName(String lowerFirstCharName) {
        this.lowerFirstCharName = lowerFirstCharName;
    }

    public String getLowerName() {
        return lowerName;
    }

    public void setLowerName(String lowerName) {
        this.lowerName = lowerName;
    }

    public String getUpperName() {
        return upperName;
    }

    public void setUpperName(String upperName) {
        this.upperName = upperName;
    }

    public String getUpperFirstCharName() {
        return upperFirstCharName;
    }

    public void setUpperFirstCharName(String uppperFirstCharName) {
        this.upperFirstCharName = uppperFirstCharName;
    }
}
