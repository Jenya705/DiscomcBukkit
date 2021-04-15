import com.github.jenya705.data.DataFactory;
import com.github.jenya705.data.DataType;
import com.github.jenya705.data.MultiDataFactory;
import com.github.jenya705.data.SerializedData;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class YamlDataTest {

    @Test
    public void test(){

        DataFactory dataFactory = new MultiDataFactory();
        SerializedData data = dataFactory.createData("name: Jenya705\n" +
                "number: 705\n" +
                "numberFloat: 705.0\n" +
                "numbers: [1, 2, 3, 4]\n" +
                "directory:\n" +
                "    name: Directory", DataType.YAML);

        assertEquals("Jenya705", data.getString("name"));
        assertEquals(705, (long) data.getInteger("number"));
        assertEquals(705.0f, data.getFloat("numberFloat"), 0);
        assertEquals(Arrays.asList(1, 2, 3, 4), data.getList("numbers"));
        assertEquals("Directory", data.getDirectory("directory").getString("name"));

    }

}
