<h1 align="center">🌿 ỨNG DỤNG IOTs XÂY DỰNG QUẢN LÝ FARMSTAY THÔNG MINH</h1>

<p align="center">
  <img src="https://img.shields.io/badge/IoT-Internet%20of%20Things-blue?logo=arduino&logoColor=white"/>
  <img src="https://img.shields.io/badge/MQTT-Protocol-orange?logo=raspberrypi&logoColor=white"/>
  <img src="https://img.shields.io/badge/MongoDB-Database-green?logo=mongodb&logoColor=white"/>
  <img src="https://img.shields.io/badge/Node.js-Backend-success?logo=node.js&logoColor=white"/>
</p>

---

## 📖 Giới thiệu

Đề tài **“Ứng dụng IoTs xây dựng quản lý Farmstay thông minh”** (mã số YSC5.F119) được thực hiện trong khuôn khổ **Hội nghị Khoa học Trẻ lần 5 – 2023 (YSC2023)** của **Trường Đại học Công nghiệp TP. Hồ Chí Minh (IUH)**.  

Mục tiêu của đề tài là ứng dụng **Internet of Things (IoT)** trong việc xây dựng một hệ thống **Farmstay thông minh** – nơi người dùng có thể quản lý, theo dõi, và điều khiển các thiết bị điện, cảm biến, khu vực thuê... từ xa thông qua **website và ứng dụng Android**.  

---

## 🎯 Mục tiêu của đề tài

- Xây dựng **mô hình Farmstay thông minh** với hệ thống cảm biến giám sát (nhiệt độ, độ ẩm, ánh sáng...).
- Phát triển **website quản lý** các khu vực cho thuê.
- Tích hợp **chat nội bộ** và cảnh báo thông minh.
- Phát triển **ứng dụng Android** giúp người dùng điều khiển thiết bị và nhận thông báo.
- Sử dụng giao thức **MQTT** để thu thập dữ liệu cảm biến và truyền tải theo thời gian thực.
- Ứng dụng **JWT (JSON Web Token)** cho xác thực bảo mật người dùng.

---

## 🧱 Kiến trúc hệ thống

```
Client (Android App / Web) 
        ↓
   API Server (Node.js + Express)
        ↓
   Message Broker (MQTT)
        ↓
   IoT Devices (ESP8266 / Sensors)
        ↓
   MongoDB Database
```

### 🔹 Các thành phần chính:
- **IoT Devices:** Các module ESP8266, cảm biến môi trường, thiết bị điện thông minh.  
- **MQTT Broker:** Giao thức truyền dữ liệu cảm biến thời gian thực.  
- **Backend (Node.js + Express):** Xử lý API, xác thực, và quản lý dữ liệu.  
- **Frontend (React / HTML + CSS):** Giao diện web quản lý farmstay.  
- **Mobile App (Flutter / Android):** Ứng dụng điều khiển và theo dõi.  
- **Database (MongoDB):** Lưu trữ thông tin người dùng, thiết bị, và khu vực farmstay.  

---

## 🧠 Công nghệ sử dụng

| Thành phần | Công nghệ |
|-------------|------------|
| **Ngôn ngữ** | JavaScript, Dart |
| **Frontend** | HTML5, CSS3, React (hoặc Web Template) |
| **Backend** | Node.js, Express.js |
| **Cơ sở dữ liệu** | MongoDB |
| **IoT** | ESP8266, MQTT Protocol |
| **Giao thức bảo mật** | JWT |
| **Realtime** | Socket.IO |
| **Mobile App** | Flutter / Android Studio |

---

## ⚙️ Chức năng chính

✅ Quản lý khu vực farmstay (cho thuê, trạng thái, cảm biến)  
✅ Theo dõi thông số môi trường theo thời gian thực  
✅ Hệ thống cảnh báo vượt ngưỡng (nhiệt độ, độ ẩm, điện áp)  
✅ Điều khiển thiết bị từ xa (đèn, máy bơm, quạt, cửa,...)  
✅ Ứng dụng Android cho khách thuê farmstay  
✅ Hệ thống chat nội bộ giữa quản lý và khách thuê  
✅ Xác thực bảo mật bằng JWT  
✅ Kết nối IoT qua MQTT – tốc độ cao, ổn định  

---

## 📸 Mô hình phần cứng (minh họa)

```
+---------------------- +
|  FARMSTAY MANAGER   |
+---------------------- +
       |       ^
       v       |
+--------------+-----------+
|  MQTT Broker (Mosquitto) |
+--------------+-----------+
       |       |
       v       v
+------+----+  +------+
| ESP8266  |  |Sensors|
+-----------+  +------+
```

---

## 🚀 Kết quả & Ứng dụng

- Hệ thống hoạt động ổn định, có thể mở rộng mô hình quản lý farmstay thực tế.  
- Cho phép giám sát và điều khiển linh hoạt từ xa.  
- Giao diện thân thiện, dễ sử dụng cho cả người quản lý và người thuê.  
- Đảm bảo **an toàn dữ liệu, bảo mật người dùng** thông qua JWT & cơ sở dữ liệu riêng.  
- Tăng trải nghiệm người dùng và tính tự động hóa trong du lịch farmstay.  

---

## 🧩 Nhóm thực hiện

**Khoa Công nghệ Điện tử – Trường Đại học Công nghiệp TP.HCM (IUH)**  
👨‍💻 **Nhóm sinh viên thực hiện:**
- Trần Nam Anh  
- Đoàn Hoàng Ca  
- Tăng Hồ Trung Nam  

---

## 🏁 Kết luận

Đề tài đã chứng minh tính khả thi trong việc ứng dụng **IoT và MQTT** vào lĩnh vực du lịch farmstay, giúp **tự động hóa quản lý, nâng cao trải nghiệm người dùng, và tối ưu chi phí vận hành**.  
Trong tương lai, nhóm hướng đến việc mở rộng sang mô hình **smart resort** và **kết nối hệ thống AI phân tích dữ liệu cảm biến**.

---

> 💬 Nếu bạn thấy dự án hữu ích, hãy ⭐ **Star** repo để ủng hộ nhóm nghiên cứu nhé!
