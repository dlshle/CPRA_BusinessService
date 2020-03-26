# Business Service

# TODO:
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
### User Collection
```
{
	id: "1", 
	username: "String", 
	email: "String", 
	password: "String",  //passwd on the front-end
	salt: "String",
	favorites: ["1", "2", "3"],
	createdDate: date_type
}
// terms is a list of term ids
```
### Course Collection
```
{
	id: "1",
	name: "String",
	description: "String",
	termIds: ["1", "2", "3"]
}
// terms is a list of term ids
```
### Term Collection
```
{
	id: "1",
	courseId: "1", //reference to parent course
	name: "String",
	semester: "String", //All uppercase{SPRING, SUMMER, FALL}
	professorIds: ["1", "2"],
	description: "String",
	rating: 1.5,
	comments: ["1", "2", "3"]
}
// taughBy is a list of professor ids
// rating is the average rating from comments
// comments is a list of comment ids
```
### Comment Collection
```
{
	id: "1",
	content: "String",
	author: "1",
	lastEditedBy: "1",
	lastModifiedDate: "String"
}
// author is the user id of this comment
```
### Professor Collection
```
{
	id: "1",
	name: "String",
	description: "String",
	termIds: ["1", "2", "3"]
}
// terms is a list of term ids
```

## API End Points
TODO
## User authentication
JWT token is used for validating current login user. Some APIs require user 
login to access. Without valid authentication, an error message will be 
returned from the server(handled in controllers with help from 
authentication utilities).

JWT token consists of body and signature. The body part only contains the 
username, and the signature is the password corresponding to the username.

Tokens will be renewed every 30 minutes. Implementation details are unspecified.

## Integration with Comment Filtering Service
Considering using different ports for different services. Once a comment is 
received, pass it to the Comment Filtering Service. Once a decision is made, 
CFS saves the comment in MongoDB.

## Future discussion
Whether a distributed database is needed to cache the current login user tokens?
