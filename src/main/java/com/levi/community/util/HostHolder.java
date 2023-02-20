package com.levi.community.util;

import com.levi.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * Hold user information
 */
@Component
public class HostHolder {
    private final ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }
}
