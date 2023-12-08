package com.fbergeron.solitaire;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

public class SolitaireRanking extends JFrame {
    private JTextArea textArea;

    public SolitaireRanking() {
        setTitle("Solitaire Ranking");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); 
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(60, 60, 60));  

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.BOLD, 16));  
        textArea.setForeground(Color.WHITE);
        textArea.setOpaque(false);
        textArea.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel textPanel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        textPanel.setOpaque(false);
        textPanel.add(textArea);

        JScrollPane scrollPane = new JScrollPane(textPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);

        panel.add(scrollPane, BorderLayout.CENTER);
        add(panel);

        loadJSONData();

        setVisible(true);
    }

    private void loadJSONData() {
        try {
        	String jsonFilePath = new File("ranking.json").getAbsolutePath();
        	
            String jsonData = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
            
            java.util.List<Resultado> resultados;    
            java.lang.reflect.Type listType = new TypeToken<java.util.List<Resultado>>() {}.getType();
             
            resultados = new Gson().fromJson(jsonData, listType);
            Collections.sort(resultados, Resultado.getComparatorByTempo());
            
            StringBuilder sb = new StringBuilder();
            
            sb.append("Ranking (hh:mm)").append(System.lineSeparator()).append(System.lineSeparator());
            
            for(Integer i = 0; i < resultados.size(); i++) {
            	Resultado resultado = resultados.get(i);
            	
            	sb.append("Tempo: ").append(resultado.getTempo()).append(System.lineSeparator());
                sb.append("Movimentos: ").append(resultado.getMovimentos()).append(System.lineSeparator());
                sb.append(System.lineSeparator());
            }
            
            textArea.setText(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao ler o arquivo JSON.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SolitaireRanking());
    }
}
