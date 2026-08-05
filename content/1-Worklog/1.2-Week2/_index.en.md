---
title: "Week 2 - Data model and APIs"
date: 2026-08-05
weight: 2
chapter: false
pre: "<b>1.2.</b>"
description: "Normalize entities, relationships, and REST contracts between services."
---

## Objective

Build a data foundation for structured courses, transactions, and learning progress.

## Work completed

- Reviewed 12 core entities: Account, Category, SubCategory, Course, Chapter, Lesson, Order, OrderItem, PromoCode, OTP, LessonProgress, and ContactMessage.
- Normalized request/response models so the web application does not bind directly to JPA entities.
- Added database indexes and tuned HikariCP/Tomcat for queries and concurrent load.

## Outcome

The backend exposes resource-oriented APIs for accounts, catalog data, content, orders, and progress. Real data can replace mock sections throughout the UI.

## Lesson learned

Stable API contracts isolate presentation changes from persistence details and reduce serialization and cyclic-relation defects.
