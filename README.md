# Task Manager Web App

> Simple To-do Application

My first Java web app.

## Endpoints

All the tasks operations, within the `/tasks/` root path, requires authentication. The authentication method follows the [basic access](https://en.wikipedia.org/wiki/Basic_access_authentication) standard, where username and password must be sent through request's headers in Base64 encoding: `Authorization: Basic <credentials>`. Here the `credentials` must be generated by encoding the string `<username>:<password>`.

Table below summarize the application's endpoints.

| Path          | Method | Body              | Description                                         |
| ------------- | ------ | ----------------- | --------------------------------------------------- |
| `/users/`     | `POST` | [User](#users)    | Register a new user                                 |
| `/tasks/`     | `POST` | [Task](#tasks)    | Create a new task for the authenticated user        |
| `/tasks/`     | `GET`  |                   | Show the task list of the authenticated user        |
| `/tasks/{id}` | `PUT`  | [Task](#tasks)    | Update the task of the given ID with new properties |

> [!NOTE]  
> Model's auto-generated fields are not required to be sent in the request body.

## Models

There are 2 entities in the application: User and Task.

### Users

An user owns multiple tasks.

| Property      | Data Type     | Required  | Constraint     | Description                   | Autogenerated ? |
| ------------- | ------------- | --------- | -------------- | ----------------------------- | ----------------|
| id            | UUID          | Yes       | `Unique      ` | User's ID                     | Yes             |
| username      | String        | Yes       | `length < 255` | Username                      | No              |
| name          | String        | No        | `length < 255` | User's full name              | No              |
| password      | String        | Yes       | `length < 255` | User's password               | No              |
| createdAt     | LocalDateTime | Yes       |                | User's registration timestamp | Yes             |


### Tasks

Tasks can be only managed by their owners.

| Property      | Data Type     | Required  | Constraint              | Description                   | Autogenerated ? |
| ------------- | ------------- | --------- | ----------------------- | ----------------------------- | ----------------|
| id            | UUID          | Yes       | `Unique`                | Task's ID                     | Yes             |
| userId        | UUID          | Yes       | `Foreign Key`           | Task's owner ID               | Yes             |
| title         | String        | Yes       | `3 < length < 50`       | Task's Title                  | No              |
| description   | String        | No        | `length < 255`          | Task's full name              | No              |
| priority      | String        | No        | `HIGH | MEDIUM | LOW`   | Task's password               | No              |
| startAt       | LocalDateTime | No        | Cannot be after `endAt` | Task's start timestamp        | No              |
| endAt         | LocalDateTime | No        |                         | Task's end timestamp          | No              |
| createdAt     | LocalDateTime | Yes       |                         | Task's registration timestamp | Yes             |