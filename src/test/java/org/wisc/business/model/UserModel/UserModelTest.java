package org.wisc.business.model.UserModel;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class UserModelTest {
    @Test
    public void testUser() {
        User u = new User();
        ArrayList<String> fav = new ArrayList<String>();
        assertNotNull(u);
        assertNull(u.getCreatedDate());
        assertNull(u.getEmail());
        assertNull(u.getFavorite());
        assertNull(u.getId());
        assertNull(u.getName());
        assertNull(u.getPassword());
        assertNull(u.getSalt());
        assertNull(u.getUsername());


        u.setCreatedDate(new Date());
        assertEquals(new Date(), u.getCreatedDate());
        u.setEmail("lll");
        assertEquals("lll", u.getEmail());

        u.setFavorite(fav);
        assertEquals(fav, u.getFavorite());
        u.setId("ttt");
        assertEquals("ttt", u.getId());
        u.setName("aaa");
        assertEquals("aaa", u.getName());
        u.setPassword("bbb");
        assertEquals("bbb", u.getPassword());
        u.setSalt("ccc");
        assertEquals("ccc", u.getSalt());
        u.setUsername("ddd");
        assertEquals("ddd", u.getUsername());




        User u2 = new User("as","aaa","bbb","asa","asd",fav,new Date(),"fff",true);
        assertNotNull(u);
        assertNotNull(u2.getId());
        assertNotNull(u2.getEmail());
        assertNotNull(u2.getUsername());
        assertNotNull(u2.getName());
        assertNotNull(u2.getPassword());
        assertNotNull(u2.getFavorite());
        assertNotNull(u2.getCreatedDate());
        assertNotNull(u2.getSalt());
        assertNotNull(u2.isAdmin());




        u2.setCreatedDate(new Date());
        assertEquals(new Date(), u2.getCreatedDate());
        u2.setEmail("lll");
        assertEquals("lll", u2.getEmail());
        u2.setFavorite(fav);
        assertEquals(fav, u2.getFavorite());
        u2.setId("ttt");
        assertEquals("ttt", u2.getId());
        u2.setName("aaa");
        assertEquals("aaa", u2.getName());
        u2.setPassword("bbb");
        assertEquals("bbb", u2.getPassword());
        u2.setSalt("ccc");
        assertEquals("ccc", u2.getSalt());
        u2.setUsername("ddd");
        assertEquals("ddd", u2.getUsername());

        Date curr = new Date();
        User u3 =
                User.builder().email("abc").password("def").id("hij").isAdmin(true).favorite(fav).createdDate(curr).salt("klm").build();
        assertEquals(u3.getEmail(), "abc");
        assertEquals(u3.getPassword(), "def");
        assertEquals(u3.getId(), "hij");
        assertEquals(u3.isAdmin(), true);
        assertEquals(u3.getFavorite(), fav);
        assertEquals(u3.getCreatedDate(), curr);
        assertEquals(u3.getSalt(), "klm");
    }


}
