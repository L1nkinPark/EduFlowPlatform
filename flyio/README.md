# EduFlow – Fly.io Dev Environment

Hướng dẫn deploy EduFlow Platform lên Fly.io để test trước khi triển khai AWS.

## Prerequisites

1. **Fly.io account**: Đăng ký tại [fly.io](https://fly.io)
2. **flyctl CLI**: Cài đặt theo hướng dẫn bên dưới
3. **MySQL database**: Dùng [PlanetScale](https://planetscale.com) (free tier) hoặc [Aiven](https://aiven.io)

## 1. Cài đặt flyctl

```powershell
# Windows (PowerShell)
powershell -Command "iwr https://fly.io/install.ps1 -useb | iex"

# Hoặc dùng Scoop
scoop install flyctl
```

## 2. Đăng nhập

```bash
fly auth login
```

## 3. Setup MySQL Database

### Option A: PlanetScale (Recommended – Free)

1. Đăng ký tại [planetscale.com](https://planetscale.com)
2. Tạo database `eduflow_db`
3. Lấy connection string:
   ```
   jdbc:mysql://<host>/<database>?sslMode=VERIFY_IDENTITY
   ```

### Option B: Aiven MySQL (Free trial)

1. Đăng ký tại [aiven.io](https://aiven.io)
2. Tạo MySQL service
3. Lấy connection details từ dashboard

## 4. Deploy Backend

```bash
cd BE_EduFlow

# Tạo app trên Fly.io (lần đầu)
fly launch --name eduflow-be-dev --region sin --no-deploy

# Set secrets (thay thế giá trị thực)
fly secrets set \
  DATABASE_URL="jdbc:mysql://<host>:3306/eduflow_db?useSSL=true" \
  DATABASE_USERNAME="<db_user>" \
  DATABASE_PASSWORD="<db_password>" \
  MAIL_USERNAME="<smtp_email>" \
  MAIL_PASSWORD="<app_password>" \
  CONTACT_NOTIFICATION_EMAIL="<support_inbox>" \
  CORS_ALLOWED_ORIGINS="https://eduflow-fe-dev.fly.dev,https://eduflow-be-dev.fly.dev" \
  JWT_SECRET="<base64-encoded-32-byte-secret>" \
  VNPAY_TMN_CODE="<vnpay-merchant-code>" \
  VNPAY_HASH_SECRET="<vnpay-hash-secret>" \
  VNPAY_URL="https://sandbox.vnpayment.vn/paymentv2/vpcpay.html" \
  DEMO_INSTRUCTOR_PASSWORD="<strong-demo-password>"

# Deploy
fly deploy

# Kiểm tra logs
fly logs

# Kiểm tra status
fly status
```

## 5. Deploy Frontend

```bash
cd FE_EduFlow

# Tạo app trên Fly.io (lần đầu)
fly launch --name eduflow-fe-dev --region sin --no-deploy

# Set secrets
fly secrets set \
  BACKEND_URL="https://eduflow-be-dev.fly.dev" \
  CLOUDINARY_CLOUD_NAME="<cloudinary-cloud-name>" \
  CLOUDINARY_API_KEY="<cloudinary-api-key>" \
  CLOUDINARY_API_SECRET="<cloudinary-secret>" \
  JWT_SECRET="<same-secret-as-backend>"

# Deploy
fly deploy

# Kiểm tra
fly logs
fly status
```

## 6. Verify

```bash
# Health check Backend
curl https://eduflow-be-dev.fly.dev/actuator/health

# Mở Frontend
fly open -a eduflow-fe-dev

# Hoặc truy cập trực tiếp
# https://eduflow-fe-dev.fly.dev
```

## URLs

| Service | URL |
|---------|-----|
| Frontend | https://eduflow-fe-dev.fly.dev |
| Backend API | https://eduflow-be-dev.fly.dev |
| Backend Health | https://eduflow-be-dev.fly.dev/actuator/health |

## Quản lý

```bash
# Xem logs realtime
fly logs -a eduflow-be-dev

# SSH vào container
fly ssh console -a eduflow-be-dev

# Scale up/down
fly scale count 2 -a eduflow-be-dev  # 2 instances
fly scale count 1 -a eduflow-be-dev  # 1 instance

# Restart
fly apps restart eduflow-be-dev

# Destroy (xóa hoàn toàn)
fly apps destroy eduflow-be-dev
fly apps destroy eduflow-fe-dev
```

## Chi phí ước tính

| Resource | Chi phí |
|----------|---------|
| BE Machine (shared-1x, 1GB) | ~$3.19/tháng |
| FE Machine (shared-1x, 1GB) | ~$3.19/tháng |
| PlanetScale MySQL (free tier) | $0 |
| **Tổng** | **~$6.38/tháng** |

> **Note**: Fly.io tự động stop machines khi không có traffic (auto_stop_machines = "stop"),
> nên chi phí thực tế có thể thấp hơn.
