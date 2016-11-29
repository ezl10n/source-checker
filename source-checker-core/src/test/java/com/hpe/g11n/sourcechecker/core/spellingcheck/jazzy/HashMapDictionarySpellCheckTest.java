package com.hpe.g11n.sourcechecker.core.spellingcheck.jazzy;

import org.junit.Assert;
import org.junit.Test;

import com.hpe.g11n.sourcechecker.core.spellingcheck.jazzy.HashMapDictionarySpellCheck;

/**
 * Created with IntelliJ IDEA.
 * User: Foy Lian
 * Date: 2016-08-26
 * Time: 10:38
 */
public class HashMapDictionarySpellCheckTest {

    @Test
    public void testIsCorrect() {
        HashMapDictionarySpellCheck checker = new HashMapDictionarySpellCheck(null);
        Assert.assertEquals(true, checker.isCorrect("hello"));
        Assert.assertEquals(false, checker.isCorrect("hxllx"));
        Assert.assertEquals(true, checker.isCorrect("AWT"));
    }

    @Test
    public void testGetSuggestions() {
        HashMapDictionarySpellCheck checker = new HashMapDictionarySpellCheck(null);
        Assert.assertTrue(checker.getSuggestions("hellx").size() > 0);
        Assert.assertTrue(checker.getSuggestions("oy").size() > 0);
    }
}
