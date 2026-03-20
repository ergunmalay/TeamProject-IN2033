-- =====================================================
-- IPOS-PU Database Schema
-- Nova Solutions — Team 24/C
-- =====================================================
-- Run this script to create all tables for the IPOS-PU subsystem.
-- Usage: mysql -u root -p ipos_pu < ipos_pu_schema.sql
--
-- Tables (mapped to use cases):
--   members                 UC-01a/b, UC-02, UC-03
--   commercial_applications UC-01b
--   products                UC-04, UC-05, UC-06
--   cart_items              UC-06, UC-07
--   orders                  UC-08, UC-09, UC-10
--   order_items             UC-08, UC-09
--   payments                UC-12
--   refunds                 UC-10
--   email_queue             UC-11 (store-and-retry when SMTP fails)
--   promotion_campaigns     UC-13, UC-14, UC-15, UC-16
--   campaign_products       UC-13, UC-16
--   campaign_tracking       UC-15, UC-16, UC-19
--   admin_users             UC-13, UC-14, UC-17-19
-- =====================================================

-- TODO: Dan + Daanish — Week 7, Days 2-3 (CRITICAL PATH)

CREATE TABLE IF NOT EXISTS members (
    id                 INT AUTO_INCREMENT PRIMARY KEY,
    name               VARCHAR(100)        NOT NULL,
    email              VARCHAR(255)        NOT NULL UNIQUE,
    password_hash      VARCHAR(60)         NOT NULL,
    member_type        VARCHAR(20)         NOT NULL,  -- 'NonCommercial' | 'Commercial'
    membership_status  VARCHAR(20)         NOT NULL,  -- 'Approved' | 'Pending' | 'Rejected'
    is_first_login     BOOLEAN             NOT NULL DEFAULT TRUE
);

-- Migration: run this if the members table already exists without is_first_login
-- ALTER TABLE members ADD COLUMN is_first_login BOOLEAN NOT NULL DEFAULT TRUE;
