@storefront
@component_product
@epic_product_listing_page
Feature: Product Listing Page

  Scenario Outline: Verify that IBO able to sort items on PLP [WR-13765, WR-13768, WR-13761]
    Given User with ibo_user role logs in to storefront page
    And System runs product index job via Script Execution
    When User navigates to existed product's supercategory <category>
    Then Title of category <category> is displayed on PLP
    And Default count of products is up to <default_count> on PLP
    And User clicks on 'Show more' button till all products in category are displayed on PLP
    Then All items are displayed for category <category> on PLP
    # by name
    When User selects NAME_DESC sorting option on PLP
    And User clicks on 'Show more' button till all products in category are displayed on PLP
    Then All PLP items should be sorted by NAME_DESC order (hero products amount is 0)
    When User selects NAME_ASC sorting option on PLP
    And User clicks on 'Show more' button till all products in category are displayed on PLP
    Then All PLP items should be sorted by NAME_ASC order (hero products amount is 0)
    # by price
    When User applies sorting by PRICE_ASC option
    And User clicks on 'Show more' button till all products in category are displayed on PLP
    Then All PLP items should be sorted by PRICE_ASC order (hero products amount is 0)
    When User applies sorting by PRICE_DESC option
    And User clicks on 'Show more' button till all products in category are displayed on PLP
    Then All PLP items should be sorted by PRICE_DESC order (hero products amount is 0)
    # by category
    When User selects CATEGORY_DESC sorting option on PLP
    And User clicks on 'Show more' button till all products in category are displayed on PLP
    Then All PLP items should be sorted by CATEGORY_DESC order (hero products amount is 0)
    When User selects CATEGORY_ASC sorting option on PLP
    And User clicks on 'Show more' button till all products in category are displayed on PLP
    Then All PLP items should be sorted by CATEGORY_ASC order (hero products amount is 0)
    # by novelty
    When User selects Oldest First sorting option on PLP
    And User clicks on 'Show more' button till all products in category are displayed on PLP
    And System gets all items from <category> category in <catalog> catalog via Flexible Search
    Then All PLP items should be sorted by ADDED DATE(OLDEST...NEWEST) order (hero products amount is 0)
    When User selects Newest First sorting option on PLP
    And User clicks on 'Show more' button till all products in category are displayed on PLP
    And System gets all items from <category> category in <catalog> catalog via Flexible Search
    Then All PLP items should be sorted by ADDED DATE(NEWEST...OLDEST) order (hero products amount is 0)
    When User selects POINT_VALUE_ASC sorting option on PLP
    And User clicks on 'Show more' button till all products in category are displayed on PLP
    Then All PLP items should be sorted by POINT_VALUE_ASC order (hero products amount is 0)
    When User selects POINT_VALUE_DESC sorting option on PLP
    And User clicks on 'Show more' button till all products in category are displayed on PLP
    Then All PLP items should be sorted by POINT_VALUE_DESC order (hero products amount is 0)

  @CA@US
    Examples:
      | category    | catalog                     | default_count |
      | Supplements | Lynx Product Catalog Online | 9             |

  @DO
    Examples:
      | category    | catalog                     | default_count |
      | Supplements | Lynx Product Catalog Online | 9             |

  Scenario Outline: Verify that Guest/Customer is able to view and sort items on PLP [WR-13768, WR-13761]
    Given User with <user> role logs in to storefront page
    And System runs product index job via Script Execution
    When User navigates to existed product's supercategory <category>
    Then Title of category <category> is displayed on PLP
    And Default count of products is up to <default_count> on PLP
    And User clicks on 'Show more' button till all products in category are displayed on PLP
    Then All items are displayed for category <category> on PLP
    # by name
    When User selects NAME_ASC sorting option on PLP
    And User clicks on 'Show more' button till all products in category are displayed on PLP
    Then All PLP items should be sorted by NAME_ASC order (hero products amount is 0)
    When User selects NAME_DESC sorting option on PLP
    And User clicks on 'Show more' button till all products in category are displayed on PLP
    Then All PLP items should be sorted by NAME_DESC order (hero products amount is 0)
    # by price
    When User applies sorting by PRICE_ASC option
    And User clicks on 'Show more' button till all products in category are displayed on PLP
    Then All PLP items should be sorted by PRICE_ASC order (hero products amount is 0)
    When User applies sorting by PRICE_DESC option
    And User clicks on 'Show more' button till all products in category are displayed on PLP
    Then All PLP items should be sorted by PRICE_DESC order (hero products amount is 0)
    # by category
    When User selects CATEGORY_DESC sorting option on PLP
    And User clicks on 'Show more' button till all products in category are displayed on PLP
    Then All PLP items should be sorted by CATEGORY_DESC order (hero products amount is 0)
    When User selects CATEGORY_ASC sorting option on PLP
    And User clicks on 'Show more' button till all products in category are displayed on PLP
    Then All PLP items should be sorted by CATEGORY_ASC order (hero products amount is 0)
    # by novelty
    When User selects Oldest First sorting option on PLP
    And User clicks on 'Show more' button till all products in category are displayed on PLP
    And System gets all items from <category> category in <catalog> catalog via Flexible Search
    Then All PLP items should be sorted by ADDED DATE(OLDEST...NEWEST) order (hero products amount is 0)
    When User selects Newest First sorting option on PLP
    And User clicks on 'Show more' button till all products in category are displayed on PLP
    And System gets all items from <category> category in <catalog> catalog via Flexible Search
    Then All PLP items should be sorted by ADDED DATE(NEWEST...OLDEST) order (hero products amount is 0)

  @CA@US
    Examples:
      | user     | category    | catalog                     | default_count |
      | CUSTOMER | Supplements | Lynx Product Catalog Online | 9             |
      | GUEST    | Supplements | Lynx Product Catalog Online | 9             |

  @DO
    Examples:
      | user  | category    | catalog                     | default_count |
      | GUEST | Supplements | Lynx Product Catalog Online | 9             |

  Scenario Outline: Verify that IBO is able to view Product price, basic information and Hero image on PLP [WR-13764, WR-13763]
    Given User with <user> role logs in to storefront page
    When User navigates to existed product's supercategory <category>
    And User clicks on 'Show more' button till all products in category are displayed on PLP
    Then Title of category <category> is displayed on PLP
    And All items have RETAIL_PRICE on PLP
    And All items have IBO_PRICE on PLP
    And All items have PV/BV price on PLP
    And All items have name and sku number on PLP
    And Image for <with_ibo_promo> product is visible on PLP
    And User sees product <with_ibo_promo> with absolute promo ibo price on PLP according to Flexible Search result

  @CA
    Examples:
      | user     | with_ibo_promo | category     |
      | ibo_user | 103977C        | Joint Health |

  @US
    Examples:
      | user     | with_ibo_promo | category |
      | ibo_user | 100186         | Shop     |

  @DO
    Examples:
      | user     | with_ibo_promo | category |
      | ibo_user | 100186         | Shop     |

  Scenario Outline: Verify that Customer able to view Product price, basic information and Hero image on PLP [WR-13766, WR-25967, WR-13763]
    Given User with <user> role logs in to storefront page
    And System deletes all products from Shopping Cart for User via Impex
    When User navigates to existed product's supercategory <category>
    Then Title of category <category> is displayed on PLP
    When User clicks on 'Show more' button till all products in category are displayed on PLP
    Then All items have name and sku number on PLP
    And Image for <with_customer_promo> product is visible on PLP
    Then User sees product <with_customer_promo> with absolute promo retail price on PLP according to Flexible Search result
    When User adds product <with_customer_promo> to Shopping cart from PLP
    And User navigates to Shopping Cart Page
    Then User sees product <with_customer_promo> retail price in Shopping Cart according to Flexible Search result
    And Retail price of product <with_customer_promo> is strike-through
    And User sees product <with_customer_promo> additional RETAIL_PRICE in Shopping Cart according to Flexible Search result

  @CA
    Examples:
      | user     | with_customer_promo | category    |
      | customer | 100280C             | Supplements |

  @US
    Examples:
      | user     | with_customer_promo | category    |
      | customer | 100280              | Supplements |

  Scenario Outline: Verify that Guest user is able to view Product price, basic information and Hero image on PLP [WR-14542, WR-13763]
    Given User with <user> role logs in to storefront page
    When User navigates to existed product's supercategory <category>
    And User clicks on 'Show more' button till all products in category are displayed on PLP
    Then User sees product <with_guest_promo> with absolute promo retail price on PLP according to Flexible Search result
    And Title of category <category> is displayed on PLP
    And All items have name and sku number on PLP
    And Image for <with_guest_promo> product is visible on PLP

  @CA
    Examples:
      | user  | with_guest_promo | category    |
      | guest | 100280C          | Supplements |

  @US@DO
    Examples:
      | user  | with_guest_promo | category    |
      | guest | 100280           | Supplements |