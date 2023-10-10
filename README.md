# EasyHomeManagement
Creating a web application to manage  grocery stock and budget, along with organizing buying planning, can be a great way to streamline the home management tasks. Here's the plan to get to this:

<u>Features and functionalities</u> in this web application. Those are some key features would include:
**Inventory management:** Tracking the stock of groceries and household items, along with their categories (grocery, health products, furniture, cleaning products, etc.).
**Budget management:** Allowing as to set and manage our household budget.
**Low stock reminder:** Automatically reminding when certain items are near to finish.
**Buying planning:** Suggesting a buying plan based on the budget and priority of items running low.

<u>Technology Stack:</u> the technologies for in this web application. I used popular web development frameworks **Java17** and **spring boot 3.1.2** , and **thymeleaf** for the frontend.

***The Backend:*** Implement the backend logic to handle creation of the users account , lists, items , inventory management, budget tracking, and buying planning. This involves creating APIs to interact with the database and handle client requests.

***The Frontend:*** Create a client-friendly and responsive frontend that allows  to manage the inventory, budget, and buying planning to make the app visually appealing. (This frontend will communicate with the API Gateway to access the different functionalities in the futur but now it only part of the the groceryListService).For this purpose the applicatin use Thymeleaf , which is a server-side Java template engine that allows to create dynamic web pages by blending the HTML templates with Java code. It acts as a "motor" or engine for rendering templates, where you can embed dynamic data and expressions directly into your HTML templates using Thymeleaf's syntax. Thymeleaf templates are processed on the server side to generate the final HTML content that is sent to the client's browser. This allows to create dynamic and data-driven web pages without needing to write extensive JavaScript code on the client side, 

***User Authentication:*** the client authentication and authorization to ensure that only authorized users can access and modify the data.The application uses Spring Security choosing to use a custom UserDetailsService, typically making this choice to customize the way Spring Security loads user details (like username, password, and authorities) from the database.

***Design the Database:*** the structure of the database to store information about items, their categories, stock levels, prices, and any other relevant details. A well-organized database will make it easier to retrieve and manipulate data in our application.For this purpose I will use Postgresql 

***Deployment:*** The web hosting service and deployement of the web application is the AWS Cloud Server, to make it accessible online.

**GroceryListService :** is the first microserice of this application aims to be  , which will handle these functionnalities: 
Creation account: there is 2 kind of account user account , wich give the possibility of create lists and grouping them by category and access the lists in the home page  , adding items to list , modifing list .
And the second kind of account is the Admin account in which we add to the functionalities of the user account other possiblity to have access to all clients account edit them or delete them.
Security : The client registration, login, and authentication and the Set up access control and authorization mechanisms is implemented using spring security.For the purpose of loose coupling The application uses 3 table for the spring security to store the users username/passwords/authorities and add to this there is a separate table for the client username and all other properties.

**Microservices architecture** for the Easy Home Management application is a good idea, especially if we want to build a scalable, flexible, and maintainable system. Microservices allow us to break down our application into smaller, independent services that can be developed, deployed, and scaled independently. Each microservice can have its own specific functionality and interact with other microservices through APIs. Here's a suggested architecture for our microservices-based home management application:

***GroceryListService*** This first one can manage two great services which are : 
Responsible for managing the inventory of the grocery and household items. It will handle adding new items, updating stock levels, and providing information about items nearing completion (Inventory Service).
Manage the categories of items (grocery, health products, furniture, etc.). This service will provide information about different item categories and can be useful for organizing the inventory (Category Service).

***API Gateway:*** Implement an API Gateway as the entry point for all client requests. The API Gateway will route incoming requests to the appropriate microservices and handle authentication and authorization.

***Authentication and Authorization Service:*** Create a separate microservice responsible for managing client authentication and authorization. This service will handle client registration, login, and ensure that only authorized users can access certain functionalities.

***Budget Service:*** Build a microservice that manages the household budget. It will allow users to set and modify their budget, track expenses, and provide insights into spending patterns.

***Buying Planning Service:*** This microservice will handle the logic for suggesting a buying plan based on the inventory stock levels, priority, and budget constraints. It will offer recommendations for purchasing items that are running low.

***Notification Service:*** Implement a microservice responsible for sending notifications to users, such as low stock alerts or budget updates.

***Database Management:*** Each microservice should have its own database to ensure data isolation and improve scalability. 

***Containerization and Orchestration:*** Consider containerizing each microservice using Docker and use an orchestration tool like Kubernetes to manage and deploy the containers. This will simplify deployment and scaling.

***Monitoring and Logging:*** Implement monitoring and logging for each microservice to ensure that we can track system performance, detect issues, and troubleshoot when necessary.

__Functionalities To Add :__
*Low Stock Reminder:* Set up a system that regularly checks the stock levels and sends  reminders when items are near to finish.
*Buying Planning Algorithm:* Develop an algorithm that suggests a buying plan based on the priority, budget, and quantity of items running low. This could be a combination of factors like the urgency of the item, its cost, and the available budget.
*Testing and Bug Fixing:* Thoroughly test our web application and fix any bugs or issues that arise during testing.
*Continuous Improvement:* Consider gathering feedback from users (our self and other family members and friends) to make improvements and add new features over time.




In a microservices architecture, each service is typically designed to be as independent and self-contained as possible. This means that each service should be responsible for its own data and business logic, and the service should expose an API to communicate with other services or the client application. As such, a strict adherence to the traditional MVC (Model-View-Controller) pattern might not be the best fit for every microservice.
While some aspects of the MVC pattern can still be useful within a microservice, such as separating the concerns of data manipulation (Model), data representation (View), and business logic (Controller), we might want to consider alternative architectural patterns that align better with the microservices principles.

One such pattern commonly used in microservices is the Domain-Driven Design (DDD) pattern. In DDD, the focus is on designing the software around the core domain of the application, identifying bounded contexts, and dividing the application into smaller, manageable components based on business domains. Each microservice can then represent a specific bounded context and encapsulate its own domain logic and data.
Here's how we could structure the Budget Service in a microservices architecture with DDD in mind:

Domain Logic (Model): The Budget Service should contain the domain logic that governs the management of the household budget. This might include rules for setting budgets, tracking expenses, and generating budget-related insights.
API (Controller): The Budget Service should expose an API that allows other services or the client application to interact with it. This API could include endpoints for setting the budget, updating expenses, retrieving budget information, and more.
Data Storage: Each microservice should have its own database to ensure data isolation. In the case of the Budget Service, it would store information related to budgets, expenses, and any other relevant data.
Service Interface (View): In a microservices architecture, the concept of "View" might differ from the traditional MVC pattern. The Budget Service could provide a service interface to offer data in a format that other services or clients can consume. This could be in the form of RESTful APIs, GraphQL endpoints, or other communication methods.

Ultimately, the key is to strike a balance between maintaining the independence of each microservice while ensuring they can work together harmoniously to achieve the overall goals of the Easy Hme Management application.

Notes : 

For the security , I used Spring Security **org.springframework.boot/ spring-boot-starter-security** with the module of thymeleaf  that handel the relationship with this frmaework **org.thymeleaf.extras/ thymeleaf-extras-springsecurity6/ 3.1.0.M1** is used for integrating Thymeleaf (a popular template engine) with Spring Security (a security framework for Java applications) in the  context of Spring Security version 6. 
The thymeleaf-extras-springsecurity6 module provides various Thymeleaf dialects and utilities that make it easier to work with Spring Security concepts within the Thymeleaf templates. These dialects allow to create templates that are aware of Spring Security's authentication and authorization features, making it simpler to control content visibility based on user roles, permissions, and authentication status.
