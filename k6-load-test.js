import http from 'k6/http';
import { check, sleep } from 'k6';

// k6 Options: Scenarios & Thresholds to prevent performance bottlenecks
export const options = {
  stages: [
    { duration: '20s', target: 50 },  // Ramp up to 50 virtual users
    { duration: '30s', target: 50 },  // Steady state with 50 VUs
    { duration: '10s', target: 0 },   // Ramp down to 0 VUs
  ],
  thresholds: {
    // 95% of requests must complete below 8000ms (accounting for remote network latency and tiny ECS CPU container doing BCrypt)
    http_req_duration: ['p(95)<8000'],
    // Error rate must be less than 5%
    http_req_failed: ['rate<0.05'],
    checks: ['rate>0.99'],
  },
};

// Allow CI or a reviewer to target another environment without editing the
// script. The fallback is the public EduFlow ALB verified on 5 August 2026.
const BASE_URL = (__ENV.BASE_URL ||
  'http://eduflow-dev-alb-560717424.ap-southeast-1.elb.amazonaws.com'
).replace(/\/$/, '');

export default function () {
  // Scenario 1: Fetch Courses Catalog (Read-heavy endpoint)
  const coursesRes = http.get(`${BASE_URL}/api/courses`);
  check(coursesRes, {
    'Courses API status is 200': (r) => r.status === 200,
    'Courses payload is not empty': (r) => r.json() !== null,
  });

  sleep(0.5);

  // Scenario 2: Fetch Categories (Cached metadata endpoint)
  const categoriesRes = http.get(`${BASE_URL}/api/categories`);
  check(categoriesRes, {
    'Categories API status is 200': (r) => r.status === 200,
  });

  sleep(0.5);

  // Scenario 3: Fetch the public dashboard counters. Keep the shared load test
  // read-only so it can be repeated without using or publishing demo passwords.
  const statsRes = http.get(`${BASE_URL}/api/public/stats`);
  check(statsRes, {
    'Public stats API status is 200': (r) => r.status === 200,
    'Public stats payload is available': (r) => r.json('payload.totalCourses') !== undefined,
  });

  sleep(1);
}
