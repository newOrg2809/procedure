package dev.sanero.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScreenInformation {
    private String korName;
    private String code;
    private String mapperFile;
    private String event;
    private String method;
    private int numbOfMethod;
    private List<String> methodList;
    private String module;

    public ScreenInformation() {
        methodList = new ArrayList<>();
    }

    public ScreenInformation(String korName, String code, String mapperFile, String event, String method) {
        this.korName = korName;
        this.code = code;
        this.mapperFile = mapperFile;
        this.event = event;
        this.method = method;
        this.methodList = getMethodListFromMethods(method);
        this.numbOfMethod = this.methodList.size();
        this.module = mapperFile.substring(0, 2).toLowerCase();
    }

    public String getKorName() {
        return korName;
    }

    public void setKorName(String korName) {
        this.korName = korName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMapperFile() {
        return mapperFile;
    }

    public void setMapperFile(String mapperFile) {
        this.mapperFile = mapperFile;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getNumbOfMethod() {
        return numbOfMethod;
    }

    public void setNumbOfMethod(int numbOfMethod) {
        this.numbOfMethod = numbOfMethod;
    }

    public List<String> getMethodList() {
        return methodList;
    }

    public void setMethodList(List<String> methodList) {
        this.methodList = methodList;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    @Override
    public String toString() {
        return "ScreenInformation{" +
                "korName='" + korName + '\'' +
                ", code='" + code + '\'' +
                ", mapperFile='" + mapperFile + '\'' +
                ", event='" + event + '\'' +
                ", numbOfMethod=" + numbOfMethod +
                ", methodList=" + methodList +
                ", module='" + module + '\'' +
                '}';
    }

    private List<String> getMethodListFromMethods(String method) {
        return Arrays.asList(method.split("\n"));
    }
}
