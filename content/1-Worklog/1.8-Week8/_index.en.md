---
title: "Week 8 - Bilingual UX"
date: 2026-08-05
weight: 8
chapter: false
pre: "<b>1.8.</b>"
description: "Complete Vietnamese/English i18n, typography, navigation, and errors."
---

## Objective

Provide consistent Vietnamese and English experiences across primary and secondary journeys.

## Work completed

- Normalized `messages`, `messages_vi`, and `messages_en` bundles.
- Localized headers, footers, auth, catalog, profiles, policies, and error pages.
- Fixed fonts, malformed characters, form alignment, learning-page scrolling, and branding.
- Added bundle parity tests to detect missing translation keys.

## Outcome

Language support now extends beyond the homepage. Vietnamese copy and VND prices render correctly in production UI.

## Lesson learned

i18n is a data contract; automated parity checks are more reliable than reviewing every template by hand.
