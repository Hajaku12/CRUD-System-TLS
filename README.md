# Property Management System with Spring Boot and MySQL and

This project is a property management system built using Spring Boot, JPA (Java Persistence API), and MySQL. It allows users to register, log in, and perform CRUD (Create, Read, Update, Delete) operations on property records. The application exposes a REST API for interaction and is deployed on AWS using EC2 instances.

### Key Components

1. **Backend (Spring Boot)**
    - **PropertyService**: Contains business logic for managing properties.
    - **PropertyRepository**: Handles database operations for property entities.
    - **PropertyController**: Exposes API endpoints for property operations.
    - **UserService**: Manages user-related operations like authentication and registration.
    - **UserRepository**: Handles database operations for user entities.
    - **UserController**: Exposes API endpoints for user operations.

2. **Frontend**
    - HTML pages for login, registration, and property management.
    - JavaScript for handling user interactions and making API calls.

3. **Database (MySQL)**
    - Stores user and property data.
   
4. **Deployment (AWS)**
    - Two EC2 instances: one for the Apache server and MySQL, and another for the Spring Boot application.
    - Let's Encrypt certificates for HTTPS.

### Configuration Files

- **`application.properties`**: Configures the Spring Boot application, including database connection, server port, and SSL settings.
- **`docker-compose.yml`**: Defines the MySQL service for Docker.

### Key Functionalities

- **User Registration and Login**: Interfaces for users to register and log in.
- **CRUD Operations on Properties**: Users can create, read, update, and delete property records.
- **REST API**: Exposes endpoints for interacting with property and user data.
- **HTTPS**: Secures communication using SSL certificates.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

You need to install the following tools and configure their dependencies:

1. **Java** (version 8 or above)

    ```bash
    java -version
    ```

   Should return something like:

    ```bash
    java version "1.8.0"
    Java(TM) SE Runtime Environment (build 1.8.0-b132)
    Java HotSpot(TM) 64-Bit Server VM (build 25.0-b70, mixed mode)
    ```

2. **Maven**
    - Download Maven from [here](http://maven.apache.org/download.cgi)
    - Follow the installation instructions [here](https://maven.apache.org/install.html)

   Verify the installation:

    ```bash
    mvn -version
    ```

   Should return something like:

    ```bash
    Apache Maven 3.6.3
    ```

## Installing

1. Clone the repository and navigate into the project directory:

    ```bash
    git clone  https://github.com/Hajaku12/CRUD-System-TLS.git
    cd CRUD-System-TLS
    ```

2. Build the project:

    ```bash
    mvn clean package
    ```

   Should display output similar to:

    ```bash
    [INFO] BUILD SUCCESS
    ```

## Test Resutls

To run the tests use:

  ```bash
  mvn test
  ```
![img_5.png](images%2Fimg_5.png)

## Deployment in AWS

To run the program on AWS, we need to have two instances, in my case, they are the following.

![img_4.png](images%2Fimg_4.png)

On the `apache_server` instance, both MySQL and the Apache web server will be set up, while the Spring Boot application will be deployed on a separate instance. Each machine will have a Let's Encrypt certificate generated to enable HTTPS.

Each EC2 instance requires a unique DNS domain to generate Let's Encrypt certificates. You can obtain free domains from the following website: [DuckDNS](https://www.duckdns.org/).
![alt text](images/imageDNS.png)

* **Apache and MySQL Server** ---> labserverapachee.duckdns.org
* **Backend Server** ---> serverapacheback.duckdns.org

### **instance ServerSQL with Apache**

#### A. Install SQL

1. To set up SQL on the EC2 instance, please consult the README file in the following repository.
    ```bash
    https://github.com/Hajaku12/CRUD-System-to-Management.git
    ```

2. Configuration file and Creating the Database

    ![img_1.png](images%2Fimg_1.png)

    ![img_2.png](images%2Fimg_2.png)
#### B. Install APACHE

1. Connect to the EC2 instance.

   To begin, establish a connection to the EC2 instance via SSH. Execute the following command:
    ```bash
    ssh -i "apache_server_key.pem" ec2-user@ec2-3-81-46-187.compute-1.amazonaws.com
    ```

2. Update the packages.
    ```bash
    sudo yum update -y
    ```

3. Install Apache.

    ```bash
    sudo yum install httpd -y
    ```

4. Start and enable Apache.

    ```bash
    sudo systemctl start httpd
    sudo systemctl enable httpd
    ```

5. Check the status of Apache:

    ```bash
    sudo systemctl status httpd
    ```

#### C. Let's Encrypt certificate

1. Install Dependencies:

   To use Certbot, first, install Python and pip (a package manager for Python):

    ```bash
    sudo yum install python3-pip -y
    sudo pip3 install certbot

    ```
   You also need to install the Apache integration package:

     ```bash
    sudo yum install python-certbot-apache

    ```
2.  Configure Apache

    Edit the configuration file for your virtual host for your domain:

    ```bash
    sudo nano /etc/httpd/conf.d/labserverapachee.duckdns.org.conf
    ```

    Ensure your configuration file includes the following settings:
    ```bash
    <VirtualHost *:80>
        ServerName labserverapachee.duckdns.org
        DocumentRoot /var/www/html

        RewriteEngine on
        RewriteCond %{SERVER_NAME} =labserverapachee.duckdns.org
        RewriteRule ^ https://%{SERVER_NAME}%{REQUEST_URI} [L,R=301]
    </VirtualHost>
    ```

3. Run Certbot

   Once the Apache configuration file is ready, you can run Certbot to obtain the SSL certificate:
    ```bash
    sudo certbot --apache -v -d labserverapachee.duckdns.org
    ```

4. Verification

   Verify the certificate's functionality by opening your preferred browser and navigating to the DNS `labserverapachee.duckdns.org` using the HTTPS protocol.
    
    ![img.png](images%2Fimg.png)


### **instance Backend (SprinBoot)**

#### A. Let's Encrypt certificate

1. Connect to the EC2 instance.

   First, you must connect to the EC2 instance using SSH. Use the following command:

    ```bash
    ssh -i "Proyecto-Property-key.pem" ec2-user@ec2-3-87-29-180.compute-1.amazonaws.com
    ```
2. Install Certbot

   You can do so using the following commands:

    ```bash
    sudo yum update -y
    sudo yum install -y certbot python2-certbot-apache

    ```
3. Obtain a Let's Encrypt Certificate

    ```bash
    sudo certbot certonly --standalone -d serverapacheback.duckdns.org

    ```
    ![img_3.png](images%2Fimg_3.png)

   This command will generate the certificate files:

   + `fullchain.pem`: This is the certificate chain.
   + `privkey.pem`: This is the private key.

4. Copy the certificates to a location accessible by Spring Boot

    ```bash
    sudo cp /etc/letsencrypt/live/serverapacheback.duckdns.org/fullchain.pem /home/ec2-user/
    sudo cp /etc/letsencrypt/live/serverapacheback.duckdns.org/privkey.pem /home/ec2-user/
    ```

5. Verify access permissions

    ```bash
    sudo chown ec2-user:ec2-user /home/ec2-user/privkey.pem
    sudo chmod 600 /home/ec2-user/privkey.pem

    sudo chown ec2-user:ec2-user /home/ec2-user/fullchain.pem
    sudo chmod 600 /home/ec2-user/fullchain.pem
    ```

6. Configure Spring Boot for HTTPS: Modify the application.properties file:

    ```bash
    server.port=443
    server.ssl.key-store-type=PKCS12
    server.ssl.key-store=/home/ec2-user/keystore.p12
    server.ssl.key-store-password=password123
    server.ssl.key-alias=springboot
    ```

7. Configuration of CORS in Spring Boot

   To enable communication between servers, CORS must be configured on the project endpoints to allow requests from the Apache server.

    Additionally, you can configure CORS globally in the application by using a `WebConfig` class.

8. Convert the certificates to a PKCS12 format: Spring Boot requires a keystore in PKCS12 format.

    ```bash
    sudo yum install openssl -y
    ```

    ```bash
    openssl pkcs12 -export -in /home/ec2-user/fullchain.pem -inkey /home/ec2-user/privkey.pem \ -out /home/ec2-user/keystore.p12 -name "springboot" -password pass:password123
    ```

    ```bash
    sudo chown ec2-user:ec2-user /home/ec2-user/keystore.p12
    sudo chmod 600 /home/ec2-user/keystore.p12
    ```
7. Upload the project's JAR file to the AWS instance and Run the Spring Boot Application.

    ```bash
    sudo java -jar PropertyApplication-0.0.1-SNAPSHOT.jar
    ```

8. You can now access the webpage using the following URL, as shown in the video.

    ```bash
    http://serverapache.duckdns.org/login.html
    ```

[VideoTLS.mp4](images%2FVideoTLS.mp4)
   
## Diagram Class

![img_6.png](images%2Fimg_6.png)

## Class Diagram Explanation

This diagram illustrates the structure of the Property Management System and depicts the relationships between the primary classes in the backend.

### Classes:

1. **Property (Entity)**

   - Represents a real estate property in the database with fields such as `id`, `address`, `price`, `size`, and `description`. This class includes getter and setter methods to access and modify these fields.

2. **User (Entity)**

   - Represents a user in the system, mapped to a database table. Users are used for authentication and system management, with fields like `id`, `username`, and `password` (which is encrypted).

3. **PropertyRepository (Repository)**

   - Provides CRUD operations for `Property` entities using JPA. It includes a custom query method to find properties by their address.

4. **UserRepository (Repository)**

   - Manages CRUD operations for `User` entities. It includes a method to find a user by their username.

5. **PropertyService (Service)**

   - Contains the business logic related to property management. It interacts with the repository to create, update, delete, and retrieve properties.

6. **UserService (Service)**

   - Handles the business logic for user management, such as authentication and registration, and interacts with the repository for database operations.

7. **PropertyController (Controller)**

   - Provides API endpoints to manage properties (create, retrieve, update, delete). It communicates with `PropertyService` to execute business logic.

8. **UserController (Controller)**

   - Exposes API endpoints for user operations, such as registration and authentication. It communicates with `UserService` to handle these actions.
## Architecture

![img_7.png](images%2Fimg_7.png)

### Diagram Explanation

### Explanation of the Diagram

1. **Browser**:
   - Users access the application via a browser, which interacts with both the Apache server and the Spring Boot backend. All communication between the browser and the servers is encrypted using HTTPS on **port 443**.

2. **EC2 Instance: Apache & MySQL**:
   - This represents an Amazon EC2 instance hosting both the **Apache server** and **MySQL database**.
   - **Apache Server**:
      - The Apache server delivers static HTML pages to the browser. These pages include:
         - `login.html`: The login page for user authentication.
         - `register.html`: The registration page for new user sign-up.
         - `properties.html`: A page for managing or viewing properties.
   - **MySQL Database**:
      - The Apache server also interacts with the MySQL database, which stores and manages data for the web application. Apache can query MySQL for user or property data.

3. **EC2 Instance: Spring Boot**:
   - This represents another Amazon EC2 instance running the **Spring Boot backend**, which handles the core business logic of the application.
   - **Spring Boot Backend**:
      - The Spring Boot backend processes requests from the browser and interacts with the database as needed. It includes:
         - `Properties Service`: A service responsible for operations related to real estate properties (e.g., creating, updating, and deleting properties).
         - `User Service`: A service that manages user-related operations such as authentication, registration, and profile management.

4. **HTTPS Connections**:
   - **Browser to Apache (HTTPS)**: The browser securely communicates with the Apache server over HTTPS on **port 443**. The Apache server serves static HTML pages (login, register, properties) to the browser.
   - **Browser to Spring Boot (HTTPS)**: The browser also makes secure HTTPS requests directly to the Spring Boot backend on **port 443** for dynamic operations, such as interacting with the `Properties` and `User` services (e.g., saving new property data, or authenticating users).

5. **Apache to Spring Boot (HTTP)**:
   - While the browser communicates with both servers using HTTPS, the Apache server communicates with the Spring Boot backend over HTTP. This internal communication could be done over HTTP since it is within the secured internal network between the two EC2 instances, although it can also be upgraded to HTTPS for added security.

## Generating Project Documentation

1. **Generate the Site**
   - Run the following command to generate the site documentation:
     ```sh
     mvn site
     ```

2. **Add Javadoc Plugin for Documentation**
   - Add the Javadoc plugin to the `reporting` section of the `pom.xml`:
     ```xml
     <project>
       ...
       <reporting>
         <plugins>
           <plugin>
             <groupId>org.apache.maven.plugins</groupId>
             <artifactId>maven-javadoc-plugin</artifactId>
             <version>2.10.1</version>
             <configuration>
               ...
             </configuration>
           </plugin>
         </plugins>
       </reporting>
       ...
     </project>
     ```

   - To generate Javadoc as an independent element, add the plugin in the `build` section of the `pom.xml`:
     ```xml
     <project>
       ...
       <build>
         <plugins>
           <plugin>
             <groupId>org.apache.maven.plugins</groupId>
             <artifactId>maven-javadoc-plugin</artifactId>
             <version>2.10.1</version>
             <configuration>
               ...
             </configuration>
           </plugin>
         </plugins>
       </build>
       ...
     </project>
     ```

3. **Generate Javadoc Commands**
   - Use the following commands to generate Javadocs:
     ```sh
     mvn javadoc:javadoc
     mvn javadoc:jar
     mvn javadoc:aggregate
     mvn javadoc:aggregate-jar
     mvn javadoc:test-javadoc
     mvn javadoc:test-jar
     mvn javadoc:test-aggregate
     mvn javadoc:test-aggregate-jar
     ```

## License
This project is licensed under the MIT License - see the `LICENSE.txt` file for details.

## Versioned

We use [Git](https://github.com/) for version control. For available versions, see the tags in this repository.

## Author

**Hann Jang** 

