package com.github.jenya705;

import com.github.jenya705.data.MinecraftConfigData;
import com.github.jenya705.data.SerializedData;

public interface DiscomcConfig {

    void load(SerializedData data);

    void save(SerializedData data);

}
