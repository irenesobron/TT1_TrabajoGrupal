package com.ejemplo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProblemDetailsTest {

    @Test
    public void testConstructor() {
        ProblemDetails pd = new ProblemDetails("type1", "title1", 400, "detail1", "instance1");
        assertEquals("type1", pd.getType());
        assertEquals("title1", pd.getTitle());
        assertEquals(400, pd.getStatus());
        assertEquals("detail1", pd.getDetail());
        assertEquals("instance1", pd.getInstance());
    }

    @Test
    public void testConstructorWithNulls() {
        ProblemDetails pd = new ProblemDetails(null, null, null, null, null);
        assertNull(pd.getType());
        assertNull(pd.getTitle());
        assertNull(pd.getStatus());
        assertNull(pd.getDetail());
        assertNull(pd.getInstance());
    }

    @Test
    public void testSetType() {
        ProblemDetails pd = new ProblemDetails("type1", "title1", 400, "detail1", "instance1");
        pd.setType("newType");
        assertEquals("newType", pd.getType());
    }

    @Test
    public void testSetTitle() {
        ProblemDetails pd = new ProblemDetails("type1", "title1", 400, "detail1", "instance1");
        pd.setTitle("newTitle");
        assertEquals("newTitle", pd.getTitle());
    }

    @Test
    public void testSetStatus() {
        ProblemDetails pd = new ProblemDetails("type1", "title1", 400, "detail1", "instance1");
        pd.setStatus(500);
        assertEquals(500, pd.getStatus());
    }

    @Test
    public void testSetStatusToNull() {
        ProblemDetails pd = new ProblemDetails("type1", "title1", 400, "detail1", "instance1");
        pd.setStatus(null);
        assertNull(pd.getStatus());
    }

    @Test
    public void testSetDetail() {
        ProblemDetails pd = new ProblemDetails("type1", "title1", 400, "detail1", "instance1");
        pd.setDetail("newDetail");
        assertEquals("newDetail", pd.getDetail());
    }

    @Test
    public void testSetInstance() {
        ProblemDetails pd = new ProblemDetails("type1", "title1", 400, "detail1", "instance1");
        pd.setInstance("newInstance");
        assertEquals("newInstance", pd.getInstance());
    }
}