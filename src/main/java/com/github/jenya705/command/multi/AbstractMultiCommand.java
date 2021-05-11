package com.github.jenya705.command.multi;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class AbstractMultiCommand implements MultiCommand{

    private boolean async;
    private boolean minecraft;
    private boolean discordTraditional;
    private boolean discordApplication;

}
