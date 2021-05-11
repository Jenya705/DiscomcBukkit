package com.github.jenya705.command.multi;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PROTECTED)
@AllArgsConstructor
public class ImplMultiCommandChoice implements MultiCommandChoice {

    private String name;
    private String value;

}
