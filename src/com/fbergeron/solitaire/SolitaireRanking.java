package com.fbergeron.solitaire;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SolitaireRanking extends JFrame {
    private JTextArea textArea;

    public SolitaireRanking() {
        setTitle("Solitaire Ranking");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Define o comportamento de fechamento apenas para a janela atual
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(7, 103, 45));

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setForeground(Color.WHITE);
        textArea.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;

        panel.add(scrollPane, gbc);
        add(panel);

        loadJSONData();

        setVisible(true);
    }

    private void loadJSONData() {
        try {
            String jsonFilePath = "C:\\Users\\ktmig\\Desktop\\MS28S\\resultado.json"; 
            String jsonData = new String(Files.readAllBytes(Paths.get(jsonFilePath)));

            Gson gson = new Gson();
            Resultado resultado = gson.fromJson(jsonData, Resultado.class);

            StringBuilder sb = new StringBuilder();
            sb.append("Tempo: ").append(resultado.getTempo()).append(System.lineSeparator());
            sb.append("Movimentos: ").append(resultado.getMovimentos()).append(System.lineSeparator());

            textArea.setText(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao ler o arquivo JSON.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public class Resultado {
        private String tempo;
        private int movimentos;

        public String getTempo() {
            return tempo;
        }

        public void setTempo(String tempo) {
            this.tempo = tempo;
        }

        public int getMovimentos() {
            return movimentos;
        }

        public void setMovimentos(int movimentos) {
            this.movimentos = movimentos;
        }
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SolitaireRanking());
    }
}
