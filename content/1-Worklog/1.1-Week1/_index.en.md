---
title: "Week 1 (19-25 May 2026) - DevOps and Kubernetes"
date: 2026-05-25
weight: 1
chapter: false
pre: "<b>1.1.</b>"
description: "Build reusable Kubernetes templates for frontend, backend, and MariaDB."
---

## Date

**19 May 2026 - 25 May 2026**

## Actual work

- Built reusable DevOps/Kubernetes templates for frontend, backend, and MariaDB.
- Configured Deployments, Services, Ingress, ConfigMaps, resource requests/limits, and NFS persistent volumes.
- Documented a full-stack rollout and exposed MariaDB through NodePort for the required environment.

## Outcomes and evidence

- Completed the full-stack Kubernetes structure and its operating guide.
- [Full-stack Deployments, Services, and Ingress](https://github.com/L1nkinPark/devops-ci-cd-templates/commit/b59844316c34150bd2124ecbc8034da6c56a75ab)
- [MariaDB, backend, and frontend deployment guide](https://github.com/L1nkinPark/devops-ci-cd-templates/commit/49d89f34cfe29a632e81cf6688273e0b08c4d546)
- [Resource requests and limits](https://github.com/L1nkinPark/devops-ci-cd-templates/commit/257943281d884672a9d4a93c5b602c1b15bdd222)
- [ConfigMap volume mount fix using subPath](https://github.com/L1nkinPark/devops-ci-cd-templates/commit/ac40e27457d74d6558b36e96a3dc90c12e98b78f)

## Challenge and resolution

The container initially read the wrong configuration because the ConfigMap was mounted at the wrong path. I changed the mount to use `subPath`, normalized the volume paths, and added CI validation.

## Mentor feedback

Not available yet; it will be added after mentor feedback is received.
