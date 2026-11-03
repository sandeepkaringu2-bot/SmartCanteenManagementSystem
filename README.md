# Smart Canteen Management System (Java Desktop)

A Java Swing Windows desktop PBL application with MySQL storage. It is designed as a learning project using Java OOP, Swing event handling, packages, collections, exceptions, JDBC, and SQL.

## Features
- Student registration and login
- Admin login
- View available menu, cart, bill, order placement, and student order history
- Admin food menu create/update/availability management
- Admin order list and status changes
- Daily completed-sales total

## Requirements
- Windows 10/11
- JDK 17 or later (JDK 21 is also fine)
- MySQL Server 8.x and MySQL Workbench (Workbench is a GUI, Server is required)
- Maven 3.8+ (or VS Code Java Extension Pack with Maven support)
- VS Code Java Extension Pack

## Step 1: Set up MySQL
1. Start MySQL Server.
2. Open MySQL Workbench and connect to your local server.
3. Open `database/schema.sql` from this project.
4. Execute the whole script. It creates `smart_canteen`, its tables, an admin, and sample menu items.
5. The demo admin login is username `admin`, password `admin123`.

The admin password is seeded as a SHA-256 hash. This hash approach is for this educational demo; production systems should use salted adaptive password hashing and stronger account controls.

## Step 2: Configure the database account
Open `src/main/resources/db.properties` and set:
- `db.url` — usually the default local URL shown there
- `db.username` — your MySQL username (commonly `root` locally)
- `db.password` — your MySQL password; blank only if your local root account has no password

Do not upload real database credentials to a public repository.

## Step 3: Open in VS Code
1. Extract the project ZIP to a folder, for example `C:\College\SmartCanteenManagementSystem`.
2. In VS Code choose **File > Open Folder** and select the extracted project folder (the one containing `pom.xml`).
3. Install the **Extension Pack for Java** if prompted.
4. Wait for Maven to resolve the MySQL Connector/J dependency. Internet access may be needed the first time.
5. Confirm `pom.xml` is recognized as a Maven project.

## Step 4: Run
Option A — VS Code:
1. Open `src/main/java/com/smartcanteen/Main.java`.
2. Click **Run** above the `main` method.

Option B — VS Code terminal / Windows terminal:
```powershell
mvn clean compile
mvn exec:java -Dexec.mainClass="com.smartcanteen.Main"
```
If the `exec` command is not available in your Maven setup, run Main.java with VS Code's Run action. The provided `pom.xml` includes the MySQL driver dependency.

## First login and test
- Admin: `admin` / `admin123`
- Student: use **Register as Student** on the login screen, then log in.
- Admin can add/edit foods and update order statuses.
- Student can choose a menu item, add it to cart, and place an order.

## Folder and file guide
- `pom.xml`: Maven project metadata and MySQL JDBC dependency.
- `database/schema.sql`: database/table setup and sample records.
- `src/main/resources/db.properties`: local database connection configuration.
- `Main.java`: application entry point; launches Swing UI on the Event Dispatch Thread.
- `database/DBConnection.java`: reads properties and opens JDBC connections.
- `model/User.java`: logged-in user data and role helper.
- `model/FoodItem.java`: food data model.
- `model/CartItem.java`: food and quantity held in the cart; computes line total.
- `model/OrderSummary.java`: order row data used by history/admin views.
- `dao/UserDAO.java`: SQL operations for login and registration.
- `dao/FoodDAO.java`: SQL operations for retrieving and maintaining menu items.
- `dao/OrderDAO.java`: SQL order creation transaction, history, status and sales queries.
- `service/CanteenService.java`: validation and application rules; UI calls this layer.
- `service/PasswordUtil.java`: SHA-256 password hashing utility for this demo.
- `ui/LoginFrame.java`: login screen and role-based navigation.
- `ui/RegisterFrame.java`: student registration dialog.
- `ui/StudentDashboard.java`: student menu, cart, and order history tabs.
- `ui/AdminDashboard.java`: admin menu maintenance, order processing, sales view.
- `ui/UIUtil.java`: small shared Swing message/button/panel helpers.

## Architecture / data flow
Swing UI -> CanteenService (validation/business rules) -> DAO classes (SQL) -> DBConnection (JDBC) -> MySQL.
Model classes carry data between layers. Prepared statements are used for user-provided SQL values. Order header and order lines are saved in one transaction.

## Notes / limitations for the PBL version
- This is a local desktop application; MySQL must be running on the same PC or configured host.
- There is no online payment integration.
- A cart is kept in memory until an order is placed; closing the app clears an unsubmitted cart.
- SHA-256 is used to avoid storing plain text in this demonstration, but SHA-256 alone is not a production password-storage scheme. Use BCrypt/Argon2 with salts for a real deployed application.
- Food items are marked unavailable rather than permanently deleted, so historical order references remain valid.
