# Zymarts - Multi-Tenant SaaS E-Commerce Backend

Backend for **Zymarts**, a multi-tenant SaaS e-commerce platform built with Java and Spring Boot. Companies (tenants) sign up and get their own storefront, while a super-admin layer manages companies, subscriptions, packages, features and global settings.

> **Note:** This repository contains the `src/` source tree for reference. Build files (`pom.xml`) are not included, so it is not runnable as-is.

## Highlights

- Multi-tenant architecture: super-admin, company (tenant), employee and customer roles
- JWT-based authentication for each role, plus guest cart tokens
- Payments with Stripe (PaymentIntents + webhook handling)
- Subscriptions, packages and feature flags per tenant
- Catalog: products, categories, banners, coupons, countries with multilingual (EN/AR) translations
- Order and cart management per storefront
- Email service (verification and password reset codes)

## Tech Stack

- Java, Spring Boot (Web, Data JPA, Security, Mail)
- MySQL
- Stripe API
- JWT (jjwt)

## Configuration

Secrets are read from environment variables:

| Variable | Purpose |
| --- | --- |
| `DB_USERNAME` / `DB_PASSWORD` | MySQL credentials |
| `JWT_SECRET` | JWT signing secret (64+ random chars) |
| `STRIPE_SECRET_KEY` / `STRIPE_WEBHOOK_SECRET` | Stripe API keys |

See `src/main/resources/application.yaml`.
