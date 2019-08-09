/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.overlay;

import cap.audio.Song;
import cap.audio.SongPlayer;
import cap.gui.colorscheme.ColorScheme;
import cap.gui.colorscheme.darkmode.DarkMode;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.net.URL;
import javax.swing.JFrame;

/**
 *
 * @author Wessel
 */
public class SongInfoOverlay extends JFrame implements MouseMotionListener, MouseListener {
    
    // MARK: - Constants
    
    private static final class Layout {
        public static final int maximumWidth = 380;
    }
    
    // MARK: - Private properties
    
    private final SongInfoPanel infoPanel;
    private boolean isMovable = false;
    private Point previousMousePoint = null;
    
    // MARK: - Initialisers
    
    public SongInfoOverlay(ColorScheme colorScheme) {
        this.infoPanel = new SongInfoPanel(colorScheme);
        
        super.add(infoPanel);
        super.setUndecorated(true);
        super.setBackground(new Color(0, 0, 0, 0));
        super.getContentPane().setBackground(new Color(0, 0, 0, 0));
        super.setSize(Layout.maximumWidth, infoPanel.getPreferredSize().height);
        super.setAlwaysOnTop(true);
        super.setFocusable(false);
        super.addMouseMotionListener(this);
        super.addMouseListener(this);
    }
    
    // MARK: - Public methods
    
    public void updateForPlayer(SongPlayer player) {
        infoPanel.updateForPlayer(player);
    }
    
    public void setIsMovable(boolean isMovable) {
        this.isMovable = isMovable;
    }
    
    // MARK: - MouseMotionListener
    
    @Override
    public void mouseDragged(MouseEvent e) {
        if(previousMousePoint != null && isMovable) {
            int dx = e.getLocationOnScreen().x - previousMousePoint.x;
            int dy = e.getLocationOnScreen().y - previousMousePoint.y;
            int newX = getLocation().x + dx;
            int newY = getLocation().y + dy;
            super.setLocation(newX, newY);
        }
        
        previousMousePoint = e.getLocationOnScreen();
    }

    @Override
    public void mouseMoved(MouseEvent e) {}
    
    // MARK: - MouseListener
    
    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        previousMousePoint = e.getLocationOnScreen();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        previousMousePoint = null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
    
}
