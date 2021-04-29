package com.github.jenya705;

import com.github.jenya705.command.parser.argument.VanillaCommandArgument;
import com.github.jenya705.command.parser.data.StringCommandData;
import com.github.jenya705.command.parser.data.StringCommandParseResult;
import com.github.jenya705.command.parser.scheme.VanillaCommandDataScheme;
import com.github.jenya705.data.types.PrimitiveValueType;
import com.github.jenya705.util.Pair;
import lombok.SneakyThrows;
import org.apache.commons.lang.math.NumberUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public class StringCommandDataTest {

    private final StringCommandData testData = new StringCommandData(
            new VanillaCommandDataScheme(
                    new Pair<>("name", new VanillaCommandArgument(PrimitiveValueType.STRING, true)),
                    new Pair<>("id", new VanillaCommandArgument(PrimitiveValueType.LONG, true)),
                    new Pair<>("banned", new VanillaCommandArgument(PrimitiveValueType.BOOLEAN, true)),
                    new Pair<>("time", new VanillaCommandArgument(PrimitiveValueType.LONG, false, 10L))
            )
    );

    public void clear() {
        testData.getData().clear();
    }

    @Test
    public void successTestDefaultSetArgs() {

        String[] commandData = new String[] {
                "name:Jenya705",
                "id:321321",
                "banned:false"
        };

        clear();

        StringCommandParseResult result = testData.initialize(commandData);

        assertEquals(StringCommandParseResult.SUCCESS, result);
        assertEquals("Jenya705", testData.getString("name", null));
        assertEquals(321321L, testData.getLong("id", 0L).longValue());
        assertEquals(false, testData.getBoolean("banned", true));
        assertEquals(10L, testData.getLong("time", 20L).longValue());

    }

    @Test
    public void successTestDefaultNoArgs() {
        String[] commandData = new String[] {
                "Jenya705", "321321", "false"
        };

        clear();

        StringCommandParseResult result = testData.initialize(commandData);

        assertEquals(StringCommandParseResult.SUCCESS, result);
        assertEquals("Jenya705", testData.getString("name", null));
        assertEquals(321321L, testData.getLong("id", 0L).longValue());
        assertEquals(false, testData.getBoolean("banned", true));
        assertEquals(10L, testData.getLong("time", 20L).longValue());

    }

    @Test
    public void requiredArgumentNotGiven() {
        String[] commandData = new String[] {
                "Jenya705", "321321"
        };
        clear();
        StringCommandParseResult result = testData.initialize(commandData);
        assertEquals(StringCommandParseResult.REQUIRED_ARGUMENT_NOT_GIVEN, result);
    }

    @Test
    public void tooManyArguments() {
        String[] commandData = new String[] {
                "Jenya705", "321321", "false", "312321", "a"
        };
        clear();
        StringCommandParseResult result = testData.initialize(commandData);
        assertEquals(StringCommandParseResult.TOO_MANY_ARGUMENTS, result);
    }

    @Test
    public void givenArgumentNotExist() {
        String[] commandData = new String[] {
                "Jenya705",
                "id:321321",
                "banned:false",
                "quota:false"
        };
        clear();
        StringCommandParseResult result = testData.initialize(commandData);
        assertEquals(StringCommandParseResult.GIVEN_ARGUMENT_NOT_EXIST, result);
    }

    @Test
    public void argumentTypeNotRight() {
        String[] commandData = new String[] {
                "name:jenya705",
                "id:321321a",
                "banned:false"
        };
        clear();
        StringCommandParseResult result = testData.initialize(commandData);
        assertEquals(StringCommandParseResult.ARGUMENT_TYPE_NOT_RIGHT, result);
    }

}
