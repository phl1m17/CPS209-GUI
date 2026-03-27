# 2D Survival Game – CPS209 Project

## Overview

This project is a 2D survival game developed in Java using Swing and Java 2D graphics.
The player must gather resources, craft tools, survive enemy attacks at night, and manage health using power-ups.

As days progress, enemies become more frequent, requiring the player to use strategy, crafting, and timing to survive longer.

---

## Features

### Player Movement

- Move left and right across the world
- Jump with gravity and collision detection
- Transition between different world areas

### Day and Night Cycle

- The game alternates between day and night
- Enemies spawn during the night
- Difficulty increases over time

### Combat System

- Attack enemies using crafted weapons
- Strength power-ups temporarily increase damage
- Enemies deal damage when they collide with the player

### Resource Gathering

- Trees can be chopped to collect wood
- Tools affect how quickly resources are gathered

### Crafting System

Players can craft tools using collected wood:

- **Sword** – increases attack damage
- **Axe** – improves resource gathering

### Inventory System

- Stores collected resources and tools
- Supports multiple item slots
- Allows switching between items using number keys

### Power-Ups

- **Health Power-Up** – restores health
- **Strength Power-Up** – increases attack damage for a limited time
- Active strength effects display a timer on the screen

### Game Over System

- The game ends when the player's health reaches zero
- A restart option allows the player to start a new game

---

## Controls

| Action                | Key               |
| --------------------- | ----------------- |
| Move Left             | A or Left Arrow   |
| Move Right            | D or Right Arrow  |
| Jump                  | Space or Up Arrow |
| Attack                | Mouse Click       |
| Open Inventory        | E                 |
| Pause Game            | ESC               |
| Select Inventory Slot | 1, 2, 3           |

---

## How to Run the Game

1. Open the project in **Visual Studio Code**, **IntelliJ**, or another Java IDE.
2. Compile the project.
3. Run the main file:

```
Project2Runner.java
```

The game window will launch and display the main menu.

---

## Project Structure

```
CPS209-Project/
│
├── src/
│   ├── Player.java
│   ├── Panel.java
│   ├── World.java
│   ├── Mob.java
│   ├── Tree.java
│   ├── PowerUp.java
│   ├── Inventory.java
│   ├── InventoryScreen.java
│   ├── TimeManager.java
│   ├── GameOverScreen.java
│   └── Project2Runner.java
│
├── README.md
└── .gitignore
```

---

## Technologies Used

- Java
- Swing (GUI framework)
- Java 2D Graphics
- Object-Oriented Programming (OOP)

---

## Future Improvements

- Additional enemy types
- Sound effects and background music
- More crafting recipes
- Improved animations
- Save and load system

---

## Author

Phil Clarence Manag
CPS209 – Computer Science Project
