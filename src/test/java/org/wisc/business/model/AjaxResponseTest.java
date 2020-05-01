package org.wisc.business.model;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class AjaxResponseTest {
    @Test
    public void testAjaxResponse() {
        AjaxResponse r = new AjaxResponse();
        assertNotNull(r);
        assertNull(r.getMessage());
        assertNull(r.getStatus());
        assertNull(r.getData());
        assertFalse(r.isSuccess());

        r.setMessage("hello");
        assertEquals(r.getMessage(), "hello");
        Date d = new Date();
        r.setData(d);
        assertEquals(r.getData(), d);
        r.setSuccess(true);
        assertTrue(r.isSuccess());
        r.setStatus(123);
        assertEquals(r.getStatus(), 123);

        AjaxResponse s = AjaxResponse.success();
        assertTrue(s.isSuccess());
        assertEquals(s.getMessage(), "success");
        assertEquals(s.getStatus(), 200);
        assertNull(s.getData());

        AjaxResponse e = AjaxResponse.error(100, "error");
        assertFalse(e.isSuccess());
        assertEquals(e.getStatus(), 100);
        assertEquals(e.getMessage(), "error");

        AjaxResponse ss = AjaxResponse.success(d);
        assertEquals(ss.getData(), d);
        assertTrue(s.isSuccess());
        assertEquals(s.getMessage(), "success");
        assertEquals(s.getStatus(), 200);
    }
}
