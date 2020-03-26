package org.wisc.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wisc.business.authentication.SecurityUtil;
import org.wisc.business.dao.UserDAO;
import org.wisc.business.model.UserModel.User;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;

    public User findByParam(User paramUser) {
        if (paramUser.getId() != null)
            return findById(paramUser.getId());
        if (paramUser.getEmail() != null && !paramUser.getEmail().equals(""))
            return findByEmail(paramUser.getEmail());
        if (paramUser.getUsername() != null && !paramUser.getUsername().equals(""))
            return findByUsername(paramUser.getUsername());
        return null;
    }

    public User findById(String id) {
        if (id == null)
            return null;
        return userDAO.findById(id).get();
    }

    public User findByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    public List<User> findByName(String name) {
        return userDAO.findAllByNameLike(name);
    }

    public List<User> all() {
        return userDAO.findAll();
    }

    public User add(User user) throws DuplicateEmailException, DuplicateUserNameException{
        if (userDAO.findByUsername(user.getUsername()) != null)
            throw new DuplicateUserNameException();
        if (userDAO.findByEmail(user.getEmail()) != null)
            throw new DuplicateEmailException();
        // salt and hash password
        user.setSalt(SecurityUtil.generateSalt(user.getPassword().length()));
        user.setPassword(SecurityUtil.hashPassword(user.getPassword(),
                user.getSalt()));
        // set favorite list and cdate
        user.setFavorite(new LinkedList<>());
        user.setCreatedDate(new Date());
        User result;
        try {
            result = userDAO.save(user);
        } catch (Exception ex) {
            return null;
        }
        return result;
    }

    public User update(User user) throws DuplicateEmailException, DuplicateUserNameException{
        // use id to identify user
        User oldUser = findById(user.getId());
        if (oldUser == null)
            return null;
        // only update password
        if (user.getPassword() != null && user.getPassword().length() >= 12) {
            // set password
            oldUser.setSalt(SecurityUtil.generateSalt(user.getPassword().length()));
            oldUser.setPassword(SecurityUtil.hashPassword(user.getPassword(),
                    oldUser.getSalt()));
            return userDAO.save(user);
        }
        // update other stuff
        if (user.getEmail() != null && user.getEmail().length() > 6) {
            if (userDAO.findByEmail(user.getEmail()) != null)
                throw new DuplicateEmailException();
            oldUser.setEmail(user.getEmail());
        }
        if (user.getUsername() != null && user.getUsername().length() > 6) {
            if (userDAO.findByUsername(user.getUsername()) != null)
                throw new DuplicateUserNameException();
            oldUser.setUsername(user.getUsername());
        }
        if (user.getFavorite() != null)
            oldUser.setFavorite(user.getFavorite());
        User result;
        try {
            result = userDAO.save(user);
        } catch (Exception ex) {
            return null;
        }
        return result;
    }

    public boolean delete(User user) {
        if (findById(user.getId()) == null)
            return false;
        userDAO.delete(user);
        return true;
    }
}
