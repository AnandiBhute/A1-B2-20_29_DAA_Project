import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class TimetableScheduler extends JFrame {
    JTextArea outputArea;
    int n, m = 35;
    int[][] graph;
    int[] color;
    String[] subjects, teachers, classes, rooms;

    public TimetableScheduler() {
        setTitle("Timetable Scheduler");
        setSize(850, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton uploadBtn = new JButton("Upload CSV");
        JButton runBtn = new JButton("Generate Timetable");
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 14));

        uploadBtn.addActionListener(e -> loadCSV());
        runBtn.addActionListener(e -> runScheduler());

        JPanel topPanel = new JPanel();
        topPanel.add(uploadBtn);
        topPanel.add(runBtn);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);
    }

    void loadCSV() {
        try {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                java.util.List<String[]> rows = new ArrayList<>();
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                br.readLine(); // Skip header
                while ((line = br.readLine()) != null) {
                    rows.add(line.split(","));
                }
                n = rows.size();
                subjects = new String[n];
                teachers = new String[n];
                classes = new String[n];
                rooms = new String[n];

                for (int i = 0; i < n; i++) {
                    String[] data = rows.get(i);
                    subjects[i] = data[0].trim();
                    teachers[i] = data[1].trim();
                    classes[i] = data[2].trim();
                    rooms[i] = data[3].trim();
                }

                outputArea.setText("Loaded " + n + " records from CSV successfully!\n");
            }
        } catch (Exception ex) {
            outputArea.setText("Error loading CSV: " + ex.getMessage());
        }
    }

    void buildGraph() {
        graph = new int[n][n];
        color = new int[n];

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (teachers[i].equalsIgnoreCase(teachers[j]) ||
                    classes[i].equalsIgnoreCase(classes[j]) ||
                    rooms[i].equalsIgnoreCase(rooms[j])) {
                    graph[i][j] = graph[j][i] = 1; 
                }
            }
        }
    }

    boolean isSafe(int node, int c) {
        for (int i = 0; i < n; i++) {
            if (graph[node][i] == 1 && color[i] == c)
                return false;
        }
        return true;
    }

    boolean mColor(int node) {
        if (node == n) return true; 

        for (int c = 1; c <= m; c++) {
            if (isSafe(node, c)) {
                color[node] = c;
                if (mColor(node + 1))
                    return true;
                color[node] = 0; 
            }
        }
        return false;
    }

    String slotToTime(int slot) {
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        String[] times = {
            "9:00 - 9:40 AM",
            "9:40 - 10:20 AM",
            "10:20 - 11:00 AM",
            "11:00 - 11:40 AM",
            "12:00 - 12:40 PM", 
            "12:40 - 1:20 PM",
            "1:20 - 2:00 PM"
        };

        int dayIndex = (slot - 1) / 7;
        int timeIndex = (slot - 1) % 7;

        if (dayIndex < days.length)
            return days[dayIndex] + " " + times[timeIndex];
        else
            return "Extra Slot " + slot;
    }

    void printSolution() {
    outputArea.append("\nCLASSWISE TIMETABLE:\n\n");

    Map<String, java.util.List<Integer>> classMap = new TreeMap<>(); 
    for (int i = 0; i < n; i++) {
        classMap.computeIfAbsent(classes[i], k -> new ArrayList<>()).add(i);
    }

    for (String cls : classMap.keySet()) {
        outputArea.append("══════════════════════════════════════════════════\n");
        outputArea.append("Class " + cls + ":\n");
        outputArea.append("══════════════════════════════════════════════════\n");

        java.util.List<Integer> indices = classMap.get(cls);
        indices.sort(Comparator.comparingInt(i -> color[i]));

        for (int i : indices) {
            outputArea.append(String.format(
                "%-20s → %-35s (Teacher: %-15s | Room: %-5s)\n",
                subjects[i],
                slotToTime(color[i]),
                teachers[i],
                rooms[i]
            ));
        }
        outputArea.append("\n");
    }
}

    void runScheduler() {
        if (subjects == null) {
            outputArea.setText("Please upload a CSV first.\n");
            return;
        }

        outputArea.append("\nBuilding conflict graph...\n");
        buildGraph();
        Arrays.fill(color, 0);

        outputArea.append("Assigning time slots...\n");
        if (mColor(0)) {
            printSolution();
        } else {
            outputArea.append("No valid timetable found.\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TimetableScheduler().setVisible(true));
    }
}