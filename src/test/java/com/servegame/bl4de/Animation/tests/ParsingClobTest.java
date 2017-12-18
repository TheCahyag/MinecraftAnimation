package com.servegame.bl4de.Animation.tests;

import com.servegame.bl4de.Animation.util.SQLHelper;
import com.servegame.bl4de.Animation.util.Util;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * File: ParsingClobTest.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class ParsingClobTest {

    /**
     * Test that the StringContinuation parses the string correctly
     */
    @Test
    public void utilContinuationCorrectnessTest(){
        String before = "BlockState={ContentVersion=2, BlockState=minecraft:stonebrick[variant=stonebrick]}";
        String shouldBe = "BlockState={ContentVersion=2, BlockState=\"minecraft:stonebrick[variant=stonebrick]\"}";
        assertEquals(shouldBe, new Util.StringContinuation(before).getString());
    }

    /**
     * Send a single string through the continuation class multiple times
     */
    @Test
    public void utilContinuationMultiplePassTest(){
        String before = "BlockState={ContentVersion=2, BlockState=minecraft:stonebrick[variant=stonebrick]}";
        String shouldBe = "BlockState={ContentVersion=2, BlockState=\"minecraft:stonebrick[variant=stonebrick]\"}";
        String tmp = new Util.StringContinuation(before).getString();
        assertEquals(shouldBe, new Util.StringContinuation(tmp).getString());
    }

    /**
     * Test large strings to see if parsing the string blows the heap
     */
    @Test
    public void utilContinuationStressTest(){
        try {
            parseLongString();
        } catch (OutOfMemoryError e){
            fail("Ran out of memory");
        }
    }

    /**
     * Incomplete
     * @throws OutOfMemoryError
     */
    private static void parseLongString() throws OutOfMemoryError {
        try (Connection conn = SQLHelper.getConnection()){
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT data FROM ANIMATION_TABLE WHERE NAME='cliff'");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                new Util.StringContinuation(rs.getString("data")).getString();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
