/***************************************************************************
 *  Copyright 2010,2011 by Ahmed Galal, Ahmed Mohammed Aly,
 *  Sarah Hamid and Mohammed Ahmed Ramadan
 *  contact: ceng.ahmedgalal@gmail.com
 *
 *  This file is part of Behavioral Monitoring Tool.
 *
 *  Behavioral Monitoring Tool is free software: you can redistribute it
 *  and/or modify it under the terms of the GNU General Public License as
 *  published by the Free Software Foundation, version 3 of the
 *  License.
 *
 *  Behavioral Monitoring Tool is distributed in the hope that it
 *  will be useful, but WITHOUT ANY WARRANTY; without even the implied
 *  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Behavioral Monitoring Tool.
 *  If not, see <http://www.gnu.org/licenses/>.
 *   
 **************************************************************************/

package utils.video.input;
  import javax.swing.*;
  import javax.media.*;
  import java.awt.*;
  import java.awt.event.*;
  import java.net.*;
  import java.io.*;
  
  public class PlayVideo2 extends JFrame {
  
    Player player;
    Component center;
    Component south;
  
    public PlayVideo2() {
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      JButton button = new JButton("Select File");
      ActionListener listener = 
          new ActionListener() {
        public void actionPerformed(
            ActionEvent event) {
          JFileChooser chooser = 
            new JFileChooser(".");
          int status = 
            chooser.showOpenDialog(PlayVideo2.this);
          if (status == 
              JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
              load(file);
            } catch (Exception e) {
              System.err.println("Try again: " + e);
            }
          }
        }
      };
      button.addActionListener(listener);
      getContentPane().add(button, 
        BorderLayout.NORTH);
      pack();
      show();
    }
  
    public void load(final File file) 
        throws Exception {
      URL url = file.toURL();
      final Container contentPane = 
        getContentPane();
      if (player != null) {
        player.stop();
      }
      player = Manager.createPlayer(url);
      ControllerListener listener = 
          new ControllerAdapter() {
        public void realizeComplete(
            RealizeCompleteEvent event) {
          Component vc = 
            player.getVisualComponent();
          if (vc != null) {
            contentPane.add(vc, 
              BorderLayout.CENTER);
            center = vc;
          } else {
            if (center != null) {
              contentPane.remove(center);
              contentPane.validate();
            }
          }
          Component cpc = 
            player.getControlPanelComponent();
          if (cpc != null) {
            contentPane.add(cpc, 
              BorderLayout.SOUTH);
            south = cpc;
          } else {
            if (south != null) {
              contentPane.remove(south);
              contentPane.validate();
            }
          }
          pack();
          setTitle(file.getName());
        }
      };
      player.addControllerListener(listener);
      player.start();
    }
  
    public static void main(String args[]) {
      PlayVideo2 pv = new PlayVideo2();
    }
  }