package me.ryan.learnakka;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringReverseServiceTest {

    @Test
    public void shouldReverseString() throws Exception {
        StringReverseService reverseService = new StringReverseService();
        String original = "Foo Bar";
        String actual = reverseService.get(original);
        assertEquals("raB ooF", actual);
    }
}