package com.hpe.g11n.sourcescoring.spellcheck.msword.impl;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Foy Lian
 * Date: 2016-08-22
 * Time: 10:47
 */
public class MSWordSpellCheckerTest {
    @Test
    public void testSuggestion() {
        MSWordSpellChecker sp=new MSWordSpellChecker();
        Assert.assertNotNull(sp.suggestion("aaxx G00d and Fox!!"));
    }
}
