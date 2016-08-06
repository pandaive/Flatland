# Programming Flatland
Project for Sub-symbolic AI methods on NTNU (Norwegian University of Science and Technology)
The project is an implementation of Evolving Neural Networks for a Flatland Agent

Project information:
You will use your evolutionary algorithm (EA) to evolve the weights for a small artificial neural
network (ANN) whose fitness will depend upon the performance of an agent controlled by the
ANN. This agent will do a minimally cognitive task similar to those used in the field of artificial
life (ALife) to illustrate the emergence of rudimentary intelligence. Specifically, the agent will
move around in a 2-dimensional grid and try to eat as much "food" as possible while avoiding
"poison".
The ANNs that you evolve will control an agent that moves about in Flatland (Figure 1), a
simple N x N grid environment in which cells contain food, poison or nothing at all. No cell can
contain more than one item. The grid is toroidal, meaning that it has no edges nor corners and
thus looks like a torus or doughnut. So when the agent moves off the grid to the far right (left),
it comes back onto the grid on the far left (right). Similarly, when it moves off the top (bottom)
of the grid, it comes back on the bottom (top).
On each timestep, the agent can stay put or move one cell forward, to the its left, or to its right.
The agent has a front end from which the directions forward, left and right are based. Before
moving left or right, the agent is assumed to turn to the left or right, thus changing its heading
direction. So on a left (right) move, the agent actually does 2 things: 1) turns left (right) and
2) moves forward.
When the agent moves into a cell, it automatically eats whatever the cell contains. The agent's
goal is to eat as much food as possible, while avoiding the poison.
The Food-Poison Distribution (FPD) for Flatland is a pair of probabilities (f, p), where f is
the probability of filling an empty cell with food, while p is the probability of filling one of the
remaining unfilled cells with poison. Then, a scenario in Flatland is a grid with food and poison
placed in it stochastically, based on a user-chosen FPD. Hence, one FPD can be the basis for
many different scenarios.