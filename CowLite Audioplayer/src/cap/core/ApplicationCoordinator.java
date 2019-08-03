/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.core;

import cap.control.HotkeyListener;
import cap.audio.PlaylistPlayer;
import cap.audio.SongPlayer;
import cap.audio.youtube.YouTubeService;
import cap.core.services.PlaylistStoreInterface;
import cap.core.DefaultMenuCoordinator.DefaultMenuContextInterface;
import cap.core.services.AppStateService;
import cap.gui.Window;
import cap.gui.mainscreen.MainScreenController;
import java.io.IOException;
import cap.gui.colorscheme.ColorScheme;

/**
 *
 * @author Wessel
 */
public class ApplicationCoordinator implements Coordinator, HotkeyListener.HotkeyListenerDelegate, Window.WindowDelegate {
    
    // MARK: - Constants
    
    private static final double volumeChangeAmount = 0.05;
    
    // MARK: - Private properties
    
    private final MainScreenController mainScreenController;
    private final PlaylistPlayer playlistPlayer;
    private final Coordinator defaultMenuCoordinator;
    private final AppStateService appStateService;
    private final HotkeyListener hotkeyListener;
    
    private Window window;
    
    // MARK: - Initialisers
    
    public ApplicationCoordinator(ColorScheme colorScheme, HotkeyListener hotkeyListener, PlaylistPlayer playlistPlayer, PlaylistStoreInterface playlistStore, DefaultMenuContextInterface menuContext, AppStateService appStateService) throws IOException {
        this.playlistPlayer = playlistPlayer;
        this.mainScreenController = new MainScreenController(colorScheme, playlistPlayer, new YouTubeService(), playlistStore);
        this.defaultMenuCoordinator = new DefaultMenuCoordinator(colorScheme, menuContext);
        this.appStateService = appStateService;
        
        // Catch global hotkey events
        this.hotkeyListener = hotkeyListener;
        hotkeyListener.setDelegate(this);
    }
    
    // MARK: - Coordinator
    
    @Override
    public void start(Window window) {
        this.window = window;
        window.presentViewController(mainScreenController);
        window.setDelegate(this);
        
        defaultMenuCoordinator.start(window);
    }
    
    // MARK: - HotkeyListenerDelegate
    
    @Override
    public void didPressPlay() {
        if(playlistPlayer.getPlayer().getPlayerState() == SongPlayer.PlayerState.playing) {
            playlistPlayer.getPlayer().stop();
        }
        
        if(playlistPlayer.getPlayer().getSong() == null ? playlistPlayer.playNextSong() : playlistPlayer.getPlayer().play()) {
            // TODO Make sure pause button is set
        } else {
            // TODO make sure play button is set
        }
    }

    @Override
    public void didPressPause() {
        playlistPlayer.getPlayer().pause();
        // TODO set play button
    }

    @Override
    public void didPressStop() {
        playlistPlayer.getPlayer().stop();
        // TODO set play button
    }

    @Override
    public void didPressPrevious() {
        playlistPlayer.playPreviousSong();
    }

    @Override
    public void didPressNext() {
        playlistPlayer.playNextSong();
    }

    @Override
    public void didPressVolumeUp() {
        playlistPlayer.getPlayer().setVolume(playlistPlayer.getPlayer().getVolume() + volumeChangeAmount);
    }

    @Override
    public void didPressVolumeDown() {
        playlistPlayer.getPlayer().setVolume(playlistPlayer.getPlayer().getVolume() - volumeChangeAmount);
    }

    @Override
    public void repositionOverlay(int dx, int dy) {
        // TODO
    }

    @Override
    public void allowOverlayRepositioning() {
        // TODO
    }

    @Override
    public void toggleOverlay() {
        // TODO 
    }
    
    // MARK: - WindowDelegate

    @Override
    public void didPressCloseWindow(Window window) {
        if(window == this.window) {
            appStateService.saveWindowSettings(window.getLocation(), window.getSize(), window.isFullScreen());
            appStateService.saveVolume(playlistPlayer.getPlayer().getVolume());
            appStateService.savePlaylistMode(playlistPlayer.getPlaylist().getMode());
            appStateService.saveControls(hotkeyListener.getControls());
            System.exit(0);
        }
    }
    
}
