=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: vxjian
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. Collections
I will be storing an ID number of each target and the Target object itself. I will also be storing an ID number of each bird and the Bird object itself. Each element type will be stored with a TreeMap<Integer, Target> or TreeMap<Integer, Bird>, where the key is the ID number and the value is the gameObject. Each time a bird is shot, I will use its id number to get the object from the treemap and decrease its lives. I will use a similar method to decrease the lives of a target every time it is collided against.

By doing this, I can implement JUnit testing more easily. Outside of the GameCourt class, I am able to access the private Target and Bird objects created within GameCourt with just the ID number of a Target or Bird.
  
  
  2. File I/O
I will make a “save” button that stores the state of the game. Each time a game is saved, a file is created where lines 1-3 are written to be the numbers of lives the birds have left, and lines 4-6 are the number of lives the targets have left. A user can restore the most recently saved version of the game with the “get last saved game” button. When a game state is restored, the lines of the file are read and the lives of Birds and Targets are restored.
  	

  3. Inheritance/Subtyping for Dynamic Dispatch
The first subtype of bird is called yellowbird. In addition to all functionalities of bird, yellowbirds have the ability to drop a brown pelt when the user presses the “d” key. When the brown pelts hit a target, the target also decreases by one hit. So, yellowbird will have a method called dropPelt().
The second subtype of bird is called blackbird. Blackbirds can drop down vertically when the user presses the “d” key. Blackbirds will have a method called dropDown().

  4. Testable Component
The state of my game will be the GameCourt class, which has the treemap fields of the targets and birds. Everytime the user takes a turn shooting a bird, the GameCourt will be updated, changing the hit or life values of targets and birds. There will be methods in stage that get information about the treemaps, so I can test the game state with JUnit tests. I will test that the values of the treemaps are updated correctly, and if it correctly determined if the user won the game.



=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

Bird: This is the main bird class. Birds can be launched to hit the targets and decrease target lives. Each time a bird is launched, its life number decreases by 1. Birds have a total of 5 lives. If a bird has 0 lives, the user is unable to launch the bird.

BlackBird: A subtype of bird, which also has the ability to drop down vertically when the user presses the “d” key.

YellowBird: A subtype of bird, which also has the ability to drop pelts vertically when the user presses the “d” key.

Pelt: A gameObject that is created when YellowBird uses its superpower. When the user presses the "d" key, if a yellowBird is in launch, a pelt is created and drops down vertically from the position the yellowbird was in.

Target: The target object that birds are trying to hit. Each target has 5 lives, and every time a target is hit, its lives decreases by 1. If all targets are defeated (lives = 0) before the birds lose all their lives, the user wins.

Eliminated: This is a gameObject whose image appears on top of a target once it is defeated. It functions to let the user know the target has been defeated.

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

At first, I wanted to implement it so that every turn, if a bird hits a target, the target's lives only goes down by 1. This was a struggle since the timer checks for collisions and updates the target lives every frame. There was no way to stop the timer inside the timer itself, so I did not implement the game in the way I wanted to. Instead, I made it so that a target's lives can go down by multiple scores when it is hit by a bird. I increased the number of lives a target has to make it more user-playable.


- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

I think I would refactor many of the functions in GameCourt, if given the chance. Right now, most of the game functions are implemented in GameCourt, even methods that are explicitly related to individual gameObjects. I would refactor so that the functionality is distributed more evenly between GameCourt and each class I created.



========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.

I used images from iemoji.com as my game objects and an image from artstation.com as my background.
