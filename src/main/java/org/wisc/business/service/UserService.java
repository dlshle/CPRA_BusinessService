package org.wisc.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wisc.business.authentication.SecurityUtil;
import org.wisc.business.dao.UserDAO;
import org.wisc.business.model.BusinessModel.Term;
import org.wisc.business.model.PVModels.TermPV;
import org.wisc.business.model.PVModels.UserPV;
import org.wisc.business.model.UserModel.User;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;

    @Resource
    TermService termService;

    public UserPV convertUserToUserPV(User user) {
        if (user == null)
            return null;
        List<TermPV> terms = new LinkedList<>();
        if (user.getFavorite() != null)
            user.getFavorite().forEach((f)->terms.add(termService.findById(f)));
        return new UserPV(user, terms);
    }

    public List<UserPV> convertUsersToUserPVs(List<User> users) {
        List<UserPV> results = new LinkedList<>();
        for (User u:users) {
            results.add(convertUserToUserPV(u));
        }
        return results;
    }

    public UserPV findByParam(User paramUser) {
        if (paramUser.getId() != null)
            return findById(paramUser.getId());
        if (paramUser.getEmail() != null && !paramUser.getEmail().equals(""))
            return findByEmail(paramUser.getEmail());
        if (paramUser.getUsername() != null && !paramUser.getUsername().equals(""))
            return findByUsername(paramUser.getUsername());
        return null;
    }

    public User findRawById(String id) {
        if (id == null)
            return null;
        Optional<User> u = userDAO.findById(id);
        if (!u.isPresent())
            return null;
        return userDAO.findById(id).get();
    }

    public UserPV findById(String id) {
        if (id == null)
            return null;
        return convertUserToUserPV(findRawById(id));
    }

    public User findRawByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    public UserPV findByEmail(String email) {
        return convertUserToUserPV(userDAO.findByEmail(email));
    }

    public User findRawByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    public UserPV findByUsername(String username) {
        return convertUserToUserPV(userDAO.findByUsername(username));
    }

    public List<UserPV> findByName(String name) {
        List<User> users = userDAO.findAllByNameLike(name);
        return convertUsersToUserPVs(users);
    }

    public List<UserPV> all() {
        return convertUsersToUserPVs(userDAO.findAll());
    }

    public UserPV add(User user) throws DuplicateEmailException,
            DuplicateUserNameException{
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
        return convertUserToUserPV(result);
    }

    public UserPV update(User user) throws DuplicateEmailException,
            DuplicateUserNameException{
        // use id to identify user
        User oldUser = findRawById(user.getId());
        if (oldUser == null)
            return null;
        // only update password
        if (user.getPassword() != null && user.getPassword().length() >= 12) {
            // set password
            oldUser.setSalt(SecurityUtil.generateSalt(user.getPassword().length()));
            oldUser.setPassword(SecurityUtil.hashPassword(user.getPassword(),
                    oldUser.getSalt()));
            return convertUserToUserPV(userDAO.save(user));
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
        return convertUserToUserPV(result);
    }

    public boolean delete(User user) {
        if (findById(user.getId()) == null)
            return false;
        userDAO.delete(user);
        return true;
    }
}
