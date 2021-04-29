package com.github.jenya705;

import com.github.jenya705.multichat.MentionSerializer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MentionSerializerTest {

    @Test
    public void patternTest() {

        String testSubject = "@everyone, i want to talk to <@!1> because all @here users are <@&1>, i will talk to you in <#1>";
        String answer = "!, i want to talk to ! because all ! users are !, i will talk to you in !";

        assertEquals(answer, MentionSerializer.getPattern().matcher(testSubject).replaceAll("!"));

    }

    @Test
    public void patternTest2() {

        String testSubject = "@everyone, i want to talk to <@!1> because all @here users are <@&1>, i will talk to you in <#1> qq";
        String answer = "!, i want to talk to ! because all ! users are !, i will talk to you in ! qq";

        assertEquals(answer, MentionSerializer.getPattern().matcher(testSubject).replaceAll("!"));

    }

    @Test
    public void patternTestNoMentions() {

        String testSubject = "Hi, everyone!";
        String answer = "Hi, everyone!";

        assertEquals(answer, MentionSerializer.getPattern().matcher(testSubject).replaceAll("!"));

    }

}
