# User Tasks API

### Build App
- To build the project you need to have Maven
- Inside root folder execute maven command `mvn clean package`

### Run App
- Inside root folder execute maven command `java -jar target/user-tasks-api-1.0.0.jar`
- All endpoints will be running at `http://localhost:8080/api/...`
- Get all tasks with paging from `localhost:8080/api/users/1/tasks?page=0`

### Database
- App uses H2 in memory database
- Database name `tasksdb`
- Database console `http://localhost:8080/h2-console`
- Database instance only available while app is still running

### Scheduled Jobs
- Tasks status update is scheduled for every minute inside `TaskService`