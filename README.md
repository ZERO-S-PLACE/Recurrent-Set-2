
# Recurrent Set 2

**Recurrent Set 2** is built using **JavaFX** and **Spring Data JPA**. It allows users to generate a recurrent set image for any given equation.
Calculations are performed using the Apache Commons Math library for complex numbers, and by parsing and simplifying equation into an equation tree. 

Due to the fact that generating images of recursive sets requires a lot of computation , the application may run very slowly on weaker processors

### Key Features:
- **Equation Generation**: Generate recurrent sets for any given equation.
- **Equation Parsing**: Parse equations into a tree structure and simplify them.
- **Interactive View**: 
  - **Drag to move** the view.
  - **Scroll to resize** the view.
- **Settings and Color Settings**: Store settings and color configurations in MySQL database.
- **Save Views and Equations**: You can save equations and view locations views for later use.
- **Image export**: Application enables to export generated images in **.jpg or .png** in sizes up to **20k x 20k**.

Project still in progress..
