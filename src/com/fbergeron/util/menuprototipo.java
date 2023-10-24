package com.fbergeron.util;
import javax.swing.*;

import com.fbergeron.solitaire.Solitaire;
import com.fbergeron.solitaire.SolitaireRanking;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

public class menuprototipo extends JFrame {
	
    public menuprototipo() {
        setTitle("Menu Inicial");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 450); 
        setLocationRelativeTo(null);

        // Cores
        Color backgroundColor = new Color(60, 60, 60); // Um cinza escuro
        Color buttonColor = new Color(30, 144, 255);   // Azul
        Color hoverButtonColor = new Color(70, 180, 255); // Azul claro

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(backgroundColor);

        // Panel central para os botões
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(backgroundColor);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(150, 150, 150, 150));

        JButton novoJogoButton = createButton("Novo Jogo", buttonColor, hoverButtonColor);
        JButton melhoresTemposButton = createButton("Melhores Tempos", buttonColor, hoverButtonColor);

        novoJogoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Solitaire sol = new Solitaire(true);
                sol.setLocale(Locale.ENGLISH);
                sol.setVisible(true);
                dispose();
            }
        });

        melhoresTemposButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SolitaireRanking rank = new SolitaireRanking();
                rank.setLocale(Locale.ENGLISH);
                rank.setVisible(true);
            }
        });

        centerPanel.add(novoJogoButton);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(melhoresTemposButton);

        panel.add(centerPanel, BorderLayout.CENTER);

        getContentPane().add(panel);

        setVisible(true);
    }

    private JButton createButton(String text, Color bgColor, Color hoverColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(300, 50));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new menuprototipo();
            }
        });
    }
}
