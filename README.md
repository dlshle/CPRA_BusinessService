# Business Service

Test user: 
```
{
    "username":"admin",
    "email":"badger@wisc.edu",
    "passwd":"123456"
}
```

Add user status token from /v1/auth to RequestHeader to enable all services.

## TODO:
- Add back reference(e.g. new term->use professorIds to back reference 
professors in professor.termIds)
- Add nullity test to all findBy methods.
- Integrating in memory LRUCache with DAO services.
- Make user auth services. *
- Make LFUCache for caching user status.
- Make tests for db services.
- Make tests for LRUCache.
- Make tests for API endpoints.
- Finish term professorName query
- Integration test.

## Design(Deprecated)
Typical MVC design. 
Models including business models and user model. Business
models include all models for business services(Course, Term, Professor, 
Comment). User service is a copy of user model from the user service. 

LRUCache is used to cache BusinessService database access results. 
LRFCache is used to cache user log-in status(JWT).

Controllers include course controller, term controller, professor controller,
and comment controller. All controller handle the basic CRUD of data with 
user authentication.
 
## Database and business handling
 MongoDB is used as the database server. Spring Data MongoDB is used as the 
 ORM for communicating with the db server. 
DB Design:
All ids are string.


## Request and Respond format

### Request format
For all Post request(need user login except for user):
#### User
```
Request format of user when registering
{
    email:"a@b.c",
    username:"abc",
    name:"name", // can be null
    passwd:"passwd",
}
Request format of user when updating can follow the user response format.
```

#### Course
```
{
    name:"name",
    description:"desc",
    termsIds:["1","2","3"] // or empty
}
Request format of course when updating can follow the user response format.
```

#### Term
```
{
    name:"name", // can follow course
    description:"desc", // can follow course
    year: 2019,
    season: "FALL", //"FALL", "SPRING", or "SUMMER"
    professorIds: ["1", "2"]
}  
Request format of term when updating can follow the user response format.
```

#### Comment
```
{
    termId: "1",
    rating: 5.0
}
Request format of comment when updating can follow the user response format.
```

#### Professor
```
{
    name: "name",
    description: "maybe something bad about this professor"
}

Request format of professor when updating can follow the user response format.
```

All Put and Delete request should follow the general respond format.

### General respond format
#### User
```
{
	id: "1", 
	username: "String", 
	email: "String", 
	password: "String",  //passwd on the front-end
	favorites: [
	    {
            name: "name",
            id: "1"
        }, 
        {
            name: "name1",
            id: "2"
        }
	],
	createdDate: date_type
}
// terms is a list of term name, id pair
```
#### Course 
```
{
	id: "1",
	name: "String",
	description: "String",
	terms: [
	    {
	        name: "term",
	        id: "1"
        }
	]
}
// terms is a list of term ids
```
#### Term 
```
{
	id: "1",
	courseId: "1", //reference to parent course
	name: "String",
	semester: "String", //All uppercase{SPRING, SUMMER, FALL}
	professors: [
	    {
	        name: "p",
	        id: "1"
        }, 
        {
            name: "p1",
            id: "2"
        },
	].
	description: "String",
	averageRating: 1.5,
	comments: [
	    {
	        name: "c1",
	        id: "1"
	    }
	]
}
// professors is a list of professor name, id pair
// rating is the average rating from comments
// comments is a list of comment name, id pair
```
#### Comment 
```
{
	id: "1",
	content: "String",
	author: {name: "author", id: "1"},
	lastEditedBy: {name: "new_author", id: "2"},
	lastModifiedDate: Date
}
// author and lastEditedBy are the name, id pair of the author of this comment
```
#### Professor 
```
{
	id: "1",
	name: "String",
	description: "String",
	terms: [
	    {
	        name: "term",
	        id: "1"
	    }
	]
}
// terms is a list of term ids
```

## API End Points

### Respond Objects
For all valid requests, the system responds with an AjaxResponse Object where
 it specifies the status of the response.
 
```
{
    isSuccess: boolea,
    status: int, // 200 for success, 400 for invalid request param
    message: string, // all failed requests has a corresponding error message
    data: {object} // all success response has a corresponding object
}
```

### End Points specification
All search apis return a list of results, otherwise, return a JSON object.

All bullet points in *italic* indicates the service requires login and a 
request body specifying the object that needs to be modified.

If the client tries to access services that require login without a valid 
token, the server will respond with an object with status attribute of value 
400.

All service start with "Search", "Query", and "Get All" means it responds 
with a list of objects.
All service start with "Get" and other verbs means it responds with an object. 

Auth endpoints:
- *Login*: POST /v1/auth/login/
    - Responds with a token corresponding to the login user.
- *Validate token*: GET /v1/auth/token/{token}
    - Responds with

User endpoints:
- Register: POST /v1/users/
- *Update User Info(Only user or admin can resign a user)*: PUT /v1/users/
- *Remove User(Only user or admin can resign a user)*: DELETE /v1/users/
- Get All Users: GET /v1/users/
- Get User By Id: GET /v1/users/{id}
- Get User By Email: GET /v1/users/email/{email}
- Get User By Username: GET /v1/users/username/{username}
- Search User By Name: GET /v1/users/name/{name}

Course endpoints:
- *Upload Course*: POST /v1/courses/
- *Update Course*: PUT /v1/courses/
- *Remove Course(Requires admin privilege)*: DELETE /v1/courses/
- Get All Courses: GET /v1/courses/
- Get Course By Id: GET /v1/courses/{id}
- Get Course By Name: GET /v1/courses/name/{name}

Term endpoints:
- *Add Term(requires login)*: POST /v1/terms/
- *Update Term*: PUT /v1/terms/
- *Remove Term(Requires admin privilege)*: DELETE /v1/terms/
- *Favorite Term*: PUT /v1/terms/favorite
- *Unfavorite Term*: PUT /v1/terms/unfavorite
- Get All Terms: GET /v1/terms/
- Search All Terms By Course Id: GET /v1/terms/courseId/{course_id}
- Search All Terms By Course Name: GET /v1/terms/courseName/{course_name}
- Search All Terms By Rating: GET /v1/terms/rating/{rating}
- Search All Terms By Year: GET /v1/terms/year/{year}
- Get Term By Id: GET /v1/terms/{id}
- Query Terms: GET /v1/terms/query/?year={int_year}&from={int_year_from}&to
={int_year_to}&season={str_season}&courseName={str_course_name}&averageRating
={double_average_rating}&ratingFrom={double_rating_from}&ratingTo
={double_rating_to}

Professor endpoints:
- *Upload Professor*: POST /v1/professors/
- *Update Professor*: PUT /v1/professors/
- *Remove Professor(Requires admin privilege)*: DELETE /v1/professors/
- Get All Professors: GET /v1/professors/
- Get Professor By Id: GET /v1/professors/{id}
- Search All Professor By Name: GET /v1/professors/name/{name}

Comment endpoints:
- *Upload Comment*: POST /v1/comments/
- *Update Comment*: PUT /v1/comments/
- *Remove Comment(either admin or author can remove)*: DELETE /v1/comments/
- Get All Comments: GET /v1/comments/
- Get Comment By Id: GET /v1/comments/{id}

### Back-reference relations
Since multiple back-reference relations exist in the data model. Some are 
handled such that one data updates, its parent or friend data will also update.

Back-reference relations and handlings:
- Term
    - When a term is updated, professors in its professor list and its parent 
    course will also be updated.
    - When a term is removed, professors in its professor list and its parent 
    courses will also be updated. Corresponding comments will also be removed.
- Professor
    - When a professor is updated, terms in its termIds list will also be 
    updated.
    - When a professor is removed, terms in its termIds list will also be 
    updated.
- Course
    - When a course is updated, its corresponding terms will also be updated.
    - When a course is removed, its corresponding terms will also be removed.

## User authentication
JWT token is used for validating current login user. Some APIs require user 
login to access. Without valid authentication, an error message will be 
returned from the server(handled in controllers with help from 
authentication utilities).

JWT token consists of issued time, body, and signature. The body part only 
contains the username, and the signature is the password corresponding to 
the username.

Once a token is received on the front-end, the client will inject the token 
to the header, so that for each request, the server will receive the token 
from request header. 



Tokens will be renewed every 30 minutes. Implementation details are unspecified.

## Integration with Comment Filtering Service
Considering using different ports for different services. Once a comment is 
received, pass it to the Comment Filtering Service. Once a decision is made, 
CFS saves the comment in MongoDB.
∵傻逼阿三滚犊子了，这个得再考虑

## Future discussion
Whether a distributed database is needed to cache the current login user tokens?
