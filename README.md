# Capstone QMQ
### Group #5
### Members: [Matthew Klein](https://github.com/mklein97), [Quang Pham](https://github.com/quangpham1019), [Quang Du](https://github.com/theta1995)

# Problem Statement

The Admin of Noodemy faced a significant challenge in organizing and distributing learning materials to a large audience while ensuring that users could quickly enroll in courses tailored to their needs. Noodemy aims to offer courses to users worldwide, but the traditional classroom model is not feasible for accommodating the vast geographic distances involved. Additionally, it is not cost-effective for users to travel long distances to attend one or several courses. Users from abroad encounter difficulties such as long travel times, finding accommodation, and unpredictable expenses, which can deter them from enrolling. This not only limits Noodemy's reach but also results in lost opportunities with potential global customers. Opening more branches or classrooms would require substantial investment in infrastructure, human resources, and logistics. 

By implementing an online learning platform, Noodemy can enhance its global presence and reach, serving more customers efficiently and scalably. With the rapid growth and integration of the internet into everyday life, establishing an online presence ensures that Noodemy remains relevant and aligned with current trends and audience needs.

# Technical Solution

To address the challenges outlined in our problem statement, we will develop a comprehensive web application designed specifically for Noodemy. This application will empower admins to create, update, and delete courses while providing users with the ability to browse and enroll in these courses seamlessly. By facilitating course management and user enrollment, the application will effectively tackle the core problem of organizing and distributing learning materials to a global audience.

The key features and functionalities of the application will include:

**1. Course Creation:** Noodemy admins will have the capability to create, update, and delete courses, ensuring that the course offerings are always current and relevant.

**2. Course Discovery:** The application will allow anyone, whether authenticated or unauthenticated, to browse available courses. Users will be able to filter courses by enrollment status and sort them by category and price range, making it easier to find courses that meet their needs.

**3. User Enrollment:** Users will have the ability to enroll in courses of their choice. To maintain clarity and organization, the system will prevent users from enrolling in courses they are already enrolled in.

**4. User Comments:** Enrolled users will be able to leave comments on courses to share their learning experiences. This feature will foster community engagement and provide valuable feedback for both admins and prospective students. Comments will be public and accessible to anyone, enhancing the transparency of the learning experience.

By implementing these features, the web application will not only streamline the course management process for admins but also enhance the user experience for students, ultimately expanding Noodemy's reach and effectiveness in delivering quality education globally.

# User scenarios

Scenario 1: John, a student in England, wants to enroll in a course offered by Noodemy that is not offered in his country. John cannot afford to move to the US just for the course. Using the Noodemy web app, he can enroll in the course and learn the material without all the traveling expenses.

Scenario 2: Linda, a student in the US, is currently enrolled in a university pursuing a degree in Computer Science. Linda wants to learn more about Advanced Data Algorithm, a topic her coursework does not cover. She searches for courses about “Advanced Data Algorithm” on Noodemy, reads through the comments to decide which course is suitable, and enrolls. She can enrich her learning at her own pace and convenience.

Scenario 3: Jim, an admin who wants to bring technology literacy to elder folks to protect themselves from online scams. He is able to create a course on Noodemy that is easily accessible to his targeted audience. 

# Technology Stack

Frontend: React with TypeScript for building interactive UIs with dynamic features.

Backend: Spring Boot for creating a secure REST API to handle user authentication, course management, and data storage.

Database: MySQL to store users and courses data, including relationships between users and courses.

Containerization: Docker for packaging and isolating development. 

Testing: JUnit5 for unit tests and Mockito to replace repository doubles. 

Authentication: JWT (JSON Web Token) for secure user login and role management.

# Stretch Goal

- Using TypeScript on the front for static typing, which helps catch errors at compile time rather than runtime. This can lead to fewer bugs and more robust code.

# Glossary

**User**

> A person who wants to learn about a particular concept/topic.
> 
> A user can enroll in many courses.
> 
> A user can drop a course.

**Admin**

> An expert who wants to share his/her knowledge for additional income.
> 
> They can create as many courses and set their prices as they want.
> 
> They can drop a student from their course.
> 
> They can remove the course, but only when no students are attending it.

**Role**
> The access level of a user. There are two roles, USER and ADMIN.
>
> ADMIN can perform CRUD operations on courses they create.
> 
> USER can browse courses and enroll in courses.
> 

**Course**
> Created and edited by Admin. The user can enroll or drop from the course
>
**Enrollment**
> 
> The action or state of the user being enrolled in a course.
> 
**Category**
> The course category, or the type of learning material that is being taught.
> 
> For example, Science, Math, etc.
**Comment**
> Comment left by user who enrolled in the course to express their experience.

# Database Diagrapm

![db_Schema](https://github.com/user-attachments/assets/916571b4-6072-461d-a8ac-75c287021cc4)



# Class List

### App

- `public static void main(String[])` -- instantiate all required classes with valid arguments, dependency injection. run controller

### Data Layer

data.mapper.EnrollmentMapper 
- `Public Enrollment mapRow(ResultSet resultSet, int i) throws SQLException`

data.mapper.UserMapper
- `Public User mapRow(ResultSet resultSet, int i) throws SQLException`

data.mapper.Course
- `Public Course mapRow(ResultSet resultSet, int i) throws SQLException`

data.mapper.Comment
- `Public Comment mapRow(ResultSet resultSet, int i) throws SQLException`

data.mapper.Category
- `Public Comment mapRow(ResultSet resultSet, int i) throws SQLException`

data.CourseRepository 
- `List<Course> findAll()`
- `Course findById()`
- `Course add()`
- `boolean update()`
- `boolean deleteById()`

data.UserRepository
- `List<User> findAll()`
- `User findById()`
- `User add()`

data.EnrollmentRepository
- `List<Enrollment> findAll()`
- `Enrollment findById`
- `Enrollment add()`
- `boolean update()`
- `boolean deleteById()`

data.CommentRepository
- `List<Comment> findAll()`
- `CommentfindById()`
- `Comment add()`
- `boolean update()`
- `boolean deleteById()`

data.CategoryRepository
- `List<Category> findAll()`
- `Category findById()`

data.AppUserRepository
- `public findByUsername(String username)`
- `public add(AppUser)`
- `public update(AppUser)`
- `private map()`
- `private getRolesByUserId()`
- `private updateRoles()`
	
### Models

model.AppUser
- `private appUserId int`
- `Private username String`
- `Private password String`
- `Private disabled boolean`
- `Private roles List<String>`
- `Private hasRole() boolean`
- `Getters and setters`

model.User
- `Private userId int`
- `Private firstName String`
- `Private lastName String`
- `Private email String`
- `Private dob Date`
- `Private courses List<Courses>`
- `Private appUserId int`
- `Getters and setters`
- `Override equals and hashcode`

model.Course
- `Private courseId int`
- `Private courseName String`
- `Private courseDescription String`
- `Private price BigDecimal`
- `Private category Category`
- `Getters and setters`
- `Override equals and hashcode`

model.Enrollemt
- `Private enrollmentId int`
- `Private studentId int`
- `Private courseId int`
- `Getters and setters`
- `Override equals and hashcode`

model.Category
- `Private categoryId int`
- `Private categoryName String`
- `Private categoryDescription String`
- `Private categoryCode String`
- `Getters and setters`
- `Override equals and hashcode`

model.Comment
- `Private commentId int`
- `Private userId int`
- `Private courseId int`
- `Private comment String`
- `Getters and setters`
- `Override equals and hashcode`

### Domain Layer

domain.enum 
- `ResultType {SUCCESS, IN_VALID, NOT_FOUND}`

domain.Result<T>
- `Private messages List<String>`
- `Private resultType ResultType`
- `Getters and Setters for payload and messages.`

domain.UserService
- `List<User> findAll()`
- `User findById()`
- `User add()`
- `validate(User user)`

domain.CourseService
- `List<Course> findAll()`
- `Course findById()`
- `Course add()`
- `boolean update()`
- `boolean deleteById()` 
- `validate()`

domain.EnrollmentService
- `List<Enrollment> findAll()`
- `Enrollment findById()`
- `Enrollment add()`
- `boolean update()`
- `boolean deleteById() `
- `validate()`

domain.CategoryService
- `List<Category> findAll()`
- `Category findById()`

### Controller

controller.AuthController
- `POST authenticate ("api/authenticate")`
	- request contains {username, password}
	- response contains {username, roleName, jwtToken}
- `POST register ("api/register")`
	- request contains AppUser (exclude appUserId) and UserProfile (exclude userId, appUserId)
	- response contains {userId, username}

controller.UserProfileController
- `GET all user profiles ("/api/users)` - response contains a list of UserProfile
- `GET a user profile ("/api/users/{userId}")` - response contains a UserProfile
- `PUT a user profile ("/api/users/{userId}")`
	- request contains UserProfile
	- response contains no content

controller.CourseController
- `GET all course ("/api/courses")` - response contains a list of courses
- `GET a course ("/api/courses/{courseId}")` - response contains a course object
- `POST a course ("/api/courses")` 
	- request contains Course (exclude courseId)
	- response contains the new Course (with courseId)
- `PUT a course ("/api/courses/{courseId}")`
	- request contains Course
	- response contains no content
- `DELETE a course ("/api/courses/{courseId}")` - response contains no content

controller.EnrollmentController
- `POST an enrollment ("/api/enrollments/{courseId}/{userId}")` 
	- response contains no content
- `DELETE an enrollment ("/api/enrollments/{enrollmentId}")` - response contains no content

controller.CommentController
- `GET all comments for a course ("/api/courses/{courseId}/comments")` - response contains a list of comments for a course
- `GET all comments for a user ("/api/users/{userId}/comments")` - response contains a list of comments by a user
- `POST a comment ("api/comments")`
	- request contains Comment (exclude commentId)
	- response contains the new Comment (with commentId)

controller.CategoryController
- `GET all categories ("/api/categories")` - response contains a list of all categories

### Security

security.AppUserService
- `private final AppUserRepository repository`
- `private final PasswordEncoder encoder` 
- `public AppUserService implements UserDetailsService`
- `public register(String username, String password)`
- `private validate()`

security.JwtConverter
- `public String getTokenFromUser(User user)`
- `public User getUserFromToken(String token)`

security.JwtRequestFilter
- `public JwtRequestFilter extends BasicAuthenticationFilter`

security.SecurityConfig
- `public SecurityConfig extends WebSecurityConfigurerAdapter`

## Validation

* [ ] When creating a new account
    * [ ] Username is unique and not blank
    * [ ] Date of Birth is a valid date (not under 13 years old and not in the future)
    * [ ] First name and last name is not blank
    * [ ] Email is unique, valid, and not blank. Valid email consist of an `@` and the postfix of  `.com, .org, .net`
    * [ ] Password has a length between [7,30].
* [ ] When logging in,
    * [ ] Username is required
    * [ ] Password is required
    * [ ] Username must match
    * [ ] Password must match
    * [ ] AppUser is not disabled
* [ ] When creating/updating a course
    * [ ] course name is not blank
    * [ ] course category is not null
    * [ ] course description is not blank
    * [ ] course price is a a valid positive number between [1,1000]
    * [ ] course time to complete is a valid positive number between [1,100]
* [ ] When enrolling course
	* [ ] User must not be already enrolled.
* [ ] When adding a comment
    * [ ] Enrollment must exists
    * [ ] Comment is not null or blank
    * [ ] Date created must be in the past
* [ ] When accessing user profile
	* [ ] ADMIN can access all profiles
	* [ ] USER can access his/her own profile
* [ ] Ensure user is admin when modifying course data
* [ ] Ensure user token matches when deleting comment
* [ ] Ensure user token matches when dropping course

# Task List

### Containerization
* [ ] Set up Docker with MySQL (0.5 Hour)

### Data Layer
* [ ] Create development and test schema scripts (1 Hour)
* [ ] Create JdbcTemplateRepository interface (0.25 Hour)
* [ ] Implement CourseJdbcTemplateRepository (0.5 Hour)
* [ ] Implement UserJdbcTemplateRepository (0.5 Hour)
* [ ] Implement EnrollmentJdbcTemplateRepository (0.5 Hour)
* [ ] Implement CommentJdbcTemplateRepository (0.5 Hour)
* [ ] Implement CategoryJdbcTemplateRepository (0.5 Hour)
* [ ] Create a mapper for each repository (1.5 Hour)


### Domain Layer
* [ ] Implement UserService domain logic and validation (1.0 Hour)
* [ ] Implement CourseService domain logic and validation (1.0 Hour)
* [ ] Implement CommentService domain logic and validation (1.0 Hour)
* [ ] Implement EnrollmentService domain logic and validation (1.0 Hour)
* [ ] Implement CategoryService domain logic and validation (1.0 Hour)

### Controller
* [ ] Sign up user/admin (1.0 Hour)
* [ ] User/admin login (0.5 Hour)
* [ ] Get all courses (0.5 Hour)
* [ ] Get user's personal details.
* [ ] Admin can create course (1.0  Hour)
* [ ] User can enroll in course (0.5)
* [ ] User can create comment (0.5 Hour)
* [ ] User can drop course (0.5 Hour)

### Security
* [ ] AuthController (0.5 Hour)
* [ ] JwtConverter (0.5 Hour)
* [ ] SecurityConfig (1.0 Hour)
* [ ] JwtRequestFilter (1.0 Hour)

### Front-End
* [ ] Setup React typescript app (0.1 hours)
* [ ] Create router for each page element (0.5 hours)
* [ ] Stub out each react element (0.5 hours)
* [ ] Create Course element layout (1.5 hours)
* [ ] Create Comment and Delete Confirm modals
* [ ] Create Home Page (0.5 hours)
* [ ] Create login page (0.1 hours)
* [ ] Create new user signup page (0.2 hours)
* [ ] Create add/edit course page form (0.2 hours)
* [ ] Create user and admin details page (0.1 hours)
* [ ] Implement HTTP requests to backend to populate data (3 hours)
* [ ] Implement event handling and conditionals for a fully responsive user interface (4 hours)

### Testing
* [ ] Import the necessary JUnit 5 packaging, appropriate @BeforeEach or @BeforeAll for each test. (0.5 Hour)
* [ ] Create a set_known_good_state procedure for the test schema (0.25 Hour)
* [ ] Incorporate Mockito to replace test doubles when testing the domain layer (0.5 Hour)
* [ ] Set up and write the test for CourseJdbcTemplateRepository (1.0 Hour)
	* [ ] shouldAdd()
 	* [ ] shouldUpdate()
  	* [ ] shouldDelete()
  	* [ ] shouldFindAll()
  	* [ ] shouldFindById()
* [ ] Set up and write the test for EnrollmentJdbcTemplateRepository (0.5 Hour)
	* [ ] shouldFindAll()
 	* [ ] shouldDelete()
* [ ] Set up and write the test for UserJdbcTemplateRepository (1.0 Hour)
	* [ ] shouldFindAll()
 	* [ ] shouldFindById()
  	* [ ] shouldAdd()  
* [ ] Set up and write the test for UserService validation with Mockito (1.0 Hour)
	* [ ] shouldFail_whenUsernameNotUnique()
 	* [ ] shouldFail_whenUsernameIsBlankOrNull()
  	* [ ] shouldFail_whenDobIsInTheFuture()
  	* [ ] shouldFail_whenDobLessThan13Years()
  	* [ ] shouldFail_whenEmailIsInvalid()
  	* [ ] shouldFail_whenFirstNameIsEmpty()
  	* [ ] shouldFail_whenPasswordLengthIsOutOfBound()  
* [ ] Set up and write the test for CourseService validation with Mockito (1.0 Hour)
	* [ ] shouldFail_whenPriceIsOutOfBound()
 	* [ ] shouldFail_whenHoursIsOutOfBound()
  	* [ ] shouldFail_whenNameIsEmpty()
  	* [ ] shouldFail_whenCategoryIsEmpty()
  	* [ ] shouldFail_whenDescriptionIsEmpty()   
* [ ] Set up and write the test for EnrollmentService validation with Mockito (1.0 Hour)
	* [ ] shouldFail_whenUserIsAlreadyEnrolled() 
* [ ] Set up and write the test for CommentService validation with Mockito (1.0 Hour)
	* [ ] shouldFail_whenUserIsNotEnrolledInTheCourseTheyCommented()
 	* [ ] shouldFail_whenCommentIsBlank()
     
### Stretch Goal

* [ ] Convert JavaScript to TypeScript on the front-end (4.0 Hour)

# Wireframe

![HomePage(nouser)](https://github.com/user-attachments/assets/1485828e-e4da-4ee0-acb5-b4ad8c407ee9)

![HomePage(user)](https://github.com/user-attachments/assets/1b37a4a5-41bc-4468-9376-08b07418742b)

![LoginPage](https://github.com/user-attachments/assets/d6fb8512-4409-4d3b-896a-df9dd81b7ed1)

![CreateAccount](https://github.com/user-attachments/assets/b2c98fc3-d932-4012-9f75-22fae985e443)

![AdminAccountDetails](https://github.com/user-attachments/assets/63dca70b-8c9b-4687-8e97-2de976a75e93)

![UserAccountDetails](https://github.com/user-attachments/assets/9db7563f-f2bf-41f2-8e75-5dc537df320c)

![CourseList(user)](https://github.com/user-attachments/assets/bf059e8e-e6cc-4bc2-abbc-a6fb7a7bb87a)

![CourseList(admin)](https://github.com/user-attachments/assets/6a37d414-a6a3-4af5-bc48-ee12c9b64ad8)

![CreateNewCourse](https://github.com/user-attachments/assets/3bb49044-fffe-4483-aaea-e3d82d9e05fc)

