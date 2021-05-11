package com.github.jenya705.data.types;

import com.github.jenya705.util.MentionsUtil;
import lombok.Getter;

import java.util.Objects;
import java.util.function.Function;

@Getter
public enum DiscordValueType implements ValueType{

    SNOWFLAKE_USER (
            (str) -> Objects.requireNonNull(MentionsUtil.snowflakeFromRaw(str, MentionsUtil.USER_MENTION_PATTERN, true, 2)),
            (str) -> MentionsUtil.snowflakeFromRaw(str, MentionsUtil.USER_MENTION_PATTERN, true, 2) != null
    ),
    SNOWFLAKE_ROLE (
            (str) -> Objects.requireNonNull(MentionsUtil.snowflakeFromRaw(str, MentionsUtil.ROLE_MENTION_PATTERN, true, 1)),
            (str) -> MentionsUtil.snowflakeFromRaw(str, MentionsUtil.ROLE_MENTION_PATTERN, true, 1) != null
    ),
    SNOWFLAKE_CHANNEL (
            (str) -> Objects.requireNonNull(MentionsUtil.snowflakeFromRaw(str, MentionsUtil.CHANNEL_MENTION_PATTERN, true, 1)),
            (str) -> MentionsUtil.snowflakeFromRaw(str, MentionsUtil.CHANNEL_MENTION_PATTERN, true, 1) != null
    ),
    SNOWFLAKE_MENTIONABLE(
            (str) -> Objects.requireNonNull(MentionsUtil.snowflakeFromRaw(str, MentionsUtil.MENTIONABLE_MENTION_PATTERN, true, 2)),
            (str) -> MentionsUtil.snowflakeFromRaw(str, MentionsUtil.MENTIONABLE_MENTION_PATTERN, true, 2) != null
    )
    ;

    private final Function<String, Object> createInstanceFunction;
    private final Function<String, Boolean> canBeCreatedFunction;
    DiscordValueType(Function<String, Object> createInstanceFunction,
                     Function<String, Boolean> canBeCreatedFunction) {
        this.createInstanceFunction = createInstanceFunction;
        this.canBeCreatedFunction = canBeCreatedFunction;
    }
    @Override
    public boolean canBeCreated(String stringValue) {
        return getCanBeCreatedFunction().apply(stringValue);
    }

    @Override
    public Object createInstance(String stringValue) {
        return getCreateInstanceFunction().apply(stringValue);
    }
}
