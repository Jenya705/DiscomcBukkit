package com.github.jenya705.command;

import com.github.jenya705.DiscomcConfig;
import com.github.jenya705.data.MinecraftConfigData;
import com.github.jenya705.data.SerializedData;
import lombok.Data;

@Data
public class CommandConfig implements DiscomcConfig {

    private String argumentTypeNotRight = "&c&lVERDICT:&r Argument type not right. Argument name: &a{0}&r, your value: &c{1}&r, expected type: &e{2}&r";
    private String givenArgumentNotExist = "&c&lVERDICT:&r Given argument not exist. Argument name: &a{0}&r";
    private String requiredArgumentNotGiven = "&c&lVERDICT:&r Required argument not given. Argument name: &a{0}&r, argument type: &e{1}&r";
    private String tooManyArguments = "&c&lVERDICT:&r Too many arguments. Arguments count: &c{0}&r, expected arguments count: &e{1}&r";

    @Override
    public void load(SerializedData data) {
        if (data instanceof MinecraftConfigData) {
            MinecraftConfigData minecraftConfigData = (MinecraftConfigData) data;
            setArgumentTypeNotRight(minecraftConfigData.getMessage("argumentTypeNotRight", getArgumentTypeNotRight(), true));
            setGivenArgumentNotExist(minecraftConfigData.getMessage("givenArgumentNotExist", getGivenArgumentNotExist(), true));
            setRequiredArgumentNotGiven(minecraftConfigData.getMessage("requiredArgumentNotGiven", getRequiredArgumentNotGiven(), true));
            setTooManyArguments(minecraftConfigData.getMessage("tooManyArguments", getTooManyArguments(), true));
        }
    }

    @Override
    public void save(SerializedData data) {
        if (data instanceof MinecraftConfigData) {
            MinecraftConfigData minecraftConfigData = (MinecraftConfigData) data;
            minecraftConfigData.setMessage("argumentTypeNotRight", getArgumentTypeNotRight(), true);
            minecraftConfigData.setMessage("givenArgumentNotExist", getGivenArgumentNotExist(), true);
            minecraftConfigData.setMessage("requiredArgumentNotGiven", getRequiredArgumentNotGiven(), true);
            minecraftConfigData.setMessage("tooManyArguments", getTooManyArguments(), true);
        }
    }
}
