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
    private String optionNotExist = "&c&lVERDICT:&r Option {0} is not exist, maybe it some of this options: {1}";
    private String optionNotEnded = "Available options: {0}";
    private boolean slashCommands = true;

    @Override
    public void load(SerializedData data) {
        if (data instanceof MinecraftConfigData) {
            MinecraftConfigData minecraftConfigData = (MinecraftConfigData) data;
            setArgumentTypeNotRight(minecraftConfigData.getMessage("argumentTypeNotRight", getArgumentTypeNotRight(), false));
            setGivenArgumentNotExist(minecraftConfigData.getMessage("givenArgumentNotExist", getGivenArgumentNotExist(), false));
            setRequiredArgumentNotGiven(minecraftConfigData.getMessage("requiredArgumentNotGiven", getRequiredArgumentNotGiven(), false));
            setTooManyArguments(minecraftConfigData.getMessage("tooManyArguments", getTooManyArguments(), false));
            setOptionNotExist(minecraftConfigData.getMessage("optionNotExist", getOptionNotExist(), false));
            setOptionNotEnded(minecraftConfigData.getMessage("optionNotEnded", getOptionNotEnded(), false));
        }
        setSlashCommands(data.getBoolean("slashCommands", isSlashCommands()));
    }

    @Override
    public void save(SerializedData data) {
        data.setObject("slashCommands", isSlashCommands());
        if (data instanceof MinecraftConfigData) {
            MinecraftConfigData minecraftConfigData = (MinecraftConfigData) data;
            minecraftConfigData.setMessage("argumentTypeNotRight", getArgumentTypeNotRight(), false);
            minecraftConfigData.setMessage("givenArgumentNotExist", getGivenArgumentNotExist(), false);
            minecraftConfigData.setMessage("requiredArgumentNotGiven", getRequiredArgumentNotGiven(), false);
            minecraftConfigData.setMessage("tooManyArguments", getTooManyArguments(), false);
            minecraftConfigData.setMessage("optionNotExist", getOptionNotExist(), false);
            minecraftConfigData.setMessage("optionNotEnded", getOptionNotEnded(), false);
        }
    }
}
