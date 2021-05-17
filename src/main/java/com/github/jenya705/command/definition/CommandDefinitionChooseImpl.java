package com.github.jenya705.command.definition;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PROTECTED)
@AllArgsConstructor
public class CommandDefinitionChooseImpl implements CommandDefinitionChoose {

    private String name;
    private String value;

}
