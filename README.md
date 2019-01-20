# reece address book

### Environment:

#### JAVA 1.8

#### Gradle 2.8

#### Node 10.15.0

## Repository and build

### Clone the project from Github

    git clone https://github.com/inizhao/reece-address-boook.git

### Build the project with Gradle

    ./gradlew

## How to

1. I used JHipster 5.7.2 to help generating the boilerplate and scaffolding, thus it has dependency on Node as backend project.
   And the project has a lot more code than what the basic Acceptance Criteria asked.

2. The project comes with dependencies on Node

3. Swagger for the REST API:
   http://localhost:8080/#/admin/docs

4. By running ./gradlew it launch the dev profile, which use H2 in memory database locally.

5. H2 in memory database: http://localhost:8080/h2-console

6. API Authentication uses JWT. Default user name "admin", password "admin". This credential can be used to get the authentication token.

7. To access the API, set the following value in the request header "Authorization":
   Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGgiOiJST0xFX0FETUlOLFJPTEVfVVNFUiIsImV4cCI6MTU0ODA0NDMzNX0.l1_k8k7X6IATlVT3ZEA4fxl7C6aMy5fO1lE12jbb0ASZF8A93H_oE_PdW-I69hwHy3dbSNUko9wt-vBwRxVBFA

## Acceptance Criteria

### There are already two existing address books initialised in memory

1. There are two initial address book with ID 1 and 2. Each has 3 contacts.

### Users should be able to add a new contact entry to an existing address book

1. POST Endpoint: http://localhost:8080/api/contacts
2. Test data:
   {
   "addressBookId": 2,
   "name": "Forrest Gum",
   "phone": "0409090909"
   }

### Users should be able to remove an existing contact entry from an existing address book

1. DELETE Endpoint: http://localhost:8080/api/contacts/{id}
2. Test data: http://localhost:8080/api/contacts/3

### Users should be able to retireve all contacts in an address book

1. GET Endpoint: http://localhost:8080/api/address-books/{addressBookId}/contacts?unique=false
2. Test data: http://localhost:8080/api/address-books/1/contacts?unique=true

### Users should be able to retireve a unique set of all contacts across multiple address books

1. GET Endpoint: http://localhost:8080/api/address-books/{addressBookIds}/contacts?unique=true
2. The addressBookIds is comma separated string like "1,2"
3. Test data: http://localhost:8080/api/address-books/1%2C2/contacts?unique=true

## Assumptions

1. For the Acceptance criteria "Users should be able to retireve a unique set of all contacts across multiple address books". The unique
   logic is applied against the phone number.
2. Duplicate address book names are allowed
3. Duplicate contact names are allowed.

## Areas for improvement if having more time

1. Introducing DTO for AddressBook
2. I used the dev_data.xml for both dev initial data and integration test data for timely manner.
   Ideally integration test should have it's own test data configuration. Otherwise change of initial data could break test.
3. Add user authorisation check to ensure that user can only access or handle address book which are allowed to.

## Testing

To launch your application's tests, run:

    ./gradlew test
