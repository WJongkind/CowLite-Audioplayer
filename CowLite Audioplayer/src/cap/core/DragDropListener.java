package cap.core;

import cap.core.audio.FileAudioPlayer;
import cap.gui.GraphicalInterface;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.io.File;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Wessel
 */
public class DragDropListener implements DropTargetListener
{
        @Override
        public void dragEnter(DropTargetDragEvent dtde) {}

        @Override
        public void dragOver(DropTargetDragEvent dtde) {}

        @Override
        public void dropActionChanged(DropTargetDragEvent dtde) {}

        @Override
        public void dragExit(DropTargetEvent dte) {}

        /**
         * for when a file has been dropped on the linked JFrame
         * @param dtde dropevent
         */
        @Override
        public void drop(DropTargetDropEvent dtde) {
        
        // Accept copy drops
        dtde.acceptDrop(DnDConstants.ACTION_COPY);

        // Get the transfer which can provide the dropped item data
        Transferable transferable = dtde.getTransferable();

        // Get the data formats of the dropped item
        DataFlavor[] flavors = transferable.getTransferDataFlavors();

        // Loop through the flavors
        for (DataFlavor flavor : flavors) {

            try {

                // If the drop items are files
                if (flavor.isFlavorJavaFileListType()) {

                    // Get all of the dropped files
                    List files = (List) transferable.getTransferData(flavor);

                    // Loop them through
                    for (Object file: files) {

                        File theFile = (File) file;
                        System.out.println(theFile);
                        // Print out the file path
                        if(CowLiteAudioPlayer.player instanceof FileAudioPlayer)
                            ((FileAudioPlayer)CowLiteAudioPlayer.player).addSong(theFile.getAbsolutePath());

                    }

                }

            } catch (Exception e) {

                // Print out the error stack
                e.printStackTrace();

            }
        }

        // Inform that the drop is complete
        GraphicalInterface.uptodate = false;
        CoreTime.update = true;
        dtde.dropComplete(true);
        }
            
}

