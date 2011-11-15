package de.saeger.lastfmreader;

import java.util.*;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

class TrackContainer {

	private String fileName;
	private Vector<Track> tracks = new Vector<Track>(10, 50);

	private TrackContainer() {
	}

	private TrackContainer(int length) {
		tracks.ensureCapacity(length);
	}

	private static TrackContainer aTrackContainer = null;

	public static TrackContainer getObjectreferenz() {
		if (aTrackContainer == null)
			aTrackContainer = new TrackContainer();
		// runs constructor only once
		return aTrackContainer;
	}

	public static TrackContainer getObjectreferenz(int length) {
		if (aTrackContainer == null)
			aTrackContainer = new TrackContainer(length);
		// runs constructor only once
		return aTrackContainer;
	}

	public void trackIncPlaycount(int s) {
		Track x = tracks.elementAt(s);
		x.setPlaycount((x.getPlaycount() + 1));
	}

	public int binarySearch(String name, String album, String interpret,
			String date) {
		int first = 0;
		int last = tracks.size() - 1;
		int middle = 0;
		while (first <= last) {
			middle = (first + last) / 2;
			if (getTracks().elementAt(middle).getAlbum()
					.compareToIgnoreCase(album) > 0)
				last = middle - 1;
			else if (getTracks().elementAt(middle).getAlbum()
					.compareToIgnoreCase(album) < 0)
				first = middle + 1;
			else
				return middle;
		}
		getTracks().insertElementAt(new Track(name, album, interpret, date),
				first);
		return -1;
	}

	public int linearSearch(int start, String name, String album,
			String interpret, String date) {
		int i = start;
		for (; getTracks().elementAt(i).getAlbum().compareToIgnoreCase(album) == 0;) {
			if (getTracks().elementAt(i).getName().compareToIgnoreCase(name) == 0) {
				trackIncPlaycount(i);
				if (tracks.elementAt(i).getDate().compareToIgnoreCase(date) < 0)
					tracks.elementAt(i).setDate(date);
				return 0;
			}
			if (i > 0)
				i--;
			else
				break;
		}
		i = start + 1;
		if (start == getTracks().size() - 1)
			return -1;
		for (; getTracks().elementAt(i).getAlbum().compareToIgnoreCase(album) == 0;) {
			if (getTracks().elementAt(i).getName().compareToIgnoreCase(name) == 0) {
				trackIncPlaycount(i);
				if (tracks.elementAt(i).getDate().compareToIgnoreCase(date) < 0)
					tracks.elementAt(i).setDate(date);
				return 0;
			}
			if (i < getTracks().size() - 1)
				i++;
			else
				return -1;
		}
		getTracks().insertElementAt(new Track(name, album, interpret, date), i);
		return -1;
	}

	public Vector<Track> getTracks() {
		return tracks;
	}

	public void addTrack(Track value) {
		this.tracks.add(value);
	}

	public void showData(JTable table) {
		int y;
		DefaultTableModel tModel = new DefaultTableModel(tracks.size(), 5);
		table.setModel(tModel);
		for (int i = 0; i < tracks.size(); i++) {
			y = 0;
			table.setValueAt(tracks.elementAt(i).getName(), i, y++);
			table.setValueAt(tracks.elementAt(i).getInterpret(), i, y++);
			table.setValueAt(tracks.elementAt(i).getAlbum(), i, y++);
			table.setValueAt(tracks.elementAt(i).getPlaycount(), i, y++);
			table.setValueAt(tracks.elementAt(i).getDate(), i, y);
		}
		Comparator<Integer> icomparator = new Comparator<Integer>() {
			public int compare(Integer i1, Integer i2) {
				if (i1 == i2)
					return 0;
				if (i1 < i2)
					return -1;
				else
					return 1;
			}
		};

		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(
				table.getModel());
		table.setRowSorter(sorter);
		sorter.setComparator(3, icomparator);
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String value) {
		fileName = value;
	}

}
