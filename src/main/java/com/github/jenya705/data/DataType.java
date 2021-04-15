package com.github.jenya705.data;

import lombok.Getter;

@Getter
public enum DataType {

    YAML(new String[]{"yaml", "yml"});

    private final String[] fileTypes;
    DataType(String[] fileTypes){
        this.fileTypes = fileTypes;
    }
    public boolean isType(String fileName){
        for (String fileType: fileTypes){
            if (fileName.endsWith("." + fileType)) return true;
        }
        return false;
    }

}
