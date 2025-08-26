<<<<<<< HEAD
# BudgetBounty

## Getting Started

### 1. Install Required Tools
Make sure the following are installed **before** running the project:

- **Eclipse IDE** (for Java developers)
- **Apache Maven** (3.8+)
- **Java** (17 or higher)
- **Spring Boot** (3.5.4 — matches `pom.xml`)
- **Oracle JDBC Driver** (`ojdbc11` — see version in `pom.xml`)

> 💡 Always check `pom.xml` for the exact versions of dependencies.

**Manual install of Oracle JDBC:**
1. Download `ojdbc11.jar` from [Oracle’s website](https://www.oracle.com/database/technologies/appdev/jdbc-downloads.html).
2. Install it into your local Maven repository:
   ```bash
   mvn install:install-file \
     -Dfile=ojdbc11.jar \
     -DgroupId=com.oracle.database.jdbc \
     -DartifactId=ojdbc11 \
     -Dversion=21.3.0.0 \
     -Dpackaging=jar



---

### 2. Configure Database Connection
Open `src/main/resources/application.properties` and fill in the missing details from your SQL Developer connection.
> ✅ Ensure your Oracle database is **connected, up, and running** in SQL Developer before starting the application.

---

### 3. Database Setup
On first run, tables will be auto-created via JPA.

- Insert dummy data for testing:
  - **Recommended:** Use Postman to send API requests (validates API + DB).
  - **Optional:** Insert directly in SQL Developer.

---

### 4. Running the Project in Eclipse

**1. Import Project**  
File → Import → Maven → Existing Maven Project → select the project folder → Finish.


**2. Update Project**  
Right-click the project → Maven → Update Project... → check Force Update of Snapshots/Releases → OK.


**3. Run Maven Clean**  
Right-click the project → Run As → Maven Clean.


**4. Run Maven Install**  
Right-click the project → Run As → Maven Install.


**5. Run the Application**  
Right-click the main Spring Boot application class (the one with @SpringBootApplication) → Run As → Spring Boot App.


---

### 5. Missing Dependencies

If Eclipse shows errors about Lombok or other dependencies:

**Lombok**

1. Download `lombok.jar` from [https://projectlombok.org/download](https://projectlombok.org/download)  
2. Run it:
java -jar lombok.jar


3. Point it to your Eclipse installation directory to integrate Lombok.  
4. Restart Eclipse.

**Other dependencies**  
If installing manually, place the JAR in your local Maven repo

> 💡 Always check `pom.xml` for the exact versions of dependencies.

### 6. Accessing the App
- The application will run at: [http://localhost:8081](http://localhost:8081)

=======
# BudgetBounty_NatWest_2025

NatWest BudgetBounty 2025
>>>>>>> bccbf523ba27853233817b00bbbcb44f49783dc4
