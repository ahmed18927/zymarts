
CREATE TABLE IF NOT EXISTS `admins` (
	`id` BIGINT UNSIGNED AUTO_INCREMENT ,
	`name` VARCHAR(255),
	`email` VARCHAR(255) UNIQUE,
	`phone` VARCHAR(255) UNIQUE,
	`verify_email`  DATETIME,
	`verify_phone`  DATETIME DEFAULT NULL,
	`password` VARCHAR(255),
	`phone_code` VARCHAR(255),
	`verify_code` VARCHAR(255),
	`image` VARCHAR(255) DEFAULT NULL ,
	`role` VARCHAR(255) NOT NULL DEFAULT 'ADMIN',
	`created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
);


CREATE TABLE IF NOT EXISTS `companies` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255),
    `domain` VARCHAR(255),
    `is_active` BOOLEAN DEFAULT 0,
    `package_id` BIGINT UNSIGNED NULL,
    `added_by` BIGINT UNSIGNED,
    `logo` VARCHAR(255),
    `theme_id` BIGINT UNSIGNED NULL,
    `custom_theme_settings` TEXT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(`id`)
) ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS `stores` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
	`name` VARCHAR(255),
	`company_id` BIGINT UNSIGNED NULL,
	 `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
);


CREATE TABLE IF NOT EXISTS `customers` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
	`company_id` BIGINT UNSIGNED NULL,
	`name` VARCHAR(255),
	`phone` VARCHAR(255) UNIQUE,
	`email` VARCHAR(255) UNIQUE,
	`password` VARCHAR(255),
	`image` VARCHAR(255),
	`verify_email`  DATETIME,
	`verify_phone`  DATETIME DEFAULT NULL,
	`verify_code` VARCHAR(255),
	`created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
);


CREATE TABLE IF NOT EXISTS `addresses` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
	`customer_id` BIGINT UNSIGNED NULL,
	`address` VARCHAR(255),
	`city_id` BIGINT UNSIGNED NULL,
	`country_id` BIGINT UNSIGNED NULL,
	`is_default` BOOLEAN,
	`is_active` BOOLEAN DEFAULT 0,
	`note` TEXT,
	 `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
);



-- ==========================
-- Table: settings
-- ==========================
CREATE TABLE IF NOT EXISTS settings (
    id BIGINT NOT NULL AUTO_INCREMENT,
    setting_key VARCHAR(255) NOT NULL,  -- renamed from 'key'
    value VARCHAR(255),
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ==========================
-- Table: addresses
-- ==========================
-- Addresses table
CREATE TABLE IF NOT EXISTS addresses (
    id BIGINT NOT NULL AUTO_INCREMENT,
    street VARCHAR(255),
    city VARCHAR(100),
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Address Translations table
--CREATE TABLE IF NOT EXISTS address_translations (
--    id BIGINT NOT NULL AUTO_INCREMENT,
--    address_id BIGINT NOT NULL,      -- same type as addresses.id
--    language_code VARCHAR(10),
--    translation TEXT,
--    PRIMARY KEY (id),
--    CONSTRAINT FK_address_translations_address FOREIGN KEY (address_id)
--        REFERENCES addresses(id)
--        ON DELETE CASCADE
--        ON UPDATE CASCADE
--) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE IF NOT EXISTS `sub_categories` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
	`category_id` BIGINT UNSIGNED NULL,
	`name` VARCHAR(255),
	`is_active` BOOLEAN DEFAULT 0,
	`ordering` INTEGER UNSIGNED NULL,
	`added_by` BIGINT UNSIGNED NULL,
	`parent_id` BIGINT UNSIGNED NULL,
	 `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
);

CREATE TABLE IF NOT EXISTS `sub_category_translations` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
	`name` VARCHAR(255),
	`description` VARCHAR(255) DEFAULT NULL,
	`locale` VARCHAR(255),
	`sub_category_id` BIGINT UNSIGNED,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
);

CREATE TABLE IF NOT EXISTS `products` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
	`company_id` BIGINT UNSIGNED NULL,
	`category_id` BIGINT UNSIGNED NULL,
	`sub_category_id` BIGINT UNSIGNED NULL,
	`name` VARCHAR(255),
	`sell_price` DECIMAL(10,2),
	`stock` INTEGER UNSIGNED,
	`is_active` BOOLEAN DEFAULT 0,
	`ordering` INTEGER UNSIGNED,
	`added_by` BIGINT UNSIGNED NULL,
	`store_id` BIGINT UNSIGNED NULL,
	`avg_rate` DECIMAL(10,2),
	`buying_price` DECIMAL(10,2),
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
);


    CREATE TABLE IF NOT EXISTS `product_details` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
	`product_id` BIGINT UNSIGNED NULL,
	`description` TEXT(65535),
	`specifications` TEXT,
	`sku` VARCHAR(255),
	`mpn` VARCHAR(255),
	`views` BIGINT UNSIGNED,
	`sold_quentity` BIGINT UNSIGNED,
	`discount_type` VARCHAR(255),
	`discount_value` DECIMAL(10,2),
	`start_date` DATETIME  DEFAULT NULL,
	`end_date` DATETIME  DEFAULT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
);


CREATE TABLE IF NOT EXISTS `product_images` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
	`product_id` BIGINT UNSIGNED NULL,
	`media_url` VARCHAR(255),
	`is_main` BOOLEAN DEFAULT 0,
	`media_name` VARCHAR(255),
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
);


CREATE TABLE IF NOT EXISTS `coupons` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
	`code` VARCHAR(255),
	`discount_type` VARCHAR(255),
	`discount_value` DECIMAL(10,2),
	`max_used` INTEGER UNSIGNED,
	`used` INTEGER UNSIGNED,
	`is_active` BOOLEAN DEFAULT 0,
	`start_date` DATETIME  DEFAULT NULL,
	`end_date` DATETIME  DEFAULT NULL,
	`image` VARCHAR(255),
	`is_free_shipping` BOOLEAN DEFAULT 0,
	`max_use_per_user` INTEGER UNSIGNED,
	`applied_on_discount` BOOLEAN DEFAULT 0,
	`company_id` BIGINT UNSIGNED NULL,
	`store_id` BIGINT UNSIGNED NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
)ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS `banners` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT  ,
	`company_id` BIGINT UNSIGNED NULL,
	`image` VARCHAR(255),
	`link` VARCHAR(255),
	`ordering` INTEGER UNSIGNED,
	`is_active` BOOLEAN DEFAULT 0,
	`start_date` DATETIME  DEFAULT NULL,
	`end_date` DATETIME  DEFAULT NULL,
	`category_id` BIGINT UNSIGNED NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
);


CREATE TABLE IF NOT EXISTS `carts` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
	`customer_id` BIGINT UNSIGNED,
	`guest_token` VARCHAR(255),
	`start_date` DATETIME  DEFAULT  NULL,
	`store_id` BIGINT UNSIGNED NULL,
	`coupon_id` BIGINT UNSIGNED NULL,
	`company_id` BIGINT UNSIGNED NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
);


CREATE TABLE IF NOT EXISTS `cart_items` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
	`cart_id` BIGINT UNSIGNED NULL,
	`product_id` BIGINT UNSIGNED NULL,
	`quantity` INTEGER UNSIGNED,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
);


CREATE TABLE IF NOT EXISTS `orders` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
	`customer_id` BIGINT UNSIGNED NULL,
	`address_id` BIGINT UNSIGNED NULL,
	`status` VARCHAR(255),
	`company_id` BIGINT UNSIGNED NULL,
	`store_id` BIGINT UNSIGNED NULL,
	`coupon_id` BIGINT UNSIGNED NULL,
	`coupon_data` TEXT(65535),
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
);


CREATE TABLE IF NOT EXISTS `order_items` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
	`order_id` BIGINT UNSIGNED NULL,
	`product_id` BIGINT UNSIGNED NULL,
	`price` DECIMAL(10,2),
	`quantity` INTEGER UNSIGNED,
	`product_data` TEXT,
	`total_price` DECIMAL(10,2),
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
);


CREATE TABLE IF NOT EXISTS `order_prices` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
	`order_id` BIGINT UNSIGNED NULL,
	`sub_total` DECIMAL(10,2),
	`discount` DECIMAL(10,2),
	`shipping_price` DECIMAL(10,2),
	`total_price` DECIMAL(10,2),
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
);


CREATE TABLE IF NOT EXISTS `rates` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
	`customer_id` BIGINT UNSIGNED NULL,
	`order_id` BIGINT UNSIGNED NULL,
	`rating` INTEGER UNSIGNED,
	`comment` VARCHAR(255),
	`company_id` BIGINT UNSIGNED NULL,
	`store_id` BIGINT UNSIGNED NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
);


CREATE TABLE IF NOT EXISTS `favorites` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
	`customer_id` BIGINT UNSIGNED NULL,
	`product_id` BIGINT UNSIGNED NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
);



CREATE TABLE IF NOT EXISTS `wallets` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
	`company_id` BIGINT UNSIGNED NULL,
	`balance` DECIMAL(10,2) DEFAULT 0,
	`customer_id` BIGINT UNSIGNED NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
);


CREATE TABLE IF NOT EXISTS `transactions` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
	`wallet_id` BIGINT UNSIGNED NULL,
	`subscription_id` BIGINT UNSIGNED NULL,
	`amount` DECIMAL(10,2),
	`type` VARCHAR(255),
	`balance_before` DECIMAL(10,2),
	`balance_after` DECIMAL(10,2),
	`order_id` BIGINT UNSIGNED NULL,
	`payment_data` TEXT(65535),
	`payment_status` VARCHAR(255),
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
);


CREATE TABLE IF NOT EXISTS `packages` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
--	`name` VARCHAR(255),
	`price` DECIMAL(10,2),
	`max_products` INTEGER UNSIGNED,
	`features` JSON,
	`max_order` INTEGER UNSIGNED,
	`is_active` BOOLEAN DEFAULT 0,
	`ordering` INTEGER UNSIGNED,
	`duration` INTEGER UNSIGNED,
	`added_by` BIGINT UNSIGNED,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
);

--CREATE TABLE IF NOT EXISTS `package_translations` (
--	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
--	`name` VARCHAR(255),
--	`description` VARCHAR(255) DEFAULT NULL,
--	`locale` VARCHAR(255),
--	`package_id` BIGINT UNSIGNED,
--    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
--	PRIMARY KEY(`id`)
--);
CREATE TABLE IF NOT EXISTS package_translations (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    package_id BIGINT UNSIGNED NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    locale VARCHAR(10) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_package_locale (package_id, locale),
    CONSTRAINT fk_package_translation_package
        FOREIGN KEY (package_id)
        REFERENCES packages(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `subscriptions` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
	`company_id` BIGINT UNSIGNED NULL,
	`package_id` BIGINT UNSIGNED NULL,
	`start_date` DATETIME  DEFAULT NULL,
	`end_date` DATETIME  DEFAULT NULL,
	`status` VARCHAR(255),
	`package_data` TEXT,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
);


CREATE TABLE IF NOT EXISTS `notifications` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
	`customer_id` BIGINT UNSIGNED NULL,
	`title` VARCHAR(255),
	`body` TEXT(65535),
	`type` VARCHAR(255),
	`is_read` BOOLEAN DEFAULT 0,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
);


CREATE TABLE IF NOT EXISTS `employees` (
	`id` BIGINT UNSIGNED AUTO_INCREMENT ,
	`name` VARCHAR(255),
	`email` VARCHAR(255) UNIQUE,
	`phone` VARCHAR(255) UNIQUE,
	`verify_email`  DATETIME,
	`verify_phone`  DATETIME DEFAULT NULL,
	`password` VARCHAR(255),
	`verify_code` VARCHAR(255),
	`image` VARCHAR(255),
	`company_id` BIGINT UNSIGNED NULL,
	`is_main` BOOLEAN DEFAULT 0,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
);


CREATE TABLE IF NOT EXISTS `package_permissions` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
	`name` VARCHAR(255),
	`route_name` VARCHAR(255),
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
);


CREATE TABLE IF NOT EXISTS `package_roles` (
	`id` BIGINT UNSIGNED AUTO_INCREMENT ,
	`name` VARCHAR(255),
	`added_by` BIGINT UNSIGNED,
	`is_active` BOOLEAN DEFAULT 0,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
);


CREATE TABLE IF NOT EXISTS `package_role_permission` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
	`package_role_id` BIGINT UNSIGNED NULL,
	`package_permission_id` BIGINT UNSIGNED NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
);


CREATE TABLE IF NOT EXISTS `package_role` (
	`id` BIGINT UNSIGNED AUTO_INCREMENT ,
	`package_id` BIGINT UNSIGNED NULL,
	`package_role_id` BIGINT UNSIGNED NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
);


CREATE TABLE IF NOT EXISTS `bannar_products` (
	`id` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
	`banner_id` BIGINT UNSIGNED NULL,
	`product_id` BIGINT UNSIGNED NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
);


CREATE TABLE IF NOT EXISTS `product_rates` (
	`id` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
	`rate_id` BIGINT UNSIGNED NULL,
	`product_id` BIGINT UNSIGNED NULL,
	`rating` INTEGER UNSIGNED,
	`comment` VARCHAR(255),
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
);


CREATE TABLE IF NOT EXISTS `countries` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
	`name` VARCHAR(255),
	`code` VARCHAR(255),
	`flag` VARCHAR(255),
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
);


CREATE TABLE IF NOT EXISTS `cities` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
	`country_id` BIGINT UNSIGNED NULL,
	`name` VARCHAR(255),
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
);


CREATE TABLE IF NOT EXISTS `categories` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
	`name` VARCHAR(255),
	`image` VARCHAR(255),
	`is_active` BOOLEAN DEFAULT 0,
	`ordering` INTEGER UNSIGNED,
	`added_by` BIGINT UNSIGNED NULL,
	`store_id` BIGINT UNSIGNED NULL,
	`company_id` BIGINT UNSIGNED NULL,
	`slug` VARCHAR(255),
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
);
CREATE TABLE IF NOT EXISTS product_translations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT UNSIGNED  NOT NULL,
    lang_code VARCHAR(2) NOT NULL,
    name VARCHAR(255),
    description TEXT,
    specifications TEXT,
    CONSTRAINT fk_product_translation_product
    FOREIGN KEY (product_id)
    REFERENCES products(id)
    ON DELETE CASCADE,
    CONSTRAINT uq_product_lang
    UNIQUE (product_id, lang_code)
);
CREATE TABLE IF NOT EXISTS `category_transactions` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
	`name` VARCHAR(255),
	`description` VARCHAR(255) DEFAULT NULL,
	`locale` VARCHAR(255),
	`category_id` BIGINT UNSIGNED,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
);

CREATE TABLE IF NOT EXISTS  address_translations (
    id BIGINT UNSIGNED  AUTO_INCREMENT PRIMARY KEY,
    address_id BIGINT UNSIGNED  NOT NULL,
    lang_code VARCHAR(5) NOT NULL,
    address_line TEXT,
    note TEXT,
    CONSTRAINT fk_address_translation_address FOREIGN KEY (address_id) REFERENCES addresses(id) ON DELETE CASCADE,
    CONSTRAINT uk_address_lang UNIQUE (address_id, lang_code)
);

CREATE TABLE IF NOT EXISTS `faqs` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
    `company_id` BIGINT UNSIGNED NULL,
    `question` TEXT NOT NULL,
    `answer` TEXT NOT NULL,
    `is_active` BOOLEAN DEFAULT 1,
    `ordering` INTEGER UNSIGNED DEFAULT 0,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(`id`)
)ENGINE=InnoDB;

-- FAQs Translation Table
CREATE TABLE IF NOT EXISTS `faq_translations` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
    `faq_id` BIGINT UNSIGNED NOT NULL,
    `lang_code` VARCHAR(5) NOT NULL,
    `question` TEXT NOT NULL,
    `answer` TEXT NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(`id`),
    CONSTRAINT `fk_faq_translation_faq`
    FOREIGN KEY (`faq_id`)
    REFERENCES `faqs`(`id`)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
    CONSTRAINT `uq_faq_lang`
    UNIQUE (`faq_id`, `lang_code`)
);

CREATE TABLE IF NOT EXISTS `home_sections` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
    -- ── Default English content (fallback) ──
    `hero_title_en`         VARCHAR(255) NOT NULL,
    `hero_subtitle_en`      TEXT         NOT NULL,
    `cta_primary_text_en`   VARCHAR(255) NOT NULL,
    `cta_secondary_text_en` VARCHAR(255)     DEFAULT NULL,

    -- ── CTA Links ──
    `cta_primary_link`      VARCHAR(255)     DEFAULT NULL,
    `cta_secondary_link`    VARCHAR(255)     DEFAULT NULL,

    -- ── Background Media ──
    `background_image_url`  VARCHAR(255)     DEFAULT NULL,
    `background_video_url`  VARCHAR(255)     DEFAULT NULL,

    -- ── Stats ──
    `total_clients`         INTEGER UNSIGNED DEFAULT 0,
    `total_orders`          INTEGER UNSIGNED DEFAULT 0,
    `total_products`        INTEGER UNSIGNED DEFAULT 0,

    `is_active`             BOOLEAN          DEFAULT 0,

    `created_at`            TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY(`id`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `themes`(
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255),
    `description` TEXT,
    `css_variables` TEXT,
    `layout` VARCHAR(255),
    `is_active` BOOLEAN DEFAULT 1,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(`id`)
)ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `home_section_translations` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
    `home_section_id` BIGINT UNSIGNED NOT NULL,
    `lang_code`       VARCHAR(5) NOT NULL,
    `hero_title`          VARCHAR(255) DEFAULT NULL,
    `hero_subtitle`       TEXT DEFAULT NULL,
    `cta_primary_text`    VARCHAR(255) DEFAULT NULL,
    `cta_secondary_text`  VARCHAR(255) DEFAULT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(`id`),
        CONSTRAINT `uq_home_section_lang`
            UNIQUE (`home_section_id`, `lang_code`),
            CONSTRAINT `fk_home_section_translation`
            FOREIGN KEY (`home_section_id`)
            REFERENCES `home_sections`(`id`)
            ON UPDATE CASCADE
            ON DELETE CASCADE
) ENGINE=InnoDB;
-- ALTER TABLE carts
--     MODIFY COLUMN customer_id BIGINT NULL,
--     MODIFY COLUMN guest_token VARCHAR(64) NULL;
--
-- ALTER TABLE cart_items
--     ADD COLUMN price DECIMAL(12,2) NOT NULL DEFAULT 0,
--     ADD COLUMN coupon_id BIGINT NULL,
--     ADD CONSTRAINT fk_cart_items_coupon FOREIGN KEY (coupon_id) REFERENCES coupons(id);
--
-- -- Guest cart lookups happen very frequently — index it.
-- CREATE INDEX idx_carts_guest_token ON carts(guest_token);
-- CREATE INDEX idx_carts_customer_id ON carts(customer_id);
#
--# ALTER TABLE `faqs`
--# ADD FOREIGN KEY(`company_id`) REFERENCES `companies`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `sub_categories`
--# ADD FOREIGN KEY(`category_id`) REFERENCES `categories`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `sub_categories`
--# ADD FOREIGN KEY(`parent_id`) REFERENCES `sub_categories`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `products`
--# ADD FOREIGN KEY(`category_id`) REFERENCES `categories`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `products`
--# ADD FOREIGN KEY(`sub_category_id`) REFERENCES `sub_categories`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `product_images`
--# ADD FOREIGN KEY(`product_id`) REFERENCES `products`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `product_details`
--# ADD FOREIGN KEY(`product_id`) REFERENCES `products`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `banners`
--# ADD FOREIGN KEY(`category_id`) REFERENCES `categories`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `bannar_products`
--# ADD FOREIGN KEY(`banner_id`) REFERENCES `banners`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `bannar_products`
--# ADD FOREIGN KEY(`product_id`) REFERENCES `products`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `product_rates`
--# ADD FOREIGN KEY(`rate_id`) REFERENCES `rates`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `product_rates`
--# ADD FOREIGN KEY(`product_id`) REFERENCES `products`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `order_items`
--# ADD FOREIGN KEY(`order_id`) REFERENCES `orders`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `order_prices`
--# ADD FOREIGN KEY(`order_id`) REFERENCES `orders`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `orders`
--# ADD FOREIGN KEY(`coupon_id`) REFERENCES `coupons`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `order_items`
--# ADD FOREIGN KEY(`product_id`) REFERENCES `products`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `stores`
--# ADD FOREIGN KEY(`company_id`) REFERENCES `companies`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `company_details`
--# ADD FOREIGN KEY(`company_id`) REFERENCES `companies`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `employees`
--# ADD FOREIGN KEY(`company_id`) REFERENCES `companies`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `orders`
--# ADD FOREIGN KEY(`company_id`) REFERENCES `companies`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `banners`
--# ADD FOREIGN KEY(`company_id`) REFERENCES `companies`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `rates`
--# ADD FOREIGN KEY(`company_id`) REFERENCES `companies`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `rates`
--# ADD FOREIGN KEY(`store_id`) REFERENCES `stores`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `coupons`
--# ADD FOREIGN KEY(`company_id`) REFERENCES `companies`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `coupons`
--# ADD FOREIGN KEY(`store_id`) REFERENCES `stores`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `rates`
--# ADD FOREIGN KEY(`customer_id`) REFERENCES `customers`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `transactions`
--# ADD FOREIGN KEY(`wallet_id`) REFERENCES `wallets`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `transactions`
--# ADD FOREIGN KEY(`order_id`) REFERENCES `orders`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `transactions`
--# ADD FOREIGN KEY(`subscription_id`) REFERENCES `subscriptions`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `wallets`
--# ADD FOREIGN KEY(`company_id`) REFERENCES `companies`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `wallets`
--# ADD FOREIGN KEY(`customer_id`) REFERENCES `customers`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `rates`
--# ADD FOREIGN KEY(`order_id`) REFERENCES `orders`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `cart_items`
--# ADD FOREIGN KEY(`cart_id`) REFERENCES `carts`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `cart_items`
--# ADD FOREIGN KEY(`product_id`) REFERENCES `products`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `carts`
--# ADD FOREIGN KEY(`customer_id`) REFERENCES `customers`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `carts`
--# ADD FOREIGN KEY(`store_id`) REFERENCES `stores`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `carts`
--# ADD FOREIGN KEY(`company_id`) REFERENCES `companies`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `carts`
--# ADD FOREIGN KEY(`coupon_id`) REFERENCES `coupons`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `favorites`
--# ADD FOREIGN KEY(`customer_id`) REFERENCES `customers`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `favorites`
--# ADD FOREIGN KEY(`product_id`) REFERENCES `products`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `addresses`
--# ADD FOREIGN KEY(`customer_id`) REFERENCES `customers`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `cities`
--# ADD FOREIGN KEY(`country_id`) REFERENCES `countries`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `addresses`
--# ADD FOREIGN KEY(`country_id`) REFERENCES `countries`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `addresses`
--# ADD FOREIGN KEY(`city_id`) REFERENCES `cities`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `package_role_permission`
--# ADD FOREIGN KEY(`package_role_id`) REFERENCES `package_roles`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `products`
--# ADD FOREIGN KEY(`store_id`) REFERENCES `stores`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `products`
--# ADD FOREIGN KEY(`company_id`) REFERENCES `companies`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `categories`
--# ADD FOREIGN KEY(`company_id`) REFERENCES `companies`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `categories`
--# ADD FOREIGN KEY(`store_id`) REFERENCES `stores`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `sub_categories`
--# ADD FOREIGN KEY(`added_by`) REFERENCES `employees`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `orders`
--# ADD FOREIGN KEY(`store_id`) REFERENCES `stores`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `orders`
--# ADD FOREIGN KEY(`customer_id`) REFERENCES `customers`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `orders`
--# ADD FOREIGN KEY(`address_id`) REFERENCES `addresses`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `customers`
--# ADD FOREIGN KEY(`company_id`) REFERENCES `companies`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `subscriptions`
--# ADD FOREIGN KEY(`company_id`) REFERENCES `companies`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `subscriptions`
--# ADD FOREIGN KEY(`package_id`) REFERENCES `packages`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `package_role`
--# ADD FOREIGN KEY(`package_role_id`) REFERENCES `package_roles`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `package_role`
--# ADD FOREIGN KEY(`package_id`) REFERENCES `packages`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `package_roles`
--# ADD FOREIGN KEY(`added_by`) REFERENCES `admins`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `package_role_permission`
--# ADD FOREIGN KEY(`package_permission_id`) REFERENCES `package_permissions`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `companies`
--# ADD FOREIGN KEY(`added_by`) REFERENCES `admins`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `company_details`
--# ADD FOREIGN KEY(`country_id`) REFERENCES `countries`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `company_details`
--# ADD FOREIGN KEY(`city_id`) REFERENCES `cities`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# # ALTER TABLE `package_transactions`
--# # ADD FOREIGN KEY(`package_id`) REFERENCES `packages`(`id`)
--# # ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `category_transactions`
--# ADD FOREIGN KEY(`category_id`) REFERENCES `categories`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
--# ALTER TABLE `sub_category_translations`
--# ADD FOREIGN KEY(`sub_category_id`) REFERENCES `sub_categories`(`id`)
--# ON UPDATE CASCADE ON DELETE CASCADE;
