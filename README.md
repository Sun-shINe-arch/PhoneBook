# Contact Management System

A Java Swing application for managing contacts with MySQL database integration.

## Features

- Add new contacts with name, number, address, and photo.
- Search and display contacts by name.
- Delete contacts by name and number.

## Requirements

- JDK 8 or above
- MySQL Server
- MySQL Connector/J (JDBC Driver)

## Setup

1. **Clone the repository:**
    ```bash
    git clone https://github.com/Sun-shINe-arch/contact-management-system.git
    cd contact-management-system
    ```

2. **Create MySQL database and table:**
    ```sql
    CREATE DATABASE Contact;
    USE Contact;
    CREATE TABLE create_contact (
        Photo BLOB,
        Name VARCHAR(100),
        Address VARCHAR(255),
        Number VARCHAR(20)
    );
    ```

3. **Configure MySQL credentials in Java files:**
    ```java
    private static final String url = "jdbc:mysql://127.0.0.1:3306/Contact?user=root";
    private static final String username = "root";
    private static final String password = "yourpassword";
    ```

4. **Compile and run:**
    ```bash
    javac Contact.java
    java Contact
    ```

## Usage

- **Add Contact:** Click "Add Contact", fill in details, and submit.
- **Search Contact:** Click "Search", enter the name, and submit.
- **Delete Contact:** Click "Delete", fill in name and number, and submit.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Contact

For questions or suggestions, contact me at [mk7581505@gmail.com].
