/*
 * Copyright (C) 2002-2011  Frédéric Bergeron (fbergeron@users.sourceforge.net)
 *                          and other contributors
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package com.fbergeron.solitaire;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.text.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;

import com.fbergeron.card.*;
import com.fbergeron.util.*;

/** Java version of the famous card game.
 * @author Frederic Bergeron
 * @author <A HREF="http://javasol.sourceforge.net">http://javasol.sourceforge.net</A>
 * @version Version 1.0
 */

public class Solitaire extends Frame
{
    /** Number of sequential stacks. */
    public static final int SEQ_STACK_CNT = 4;

    /** Number of solitaire stacks. */
    public static final int SOL_STACK_CNT = 7;

    /** Number of cards freed from the deck when requested. */
    public static final int FREED_CARDS_CNT = 1;

    public static final Point DECK_POS              = new Point( 5, 5 );
    public static final Point REVEALED_CARDS_POS    = new Point( DECK_POS.x + ClassicCard.DEFAULT_WIDTH + 5, 5 );
    public static final Point SEQ_STACK_POS         = new Point( REVEALED_CARDS_POS.x + ClassicCard.DEFAULT_WIDTH + 92, DECK_POS.y );
    public static final Point SOL_STACK_POS         = new Point( DECK_POS.x, SEQ_STACK_POS.y + ClassicCard.DEFAULT_HEIGHT + 5 );
    public static final Color TABLE_COLOR           = new Color(60, 60, 60);
    
    public static long timestart = System.currentTimeMillis();
    public static int counter = 0;
    
    public void zerar() {
		counter = 0;
		timestart = System.currentTimeMillis();
	}

    public Solitaire( boolean isApplet )
    {
 
        super();
        setLayout( new BorderLayout( 0, 0 ) );
        setResizable( false );

        class LocaleListener implements ItemListener {
            public LocaleListener( Locale locale ) {
                this.locale = locale;
            }

            public void itemStateChanged( ItemEvent e ) {
                Solitaire.this.setLocale( locale );
            }

            Locale locale;
        }

        class LevelListener implements ItemListener {
            public LevelListener( String level ) {
                this.level = level;
            }

            public void itemStateChanged( ItemEvent e ) {
                Solitaire.this.setGameType( level );
                Solitaire.this.newGame();
                
            }

            String level;
        }

        class LicenseListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                String title = resBundle.getString( "License" );
                String msg = resBundle.getString( "LicenseText" );
                DialogMsg licenseWindow = new DialogMsg( Solitaire.this, title, true, msg );
                licenseWindow.setLocation( 20, 20 );
                licenseWindow.setSize( 500, 300 );
                licenseWindow.setFont( new Font( "Arial", Font.PLAIN, 14 ) );
                licenseWindow.setResizable( true );
                licenseWindow.setVisible( true );
            }
        }
        
        
        class HintListener implements ItemListener {
            public void itemStateChanged(ItemEvent e) {
                if( table != null )
                    table.repaint();
            }
        }

        //Menus
        menubar = new MenuBar();
        setMenuBar( menubar );

        //Menu Options
        menuOptions = new Menu( "Options" );
        menubar.add( menuOptions );
        
        menuItemNewGame = new MenuItem( "NewGame");
        menuItemNewGame.addActionListener( new NewGameListener() );
        menuItemNewGame.setShortcut( new MenuShortcut( KeyEvent.VK_N, false ) );
        menuOptions.add( menuItemNewGame );

        menuItemRestart = new MenuItem( "Restart");
        menuItemRestart.addActionListener( new RestartListener() );
        menuItemRestart.setShortcut( new MenuShortcut( KeyEvent.VK_S, false ) );
        menuOptions.add( menuItemRestart );

        menuItemUndo = new MenuItem( "Undo");
        menuItemUndo.addActionListener( new UndoListener() );
        menuItemUndo.setShortcut( new MenuShortcut(KeyEvent.VK_U, false ) ); 
        menuItemUndo.setEnabled( false );

        menuItemLevelRandom = new CheckboxMenuItem( "Random" );
        menuItemLevelRandom.addItemListener( new LevelListener( GameInfo.RANDOM ) );
        menuItemLevelEasy = new CheckboxMenuItem( "Easy" );
        menuItemLevelEasy.addItemListener( new LevelListener( GameInfo.WINNABLE_EASY ) );
        menuItemLevelNormal = new CheckboxMenuItem( "Normal" );
        menuItemLevelNormal.addItemListener( new LevelListener( GameInfo.WINNABLE_NORMAL ) );
        menuItemLevelHard = new CheckboxMenuItem( "Hard" );
        menuItemLevelHard.addItemListener( new LevelListener( GameInfo.WINNABLE_HARD ) );
        menuItemLevelTricky = new CheckboxMenuItem( "Tricky" );
        menuItemLevelTricky.addItemListener( new LevelListener( GameInfo.WINNABLE_TRICKY ) );

        setGameType( GameInfo.RANDOM );

        menuOptions.add( menuItemUndo );
        menuOptions.add( new MenuItem( "-" ) );
        menuOptions.add( menuItemLevelRandom );
        menuOptions.add( menuItemLevelEasy );
        menuOptions.add( menuItemLevelNormal );
        menuOptions.add( menuItemLevelHard );
        menuOptions.add( menuItemLevelTricky );

        //Menu Help
        menuHelp = new Menu( "Help" );
        menubar.add( menuHelp );
        menuItemRules = new MenuItem( "Rules" );
        menuItemRules.addActionListener( new RulesListener() );
        menuItemAbout = new MenuItem( "About" );
        menuItemAbout.addActionListener( new AboutListener() );
        menuItemLicense = new MenuItem();
        menuItemLicense.addActionListener( new LicenseListener() );
        menuHelp.add( menuItemRules );
        menuHelp.add( new MenuItem( "-" ) );
        menuHelp.add( menuItemAbout );
        menuHelp.add( menuItemLicense );
        menuHelp.add( new MenuItem( "-" ) );
        menuItemHint = new CheckboxMenuItem( "Hint" );
        menuItemHint.setShortcut( new MenuShortcut( KeyEvent.VK_H, false ) );
        menuItemHint.addItemListener( new HintListener() );
        menuHelp.add( menuItemHint );
        menuHelp.add( new MenuItem( "-" ) );
        menuItemEnglish = new CheckboxMenuItem( "English" );
        menuItemEnglish.addItemListener( new LocaleListener( Locale.ENGLISH ) );
        menuItemFrench = new CheckboxMenuItem( "French" );
        menuItemFrench.addItemListener( new LocaleListener( Locale.FRENCH ) );
        menuHelp.add( menuItemEnglish );
        menuHelp.add( menuItemFrench );
        
        menuDesenvolvimento = new Menu( "Em desenvolvimento" );
        menubar.add( menuDesenvolvimento );
        
        menuRanking =  new MenuItem("Ranking");
        menuRanking.addActionListener(new RankingListener());
        
        menuPrototipo =  new MenuItem("Menu Inicial");
        menuPrototipo.addActionListener( new MenuPListener());
        
        menuDesenvolvimento.add( menuRanking );
        menuDesenvolvimento.add( menuPrototipo );

        Button dica = new Button("Hint");
        dica.setBounds(160, 500, 50, 50);
        dica.addActionListener(new hintOuvidor());
        dica.setBackground(new Color (135,206,250));
        dica.setFocusable(false);
        //add(dica);
        
        desfazer = new Button("Undo");
        desfazer.setBounds(220, 500, 50, 50);
        desfazer.addActionListener(new UndoListener());
        desfazer.setBackground(new Color (135,206,250));
        desfazer.setFocusable(false);
        desfazer.setEnabled(false);
        //add(desfazer);
        
        //Table
        table = new Table();
        add("Center", table);
        MouseManager mouseManager = new MouseManager();
        table.addMouseListener( mouseManager );
        table.addMouseMotionListener( mouseManager );

        setSize((ClassicCard.DEFAULT_WIDTH + 5) * SOL_STACK_CNT + 20 + getInsets().left + getInsets().right + 3,560);

        addWindowListener(
            new SolitaireWindowManager( this,
                isApplet ? WindowManager.HIDE_ON_CLOSE : WindowManager.EXIT_ON_CLOSE ) );

        setupWinnable();
        newGame();
        setVisible( true );
    }
    
    public Solitaire outer() {
 	   return Solitaire.this;
 	}
    /**
     * Shows or hides the component depending on the boolean flag b.
     * @param b  if true, show the component; otherwise, hide the component.
     * @see java.awt.Component#isVisible
     */
    public void setVisible(boolean b)
    {
        Dimension scrSize = getToolkit().getScreenSize();
        Dimension size = getSize();
        if(b)
        {
            setLocation( (scrSize.width - size.width) / 2, (scrSize.height - size.height) / 2 );
        }
        super.setVisible(b);
    }

    /**
     * Sets the locale.
     * @param locale Locale.
     */
    public void setLocale( Locale locale ) {
        super.setLocale( locale );
        
        resBundle = ResourceBundle.getBundle( Solitaire.class.getName() + "Ress", locale );
        
        menuOptions.setLabel( resBundle.getString( "Options" ) );
        menuItemNewGame.setLabel( resBundle.getString( "NewGame" ) );
        menuHelp.setLabel( resBundle.getString( "Help" ) );
        menuItemRules.setLabel( resBundle.getString( "Rules" ) );
        menuItemAbout.setLabel( resBundle.getString( "About" ) );
        menuItemLicense.setLabel( resBundle.getString( "License" ) );
        menuItemEnglish.setLabel( resBundle.getString( "English" ) );
        menuItemFrench.setLabel( resBundle.getString( "French" ) );
        menuItemHint.setLabel( resBundle.getString( "Hint" ) );
        menuItemRestart.setLabel( resBundle.getString( "Restart" ) );
        menuItemUndo.setLabel( resBundle.getString( "Undo" ) );
        menuItemLevelRandom.setLabel( resBundle.getString( "LevelRandom" ) );
        menuItemLevelEasy.setLabel( resBundle.getString( "LevelEasy" ) );
        menuItemLevelNormal.setLabel( resBundle.getString( "LevelNormal" ) );
        menuItemLevelHard.setLabel( resBundle.getString( "LevelHard" ) );
        menuItemLevelTricky.setLabel( resBundle.getString( "LevelTricky" ) );
        menuItemEnglish.setState( Locale.ENGLISH.equals( locale ) );
        menuItemFrench.setState( Locale.FRENCH.equals( locale ) );

        setTitle( resBundle.getString( "Solitaire" ) );

        if( frameAbout != null )
            frameAbout.setLocale( locale );
        if( frameRules != null )
            frameRules.setLocale( locale );

        if( table != null )
            table.repaint();
    }
   
    public void setGameType( String gameType ) {
        this.gameInfo.setType( gameType );
        menuItemLevelRandom.setState( GameInfo.RANDOM.equals( gameType ) );
        menuItemLevelEasy.setState( GameInfo.WINNABLE_EASY.equals( gameType ) );
        menuItemLevelNormal.setState( GameInfo.WINNABLE_NORMAL.equals( gameType ) );
        menuItemLevelHard.setState( GameInfo.WINNABLE_HARD.equals( gameType ) );
        menuItemLevelTricky.setState( GameInfo.WINNABLE_TRICKY.equals( gameType ) );
    }

    private void pushGameState( GameState state ) {
        gameStates.add( state );
        menuItemUndo.setEnabled( gameStates.size() > 1 );
        desfazer.setEnabled(gameStates.size() > 1 );
    }

    private void popGameState() {
        gameStates.remove( gameStates.size() - 1 );
        menuItemUndo.setEnabled( gameStates.size() > 1 );
        desfazer.setEnabled(gameStates.size() > 1 );
    }

    public static void main( String[] args ) {
        Locale loc = null;
        if( args.length == 0 )
            loc = Locale.ENGLISH;
        else if( args.length == 1 )
            loc = args[ 0 ].equals( "fr" ) ? Locale.FRENCH : Locale.ENGLISH;

        if( loc == null )
            System.out.println( "Usage : java com.fbergeron.solitaire.Solitaire [locale]\n\n" +
                "locale may be fr (for french) or en (for english).\n" );
        else {
            Solitaire sol = new Solitaire( false );
            sol.setLocale( loc );
        }
        
    }

    /**
     * Starts a new Solitaire game.
     */
    public void newGame() {
        // Get a random seed according to the game type
        gameInfo.setSeed( -1 );
        Random aRandom = new Random();
        if (gameInfo.getType().equals(GameInfo.WINNABLE_EASY))
            gameInfo.setSeed( easyGames[aRandom.nextInt(easyGames.length)] );
        if (gameInfo.getType().equals(GameInfo.WINNABLE_NORMAL))
            gameInfo.setSeed( normalGames[aRandom.nextInt(normalGames.length)] );
        if (gameInfo.getType().equals(GameInfo.WINNABLE_HARD))
            gameInfo.setSeed( hardGames[aRandom.nextInt(hardGames.length)] );
        if (gameInfo.getType().equals(GameInfo.WINNABLE_TRICKY))
            gameInfo.setSeed( trickyGames[aRandom.nextInt(trickyGames.length)] );

        if( gameInfo.getSeed() == -1 )
            gameInfo.setSeed( aRandom.nextInt(1000000) );
        deck = new ClassicDeck( table );
        deck.shuffle( gameInfo.getSeed() );
        deck.setLocation( DECK_POS.x, DECK_POS.y );

        revealedCards = new Stack();
        revealedCards.setLocation( REVEALED_CARDS_POS.x, REVEALED_CARDS_POS.y );

        seqStack = new SequentialStack[ SEQ_STACK_CNT ];
        for ( int i = 0; i < SEQ_STACK_CNT; i++ ) {
            seqStack[ i ] = new SequentialStack();
            seqStack[ i ].setLocation( SEQ_STACK_POS.x + i * (ClassicCard.DEFAULT_WIDTH + 5), SEQ_STACK_POS.y );
        }

        solStack = new SolitaireStack[ SOL_STACK_CNT ];
        for ( int i = 0; i < SOL_STACK_CNT; i++ ) {
            solStack[ i ] = new SolitaireStack();
            solStack[ i ].setSpreadingDirection( Stack.SPREAD_SOUTH );
            solStack[ i ].setSpreadingDelta( 20 );
            solStack[ i ].setLocation( SOL_STACK_POS.x + i * (ClassicCard.DEFAULT_WIDTH + 5), SOL_STACK_POS.y );
        }

        currStack = new Stack();
        currStack.setSpreadingDirection( Stack.SPREAD_SOUTH );
        currStack.setSpreadingDelta( 20 );

        distributeCards();
        if( table != null )
            table.repaint();
    }

    /**
     * Requests cards from the deck.
     * The number of new cards is equal to FREED_CARDS_CNT.
     */
    public void getNewCards() {
    	
    	//Contar movimento
    	if(!deck.isEmpty() || !revealedCards.isEmpty()) {
    		counter++;
    	}
    	
        //First, restore the deck if it's empty.
    	if( deck.isEmpty() ) {
            for( ; !revealedCards.isEmpty(); ) {
                ClassicCard c = ((ClassicCard)revealedCards.pop());
                c.turnFaceDown();
                deck.push( c );
            }
        }

        for( int i = 0; !deck.isEmpty() && i < FREED_CARDS_CNT; i++ ) {
            ClassicCard c = ((ClassicCard)deck.pop());
            c.turnFaceUp();
            revealedCards.push( c );
        }
        // Save the state of the game after the move
        pushGameState( new GameState( gameInfo, deck, revealedCards, solStack, seqStack, null, null, null ) );

        // Flag which cards can be moved legally
        GameState gs = new GameState( gameInfo, deck, revealedCards, solStack, seqStack );
        legalGs = gs.legalMoves( false );

        if( table != null )
            table.repaint();
    }

    /**
     * Plays a stack of cards from a stack to another stack.
     * @param curr Current stack of cards to be played.
     * @param src Stack where the card comes from.
     * @param dst Stack where the card is played.
     */
    public void play( Stack curr, Stack src, Stack dst ) {
        if( curr != null )
            curr.reverse();
        if( dst != null && dst.isValid( curr ) ) {
            for( ; !curr.isEmpty(); )
                dst.push( curr.pop() );
            if( !src.isEmpty() && src.top().isFaceDown() ) {
                ClassicCard topCard = ((ClassicCard)src.top());
                topCard.turnFaceUp();
            }
            // Save the state of the game after the move
            pushGameState( new GameState( gameInfo, deck, revealedCards, solStack, seqStack, null, null, null ) );
            counter++;
            
            
            // Flag which cards can be moved legally
            GameState gs = new GameState( gameInfo, deck, revealedCards, solStack, seqStack );
            legalGs = gs.legalMoves( false );
            if( isGameWon() )
                congratulate();
        }
        else {
            for( ; !curr.isEmpty(); )
                src.push( curr.pop() );
        }
        if( table != null )
            table.repaint();
    }

    /** Each time a new game begins, we have to distribute the
     * cards in colums.  The number of columns is equal to SOL_STACK_CNT.
     */
    private void distributeCards() {
        for( int i = 0; i < SOL_STACK_CNT; i++ ) {
            ClassicCard c = ((ClassicCard)deck.pop());
            c.turnFaceUp();
            solStack[ i ].push( c );
            for( int j = i+1; j < SOL_STACK_CNT; j++ )
                solStack[ j ].push( deck.pop() );
        }
        
        // Save the initial game state
        pushGameState( new GameState( gameInfo, deck, revealedCards, solStack, seqStack, null, null, null ) );
        
        // Flag which cards can be moved legally
        GameState gs = new GameState( gameInfo, deck, revealedCards, solStack, seqStack );
        legalGs = gs.legalMoves( false );
    }

    /**
     * @return <CODE>true</CODE>, if the game is won.
     * <CODE>false</CODE> otherwise.
     */
    private boolean isGameWon() {
        boolean gameWon = deck.isEmpty() && revealedCards.isEmpty();
        if( gameWon )
            for( int i = 0; i < SOL_STACK_CNT && gameWon; i++ )
                gameWon = gameWon && solStack[ i ].isEmpty();
        return( gameWon );
    }

    /**
     * Shows a frame congratulating the player.
     */
    private void congratulate() {
        FrameCongratulations f = new FrameCongratulations(timestart, System.currentTimeMillis(), counter, this);
        
        f.setVisible( true );
    }

    class AboutListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if( frameAbout == null )
                frameAbout = new FrameAbout();
            frameAbout.setLocale( Solitaire.this.getLocale() );
            frameAbout.setVisible( true );
        }
    }

    public class NewGameListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            newGame();
            zerar();
        }
    }
    
    class RestartListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            gameStates.get( 0 ).restoreGameState( gameInfo, deck, revealedCards, solStack, seqStack, null );
            gameStates = new ArrayList<GameState>();
            pushGameState( new GameState( gameInfo, deck, revealedCards, solStack, seqStack, null, null, null ) );
            
            zerar();
            
            // Flag which cards can be moved legally
            GameState gs = new GameState( gameInfo, deck, revealedCards, solStack, seqStack );
            legalGs = gs.legalMoves( false );
            
            table.repaint();
        }
    }
    class UndoListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if( gameStates.size() - 2 >= 0 ) {
                gameStates.get( gameStates.size() - 2 ).restoreGameState( gameInfo, deck, revealedCards, solStack, seqStack, null );
                popGameState();
                counter++;
            }
            GameState gs = new GameState( gameInfo, deck, revealedCards, solStack, seqStack );

            // Flag which cards can be moved legally
            legalGs = gs.legalMoves( false );
            
            table.repaint();
        }
    }

    class RulesListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if( frameRules == null )
                frameRules = new FrameRules();
            frameRules.setLocale( Solitaire.this.getLocale() );
            frameRules.setVisible( true );
        }
    }
    
    class RankingListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if( frameRanking == null )
            	frameRanking = new SolitaireRanking();
            frameRanking.setLocale( Solitaire.this.getLocale() );
            frameRanking.setVisible( true );
        }
    }
    
    class MenuPListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if( frameMenuP == null )
            	frameMenuP = new menuprototipo();
        }
    }
    
    class MouseManager extends MouseAdapter implements MouseMotionListener {
    	public void mouseMoved(MouseEvent e) {
        }

        public void mouseDragged(MouseEvent e) {
            if( currStack != null && translation != null ) {
                Point p = e.getPoint();
                currStack.setLocation( p.x - translation.x, p.y - translation.y );
                table.repaint();
            }
        }

        public void mousePressed(MouseEvent e) {
            if( !e.isMetaDown() && !e.isControlDown() && !e.isShiftDown() ) {
                ClassicCard c = null;
                Point p = e.getPoint();

                if( deck.contains( p ) )
                    getNewCards();
                else {
                    if( !revealedCards.isEmpty() && revealedCards.top().contains( p ) ) {
                        src = revealedCards;
                        c = ((ClassicCard)src.top());
                    }
                    else {
                        for( int i = 0; i < SOL_STACK_CNT && src == null; i++ ) {
                            if( !solStack[ i ].isEmpty() && solStack[ i ].contains( p ) ) {
                                src = solStack[ i ];
                                c = ((ClassicCard)src.getClickedCard( p ));
                            }
                        }
                        for( int i = 0; i < SEQ_STACK_CNT && src == null; i++ ) {
                            if( !seqStack[ i ].isEmpty() && seqStack[ i ].contains( p ) ) {
                                src = seqStack[ i ];
                                c = ((ClassicCard)src.top());
                            }
                        }
                    }
                    //We don't allow to drag hidden cards
                    if( c != null && c.isFaceDown() ) {
                        src = null;
                        c = null;
                    }
                    if( src != null && c != null ) {
                        Point loc = c.getLocation();
                        translation = new Point( p.x - loc.x, p.y - loc.y );
                        currStack = src.pop( c );
                        currStack.reverse();
                        currStack.setLocation( p.x - translation.x, p.y - translation.y );
                        curr = currStack;
                    }
                }
            }
        }

        public void mouseReleased(MouseEvent e) {
    
            Point p = e.getPoint();

            for( int i = 0; i < SOL_STACK_CNT && dst == null; i++ ) {
                if( solStack[ i ].contains( p ) )
                    dst = solStack[ i ];
            }
            for( int i = 0; i < SEQ_STACK_CNT && dst == null; i++ ) {
                if( seqStack[ i ].contains( p ) )
                    dst = seqStack[ i ];
            }
            if( curr != null && src != null )
                play( curr, src, dst );
            curr = src = dst = null;
        }

        private   Stack   curr;
        private   Stack   src;
        private   Stack   dst;
        private   Point   translation;
    }
    public class hintOuvidor implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	if (menuItemHint.getState()) {
        		menuItemHint.setState(false);
        	}else {
        		menuItemHint.setState(true);
        	}
        }
    }
    
    class Table extends Canvas
    {
    	
        public void update( Graphics g ) {
            paint( g );
        }

        public void paint( Graphics g ) {
            //Create offscreen
            Dimension dim = this.getSize();
            if( offscreen == null ) {
                offscreen = this.createImage( dim.width, dim.height );
                offscreenGr = (Graphics2D)offscreen.getGraphics();
            }

            //Enable antialiasing
            offscreenGr.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );

            //Draw background
            offscreenGr.setColor( TABLE_COLOR );
            offscreenGr.fillRect( 0, 0, dim.width, dim.height );
            
            //Draw deck
            if( deck != null )
                if( deck.isEmpty() ) {
                    Point loc = deck.getLocation();
                    RoundRectangle2D border = new RoundRectangle2D.Double(
                            loc.x, loc.y, Card.DEFAULT_WIDTH - 1, Card.DEFAULT_HEIGHT - 1, 20, 20);

                    offscreenGr.setClip(border); // Don't draw outside the lines
                    
                    offscreenGr.setColor( new Color (60,179,113) );
                    offscreenGr.fillRect( loc.x, loc.y, ClassicCard.DEFAULT_WIDTH, ClassicCard.DEFAULT_HEIGHT );
                }
                else {
                    boolean xx = menuItemHint.getState();
                    deck.top().paint( offscreenGr, menuItemHint.getState() );
                }

            //Draw revealedCards
            if( revealedCards != null && !revealedCards.isEmpty() )
                revealedCards.top().paint( offscreenGr, menuItemHint.getState() );

            //Draw sequential stacks
            if( seqStack != null )
                for( int i = 0; i < Solitaire.SEQ_STACK_CNT; i++ )
                    seqStack[ i ].paint( offscreenGr, menuItemHint.getState() );

            //Draw solitaire stacks
            if( solStack != null )
                for( int i = 0; i < Solitaire.SOL_STACK_CNT; i++ )
                    solStack[ i ].paint( offscreenGr, menuItemHint.getState() );

            //Draw current stack
            if( currStack != null && !currStack.isEmpty())
                currStack.paint( offscreenGr,menuItemHint.getState() );

            //Draw game info
            if( gameInfo != null && gameInfo.getType() != null && gameInfo.getSeed() != -1 && resBundle != null ) {
                String isRandomStr = null;
                String levelStr = null;
                String gameInfoStr = null;
                if( GameInfo.RANDOM.equals( gameInfo.getType() ) ) {
                    isRandomStr = "Random ";
                    if( Arrays.asList( easyGames ).contains( gameInfo.getSeed() ) )
                        levelStr = "Easy";
                    else if( Arrays.asList( normalGames ).contains( gameInfo.getSeed() ) )
                        levelStr = "Normal";
                    else if( Arrays.asList( hardGames ).contains( gameInfo.getSeed() ) )
                        levelStr = "Hard";
                    else if( Arrays.asList( trickyGames ).contains( gameInfo.getSeed() ) )
                        levelStr = "Tricky";
                    gameInfoStr = MessageFormat.format( resBundle.getString( "GameInfoRandom" ), gameInfo.getSeed() );
                }
                else {
                    isRandomStr = "";
                    if( gameInfo.getType().equals( GameInfo.WINNABLE_EASY ) )
                        levelStr = "Easy";
                    else if( gameInfo.getType().equals( GameInfo.WINNABLE_NORMAL ) )
                        levelStr = "Normal";
                    if( gameInfo.getType().equals( GameInfo.WINNABLE_HARD ) )
                        levelStr = "Hard";
                    if( gameInfo.getType().equals( GameInfo.WINNABLE_TRICKY ) )
                        levelStr = "Tricky";
                    gameInfoStr = MessageFormat.format( resBundle.getString( "GameInfoWinnable" ), resBundle.getString( "Level" + levelStr ), gameInfo.getSeed() );
                }
                Font gameInfoFont = new Font( "Arial", Font.PLAIN, 14 );
                FontMetrics gameInfoFontMetrics = offscreenGr.getFontMetrics( gameInfoFont );
                offscreenGr.setFont( gameInfoFont );
               
                int x = 10;
                int y = 485;
                
                offscreenGr.setColor( Color.lightGray ); // Shadow
                offscreenGr.setColor( Color.black ); // Text
                
                //movimentos
                offscreenGr.setColor( Color.lightGray ); // Shadow
                offscreenGr.setColor( Color.black ); // Text
                
                //tempo atualizado
                long agr = (System.currentTimeMillis() - timestart)/1000;
                long minutos = agr/60;
                long segundos = agr % 60;
                String agora = "";
    
                if (minutos<60) {
                	if(segundos<10) {
                		agora = "0" + minutos + ":0" + segundos;
                	}else {
                		agora = "0" + minutos + ":" + segundos;
                	}
                }else {
                	if(segundos<10) {
                		agora = "" + minutos + ":0" + segundos;
                	}else {
                		agora = "" + minutos + ":" + segundos;
                	}
                }
                           
                offscreenGr.setColor(Color.lightGray); // Sombra
                offscreenGr.drawString(agora, 10, getHeight() - 10); // Texto

                offscreenGr.setColor(Color.black); // Texto
                //offscreenGr.drawString(agora, 9, getHeight() - 11); // Sombra
                
                if(!isGameWon()) {
                	table.repaint(6);
                }
        
            }
            
            g.drawImage( offscreen, 0, 0, this );
        }

        public void destroy() {
            offscreenGr.dispose();
        }

        private Image       offscreen;
        private Graphics2D  offscreenGr;
    }

    class SolitaireWindowManager extends WindowManager {

        SolitaireWindowManager( Window window, int action ) {
            super( window, action );
        }

        public void windowClosing( WindowEvent e ) {
            if( frameAbout != null ) {
                frameAbout.dispose();
                frameAbout = null;
            }
            if( frameRules != null ) {
                frameRules.dispose();
                frameRules = null;
            }
            super.windowClosing( e );
        }
    }
    
    // Set up the game seed references according to difficulty
    // These games were found by using an adapted version of this program which
    // tries to solve a game from a seeded position. 3000 games were played and 100 winnable
    // were chosen for each category.
    // A statistical analysis was performed on the winnable games and were classed according
    // to the number of positions analysed and deadends reached before finding the solution.
    // Basically if the program solver found it hard to find the winning solution then a 
    // human should also.
    // The tricky difficulty levels corresponds to games which should only be able to be won
    // if at some stage the player does not play a legal move but instead draws cards. For example 
    // a 4 clubs could be put onto a sequential stack but instead the player draws cards until
    // a 3 hearts is revealed which can then go under the 4 clubs.
    private void setupWinnable() {
        easyGames = GameLevels.easyGame;

        normalGames = GameLevels.normalGames;
        
        hardGames = GameLevels.hardGames;
        
        trickyGames = GameLevels.trickyGames;
    }  
           
    protected   Image               backgroundImage;
    protected   Stack               currStack;
    protected   ClassicDeck         deck;
    protected   Stack               revealedCards;
    protected   SolitaireStack[]    solStack;
    protected   SequentialStack[]   seqStack;
    protected   Table               table;
    
    // Holds the state of the solitaire game after each move
    protected ArrayList<GameState>  gameStates = new ArrayList<GameState>();
    
    // Holds all the legal moves from a given position (held as game states after move)
    protected ArrayList<GameState>  legalGs = new ArrayList<GameState>();
    
    // Holds the winnable games classed by difficulty (100 each ATM)
    protected int[] easyGames;
    protected int[] normalGames;
    protected int[] hardGames;
    protected int[] trickyGames;
    
    protected GameInfo gameInfo = new GameInfo();

    static protected ResourceBundle resBundle;

    private Button				desfazer;
    private MenuBar             menubar;
    private Menu                menuOptions;
    private Menu                menuDesenvolvimento;
    private MenuItem            menuRanking;
    private MenuItem            menuPrototipo;
    private Menu                menuHelp;
    private MenuItem            menuItemNewGame;
    private MenuItem            menuItemRestart;
    private MenuItem            menuItemUndo;
    private CheckboxMenuItem    menuItemLevelRandom;
    private CheckboxMenuItem    menuItemLevelEasy;
    private CheckboxMenuItem    menuItemLevelNormal;
    private CheckboxMenuItem    menuItemLevelHard;
    private CheckboxMenuItem    menuItemLevelTricky;
    private MenuItem            menuItemRules;
    private MenuItem            menuItemAbout;
    private MenuItem            menuItemLicense;
    private CheckboxMenuItem    menuItemHint;
    private CheckboxMenuItem    menuItemEnglish;
    private CheckboxMenuItem    menuItemFrench;

    private FrameAbout          frameAbout;
    private FrameRules          frameRules;
    private SolitaireRanking    frameRanking; 
    private menuprototipo       frameMenuP;
}

