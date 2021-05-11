package com.github.jenya705.command.parser;

import com.github.jenya705.Discomc;
import com.github.jenya705.data.types.ValueType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.text.MessageFormat;

@Getter
@AllArgsConstructor
public class CommandDataInitializeResult {

    private final CommandDataParseResult type;
    private final String message;

    public static CommandDataInitializeResult createWithMessage(CommandDataParseResult type,
                                                                String argumentName, String senderValue, ValueType argumentValueType,
                                                                int argumentsCount, int realArgumentsCount) {
        if (type == CommandDataParseResult.ARGUMENT_TYPE_NOT_RIGHT) {
            return CommandDataInitializeResult.argumentTypeNotRight(argumentName, senderValue, argumentValueType);
        }
        else if (type == CommandDataParseResult.GIVEN_ARGUMENT_NOT_EXIST) {
            return CommandDataInitializeResult.givenArgumentNotExist(argumentName);
        }
        else if (type == CommandDataParseResult.REQUIRED_ARGUMENT_NOT_GIVEN) {
            return CommandDataInitializeResult.requiredArgumentNotGiven(argumentName, argumentValueType);
        }
        else if (type == CommandDataParseResult.TOO_MANY_ARGUMENTS) {
            return CommandDataInitializeResult.tooManyArguments(argumentsCount, realArgumentsCount);
        }
        return new CommandDataInitializeResult(type, null);
    }

    public static CommandDataInitializeResult argumentTypeNotRight(String argumentName, String senderValue, ValueType expectedType) {
        return new CommandDataInitializeResult(CommandDataParseResult.ARGUMENT_TYPE_NOT_RIGHT,
                MessageFormat.format(Discomc.getPlugin().getCommandModule().getConfig().getArgumentTypeNotRight(),
                        argumentName, senderValue, expectedType.toString()
                ));
    }

    public static CommandDataInitializeResult givenArgumentNotExist(String argumentName) {
        return new CommandDataInitializeResult(CommandDataParseResult.GIVEN_ARGUMENT_NOT_EXIST,
                MessageFormat.format(Discomc.getPlugin().getCommandModule().getConfig().getGivenArgumentNotExist(),
                        argumentName));
    }

    public static CommandDataInitializeResult requiredArgumentNotGiven(String argumentName, ValueType argumentType) {
        return new CommandDataInitializeResult(CommandDataParseResult.REQUIRED_ARGUMENT_NOT_GIVEN,
                MessageFormat.format(Discomc.getPlugin().getCommandModule().getConfig().getRequiredArgumentNotGiven(),
                        argumentName, argumentType.toString()));
    }

    public static CommandDataInitializeResult tooManyArguments(int argumentsCount, int expectedArgumentsCount) {
        return new CommandDataInitializeResult(CommandDataParseResult.TOO_MANY_ARGUMENTS,
                MessageFormat.format(Discomc.getPlugin().getCommandModule().getConfig().getTooManyArguments(),
                        Integer.toString(argumentsCount), Integer.toString(expectedArgumentsCount)));
    }

}
