### Timetable Scheduler using Backtracking in Java

## Objectives

To automatically generate a conflict-free timetable.

To allocate teachers, classes, and rooms efficiently.

To implement a backtracking algorithm for optimized scheduling.

## Project Information

This project is a Java-based timetable generator that reads subject, teacher, class, and room data from a CSV file and produces a valid schedule. It ensures that no teacher or room is assigned to multiple classes at the same time. The system follows a modular design where methods such as mColor() and nextValue() handle the core backtracking logic, improving readability and maintainability.

## Algorithm Explanation

The scheduling task is modeled as a graph coloring problem, where each class-session is represented as a node.

The mColor() method attempts to assign a valid time slot (color) to each node while checking for constraint violations.

If a conflict is detected (e.g., same teacher, class, or room overlap), the algorithm backtracks using the nextValue() method to try a new slot.

This continues until a complete, valid timetable is generated that satisfies all constraints.

## Time Complexity

The algorithm has a time complexity of O(mⁿ), where:

n = number of sessions or nodes

m = number of available time slots

In the worst case, it explores all possible combinations, but pruning invalid paths early significantly improves average performance.

## Result and Conclusion

The program successfully generates a valid, conflict-free timetable that meets all given constraints. It demonstrates the practical application of backtracking in solving real-world scheduling and optimization problems.

## Future Scope

Integration of a graphical user interface (GUI) using JavaFX or Swing.

Exporting the generated timetable to PDF or Excel formats.

Implementing automatic optimization for teacher workload and class balancing.
