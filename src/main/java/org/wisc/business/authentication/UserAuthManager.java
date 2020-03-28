package org.wisc.business.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.wisc.business.cache.LFUCache;
import org.wisc.business.model.PVModels.UserPV;
import org.wisc.business.model.UserModel.User;
import org.wisc.business.service.DuplicateEmailException;
import org.wisc.business.service.DuplicateUserNameException;
import org.wisc.business.service.UserService;

import java.util.concurrent.*;

// cache logged in user and keep track of their login status
public class UserAuthManager {
    @Autowired
    UserService userService;

    private ExecutorService cacheThreadExecutor;

    public static final long VALIDATION_PERIOD = 1800000; // 30 mins valid

    private class UserStatusPair {
        User user;
        long timeEffective;

        public UserStatusPair(User user, long timeEffective) {
            this.user = user;
            this.timeEffective = timeEffective;
        }

        public User updateUser(User newUser) {
            timeEffective = System.currentTimeMillis();
            Future<User> result = cacheThreadExecutor.submit(()-> {
                UserPV userFromDatabase = null;
                try {
                    userFromDatabase = userService.update(newUser);
                } catch (DuplicateEmailException e) {
                    userFromDatabase = null;
                } catch (DuplicateUserNameException e) {
                    userFromDatabase = null;
                } finally {
                    if (userFromDatabase != null) {
                        synchronized (this) {
                            user = userFromDatabase.toRawType();
                        }
                        return user;
                    }
                    return null;
                }
            });
            User user = null;
            try {
                user = result.get(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                user = null;
            } catch (ExecutionException e) {
                user = null;
            } catch (TimeoutException e) {
                user = null;
            } finally {
                return user;
            }
        }

        public void updateTime() {
            timeEffective = System.currentTimeMillis();
        }
    }

    // token as key pair as val
    private LFUCache<String, UserStatusPair> cache;

    public UserAuthManager(int size) {
        cache = new LFUCache<>(size);
        cacheThreadExecutor = Executors.newFixedThreadPool(size);
    }

    public void put(String token, User user) {
        cache.put(token, new UserStatusPair(user, System.currentTimeMillis()));
    }

    public User get(String token) {
        UserStatusPair pair = cache.get(token);
        if (pair == null)
            return null;
        if (System.currentTimeMillis() - pair.timeEffective > UserAuthManager.VALIDATION_PERIOD)
            return null;
        pair.updateTime();
        return pair.user;
    }
}
