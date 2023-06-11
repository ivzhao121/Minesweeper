# Core Concepts

- The first concept I used was 2-D arrays, which implemented the board of my game. This feature is appropriate because Minesweeper is a game with
  a grid, where each cell holds a value. As a result, my 2-D array holds numbers that correspond to different things of the game, such as mines or 
  squares that tell how many mines adjacent there are, on the board. I also used a 2-D array to represent which cell was revealed by the user. 
  This 2-D array of boolean values correspond to the 2-D array for the board. I felt this was appropriate because each cell on the game board 
  should have a corresponding variable that tells whether it is revealed or not. 

- The second concept I used was collections. I used collections to implement my flag and undo function for Minesweeper. By 
  keeping the squares in collections, I can add and remove specific flags or undo a move any time and as much as I want during the game. 
  Also the order of elements matters for the undo function, which means a LinkedList is perfect because whenever I wanted to undo a move I 
  could easily access the last object of the LinkedList and go from there. If I had just used an array my code would've been less efficient 
  and harder to implement because there isn't a specific size I know what to make the array to be. This is why collections are the more appropriate 
  structure here.
    
- The third concept I used was the JUnit testable component. Because my game follows the model, view, controller framework, I needed to make 
  sure that my model is correctly implemented and functional. My model is separated and does not rely on other components like GUI components so 
  this makes my model testable. I can initiate a Minesweeper object by itself and simulate a game by itself as a result. This allows for testing 
  just of the model with JUnit. 

- The fourth concept I used was recursion. I used recursion to implement the case where the user reveals a zero. Revealing a zero reveals all 
  nearby zeros and stops when the connecting squares touch a non-zero and non-mine square. Because each board is different and we cannot determine 
  how far to iterate from the original zero square, iteration is not fitting for this function. Recursion is more appropriate as the squares can 
  just keep being revealed until we hit the base case, which is when we hit a connecting non-zero, non-mine square. 

# Your Implementation
- The Minesweeper class is the model for the game. This model is what determines the view of the game board and status. Controllers can also change 
this model.
	
- The GameTest class is where the Minesweeper class is tested for correctness.

- The GameBaord class is where the board is drawn and shown to the player. It also creates controllers for user inputs on the board. Thus this class
contains components that represent the "view" and "controller" aspect the game framework. 
	
- The RunMinesweeper class makes all the frames that are apart of the game. It is the first thing called when the game first starts to initiate the 
different widgets of the game including buttons, popup instructions, and the game board.  
