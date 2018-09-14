### High level design
* The program consists of 8 classes in total. 
* Welcome.java is the welcome page that displays when the program starts, and it triggers the Main.java to run when the user presses the "Enter" key. 
* The Main class creates the animation and controls the interaction between the bouncer, the bricks, the paddle, and the power-ups. 
* The bouncer has its own class named Bouncer.java, which deals with the bouncer's the movement and collision with the wall
* There are 3 types of power-ups: ExtraBallPower, PointsPower, and SizePower. These three power-ups all have their own class and inherits the PowerUp class. The PowerUp class 
handles the movement of the power-ups and the collision with the paddle
* The Texts class contains the winning and losing text when the game reaches a certain stage

### How to add new features
* Add different levels with different layout of bricks
    * Create a new .txt file in the resources folder. Write digits from 1-4
    * Import the file as a string in the global field
    * Depending on the number of columns in your design, consider changing BRICK_COLUMN in the Main class's field
    * Change the switch(current_level) statement in the setUpGame method
* Add a different power-up
    * Create a class that extends PowerUp.java
    * Depending on the effect of your method, you might want to override the hitPaddle method. This method deals with
    what would happen when the power-up hits the paddle
    * Import the image as a string in the global field of Main class
    * Initiate an array of your power-up in the setUpGame class
    * In the for loop within the step method, create an instance of your power-up and specify the effect

### Design trade-offs
* I created the paddle directly inside the Main class rather than creating a separate class for it. When dealing with the
collision with the paddle, I simply used the context from Main class to get the paddle. This implementation is fairly convenient,
but it would be nicer to have a separate class for the paddle so that the code becomes more organized.
* I did not create a separate class for bricks, but I used Scanner to read the digits from .txt files and understand the layout
of different bricks. In order to add or modify the layout, one must be familiar with the convention that I use, which is a con
for this design. For instance, the digit 4 in the .txt file means it will create a brick that goes away after 3 hits, and the
digit 1 in the .txt file means there is no brick. This design might be confusing for people who see my code for the first time.
It is probably a better practice to create 3 different brick classes.

### Assumptions/Decisions to simplify or resolve ambiguities in the project's functionality
* At first I tried to use the .gif files from the resources folder to create bricks. However, I was not familiar with
the coordinates for ImageViews, and I had trouble making the bouncer bounce correctly from the brick. Thus, I chose rectangles
to construct bricks in the end.
* When the brick is supposed to disappear, I first set the width and the
height to 0, but the bouncer still bounces around that region where the brick existed previously. Therefore, in order to
remove the bricks, I chose to set the X and Y to a negative value additionally. In this way, it resolves the conflict, but
there must be better implementations.