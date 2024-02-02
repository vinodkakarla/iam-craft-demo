# Craft Demo for IAM 
This is a Spring Boot application named `IamCraftDemoApplication`. It is a document storage service that interacts with AWS S3 for storing and retrieving documents. It also provides features like auditing and caching.
## Prerequisites
- Java 17 or higher- Maven- AWS S3 account
## Installation
1. Clone the repository```git clone https://github.com/vinodkakarla/iam-craft-demo.git```
2. Navigate to the project directory```cd IamCraftDemoApplication```
3. Build the project```mvn clean install```
## Usage
Run the application using the following command:
```mvn spring-boot:run```
## Features
- List Objects: Lists all the objects in the S3 bucket.- Get Object Metadata: Retrieves the metadata of a specific object.- Audit Object Metadata: Audits the metadata of all objects.
## Testing
The project contains unit tests for the `JsonUtils` class. Run the tests using the following command:
```mvn test```
## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.
