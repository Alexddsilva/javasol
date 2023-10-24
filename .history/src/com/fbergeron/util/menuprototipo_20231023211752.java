package com.fbergeron.util;
import javax.swing.*;

import com.fbergeron.solitaire.Solitaire;
import com.fbergeron.solitaire.SolitaireRanking;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

public class menuprototipo extends JFrame {
	
	public static final Color BACK_COLOR  = new Color(7, 103, 45);
	
    public menuprototipo() {
        setTitle("Menu Inicial");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 400); 
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setBackground(BACK_COLOR); 
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); 

        JButton novoJogoButton = new JButton("Novo Jogo");
        JButton melhoresTemposButton = new JButton("Melhores Tempos");

        novoJogoButton.setAlignmentX(Component.CENTER_ALIGNMENT); 
        melhoresTemposButton.setAlignmentX(Component.CENTER_ALIGNMENT); 

        novoJogoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Solitaire sol = new Solitaire( true );
                sol.setLocale( Locale.ENGLISH );
                sol.setVisible( true );
                dispose();
            }
        });

        melhoresTemposButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	SolitaireRanking rank = new SolitaireRanking();
            	rank.setLocale( Locale.ENGLISH );
            	rank.setVisible( true );
            }
        });

        Font buttonFont = new Font("Arial", Font.BOLD, 16);
        Color buttonBackgroundColor = Color.WHITE;
        Color buttonForegroundColor = Color.BLACK;
        Color buttonBorderColor = Color.BLUE;

        novoJogoButton.setFont(buttonFont);
        novoJogoButton.setBackground(buttonBackgroundColor);
        novoJogoButton.setForeground(buttonForegroundColor);
        novoJogoButton.setFocusPainted(false);
        novoJogoButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(buttonBorderColor, 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        melhoresTemposButton.setFont(buttonFont);
        melhoresTemposButton.setBackground(buttonBackgroundColor);
        melhoresTemposButton.setForeground(buttonForegroundColor);
        melhoresTemposButton.setFocusPainted(false);
        melhoresTemposButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(buttonBorderColor, 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        panel.add(Box.createVerticalGlue()); 

        panel.add(novoJogoButton);
        panel.add(Box.createVerticalStrut(10)); 
        panel.add(melhoresTemposButton);

        panel.add(Box.createVerticalGlue()); 

        add(panel);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new menuprototipo();
            }
        });
    }
}
