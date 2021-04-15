package com.github.jenya705.data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class MultiDataFactory implements DataFactory {

    @Override
    public SerializedData createData() {
        return new YamlData();
    }

    @Override
    public SerializedData createData(DataType dataType) {
        if (DataType.YAML == dataType) {
            return new YamlData();
        }

        throw new IllegalArgumentException(
                String.format("DataType %s is not supported", dataType));

    }

    @Override
    public SerializedData createData(File file) throws IOException {
        if (DataType.YAML.isType(file.getName())){
            return createData(file, DataType.YAML);
        }

        throw new IllegalArgumentException(String.format(
                "Format of file %s is not supported", file.getName()));

    }

    @Override
    public SerializedData createData(File file, DataType dataType) throws IOException {
        SerializedData result = createData(dataType);
        String content = new String(Files.readAllBytes(file.toPath()));
        result.initialize(content);
        return result;
    }

    @Override
    public SerializedData createData(String content) {
        SerializedData data = new YamlData();
        data.initialize(content);
        return data;
    }

    @Override
    public SerializedData createData(String content, DataType dataType) {
        SerializedData result = createData(dataType);
        result.initialize(content);
        return result;
    }
}
