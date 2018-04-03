## This example shows how to implement the login feature using the frontend-backend-separation design.

### To run this example:
1. clone this repository using `git clone https://github.com/UCI-Chenli-teaching/project2-login-example.git`
2. open Eclipse -> File -> import -> under "Maven" -> "Existing Maven Projects" -> Click "Finish".
3. For "Root Directory", click "Browse" and select this repository's folder. Click "Finish".
4. You can run this project on Tomcat now. The default username is `anteater` and password is `123456` .

### Brief Explanation

`login.html` contains the login form, but the default form action is disabled. It also includes jQuery and `login.js`.


`login.js` handles submitting the form. 
  - In line `37`, it sets up an event listener for the form submit action and binds it to `submitLoginForm` function. 
  - In function `submitLoginForm`, it disables the default form action and then sends a HTTP POST request to the backend.
  - In function `handleLoginResult`, it parses the JSON data sent by the backend, and if login is successful, it redirects to the `index.html` page. It login fails, it shows the error message.


`Login.java` is the server endpoint to handle the login request. It contains the following steps 
  - It gets the username and password values from the parameters.
  - It verifies the username and password.
  - If login success, it puts the `User` object in the session. Then sends back a JSON response: `{"status": "success", "message": "success"}` .
  - If login fails, the JSON response will be: `{"status": "success", "message": "incorrect password"}` .
   
 
 `LoginFilter.java` is a special `Filter` class. The purpose of this class is to implement: if the user hasn't login, redirect to the `login.html` page. 
   - A `Filter` class intercepts all incoming requests and decides if this request is allowed according to certain rules. See a detailed about `Filter`in a tutorial here: http://tutorials.jenkov.com/java-servlets/servlet-filters.html
   - The `web.xml` file needs to have the configuration of this `LoginFilter` class. 
   - In `Filter`, all requests will pass through the `doFilter` function.
   - `LoginFilter` fist checks if the URL is allowed to access without loggin in: `login.html`, `login.js`, and `login` (the url mapped to `Login.java`) are allowed.
   - Then it checks the session to see if the user already logs in. If not, it redirects the request to `login.html`.
  
 
