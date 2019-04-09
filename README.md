# java2DGameEngine
This is a 2D Game engine I am creating in Java for commercial use.
Examples of usage can be found in [examples](src/examples/)
##### Reference
engine.Window:
 - Allows for the creation of a custom window.
 - Functions:
   - setFullscreen()
   - setWindowSize(Dimension);
   - changeLocation(int x, int y); //adds the x and y to the current location
   - addKeyListen(KeyListener), addMouseListen(MouseListener), addMouseMotionListen(MouseMotionListener), addMouseWheelListen(MouseWheelListener);
   - addComp(Component) //adds any component not listed above
   - setVisible() //makes the JFrame seen, as well as setting some important variables like WIDTH, HEIGHT, WINDOW_RECT
   - stop() //stops the loop/program
   - paint(Graphics), update() //These both need overriding when extending `Window`. Only call `super.paint(Graphics)`.
   - drawCenteredString(Graphics2D, String, Rectangle) //draws a string in the exact center of a given rectangle.
###### Editing
Currently, I would like it if any issues or concerns would be put in the issues category. I will not be accepting any changes made directly from the community at this time.
