# 📚 Hướng Dẫn Cài Đặt & Phát Triển Ứng Dụng Đọc Truyện Tranh (Manga Reader) - Nhóm G4

Dự án Bài tập lớn phát triển ứng dụng đọc truyện tranh (Manga Reader) trên nền tảng Android. Sử dụng ngôn ngữ **Java**, giao diện **XML Layouts**, mô hình kiến trúc chuẩn **100% MVVM** và Backend **Supabase (PostgreSQL + Storage)**.

---

## 🛠️ Công Nghệ & Thư Viện Sử Dụng

* **Ngôn ngữ lập trình**: Java (JDK 11+)
* **Giao diện**: Android Native XML Layouts (Material Design 3)
* **Kiến trúc ứng dụng**: 100% MVVM (Model - View - ViewModel) & Repository Pattern
* **Quản lý trạng thái UI**: AndroidX Lifecycle `ViewModel` & `LiveData`
* **Kết nối API**: `Retrofit2` & `Gson Converter`
* **Tải & Cache Ảnh**: `Glide`
* **Backend BaaS**: Supabase (Database PostgreSQL + Storage Buckets + REST API)

---

## 🚀 Hướng Dẫn Dành Cho Các Thành Viên Trong Nhóm

### 1. Tải Mã Nguồn Về Máy (Clone Project)
Mở Terminal / Git Bash trên máy tính của bạn và chạy lệnh:

```bash
git clone https://github.com/Dekisugi-2112/App-manga-G4.git
```

### 2. Mở Dự Án Trên Android Studio
1. Mở phần mềm **Android Studio**.
2. Chọn **Open** (Mở dự án sẵn có).
3. Trỏ tới thư mục `App-manga-G4` vừa tải về.
4. Chờ khoảng 1 - 2 phút để Android Studio tự động chạy **Gradle Sync** (tải các thư viện Retrofit, Glide, ViewModel...).

### 3. Cấu Hình Kết Nối Supabase
Mở file `app/src/main/java/com/example/app_manga_g4/utils/Constants.java` và điền URL & Anon Key của dự án Supabase nhóm:

```java
package com.example.app_manga_g4.utils;

public class Constants {
    public static final String SUPABASE_URL = "https://your-supabase-project.supabase.co/";
    public static final String SUPABASE_ANON_KEY = "your-supabase-anon-key";
}
```

### 4. Khởi Động & Chạy Ứng Dụng
1. Kết nối điện thoại thật qua cáp USB (đã bật *USB Debugging*) hoặc mở máy ảo (AVD Emulator).
2. Kiểm tra ô cấu hình trên thanh công cụ hiển thị **`app`** và tên thiết bị của bạn.
3. Bấm nút **▶️ (Run)** màu xanh (hoặc ấn tổ hợp phím `Shift + F10`) để ứng dụng biên dịch và chạy!

---

## 📂 Cấu Trúc Mã Nguồn Dự Án (100% MVVM)

```
app/src/main/java/com/example/app_manga_g4/
│
├── data/                                   # === TẦNG MODEL ===
│   ├── model/                              # Các class DTO chứa dữ liệu
│   │   ├── Comic.java                      # Thông tin truyện
│   │   └── Chapter.java                    # Thông tin chương truyện
│   ├── remote/                             # Kết nối API Supabase
│   │   ├── SupabaseApi.java                # Khai báo các endpoints REST API
│   │   └── SupabaseApiClient.java          # Cấu hình Retrofit Singleton
│   └── repository/                         # Lớp điều phối dữ liệu
│       └── ComicRepository.java            # Lấy dữ liệu từ Supabase & Fallback Demo
│
├── ui/                                     # === TẦNG VIEW & VIEWMODEL ===
│   ├── home/                               # Màn hình Trang chủ
│   │   ├── HomeViewModel.java              # ViewModel Trang chủ & Tìm kiếm
│   │   └── ComicAdapter.java               # Adapter hiển thị ô truyện dạng lưới
│   └── detail/                             # Màn hình Chi tiết truyện
│       ├── ComicDetailActivity.java        # View Chi tiết truyện
│       ├── DetailViewModel.java            # ViewModel danh sách chương
│       └── ChapterAdapter.java             # Adapter danh sách chương
│
├── utils/                                  # Tiện ích dùng chung
│   ├── Constants.java                      # Lưu Supabase URL & Key
│   └── Resource.java                       # Đóng gói trạng thái Data (SUCCESS, ERROR, LOADING)
│
└── MainActivity.java                       # Activity chính (Trang chủ)
```

---

## 🗄️ Cấu Trúc Cơ Sở Dữ Liệu Supabase (Database Schema)

Chạy các đoạn mã SQL sau trên **SQL Editor** của trang quản trị Supabase Dashboard:

```sql
-- Bảng Truyện tranh (comics)
CREATE TABLE public.comics (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(100),
    description TEXT,
    cover_url TEXT NOT NULL,
    status VARCHAR(50) DEFAULT 'ongoing',
    views_count INT DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Bảng Chương truyện (chapters)
CREATE TABLE public.chapters (
    id SERIAL PRIMARY KEY,
    comic_id INT NOT NULL REFERENCES public.comics(id) ON DELETE CASCADE,
    chapter_number FLOAT NOT NULL,
    title VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
```

### Storage Buckets
Tạo 3 Buckets chế độ **Public** trên Supabase Storage:
* `comic-covers`: Chứa ảnh bìa truyện.
* `chapter-pages`: Chứa các trang ảnh đọc truyện.
* `user-avatars`: Chứa avatar người dùng.

---

## 🔄 Quy Trình Đẩy Code Lên GitHub Cho Nhóm (Git Workflow)

Trước khi bắt đầu code tính năng mới, luôn chạy:
```bash
git pull origin main
```

Sau khi làm xong tính năng và chạy thử thành công, đẩy code lên:
```bash
git add .
git commit -m "Mô tả ngắn gọn công việc bạn vừa làm (Ví dụ: Thêm màn hình Login)"
git push origin main
```

---
*Chúc nhóm G4 hoàn thành xuất sắc Bài tập lớn!* 🚀
