---
title: "Week 6 - Student experience"
date: 2026-08-05
weight: 6
chapter: false
pre: "<b>1.6.</b>"
description: "Connect discovery, purchase, learning, and progress to real data."
---

## Objective

Create a continuous journey from course discovery to resumed learning.

## Work completed

- Made catalog, search, detail, breadcrumbs, instructor, and price data dynamic.
- Restricted lesson content to accounts that purchased the course.
- Persisted lesson completion and calculated course progress percentages.
- Added “Continue learning” for owned courses and real transaction history.

## Outcome

Student pages no longer depend on mock records. Progress is stored per account and lesson and restored on later visits.

## Lesson learned

Content access must rely on a confirmed backend transaction, not a URL or button state in the frontend.
