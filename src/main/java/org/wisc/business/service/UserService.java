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
import java.util.*;

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
        if (email == null)
            return null;
        return convertUserToUserPV(userDAO.findByEmail(email));
    }

    public UserPV findByUsername(String username) {
        if (username == null)
            return null;
        return convertUserToUserPV(userDAO.findByUsername(username));
    }

    public List<UserPV> findByName(String name) {
        if (name == null)
            return new LinkedList<>();
        List<User> users = userDAO.findAllByNameLike(name);
        return convertUsersToUserPVs(users);
    }

    public List<UserPV> all() {
        return convertUsersToUserPVs(userDAO.findAll());
    }

    public UserPV add(User user) throws DuplicateEmailException,
            DuplicateUserNameException{
        if (user == null || user.getEmail() == null || user.getPassword() == null)
            return null;
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
        if (user == null || user.getId() == null)
            return null;
        // use id to identify user
        User oldUser = findRawById(user.getId());
        if (oldUser == null)
            return null;
        if (user.getPassword() != null && user.getPassword().length() >= 12) {
            // set password
            oldUser.setSalt(SecurityUtil.generateSalt(user.getPassword().length()));
            oldUser.setPassword(SecurityUtil.hashPassword(user.getPassword(),
                    oldUser.getSalt()));
        }
        // update other stuff
        if (user.getEmail() != null && user.getEmail().length() > 6 && !user.getEmail().equals(oldUser.getEmail())) {
            if (userDAO.findByEmail(user.getEmail()) != null)
                throw new DuplicateEmailException();
            oldUser.setEmail(user.getEmail());
        }
        if (user.getUsername() != null && user.getUsername().length() > 6 && !user.getUsername().equals(oldUser.getUsername())) {
            if (userDAO.findByUsername(user.getUsername()) != null)
                throw new DuplicateUserNameException();
            oldUser.setUsername(user.getUsername());
        }
        if (user.getName() != null && !user.getName().isEmpty())
            oldUser.setName(user.getName());
        if (user.getFavorite() != null) {
            HashSet<String> oldIds = new HashSet<>(user.getFavorite());
            LinkedList<String> newIds = new LinkedList<>();
            user.getFavorite().forEach((tid)->{
                if (oldIds.contains(tid)) {
                    oldIds.remove(tid);
                    newIds.add(tid);
                } else {
                    // to add
                    Term t = termService.findRawById(tid);
                    if (t != null)
                        newIds.add(tid);
                }
            });
            oldIds.forEach((tid)->newIds.remove(tid));
            oldUser.setFavorite(newIds);
        }
        User result;
        try {
            result = userDAO.save(oldUser);
        } catch (Exception ex) {
            return null;
        }
        return convertUserToUserPV(result);
    }

    public boolean delete(User user) {
        if (user == null || findById(user.getId()) == null)
            return false;
        userDAO.delete(user);
        return true;
    }

    public UserPV favorite(User user, String termId) {
        if (user == null || user.getId() == null || termId == null)
            return null;
        if (user.getFavorite().contains(termId))
            return null;
        List<String> tIds = user.getFavorite();
        tIds.add(termId);
        user.setFavorite(tIds);
        try {
            user.setPassword("");
            return update(user);
        } catch (Exception e) {
            return null;
        }
    }

    public UserPV unfavorite(User user, String termId) {
        if (user == null || user.getId() == null || termId == null)
            return null;
        if (!user.getFavorite().contains(termId))
            return null;
        List<String> tIds = user.getFavorite();
        tIds.remove(termId);
        user.setFavorite(tIds);
        try {
            user.setPassword("");
            return update(user);
        } catch (Exception e) {
            return null;
        }
    }
}
