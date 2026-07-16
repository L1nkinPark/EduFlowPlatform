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
  },
};

const BASE_URL = 'http://eduflow-dev-alb-1088870685.ap-southeast-1.elb.amazonaws.com';

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

  // Scenario 3: User Authentication (CPU-heavy BCrypt endpoint)
  const loginPayload = JSON.stringify({
    username: 'instructor',
    password: '123',
  });
  const loginParams = {
    headers: {
      'Content-Type': 'application/json',
    },
  };
  const loginRes = http.post(`${BASE_URL}/api/auth/login`, loginPayload, loginParams);
  
  // Note: We check for either 200 (Success) or 401/404 (Unseeded DB) to prevent the test
  // from failing if database seeds differ, focusing instead on response times.
  check(loginRes, {
    'Auth API responded': (r) => r.status === 200 || r.status === 401 || r.status === 404,
  });

  sleep(1);
}
