# User CRUD REST API - Step By Step Guide

## The Idea

You build a small **web service** for users. Another program (a browser, Postman, a mobile app) sends HTTP requests to it, and your service reads or changes users in a **PostgreSQL** database.

```text
Client  ──HTTP──>  UserController  ──>  UserRepository  ──Hibernate──>  PostgreSQL
(Postman)          (you write this)     (already done)                  (table "users")
```

**CRUD** means the four things you can do with data:

| Letter | Meaning | HTTP request | URL |
|---|---|---|---|
| **C** | Create | `POST` | `/api/users` |
| **R** | Read all | `GET` | `/api/users` |
| **R** | Read one | `GET` | `/api/users/{id}` |
| **U** | Update | `PUT` | `/api/users/{id}` |
| **D** | Delete | `DELETE` | `/api/users/{id}` |

## What Is Already Done

Do not change these:

| File | What it does |
|---|---|
| `entity/User.java` | One user: `id`, `firstName`, `lastName`, `email`, `age`. Hibernate makes the table from it. |
| `repository/UserRepository.java` | Saves and loads users. You write **no SQL**. |
| `application.properties` | The database address, user and password. |
| `UserControllerTest.java` | Tests that check your work. |

## Your Task

Open `controller/UserController.java`. It has **5 methods**. Each one has a `TODO` and a `TEMPORARY` line that answers `501 Not Implemented`.

For each method: write the code from the `TODO`, then delete the `TEMPORARY` line.

## The Repository Methods You Need

| Method | What it does |
|---|---|
| `userRepository.save(user)` | Saves a user. Returns the saved user (with its `id`). |
| `userRepository.findAll()` | Returns the list of all users. |
| `userRepository.findById(id)` | Returns an `Optional<User>` (maybe empty). |
| `userRepository.existsById(id)` | `true` if the user exists. |
| `userRepository.deleteById(id)` | Deletes the user. |

## The Answers You Need

| You want to return | Code |
|---|---|
| 200 with a body | `ResponseEntity.ok(body)` |
| 201 with a body | `ResponseEntity.status(HttpStatus.CREATED).body(body)` |
| 204 (no body) | `ResponseEntity.noContent().build()` |
| 404 (no body) | `ResponseEntity.notFound().build()` |

---

## Step 1 - Create The Database

Make sure PostgreSQL is running on your computer. Then create a database called `user_crud`:

```sql
CREATE DATABASE user_crud;
```

The application expects this by default:

```text
database: user_crud
username: postgres
password: postgres
port:     5432
```

If your username or password is different, change them in `src/main/resources/application.properties`.

## Step 2 - Run The Application

Run `UserCrudApplication` in IntelliJ.

Or from this folder in the terminal:

```bash
mvn spring-boot:run
```

The service is now on `http://localhost:8080`. You do not create the table: Hibernate creates `users` for you.

## Step 3 - See The Problem

Open `requests.http` in IntelliJ and run request **1** (the green arrow).

You get `501 Not Implemented`. Good, that is the starting point.

## Step 4 - Write The 5 Methods

Do them in this order. After each one, test it with the request in `requests.http`.

### Task 1 - `createUser`

```java
User saved = userRepository.save(user);
return _____;            // 201 with "saved" in the body
```

Test: request **1**. Expect `201` and a JSON with an `id`.

### Task 2 - `getAllUsers`

```java
List<User> users = _____;   // get all users from the repository
return _____;               // 200 with "users"
```

Test: request **2**. Expect `200` and a list.

### Task 3 - `getUserById`

```java
Optional<User> found = userRepository.findById(id);
if (found.isPresent()) {
    return _____;            // 200 with the user: found.get()
}
return _____;                // 404
```

Test: request **3**. Try an id that does not exist, you must get `404`.

### Task 4 - `updateUser`

```java
if (!userRepository.existsById(id)) {
    return _____;            // 404
}
newData.setId(id);           // use the id from the URL
User saved = userRepository.save(newData);
return _____;                // 200 with "saved"
```

Test: request **4**. Check with request **3** that the data changed.

### Task 5 - `deleteUser`

```java
if (!_____) {                // the user does not exist
    return _____;            // 404
}
userRepository.deleteById(id);
return _____;                // 204
```

Test: request **5**. Then request **3** must give `404`.

---

## Step 5 - Run The Tests

When all 5 methods are done, run the tests:

```bash
mvn test
```

Or run `UserControllerTest` in IntelliJ.

All 9 tests must be green. The tests use a small in-memory database, so they work even without PostgreSQL.

| If a test fails with | Check |
|---|---|
| `expected:<201> but was:<501>` | You did not delete the `TEMPORARY` line, or did not write the method. |
| `expected:<404> but was:<200>` or `500` | You forgot the "does not exist" check. |
| `expected:<200> but was:<404>` in the update test | Check `existsById(id)` and `setId(id)`. |

## Check The Database (optional)

In `psql` (or any database tool):

```sql
SELECT * FROM users;
```

Look at the console of the running application too. It prints the SQL that Hibernate runs (`insert into users ...`).

## Rules

Use Spring Boot, Spring Data JPA (Hibernate) and PostgreSQL only. Do not write SQL by hand and do not change `User` or `UserRepository`.
