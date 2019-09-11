package com.amway.uiautomation.utils;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;

import com.amway.integration.restapi.hybrisplatformwebservices.dto.CategoryDTO;
import com.amway.integration.restapi.lynxintegrationwebservices.dto.ProductDTO;
import com.amway.support.enums.Countries;
import com.amway.support.enums.LoginStrategy;
import com.amway.support.enums.Roles;
import com.amway.support.exception.Exceptions;
import com.amway.support.impex.UsersPreconditions;
import com.amway.support.models.*;
import com.amway.support.properties.EnvProperties;
import com.amway.testdata.DataSourceTemplate;
import com.amway.uiautomation.datasource.*;
import com.amway.uiautomation.datasource.addresses.DataSourceAddressesCA;
import com.amway.uiautomation.datasource.addresses.DataSourceAddressesDO;
import com.amway.uiautomation.datasource.addresses.DataSourceAddressesUS;
import com.amway.uiautomation.datasource.users.*;
import com.google.common.collect.ImmutableMap;

import static com.amway.support.consts.CommonConsts.*;
import static com.amway.support.guice.injector.GuiceInjector.getBean;
import static com.amway.uiautomation.utils.SystemPropertiesUtils.getLocalizationSystemProperty;

public class DataUtils {

    private static final EnvProperties ENV_PROPERTIES = getBean(EnvProperties.class);

    private static final ImmutableMap<Countries, DataSourceTemplate<User>> LOCALIZED_USERS_DATA_SOURCE = ImmutableMap
            .<Countries, DataSourceTemplate<User>>builder().put(Countries.UNITED_STATES, new DataSourceUsersUS())
            .put(Countries.CANADA, new DataSourceUsersCA()).put(Countries.DOMINICAN_REPUBLIC, new DataSourceUsersDO())
            .build();

    private static final ImmutableMap<Countries, DataSourceTemplate<User>> LOCALIZED_USERS_PERF_DATA_SOURCE = ImmutableMap
            .<Countries, DataSourceTemplate<User>>builder().put(Countries.UNITED_STATES, new DataSourceUsersPerfUS())
            .put(Countries.CANADA, new DataSourceUsersPerfCA()).build();

    private static final ImmutableMap<Countries, DataSourceTemplate<User>> LOCALIZED_SMOKE_USERS_DATA_SOURCE = ImmutableMap
            .<Countries, DataSourceTemplate<User>>builder()
            .put(Countries.UNITED_STATES, new DataSourceUsersConnectIdUS())
            .put(Countries.CANADA, new DataSourceUsersConnectIdCA())
            .put(Countries.DOMINICAN_REPUBLIC, new DataSourceUsersConnectIdDO()).build();

    private static final ImmutableMap<Countries, DataSourceTemplate<User>> LOCALIZED_SMOKE_USERS_QA_DATA_SOURCE = ImmutableMap
            .<Countries, DataSourceTemplate<User>>builder()
            .put(Countries.UNITED_STATES, new DataSourceUsersConnectIdUS())
            .put(Countries.CANADA, new DataSourceUsersConnectIdQACA())
            .put(Countries.DOMINICAN_REPUBLIC, new DataSourceUsersConnectIdDO()).build();

    private static final ImmutableMap<Countries, DataSourceTemplate<ReceiptUser>> LOCALIZED_RECEIPTS_USERS_DATA_SOURCE = ImmutableMap
            .<Countries, DataSourceTemplate<ReceiptUser>>builder()
            .put(Countries.UNITED_STATES, new DataSourceUsersReceiptsUS())
            .put(Countries.CANADA, new DataSourceUsersReceiptsCA())
            .put(Countries.DOMINICAN_REPUBLIC, new DataSourceUsersReceiptsDO()).build();

    private static final ImmutableMap<Countries, DataSourceTemplate<ShippingAddress>> LOCALIZED_ADDRESS_DATA_SOURCE = ImmutableMap
            .<Countries, DataSourceTemplate<ShippingAddress>>builder()
            .put(Countries.UNITED_STATES, new DataSourceAddressesUS())
            .put(Countries.CANADA, new DataSourceAddressesCA())
            .put(Countries.DOMINICAN_REPUBLIC, new DataSourceAddressesDO()).build();

    private static final ImmutableMap<String, DataSourceTemplate<ProductDTO>> products = ImmutableMap
            .<String, DataSourceTemplate<ProductDTO>>builder() //
            .put(PRODUCT.toUpperCase(), new DataSourceProduct()) //
            .put(DONATION.toUpperCase(), new DataSourceProduct()) //
            .put(BUNDLE.toUpperCase(), new DataSourceBundle()) //
            .build();

    private DataUtils() {
    }

    public static ProductDTO readProduct(String productID) {
        ProductDTO productDTO;
        try {
            productDTO = new DataSourceProduct().findOne(p -> p.getTestId().equals(productID));
        } catch (NoSuchElementException e) {
            productDTO = null;
        }
        return productDTO;
    }

    public static CategoryDTO readCategory(String categoryID) {
        return new DataSourceCategories().findOne(p -> p.getTestId().equals(categoryID));
    }

    public static ProductDTO readItem(String itemId, String itemType) {
        return products.get(itemType.toUpperCase()).where(p -> p.getTestId().equals(itemId)).findFirst().orElse(null);
    }

    public static User getUserAndLock(LoginCriteria loginCriteria) {
        ConcurrentUserPool.releaseUser();
        final User user = ConcurrentUserPool.waitForAvailableUser(loginCriteria);
        cleanUpUserForCurrentEnv(user);
        return user;
    }

    public static User getUserAndLockWithoutCleanUp(LoginCriteria loginCriteria) {
        ConcurrentUserPool.releaseUser();
        return ConcurrentUserPool.waitForAvailableUser(loginCriteria);
    }

    public static User getUserByRoleWithoutLock(Roles role) {
        return LOCALIZED_USERS_DATA_SOURCE.get(getLocalizationSystemProperty())
                .findOne(user -> user.getRole().equals(role));
    }

    public static String getUserNameByRole(String role) {
        return getUserByRoleWithoutLock(Roles.valueOf(role.toUpperCase())).getName();
    }

    public static User readUserByOktaToken(final String oktaToken) {
        return UsersPoolProviderStrategy.getUserPoolList(ENV_PROPERTIES.getUserPoolProvider()).stream()
                .filter(getFindUserByOktaTokenAndRole(oktaToken)).findFirst() //
                .orElseThrow(Exceptions.withMessage(String.format("There is no user with [%s] oktaToken", oktaToken)));
    }

    public static User readUserByEmail(final String userEmail) {
        return UsersPoolProviderStrategy.getUserPoolList(ENV_PROPERTIES.getUserPoolProvider()).stream()
                .filter(getFindUserByEmailPredicate(userEmail)).findFirst() //
                .orElseThrow(Exceptions.withMessage(String.format("There is no user with email [%s]", userEmail)));
    }

    public static User readUser(String userId) {
        return getCurrentUsersDataSourceTemplate().findOne(u -> u.getRole().toString().equalsIgnoreCase(userId));
    }

    public static UserProfile readUserProfile(String userProfileId) {
        return new DataSourceUserProfile().findOne(userProfile -> userProfile.getTestId().equals(userProfileId));
    }

    public static ShippingAddress readPrimaryAddress(String primaryAddressId) {
        final ShippingAddress shippingAddress = LOCALIZED_ADDRESS_DATA_SOURCE.get(getLocalizationSystemProperty())
                .findOne(sa -> sa.getTestId().equals(primaryAddressId));
        fillInFullAddressName(shippingAddress);
        return shippingAddress;
    }

    public static ShippingAddress readPrimaryAddress(String primaryAddressId, boolean isMac) {
        final ShippingAddress result = readPrimaryAddress(primaryAddressId,
                SystemPropertiesUtils.getLocalizationSystemProperty().getIsoCode());
        if (isMac) {
            result.setCountry(Countries.USA_MAC);
        }
        return result;
    }

    public static ShippingAddress readPrimaryAddress(String primaryAddressId, String localization) {
        final ShippingAddress result = LOCALIZED_ADDRESS_DATA_SOURCE.get(Countries.getByIsoCode(localization))
                .findOne(shippingAddress -> shippingAddress.getTestId().equals(primaryAddressId));
        fillInFullAddressName(result);
        return result;
    }

    public static CreditCard readCreditCard(String cardId) {
        return new DataSourceCreditCard().findOne(cc -> cc.getTestId().equals(cardId));
    }

    public static Image readImage(String imageId) {
        return new DataSourceImage().findOne(i -> i.getTestId().equals(imageId));
    }

    public static IBOUser readIBOUser(String iboUserId) {
        return new DataSourceUsersBC().findOne(u -> u.getTestId().equals(iboUserId));
    }

    public static TextMessage readTestData(String testDataId) {
        return new DataSourceTestData().findOne(textMessage -> textMessage.getTestId().equals(testDataId));
    }

    public static String getExpectedTextFromTestData(String testDataId) {
        return readTestData(testDataId).getExpectedText();
    }

    public static PriceRow readPriceRow(String productCode) {
        return new DataSourcePriceRow().findOne(pr -> pr.getProductCode().equals(productCode));
    }

    public static IBOUser readUserBCForARWidget(String userId, String widget) {
        return new DataSourceUsersBC().findOne(u -> u.getTestId().equals(String.format("%s %s", userId, widget)));
    }

    public static IBOUser readBCUser(String userId) {
        return new DataSourceUsersBC().findOne(u -> u.getTestId().equals(userId));
    }

    public static Resources readResources(String resourceId) {
        return new DataSourceResource().findOne(r -> r.getTestId().equals(resourceId));
    }

    public static ReceiptUser readReceiptUser(String receiptUserId) {
        return LOCALIZED_RECEIPTS_USERS_DATA_SOURCE.get(getLocalizationSystemProperty())
                .findOne(user -> user.getTestId().equals(receiptUserId));
    }

    public static User getUserById(String userId) {
        return LOCALIZED_USERS_DATA_SOURCE.get(Countries.UNITED_STATES)
                .findOne(u -> u.getRole().toString().equalsIgnoreCase(userId));
    }

    public static String getItemCode(String itemId) {
        return Objects.isNull(readProduct(itemId)) ? itemId : readProduct(itemId).getProductCode();
    }

    static List<User> getAllUsers() {
        return getCurrentUsersDataSourceTemplate().findAll();
    }

    private static Predicate<User> getFindUserByOktaTokenAndRole(String oktaToken) {
        return u -> u.getRole() != null && u.getOktaToken() != null && u.getOktaToken().equals(oktaToken);
    }

    private static Predicate<User> getFindUserByEmailPredicate(final String email) {
        return u -> u.getRole() != null && u.getLogin() != null && u.getLogin().equals(email);
    }

    private static void cleanUpUserForCurrentEnv(User user) {
        if (getBean(EnvProperties.class).getEnvSpecificProperties().isHacAccessible()) {
            final UsersPreconditions usersImpexService = getBean(UsersPreconditions.class);
            usersImpexService.cleanupUser(user);
            usersImpexService.cleanupUserDownlineIfPresent(user);
        }
    }

    private static DataSourceTemplate<User> getCurrentUsersDataSourceTemplate() {
        DataSourceTemplate<User> dataSourceTemplate;
        if (LoginStrategy.UI_CONNECT_ID.equals(ENV_PROPERTIES.getLogInStrategy())) {
            dataSourceTemplate = LOCALIZED_SMOKE_USERS_DATA_SOURCE.get(getLocalizationSystemProperty());
        } else if (LoginStrategy.UI_CONNECT_ID_QA.equals(ENV_PROPERTIES.getLogInStrategy())) {
            dataSourceTemplate = LOCALIZED_SMOKE_USERS_QA_DATA_SOURCE.get(getLocalizationSystemProperty());
        } else {
            dataSourceTemplate = LOCALIZED_USERS_DATA_SOURCE.get(getLocalizationSystemProperty());
        }
        return dataSourceTemplate;
    }

    private static void fillInFullAddressName(ShippingAddress result) {
        if (Objects.isNull(result.getFullName())) {
            if (Objects.isNull(result.getLastName())) {
                result.setFullName(result.getFirstName());
            } else if (Objects.isNull(result.getFirstName())) {
                result.setFullName(result.getLastName());
            } else {
                result.setFullName(String.format(TWO_STRINGS_CONCAT, result.getFirstName(), result.getLastName()));
            }
        }
    }
}