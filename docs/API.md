# Controller Endpoints

### 🏠 Public / Common
| Method | Path | Description |
| :--- | :--- | :--- |
| GET | `/`, `/home` | Home Page |
| GET | `/login` | Login Page |
| GET | `/register` | Registration Page |
| POST | `/register` | Register User |
| GET | `/logout` | Logout |

### 👤 User Management
| Method | Path | Description |
| :--- | :--- | :--- |
| GET | `/set-username` | Set Username (OAuth) |
| PUT | `/set-username` | Save Username |
| GET | `/admin/all` | List All Users (Admin) |

### 📚 Courses (Tutor)
| Method | Path | Description |
| :--- | :--- | :--- |
| GET | `/courses` | List All Courses |
| GET | `/courses/{id}` | View Course Details |
| GET | `/tutor/courses/add` | Create Course Form |
| POST | `/tutor/courses/save` | Save New Course |
| GET | `/tutor/courses/update/{id}` | Edit Course Form |
| PUT | `/tutor/courses/save/{id}` | Update Course |
| DELETE | `/tutor/courses/delete/{id}` | Delete Course |

### 📖 Chapters (Tutor)
| Method | Path | Description |
| :--- | :--- | :--- |
| POST | `/tutor/chapters/save` | Add Chapter to Course |
| PUT | `/tutor/chapters/save/{id}` | Update Chapter |
| DELETE | `/tutor/chapters/delete/{id}` | Delete Chapter |

### 🏷️ Categories (Admin)
| Method | Path | Description |
| :--- | :--- | :--- |
| GET | `/admin/categories` | List Categories |
| GET | `/admin/categories/add` | Create Category Form |
| POST | `/admin/categories/save` | Save New Category |
| GET | `/admin/categories/update/{id}` | Edit Category Form |
| PUT | `/admin/categories/save/{id}` | Update Category |
| DELETE | `/admin/categories/delete/{id}` | Delete Category |
