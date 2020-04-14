package org.wisc.business.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.wisc.business.model.UserModel.User;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceTest {
    @Autowired
    UserService userService;

    @Test
    void findRawById() {
        final String validId = "5e93acebeb3a8c34f285c1fa";
        final String invalidId = "5e8fef0c96635a65deabcdb8";
        assertNotNull(userService.findRawById(validId));
        assertNull(userService.findRawById(invalidId));
        assertNull(userService.findRawById(null));
    }

    @Test
    void findById() {
        final String validId = "5e93acebeb3a8c34f285c1fa";
        final String invalidId = "5e8fef0c96635a65deabcdb8";
        assertNotNull(userService.findById(validId));
        assertNull(userService.findById(invalidId));
        assertNull(userService.findById(null));
    }

    @Test
    void findRawByEmail() {
        final String validEmail = "badger@wisc.edu";
        final String invalidEmail = "invalid@email.lol";
        assertNotNull(userService.findRawByEmail(validEmail));
        assertNull(userService.findRawByEmail(invalidEmail));
        assertNull(userService.findRawByEmail(null));
    }

    @Test
    void findByEmail() {
        final String validEmail = "badger@wisc.edu";
        final String invalidEmail = "invalid@email.lol";
        assertNotNull(userService.findByEmail(validEmail));
        assertNull(userService.findByEmail(invalidEmail));
        assertNull(userService.findByEmail(null));
    }

    @Test
    void findByUsername() {
        final String validUserName = "badger@wisc.edu";
        final String invalidUserName = "invalid@email.lol";
        assertNotNull(userService.findByUsername(validUserName));
        assertNull(userService.findByUsername(invalidUserName));
        assertNull(userService.findByUsername(null));
    }

    @Test
    void findByName() {
        final String validName = "Badger";
        final String invalidName = "Regdab";
        assertFalse(userService.findByName(validName).isEmpty());
        assertFalse(!userService.findByName(invalidName).isEmpty());
        assertFalse(!userService.findByName(null).isEmpty());
    }

    @Test
    void all() {
        assertFalse(userService.all().isEmpty());
    }

    @Test
    void add() throws DuplicateUserNameException, DuplicateEmailException {
        final User validUser = User.builder().email("asdasd").password(
                "asdasd").build();
        final User invalidUser = new User();
        final User noPassword = User.builder().email("dddddaasdasdasd").build();
        final User addedUser = userService.add(validUser).toRawType();
        assertNotNull(addedUser);
        assertNull(userService.add(invalidUser));
        assertNull(userService.add(noPassword));
        // duplicate email
        boolean flag = false;
        try {
            assertNull(userService.add(validUser));
            flag = false;
        } catch (Exception ex) {
            flag = true;
        }
        assertTrue(flag);
        assertTrue(userService.delete(addedUser));
    }

    @Test
    void update() throws DuplicateUserNameException, DuplicateEmailException {
        final String validId = "5e93acebeb3a8c34f285c1fa";
        final User invalidUser = new User();
        final User oldUser = userService.findRawById(validId);
        assertNotNull(oldUser);
        final String oldName = oldUser.getName();
        final String newName = "New Name";
        oldUser.setName(newName);
        oldUser.setPassword(null);
        User updated = userService.update(oldUser).toRawType();
        assertNotNull(updated);
        assertEquals(updated.getName(), newName);
        oldUser.setName(oldName);
        updated = userService.update(oldUser).toRawType();
        assertNotNull(updated);
        assertEquals(updated.getName(), oldName);

        assertNull(userService.update(invalidUser));
        assertNull(userService.update(null));
    }

    @Test
    void delete() throws DuplicateUserNameException, DuplicateEmailException {
        final User validUser = User.builder().email("asdasd").password(
            "asdasd").build();
        final User addedUser = userService.add(validUser).toRawType();
        assertNotNull(addedUser);
        assertTrue(userService.delete(addedUser));
        assertFalse(userService.delete(new User()));
        assertFalse(userService.delete(null));
    }

    @Test
    void favoriteAndUnfavorite() {
        final String validId = "5e951252280188748b2126eb";
        final String termId = "5e911eea94585272f2c027f5";
        User validUser = userService.findRawById(validId);
        assertNotNull(validId);
        User updated = userService.favorite(validUser, termId).toRawType();
        assertTrue(updated.getFavorite().contains(termId));
        assertNull(userService.favorite(updated, termId));
        updated = userService.unfavorite(updated, termId).toRawType();
        assertFalse(updated.getFavorite().contains(termId));
        assertNull(userService.unfavorite(updated, termId));

        assertNull(userService.favorite(null, termId));
        assertNull(userService.favorite(validUser, null));
        assertNull(userService.unfavorite(null, termId));
        assertNull(userService.unfavorite(validUser, null));
    }
}