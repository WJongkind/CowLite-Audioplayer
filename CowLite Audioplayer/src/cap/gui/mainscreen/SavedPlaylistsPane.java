/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.mainscreen;

import cap.core.audio.Playlist;
import cap.gui.colorscheme.SavedListsPaneColorScheme;
import java.awt.Component;
import java.lang.ref.WeakReference;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import cap.gui.colorscheme.UILayout;

/**
 *
 * @author Wessel
 */
public class SavedPlaylistsPane extends JScrollPane {
    
    // MARK: - Associated types & constants
    
    public interface PlayListSelectionDelegate {
        public void didSelectPlayList(Playlist playList);
    }
    
    // MARK: - UI properties
    
    private final JList playlistPane;
    private final DefaultListModel<String> playlistListModel;
    
    // MARK: - Private properties
    
    private WeakReference<PlayListSelectionDelegate> delegate;
    private List<Playlist> playlists;
    
    
    // MARK: - Initialiser
    
    public SavedPlaylistsPane(UILayout colorScheme) {
        playlistPane = new JList();
        playlistListModel = new DefaultListModel<>();
        
        playlistPane.setBackground(colorScheme.savedLists().backgroundColor());
        playlistPane.setForeground(colorScheme.savedLists().textColor());
        playlistPane.addListSelectionListener(e -> didSelectPlaylist(e.getFirstIndex()));
        playlistPane.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        playlistPane.setModel(playlistListModel);
        playlistPane.setCellRenderer(new ListCellRenderer(colorScheme.savedLists()));
                
        super.setViewport(super.createViewport());
        super.getViewport().add(playlistPane);
        super.getViewport().setBackground(colorScheme.savedLists().backgroundColor());
        super.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        super.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        super.setBorder(BorderFactory.createEmptyBorder( 0, 0, 0, 0 ));
    }
    
    // MARK: - Getters & setters
    
    public void setDelegate(PlayListSelectionDelegate delegate) {
        this.delegate = new WeakReference<>(delegate);
    }
    
    public void setPlayLists(List<Playlist> playlists) {
        this.playlists = playlists;
        playlistListModel.clear();
        
        for(Playlist playlist: playlists) {
            playlistListModel.addElement(playlist.getName());
        }
    }
    
    // MARK: - ListSelectionListener
    
    private void didSelectPlaylist(int index) {
        if(delegate == null) {
            return;
        }
        
        PlayListSelectionDelegate strongDelegate = delegate.get();
        if(strongDelegate != null) {
            strongDelegate.didSelectPlayList(index >= playlists.size() ? null : playlists.get(index));
        }
    }
    
    // MARK: - Private associated types
    
    private class ListCellRenderer extends DefaultListCellRenderer {
        
        // MARK: - Private properties
        
        private final SavedListsPaneColorScheme colorScheme;
        
        // MARK: - Initialisers
        
        public ListCellRenderer(SavedListsPaneColorScheme colorScheme) {
            this.colorScheme = colorScheme;
        }
        
        // MARK: - DefaultTableCellRenderer
        
        @Override
        public Component getListCellRendererComponent(
                JList<?> list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            setBorder(noFocusBorder);
            if(isSelected) {
                c.setBackground(colorScheme.highlightBackgroundColor());
                c.setForeground(colorScheme.highlightTextColor());
            } else {
                c.setForeground(colorScheme.textColor());
                c.setBackground(getBackground());
            }
            return c;
        };
    }
    
}
