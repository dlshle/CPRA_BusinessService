# Business Service

## Features
Business service provide course, term, professor, comment data from the 
database to the client-side. 

## Design
Typical MVC design. 
Models including business models and user model. Business
models include all models for business services(Course, Term, Professor, 
Comment). User service is a copy of user model from the user service. 

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
	password: "String", 
	terms: ["1", "2", "3"]
}
// terms is a list of term ids
```
### Course Collection
```
{
	id: "1",
	title: "String",
	description: "String",
	terms: ["1", "2", "3"]
}
// terms is a list of term ids
```
### Term Collection
```
{
	id: "1",
	title: "String",
	semester: "String",
	taughtBy: ["1", "2"],
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
	lastModifiedDate: "String",
}
// author is the user id of this comment
```
### Professor Collection
```
{
	id: "1",
	name: "String",
	description: "String",
	terms: ["1", "2", "3"]
}
// terms is a list of term ids
```
## User authentication
JWT token is used for validating current login user. Some APIs require user 
login to access. Without valid authentication, an error message will be 
returned from the server(handled in controllers with help from 
authentication utilities).

JWT token consists of body and signature. The body part only contains the 
username, and the signature is the password corresponding to the username.

Tokens will be renewed every 30 minutes. Implementation details are unspecified.

## Integration with User Service
TODO

## Future discussion
Whether a distributed database is needed to cache the current login user tokens?
