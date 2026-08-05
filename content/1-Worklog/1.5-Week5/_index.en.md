---
title: "Week 5 - Course authoring"
date: 2026-08-05
weight: 5
chapter: false
pre: "<b>1.5.</b>"
description: "Complete course, chapter, video lesson, and document authoring."
---

## Objective

Connect the authoring UI to learning content that students can actually consume.

## Work completed

- Completed course creation with category, description, price, and cover image.
- Added ordered chapters and `VIDEO` or `DOCUMENT` lessons.
- Added media upload through Cloudinary or bounded local storage.
- Fixed `/app/uploads` write permissions in the frontend ECS container.

## Outcome

Instructors can create complete course structures and students receive the correct video or document. CI also asserts that the upload directory exists and is writable.

## Lesson learned

Uploads cross many layers: form encoding, multipart limits, storage, filesystem permissions, and delivery URLs must be tested together.
