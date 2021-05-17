package com.github.jenya705.data;

import java.awt.*;

public interface AdditionalConfigData {

    Color getColor(Object key);

    Color getColor(Object key, Color defaultValue);

    void setColor(Object key, Color color);


}
