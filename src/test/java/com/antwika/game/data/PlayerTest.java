package com.antwika.game.data;

import com.antwika.game.actor.Player;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTest {
    @Test
    @Tag("UnitTest")
    public void construct() {
        new Player("Alice");
    }

    @Test
    @Tag("UnitTest")
    public void serializeToString() {
        assertEquals("Actor(actorName=Alice)", new Player("Alice").toString());
    }

    @Test
    @Tag("UnitTest")
    public void getName() {
        assertEquals("Alice", new Player("Alice").getActorName());
    }
}
