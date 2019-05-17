package com.amway.uiautomation.utils;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.amway.integration.impex.FlexibleSearchService;
import com.amway.support.consts.CommonConsts;
import com.amway.support.consts.FlexibleSearchQueries;
import com.amway.support.enums.Roles;
import com.amway.support.exception.Exceptions;
import com.amway.support.guice.injector.GuiceInjector;
import com.amway.support.models.LoginCriteria;
import com.amway.support.models.User;
import com.amway.support.properties.EnvProperties;
import com.amway.uiautomation.context.SharedDataContainer;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConcurrentUserPool {

    private static final Logger LOG = LogManager.getLogger(ConcurrentUserPool.class);

    private static final EnvProperties ENV_PROPERTIES = GuiceInjector.getBean(EnvProperties.class);

    private static final List<User> AVAILABLE_USERS = ImmutableList
            .copyOf(UsersPoolProviderStrategy.getUserPoolList(ENV_PROPERTIES.getUserPoolProvider())).stream()
            .filter(u -> u.getRole() != null).collect(Collectors.toList());

    private static final Supplier<Queue<User>> REGISTERED_USERS = Suppliers
            .memoize(ConcurrentUserPool::getRegisteredUsers);

    private ConcurrentUserPool() {
    }

    private static Deque<User> getRegisteredUsers() {
        List<User> list = GuiceInjector.getBean(FlexibleSearchService.class)
                .queryForList(FlexibleSearchQueries.getFindAllRecentRegisteredUsers(), User.class);
        list.forEach(u -> u.setPassword(CommonConsts.DEFAULT_PASSWORD));
        return new LinkedList<>(list);
    }

    public static User getUserFromQueue(Roles role) {
        User user = REGISTERED_USERS.get().stream().filter(u -> u.getRole().equals(role)).findAny()
                .orElseThrow(Exceptions.withMessage("No users left"));
        REGISTERED_USERS.get().remove(user);
        return user;
    }

    public static synchronized Optional<User> tryGetAvailableUser(LoginCriteria loginCriteria) {

        if (!containsUsersWithRole(loginCriteria.getRole()))
            throw new IllegalStateException(String.format("No users with role [%s] in pool", loginCriteria));

        Optional<User> availableUser = findAvailableUser(loginCriteria);
        Optional<User> result;
        if (availableUser.isPresent()) {
            releaseUser();
            User user = availableUser.get();
            user.setAvailable(false);
            if (null == user.getName()) {
                user.setName(UserProfileUtils.getUserFullName(user));
            }
            SharedDataContainer.get().setCurrentUser(user);
            LOG.info(String.format("User [%s] with role [%s] obtained by thread [%s]", user, loginCriteria,
                    Thread.currentThread().getId()));
            result = Optional.of(user);
        } else {
            result = Optional.empty();
        }
        return result;
    }

    public static synchronized Optional<User> popUser(LoginCriteria loginCriteria){
        Optional<User> user = tryGetAvailableUser(loginCriteria);
        user.ifPresent(AVAILABLE_USERS::remove);
        return user;
    }

    public static synchronized void releaseUser() {
        Optional<User> userOptional = Optional.ofNullable(SharedDataContainer.get().getCurrentUser());
        if (userOptional.isPresent()) {
            LOG.info(String.format("Test releases user [%s]", userOptional.get()));
            userOptional.get().setAvailable(true);
        }
    }

    public static User waitForAvailableUser(LoginCriteria loginCriteria) {
        LOG.info(String.format("Test requires user with role [%s]", loginCriteria));
        Optional<User> userOptional;
        while (!(userOptional = tryGetAvailableUser(loginCriteria)).isPresent()) {
            LOG.info(String.format("Waiting user with role [%s]", loginCriteria));
            WaitHelper.waitConstTime();
        }
        return userOptional.get();
    }

    private static synchronized Optional<User> findAvailableUser(LoginCriteria loginCriteria) {
        if (loginCriteria.getUserId() == null) {
            if (loginCriteria.getAmwayAccount() == null) {
                return AVAILABLE_USERS.stream()
                        .filter(x -> x.getRole().equals(loginCriteria.getRole()) && x.isAvailable()).findFirst();
            } else {
                return AVAILABLE_USERS.stream()
                        .filter(x -> x.getRole().equals(loginCriteria.getRole())
                                && x.getAmwayAccount().equals(loginCriteria.getAmwayAccount()) && x.isAvailable())
                        .findFirst();
            }
        } else {
            return AVAILABLE_USERS.stream().filter(x -> x.getUserUID().equals(loginCriteria.getUserId())
                    && x.getRole().equals(loginCriteria.getRole()) && x.isAvailable()).findFirst();
        }
    }

    public static synchronized boolean containsUsersWithRole(Roles roles) {
        return AVAILABLE_USERS.stream().anyMatch(x -> x.getRole().equals(roles));
    }

    public static List<User> allUsers() {
        return Lists.newArrayList(AVAILABLE_USERS);
    }
}
