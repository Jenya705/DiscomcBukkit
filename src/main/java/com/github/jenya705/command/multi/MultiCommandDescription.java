package com.github.jenya705.command.multi;

import java.util.Collection;
import java.util.List;

public interface MultiCommandDescription {

    static MultiCommandDescription instance() {
        return new ImplMultiCommandDescription();
    }

    MultiCommandDescription name(String name);

    MultiCommandDescription description(String description);

    MultiCommandDescription addOption(MultiCommandOption option);

    MultiCommandDescription addOptions(Collection<MultiCommandOption> options);

    MultiCommandDescription addOptions(MultiCommandOption... options);

    String getName();

    String getDescription();

    List<MultiCommandOption> getOptions();

}
