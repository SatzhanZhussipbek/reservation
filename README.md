<h1>Room reservation application</h1>
<h2>Instructions</h2>
<li>Download the project and unpack it</li>
<li>Go to the Terminal</li>
<li>Use "cd" command to get the location of the project</li>
<li>Write "javac -project_name.java"</li>
<li>Write "java -project_name"</li>
<li>In the IDE you need to connect to H2 database</li>
<li>Then you need to create tables "Room", "Client", "ReserveItem", "PhotoItem" in the database console</li>
<li>To create the tables use the .sql file in "resources/schema.sql"</li>
<li>Go to H2 console at the address: "//localhost:8087/h2-console"</li>
<li>When you perform operations in IDE, you can see immediate results in H2 console</li>
<li>To see endpoints and request types, you can access Swagger at "//localhost:8087/swagger-ui/index.html#"

<h3>This project is the backend of the Room reservation application for Web and Mobile (both Android and IOS)</h3>

```
Number 1 -> operations with Rooms

Number 2 -> reservation operations

Number 3 -> image related operations

Number 4 -> user related operations
  
``` 
<h3>Yandex disk is used for storage of images, and the database only contains its location within the disk</h3>
