##Bearer token lấy khi login

###login: post
http://localhost:8080/apis/v1/login
{
  "email": "user1@gmail.com",
  "password": "123"
}

##Hoàng garage
-- Thêm dịch vụ mẫu
INSERT INTO services (name, description, is_active) VALUES ('Sửa lốp', 'Sửa chữa lốp xe', 1);
INSERT INTO services (name, description, is_active) VALUES ('Thay dầu', 'Thay dầu động cơ', 1);

-- Thêm loại xe mẫu
INSERT INTO vehicle_types (name, description, is_active) VALUES ('Xe máy', 'Xe máy các loại', 1);
INSERT INTO vehicle_types (name, description, is_active) VALUES ('Ô tô', 'Ô tô các loại', 1);

#garage:

##register:post _ cần Bearer token
http://localhost:8080/apis/garage/register
{
  "name": "Garage ABC1",
  "description": "Chuyên sửa xe máy và ô tô",
  "address": "123 Đường ABC, Quận 1, TP.HCM",
  "phone": "01234567891",
  "email": "garageabc1@example.com",
  "latitude": 10.762622,
  "longitude": 106.660172,
  "openTime": "08:00:00",
  "closeTime": "18:00:00",
  "serviceIds": [1, 2],
  "vehicleTypeIds": [1, 2]
}

##show gara theo id: get _ cần Bearer token
http://localhost:8080/apis/garage/1

