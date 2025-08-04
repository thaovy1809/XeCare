Hương
##users:
register: post
http://localhost:8080/apis/v1/users
{
  "name": "User1",
  "email": "user1@gmail.com",
  "password": "123",
  "createdAt": "",
  "phone": "123",
  "address": "hcm"
}

login: post
http://localhost:8080/apis/v1/login
{
  "email": "user1@gmail.com",
  "password": "123"
}

##User_VehicleTypes_Category
INSERT INTO User_VehicleTypes_Category (name, description)
VALUES ('Xe gia đình', 'Xe cá nhân hoặc gia đình sử dụng, thường có kích thước nhỏ đến trung bình.');

INSERT INTO User_VehicleTypes_Category (name, description)
VALUES ('Xe công ty', 'Xe thuộc sở hữu của công ty, dùng để đi công tác hoặc phục vụ mục đích công việc.');

INSERT INTO User_VehicleTypes_Category (name, description)
VALUES ('Xe dịch vụ', 'Xe dùng cho mục đích dịch vụ như taxi, xe công nghệ, hoặc cho thuê.');

##User_VehicleTypes: post_ cần Bearer token, cần dữ liệu bảng User_VehicleTypes_Category

http://localhost:8080/apis/user/Vehicle/create

{
  "brand": "Honda",
  "model": "Winner X",
  "licensePlate": "59A3-12345",
  "year": 2022,
  "categoryId": 1
}



