package com.emptypockets.spacemania;

import org.junit.Test;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by jenfield on 17/05/2015.
 */
@RunWith(JUnit38ClassRunner.class)
public class RootTest{
    @Test
    public void test(){
        assertTrue(true);
    }

    @Test
    public void testItFails(){
        assertFalse(true);
    }
}
