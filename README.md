# Inventory-Management-System

A Java-based solution leveraging JavaFX and JDBC for efficient inventory control. Seamlessly manage products, suppliers, and quantities in an intuitive interface. Ideal for streamlining inventory processes.

Technologies and libraries used = Written in Java 11, JavaFX GUI for intuitive user interaction JDBC and SQL for database connectivity.

The program runs via  the InventoryRunner class found in the Runner package


Database Setup :

Database Creation

Database Type: PostgreSQL

Create a Database: Execute an SQL script to create a database named "inventory_management_db".

Table Creation: "customer"
Columns:
customer_id (int, Primary Key) default = nextval('customer_customer_id_seq'::regclass)
customer_name (varchar)
email (varchar)
address (text)
Constraints:
customer_id as Primary Key

Table Creation: "product"
Columns:
product_id (int, Primary Key) default = nextval('product_product_id_seq'::regclass)
product_name (varchar)
description (text)
price (numeric(10,2))
quantity_available (int)
Constraints:
product_id as Primary Key

Table Creation: "inventory"
Columns:
inventory_id (int, Primary Key) default = nextval('inventory_inventory_id_seq'::regclass)
product_id (int, Foreign Key to product table)
quantity_on_hand (int)
last_updated (timestamp) default = CURRENT_TIMESTAMP
Constraints:
inventory_id as Primary Key
product_id as Foreign Key referencing product(product_id)

Table Creation: "orders"
Columns:
order_id (int, Primary Key) default = nextval('orders_order_id_seq'::regclass)
product_id (int, Foreign Key to product table)
order_date (timestamp) default = CURRENT_TIMESTAMP
quantity_ordered (int)
total_cost (numeric(10,2))
status (varchar)
Constraints:
order_id as Primary Key
product_id as Foreign Key referencing product(product_id)

Table Creation: "Supplier"
Columns:
supplier_id (int, Primary Key) default = nextval('supplier_supplier_id_seq'::regclass)
supplier_name (varchar)
contact_info (varchar)
address (text)
Constraints:
supplier_id as Primary Key


Credentials
Username: "your username"
Password: "your password"
URL: "your url"
Instructions
Users should set up similar PostgreSQL databases for each table and modify the connection URL, username, and password accordingly in the project code. Ensure the constraints and references are appropriately defined as per the requirements in the script.


Project Structure

src/: Contains the source code of the Inventory Management System. It has packages main and test. Within the java folder there are 4 packages :
DataBaseUtility/: Contains the database connection and utility classes.
Model/: Includes Java classes defining entities Customer, Product, Supplier, Orders, Inventory.
Runner/: Includes the runner class,  from which the program starts running.
UI/: Holds the JavaFX controllers for different UI sections.

pom.xml:/ holds the resources.
readme.md :/ holds the important informations about the  project, simply here.

Author: Volkan

I developed the core functionalities, database integration, and backend logic for this project, aiming to showcase my expertise in Java, SQL, JDBC; backend development in simple words. The JavaFX-related things like locating the buttons, setting the size, changing the background color and etc were created with guidance and support from an AI assistant (ChatGPT), I handled the backend within the UI package too


