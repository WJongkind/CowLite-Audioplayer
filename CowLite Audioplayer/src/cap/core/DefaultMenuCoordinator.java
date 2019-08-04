/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.core;

import cap.audio.Playlist;
import cap.core.services.PlaylistStoreInterface;
import cap.gui.Window;
import cap.gui.about.AboutViewController;
import cap.gui.colorscheme.ColorScheme;
import cap.gui.shared.SubMenu;
import cap.gui.shared.SubMenuItem;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;

/**
 *
 * @author Wessel
 */
public class DefaultMenuCoordinator implements Coordinator, AboutViewController.AboutViewControllerDelegate {
    
    // MARK: - Associated types
    
    public interface DefaultMenuContextInterface {
        public PlaylistStoreInterface getPlaylistStore();
        public Playlist getCurrentPlaylist();
        public String getAboutText();
        public String getFeaturesText();
    }
    
    // MARK: - Private properties
    
    private final AboutViewController aboutViewController;
    private final DefaultMenuContextInterface menuContext;
    private final SubMenu[] menus;
    
    private Window window;
    
    // MARK: - Initialisers
    
    public DefaultMenuCoordinator(ColorScheme colorScheme, DefaultMenuContextInterface menuContext) {
        this.menuContext = menuContext;
        this.aboutViewController = new AboutViewController(colorScheme);
        
        menus = new SubMenu[3];
        menus[0] = makeFileMenu(colorScheme);
        menus[1] = makeSettingsMenu(colorScheme);
        menus[2] = makeHelpMenu(colorScheme);
        
        aboutViewController.setDelegate(this);
    }
    
    private SubMenu makeFileMenu(ColorScheme colorScheme) {
        SubMenu subMenu = new SubMenu("File", colorScheme);
        
        SubMenuItem savePlaylist = new SubMenuItem("Save playlist", colorScheme, e -> didPressSavePlaylist());
        SubMenuItem removePlaylist = new SubMenuItem("Remove playlist", colorScheme, e -> didPressRemovePlaylist());
        
        subMenu.add(savePlaylist);
        subMenu.add(removePlaylist);
        
        return subMenu;
    }
    
    private SubMenu makeSettingsMenu(ColorScheme colorScheme) {
        SubMenu subMenu = new SubMenu("Settings", colorScheme);
        
        SubMenuItem layout = new SubMenuItem("Layout", colorScheme, e -> didPressLayout());
        SubMenuItem hotkeys = new SubMenuItem("Hotkeys", colorScheme, e -> didPressHotkeys());
        
        subMenu.add(layout);
        subMenu.add(hotkeys);
        
        return subMenu;
    }
    
    private SubMenu makeHelpMenu(ColorScheme colorScheme) {
        SubMenu subMenu = new SubMenu("Help", colorScheme);
        
        SubMenuItem about = new SubMenuItem("About", colorScheme, e -> didPressAbout());
        SubMenuItem features = new SubMenuItem("Features", colorScheme, e -> didPressFeatures());
        
        subMenu.add(about);
        subMenu.add(features);
        
        return subMenu;
    }
    
    // MARK: - Coordinator

    @Override
    public void start(Window window) {
        window.setSubMenus(menus);
        this.window = window;
    }
    
    // MARK: - Private methods
    
    private void didPressSavePlaylist() {
        JFileChooser chooser = null;
        LookAndFeel previousLF = UIManager.getLookAndFeel();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            chooser = new JFileChooser();
            UIManager.setLookAndFeel(previousLF);
        } catch (Exception e) {
            chooser = new JFileChooser();
        }
        chooser.setLocation(window.getLocation());
        if(chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            try {
                menuContext.getCurrentPlaylist().setName(chooser.getSelectedFile().getName());
                menuContext.getPlaylistStore().addPlaylist(menuContext.getCurrentPlaylist(), chooser.getSelectedFile());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void didPressRemovePlaylist() {
        try {
            menuContext.getPlaylistStore().removePlaylist(menuContext.getCurrentPlaylist());
        } catch(Exception e) {
            // TODO show some usefull user feedback
            e.printStackTrace();
        }
    }
    
    private void didPressLayout() {
        
    }
    
    private void didPressHotkeys() {
        
    }
    
    private void didPressAbout() {
        window.pushViewController(aboutViewController);
    }
    
    private void didPressFeatures() {
        
    }
    
    // MARK: - AboutScreenDelegate
    
    @Override
    public void didPressClose(AboutViewController sender) {
        window.popViewController();
    }
    
}