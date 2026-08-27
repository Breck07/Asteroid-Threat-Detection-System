package com.atds.project;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class ATDSUI extends JFrame {

    private JTable asteroidTable;
    private DefaultTableModel tableModel;
    private final ATDSCaller apiCaller = new ATDSCaller();

    public ATDSUI() {
        //Gui class to display the resutls
        setTitle("ASTEROID THREAT DETECTION SYSTEM");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(new Color(28, 28, 30)); 
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(28, 28, 30));

        //Add a title label
        JLabel titleLabel = new JLabel("ATDS");
        titleLabel.setFont(new Font(".SF NS Text", Font.BOLD, 28)); 
        titleLabel.setForeground(Color.WHITE);

        //Create a refresh button to reresh the results
        JButton refreshButton = new JButton("Refresh Radar");
        refreshButton.setFont(new Font(".SF NS Text", Font.BOLD, 13));
        refreshButton.setBackground(new Color(10, 132, 255)); 
        refreshButton.setForeground(Color.WHITE);
        refreshButton.putClientProperty("JButton.buttonType", "roundRect"); 
        refreshButton.setFocusPainted(false);
        refreshButton.setBorder(new EmptyBorder(8, 16, 8, 16));
        refreshButton.addActionListener(e -> loadData());

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(refreshButton, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        String[] columns = {"Asteroid Name", "Status", "Miss Distance", "Velocity"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        asteroidTable = new JTable(tableModel);
        styleTable(asteroidTable);

        JScrollPane scrollPane = new JScrollPane(asteroidTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(36, 36, 38)); 
        scrollPane.putClientProperty("JComponent.outline", "none");

        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBackground(new Color(36, 36, 38)); 
        cardPanel.putClientProperty("JComponent.arc", 16); 
        cardPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        cardPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(cardPanel, BorderLayout.CENTER);
        add(mainPanel);

        loadData();
    }

    private void styleTable(JTable table) {
        table.setRowHeight(45); 
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(new Color(36, 36, 38));
        table.setSelectionBackground(new Color(44, 44, 46));
        table.setSelectionForeground(Color.WHITE);
        table.setFont(new Font(".SF NS Text", Font.PLAIN, 14));

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(36, 36, 38));
        header.setForeground(new Color(142, 142, 147)); 
        header.setFont(new Font(".SF NS Text", Font.BOLD, 12));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(54, 54, 56)));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean isS, boolean hasF, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, isS, hasF, r, c);
                setBorder(new EmptyBorder(0, 15, 0, 15)); 
                
                if (c == 1) {
                    if ("HAZARDOUS".equals(v)) {
                        setForeground(new Color(255, 69, 58)); 
                        setFont(t.getFont().deriveFont(Font.BOLD));
                    } else {
                        setForeground(new Color(50, 215, 75)); 
                        setFont(t.getFont().deriveFont(Font.PLAIN));
                    }
                } else {
                    setForeground(isS ? Color.WHITE : new Color(229, 229, 234));
                }
                return comp;
            }
        };
        
        centerRenderer.setHorizontalAlignment(JLabel.LEFT);
        table.setDefaultRenderer(Object.class, centerRenderer);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        
        SwingWorker<List<Map<String, Object>>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Map<String, Object>> doInBackground() {
                return apiCaller.callAPI();
            }

            @Override
            protected void done() {
                try {
                    List<Map<String, Object>> asteroids = get();
                    for (Map<String, Object> asteroid : asteroids) {
                        String name = (String) asteroid.get("name");
                        boolean isDangerous = (boolean) asteroid.get("isDangerous");
                        double distance = (double) asteroid.get("missDistanceKm");
                        double speed = (double) asteroid.get("speedKmH");

                        String dangerStatus = isDangerous ? "HAZARDOUS" : "SAFE";
                        String formattedDistance = String.format("%,.0f km", distance);
                        String formattedSpeed = String.format("%,.0f km/h", speed);

                        tableModel.addRow(new Object[]{name, dangerStatus, formattedDistance, formattedSpeed});
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    public static void display() {
        SwingUtilities.invokeLater(() -> {
            FlatMacDarkLaf.setup();
            new ATDSUI().setVisible(true);
        });
    }
}