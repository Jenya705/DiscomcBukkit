package com.github.jenya705;

import com.github.jenya705.data.types.PrimitiveValueType;
import org.junit.Test;

import static org.junit.Assert.*;

public class ValueTypeTest {

    @Test
    public void primitiveIntegerTestSuccess() {
        assertEquals(0, PrimitiveValueType.INTEGER.createInstance("0"));
    }

    @Test
    public void primitiveIntegerTestFailed() {
        assertFalse(PrimitiveValueType.INTEGER.canBeCreated("!313131"));
    }

    @Test
    public void primitiveIntegerTestCanBeCreated() {
        assertTrue(PrimitiveValueType.INTEGER.canBeCreated("523"));
    }

    @Test
    public void primitiveBooleanTest() {
        assertTrue((Boolean) PrimitiveValueType.BOOLEAN.createInstance("true"));
        assertTrue((Boolean) PrimitiveValueType.BOOLEAN.createInstance("TRUE"));
        assertFalse((Boolean) PrimitiveValueType.BOOLEAN.createInstance("fAlSe"));
        assertFalse((Boolean) PrimitiveValueType.BOOLEAN.createInstance("false"));
        assertFalse(PrimitiveValueType.BOOLEAN.canBeCreated("notFalse"));
        assertTrue(PrimitiveValueType.BOOLEAN.canBeCreated("True"));
    }

}
