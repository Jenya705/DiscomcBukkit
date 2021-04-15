package com.github.jenya705.data;

import java.io.File;
import java.io.IOException;

public interface DataFactory {

    SerializedData createData();

    SerializedData createData(DataType dataType);

    SerializedData createData(File file) throws IOException;

    SerializedData createData(File file, DataType dataType) throws IOException;

    SerializedData createData(String content);

    SerializedData createData(String content, DataType dataType);

}
