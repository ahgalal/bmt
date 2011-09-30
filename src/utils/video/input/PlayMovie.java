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



import de.humatic.dsj.*;



public class PlayMovie implements java.beans.PropertyChangeListener {

	public DSFiltergraph movie;

	public PlayMovie() {}

	public void createGraph() {

		//javax.swing.JFrame f = new javax.swing.JFrame("dsj - play movie");

		//java.awt.FileDialog fd = new java.awt.FileDialog(f, "select movie", java.awt.FileDialog.LOAD);

		//fd.setVisible(true);

		//if (fd.getFile() == null) return;

		//movie = new DSMovie(fd.getDirectory()+fd.getFile(), DSFiltergraph.DD7, this);
		movie = new DSMovie("video.avi", DSFiltergraph.DD7, null);

		//f.getContentPane().add(java.awt.BorderLayout.CENTER, movie.asComponent());

		//f.getContentPane().add(java.awt.BorderLayout.SOUTH, new SwingMovieController(movie));

		//f.pack();
		System.out.println(movie.getWidth() + " X " + movie.getHeight());
		//f.setVisible(true);

		/**
		Don't do this at home. This demo relies on dsj closing and disposing off filtergraphs when the JVM exits. This is
		OK for a "open graph, do something & exit" style demo, but real world applications should take care of calling
		dispose() on filtergraphs they're done with themselves.
		 **/

		//f.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

	}

	public void propertyChange(java.beans.PropertyChangeEvent pe) {

		//System.out.println("received event or callback from "+pe.getPropagationId());

		switch(DSJUtils.getEventType(pe)) {

		}

	}

	public static void main(String[] args) throws InterruptedException{

		PlayMovie pm = new PlayMovie();
		pm.createGraph();
/*		pm.movie.pause();
		Thread.sleep(1000);
		pm.movie.play();
		Thread.sleep(1000);
		pm.movie.pause();
		Thread.sleep(1000);
		pm.movie.play();
		Thread.sleep(1000);*/
		pm.movie.pause();
		pm.movie.step(125);
		pm.movie.getImage();
/*		pm.movie.step(5);
		pm.movie.step(1);
		pm.movie.step(1);
		pm.movie.step(1);*/

	}

}