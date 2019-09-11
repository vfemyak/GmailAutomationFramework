package com.amway.uiautomation.steps.definitions.common;

import com.amway.integration.hacscripting.ImportDefaultUserDataGroovyScriptTask;
import com.amway.integration.impex.ImpexService;
import com.amway.support.consts.EventType;
import com.amway.support.enums.*;
import com.amway.support.exception.AmwayTestAutomationException;
import com.amway.support.exception.Exceptions;
import com.amway.support.models.LoginCriteria;
import com.amway.support.models.User;
import com.amway.support.properties.EnvProperties;
import com.amway.uiautomation.common.action.login.LoginAction;
import com.amway.uiautomation.common.action.login.NavigationAction;
import com.amway.uiautomation.common.action.storefront.HeaderAction;
import com.amway.uiautomation.common.action.storefront.PayPalAction;
import com.amway.uiautomation.common.facade.RegistrationFacade;
import com.amway.uiautomation.common.state.storefront.HomeState;
import com.amway.uiautomation.context.CallbackContext;
import com.amway.uiautomation.context.SharedData;
import com.amway.uiautomation.engine.WebDriverUtils;
import com.amway.uiautomation.utils.*;
import com.amway.uihttp.SharedDataPopulator;
import com.google.common.collect.ImmutableMap;
import cucumber.api.java8.En;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;
import net.jodah.failsafe.function.CheckedRunnable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static com.amway.support.consts.CharConsts.SPACE;
import static com.amway.support.consts.CharConsts.UNDERLINE;
import static com.amway.support.consts.CommonConsts.IN;
import static com.amway.support.consts.CommonConsts.USER_POOL_LOGIN;
import static com.amway.support.enums.Pages.*;
import static com.amway.support.guice.injector.GuiceInjector.getBean;
import static java.util.Optional.ofNullable;

public class LoginDefinitions implements En {

    private static final Logger LOG = LogManager.getLogger(LoginDefinitions.class);
    private static final String DEFAULT_ADDRESS = "address_1";

    @Inject
    private SharedData sharedData;
    @Inject
    private LoginAction loginAction;
    @Inject
    private HomeState homeState;
    @Inject
    private NavigationAction navigationAction;
    @Inject
    private PayPalAction payPalAction;
    @Inject
    private ImpexService impexService;
    @Inject
    private EnvProperties envProperties;
    @Inject
    private HeaderAction headerAction;
    @Inject
    private RegistrationFacade registrationFacade;

    private SharedDataPopulator sharedDataPopulator = new SharedDataPopulator();

    public LoginDefinitions() {

        When("^User logs out and restart browser$", WebDriverUtils::restart);

        When("^System restarts browser$", WebDriverUtils::restartWithoutReleasingUser);

        When("^User re-logs in to (choose account|storefront) page$", (String page) -> {
            WebDriverUtils.restartWithoutReleasingUser();
            navigationAction.navigateTo(STOREFRONT);

            User user = sharedData.getCurrentUser();

            headerAction.openLoginForm();
            loginAction.submitLoginForm(user, Pages.valueOf(page.replace(SPACE, UNDERLINE).toUpperCase()));

            navigationAction.waitForPageLoading(STOREFRONT);
            logNodeNumber(user);

            setSiteLanguage(STOREFRONT);
        });

        When("^(Locked |)User with (.+) role logs in to (cmscockpit|productcockpit|storefront|businesscenter|storefront in ASM mode|backoffice)( US| CA| DO|) page$",
                (String isLocked, Roles role, String page, String country) -> {
                    sharedData.setUserRole(role);
                    populateSiteLanguage(country);
                    loginByRole(new LoginCriteria(role), Pages.valueOf(page.replace(SPACE, UNDERLINE).toUpperCase()),
                            !isLocked.isEmpty());
                });

        When("^Registered (downline|upline|)(?: |)user logs in to (storefront|businesscenter|storefront in ASM mode|backoffice)( US| CA| DO|) page$",
                (Roles role, String page, String country) -> {
                    User user = role == null ? sharedData.getRegisteredUser()
                            : role.equals(Roles.UPLINE) ? sharedData.getUplineUser() : sharedData.getDownlineUser();
                    sharedData.setCurrentUser(user);
                    populateSiteLanguage(country);
                    CallbackContext.publishEvent(EventType.BEFORE_USER_LOGIN);
                    loginByUser(user, Pages.valueOf(page.replace(SPACE, UNDERLINE).toUpperCase()));
                    CallbackContext.publishEvent(EventType.AFTER_USER_LOGIN);
                });

        When("^User (.+) fills in and submits login data with empty cart$", (Roles role) -> {
            final User user = DataUtils.getUserAndLock(new LoginCriteria(role));
            impexService.removeUserShoppingCart(user.getOktaToken());
            fillAndSubmitCredentials(user, STOREFRONT);
        });

        When("^User logs out$", () -> {
            headerAction.clickOnMyAccountButton();
            headerAction.clickOnSignOutButton();
            WaitHelper.waitUntilLoadersDisappear();
        });

        When("^User from (.+) group logs in to businesscenter page$",
                (String groupId) -> loginByUser(DataUtils.readBCUser(groupId), BUSINESSCENTER));

        When("^User with (.+) role fills in and submits login data on (storefront|businesscenter|LOGIN) page$",
                (Roles role, String page) -> {
                    User user = DataUtils.getUserAndLockWithoutCleanUp(new LoginCriteria(role));
                    fillAndSubmitCredentials(user, Pages.valueOf(page.toUpperCase()));
                });

        When("^Locked user fills in and submits login data on (storefront|businesscenter|LOGIN) page$",
                (String page) -> {
                    User user = sharedData.getCurrentUser();
                    fillAndSubmitCredentials(user, Pages.valueOf(page.toUpperCase()));
                });

        When("^(|Locked )User with (.+) role fills in and submits (valid login|invalid login '.+') and (valid password|invalid password '.+')$",
                (String isLocked, Roles role, String loginType, String passwordType) -> {
                    User user = isLocked.isEmpty() ? DataUtils.getUserAndLock(new LoginCriteria(role))
                            : sharedData.getCurrentUser();
                    User changedUser = new User(user);
                    if (loginType.startsWith(IN)) {
                        changedUser.setLogin(CredentialUtils.retrieveCredential(loginType));
                    }
                    if (passwordType.startsWith(IN)) {
                        changedUser.setPassword(CredentialUtils.retrieveCredential(passwordType));
                    }
                    loginAction.submitLoginForm(changedUser);
                });

        When("^User with (.+) name and (.+) password logs in PayPal$",
                (String name, String password) -> payPalAction.loginToPayPal(name, password));

        When("^User creates new password (.+) on ConnectID page$", (String password) -> {
            loginAction.submitNewPassword(password);
            sharedData.setRegisteredUser(new User(sharedData.getEmailAddress(), password));
        });

        When("^User clicks on 'Continue to Hybris' button$", () -> loginAction.clickContinueToHybrisButton());

        When("^User with (.+) role fills in and submits login data in ASM mode page$",
                (String userId) -> loginAction.submitLoginOktaForm(DataUtils.readUser(userId)));

        When("^System registers upline with IBO_USER role and downline with (IBO_USER|Customer) role via http$",
                (Roles downlineRole) -> {
                    if (envProperties.getUserProvider().equals("user_pool")) {
                        loadUserFromExcelUserPool(downlineRole);
                    } else {
                        registerNewUplineIBOWithDownlineUser(downlineRole);
                    }
                });

        When("^System registers downline with (IBO_USER|Customer) role for current user via http$",
                (Roles downlineRole) -> {
                    User currentUser = sharedData.getCurrentUser();
                    sharedData.setUplineUser(currentUser);
                    String amwayAccount = currentUser.getAmwayAccount();
                    User downline = downlineRole.equals(Roles.IBO_USER)
                            ? registrationFacade.registerNewIbo(amwayAccount)
                            : registrationFacade.registerNewCustomer(amwayAccount);
                    sharedData.setDownlineUser(downline);
                });

        When("^System locks user with (.+) role$", (Roles role) -> {
            populateSiteLanguage();
            sharedData.setLockedUser(getUserAndLock(new LoginCriteria(role)));
        });

        When("^System locks registered user", () -> {
            populateSiteLanguage();
            sharedData.setLockedUser(sharedData.getRegisteredUser());
        });

        When("^System assigns user to his default Amway account$",
                () -> HybrisUserUtils.assignUserToDefaultAmwayAccount(sharedData.getCurrentUser()));

        When("^System registers user with (.+) role via (HTTP|UI)$", (Roles role, LoginStrategy strategy) -> {
            final RegistrationFacade facade = RegistrationFacade.getInstance(strategy);
            final User user = ofNullable(
                    ImmutableMap.of(
                            Roles.IBO_USER, (Supplier<User>) facade::registerNewIbo,
                            Roles.CUSTOMER, (Supplier<User>) facade::registerNewCustomer)
                            .get(role))
                    .orElseThrow(Exceptions.newUnsupportedRoleError(role)).get();
            sharedData.setRegisteredUser(user);
            sharedData.setCurrentUser(user);
        });

        When("^System registers upline and downline via http$",
                () -> registerNewUplineIBOWithDownlineUser(Roles.IBO_USER));

        When("Log in as (.+) to user-specific storefront", this::doResolveLoginForUser);

        When("Log in as current user to specific storefront", () -> doHandleLoginForUser(sharedData.getLockedUser()));

        When("Log in as Guest to specific storefront", this::doHandleStartSessionForGuest);

        When("^Test locks user with (.+) role$",
                (Roles roles) -> sharedData.setLockedUser(getUserAndLockWithoutCleanUp(roles)));

        When("Re-log in as registered user to user-specific storefront", () -> {
            final User user = sharedData.getRegisteredUser();
            sharedData.setUserRole(user.getRole());
            doHandleLoginForUser(user);
        });

        And("^set up addresses and payments for current user with backend script$", () -> {
            final User user = sharedData.getCurrentUser();
            populateSiteInformation(user);
            getBean(ImportDefaultUserDataGroovyScriptTask.class).doImport(user);
        });
    }

    private void doHandleLoginForUser(final User user) {
        sharedData.setCurrentUser(user);
        // SET BASE SITE ID FOR USER IF NO
        populateSiteInformation(user);
        populateSharedDataAddressInformation();
        // ON BEFORE LOGIN
        CallbackContext.publishEvent(EventType.BEFORE_USER_LOGIN);
        // GET HOME PAGE
        WebDriverUtils.load(envProperties.getLocalizedBaseUrl());
        WebDriverUtils.load(envProperties.getLocalizedBaseUrl() + "/sso/prepare");
        // SUBMIT LOGIN FORM
        loginAction.submitLoginForm(user);
        // WAIT BROWSER BEING REDIRECTED BACK TO STOREFRONT
        waitBrowserContainsBaseUrl();
        // ON AFTER LOGIN
        CallbackContext.publishEvent(EventType.AFTER_USER_LOGIN);
    }

    private void doHandleStartSessionForGuest() {
        final User user = new User(Roles.GUEST);
        sharedData.setCurrentUser(user);
        sharedData.setUserRole(user.getRole());
        // SET BASE SITE ID FOR USER IF NO
        populateSiteInformation(user);
        // GET HOME PAGE
        WebDriverUtils.load(envProperties.getLocalizedBaseUrl());
    }

    private void populateSharedDataAddressInformation() {
        sharedData.setShippingAddress(DataUtils.readPrimaryAddress(DEFAULT_ADDRESS));
    }

    private void populateSiteInformation(User user) {
        if (user.getBaseSiteId() == null) {
            user.setBaseSiteId(SiteId.getSiteForCountryCode(envProperties.getLocalization()));
        }

        Countries country = Countries.getByIsoCode(envProperties.getLocalization());
        if (user.getCountryEnum() == null) {
            user.setCountryEnum(country);
        }

        sharedData.setSiteLanguage(country.getLanguage());
    }

    private void waitBrowserContainsBaseUrl() {
        Failsafe.with(new RetryPolicy().withMaxRetries(100).withDelay(100, TimeUnit.MILLISECONDS)
                .retryIf(o -> !WebDriverUtils.getCurrentUrl().startsWith(envProperties.getBaseUrl())))
                .run(new CheckedRunnable() {
                    @Override
                    public void run() {
                        LoggerFactory.getLogger(getClass()).info("Wait for site loading ...");
                    }
                });
    }

    private void registerNewUplineIBOWithDownlineUser(Roles downlineRole) {
        sharedData.setUserRole(Roles.IBO_USER);
        User upline = registrationFacade.registerNewIbo();

        sharedData.setUplineUser(upline);
        User downline = downlineRole.equals(Roles.IBO_USER)
                ? registrationFacade.registerNewIbo(upline.getAmwayAccount())
                : registrationFacade.registerNewCustomer(upline.getAmwayAccount());
        sharedData.setDownlineUser(downline);
    }

    private void loadUserFromExcelUserPool(Roles downlineRole) {
        final Roles requiredRole;
        if (Roles.IBO_USER == downlineRole) {
            requiredRole = Roles.IBO_USER_UPLINE;
        } else {
            requiredRole = Roles.CUSTOMER_USER_UPLINE;
        }
        final User user = DataUtils.getUserAndLock(new LoginCriteria(requiredRole));
        sharedData.setCurrentUser(user);
        sharedData.setUplineUser(user);
        sharedData.setUplineUplineUser(user);
        if (!user.getDownline().isEmpty()) {
            sharedData.setDownlineUser(DataUtils.readUserByOktaToken(user.getDownline()));
        } else {
            throw new AmwayTestAutomationException("List of downlines is empty");
        }
    }

    private void loginByRole(final LoginCriteria loginCriteria, final Pages page, final boolean isLocked) {
        if (loginCriteria.getRole() != Roles.GUEST) {
            final User user;
            if (!isLocked) {
                user = getUserAndLock(loginCriteria);
            } else {
                user = sharedData.getLockedUser();
            }
            // ON BEFORE LOGIN
            CallbackContext.publishEvent(EventType.BEFORE_USER_LOGIN);

            navigationAction.navigateTo(page);

            if (page.equals(STOREFRONT) || page.equals(STOREFRONT_IN_ASM_MODE)) {
                headerAction.openLoginForm();
            }
            loginAction.submitLoginForm(user);
            WaitHelper.waitForJSandJQueryToLoad();
            navigationAction.waitForPageLoading(page);
            logNodeNumber(user);

            // ON AFTER LOGIN
            CallbackContext.publishEvent(EventType.AFTER_USER_LOGIN);
        } else {
            navigationAction.navigateTo(page);
            sharedData.setCurrentUser(new User(Roles.GUEST));
            navigationAction.waitForPageLoading(page);
        }
        setSiteLanguage(page);
    }

    private User getUserAndLock(final LoginCriteria loginCriteria) {
        User user;
        if (isUseRegistrationInsteadLoginForCurrentRole(loginCriteria.getRole())) {
            user = registerNewUserForRole(loginCriteria.getRole());
            sharedDataPopulator.populateUserDataToSharedData(user, sharedData);
        } else {
            user = sharedData.getCurrentUser();
            if (user == null || user.getLogin() == null) {
                user = envProperties.getUserProvider().equalsIgnoreCase(USER_POOL_LOGIN)
                        ? getUserFromUserPool(loginCriteria)
                        : getUserFromHybris(loginCriteria.getRole());
            }
        }
        return user;
    }

    private User getUserAndLockWithoutCleanUp(Roles role) {
        return DataUtils.getUserAndLockWithoutCleanUp(new LoginCriteria(role));
    }

    private User getUserFromUserPool(LoginCriteria loginCriteria) {
        return DataUtils.getUserAndLock(loginCriteria);
    }

    private User getUserFromHybris(Roles role) {
        User user = ConcurrentUserPool.getUserFromQueue(role);
        sharedData.setCurrentUser(user);
        return user;
    }

    private User registerNewUserForRole(Roles role) {
        if (Roles.IBO_USER.equals(role)) {
            return registrationFacade.registerNewIbo();
        } else {
            return registrationFacade.registerNewCustomer();
        }
    }

    private boolean isUseRegistrationInsteadLoginForCurrentRole(Roles role) {
        return (envProperties.isRegisterInsteadLogin() && Objects.isNull(sharedData.getCurrentUser()))
                && (Roles.IBO_USER.equals(role) || Roles.CUSTOMER.equals(role));
    }

    private void loginByUser(User user, Pages page) {
        navigationAction.navigateTo(page);
        headerAction.openLoginForm();
        loginAction.submitLoginForm(user);
        navigationAction.waitForPageLoading(page);
        logNodeNumber(user);
        setSiteLanguage(page);
    }

    private void fillAndSubmitCredentials(User user, Pages page) {
        loginAction.submitLoginForm(user);
        navigationAction.waitForPageLoading(page);
        logNodeNumber(user);
    }

    private void logNodeNumber(User currentUser) {
        LOG.info(String.format("User Logged in. Current cluster NODE is: [%s]. Current user: [%s]. JSESSIONID: [%s]",
                WebDriverUtils.getCookieValue(AmwayCookie.ROUTEID.getName()), currentUser.getLogin(),
                WebDriverUtils.getCookieValue(AmwayCookie.JSESSIONID.getName())));
    }

    private void setSiteLanguage(Pages page) {
        if (page.equals(STOREFRONT) || page.equals(STOREFRONT_IN_ASM_MODE)) {
            sharedData.setSiteLanguage(homeState.getSiteLanguage());
        }
    }

    private void populateSiteLanguage(String country) {
        if (country.isEmpty()) {
            populateSiteLanguage();
        } else {
            sharedData.setSiteLanguage(Countries.getByIsoCode(country.trim()).getLanguage());
        }
    }

    private void populateSiteLanguage() {
        sharedData.setSiteLanguage(Countries.getLocalizedValue().getLanguage());
    }

    private void doResolveLoginForUser(Roles role) {
        if (role != Roles.GUEST) {
            final User user = DataUtils.getUserAndLockWithoutCleanUp(LoginCriteria.of(role));
            doHandleLoginForUser(user);
        } else {
            doHandleStartSessionForGuest();
        }
    }
}
