### Objective

Your assignment is to implement a bookstore REST API using Java and Spring.

### Brief

Lohgarra, a Wookie from Kashyyyk, has a great idea. She wants to build a marketplace that allows her and her friends to
self-publish their adventures and sell them online to other Wookies. The profits would then be collected and donated to
purchase medical supplies for an impoverished Ewok settlement.

### Tasks

- Implement assignment using:
    - Language: **Java**
    - Framework: **Spring**
- Implement a REST API returning JSON or XML based on the `Content-Type` header
- Implement a custom user model with a "author pseudonym" field
- Implement a book model. Each book should have a title, description, author (your custom user model), cover image and
  price
    - Choose the data type for each field that makes the most sense
- Provide an endpoint to authenticate with the API using username, password and return a JWT
- Implement REST endpoints for the `/books` resource
    - No authentication required
    - Allows only GET (List/Detail) operations
    - Make the List resource searchable with query parameters
- Provide REST resources for the authenticated user
    - Implement the typical CRUD operations for this resource
    - Implement an endpoint to unpublish a book (DELETE)
- Implement API tests for all endpoints.
- Please use a H2 in-memory database in case your solution is not dockerized.
- Don't forget to include Swagger annotation or a Postman collection to test the endpoints.
- If there are special instructions to install or make the app work, please include them in a section of this README.

### Evaluation Criteria

- **Java** best practices
- If you are using a framework make sure best practices are followed for models, configuration and tests
- Write API tests for all implemented endpoints
- Make sure that users may only unpublish their own books
- Bonus: Make sure the user _Darth Vader_ is unable to publish his work on Wookie Books

### CodeSubmit

Please organize, design, test and document your code as if it were going into production - then push your changes to the
master branch. After you have pushed your code, you may submit the assignment on the assignment page.

All the best and happy coding,

The Fetcher Team

### Test data

- All users passwords are 'password123'
- Darth Vader user id is number 3
- Use the postman collection to test