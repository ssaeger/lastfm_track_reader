package de.saeger.lastfmreader;

class Track {

	private String name;
	private String album;
	private String interpret;
	private int playcount;
	private String date;

	public Track(String name, String album, String interpret, String date) {
		this.name = name;
		this.album = album;
		this.interpret = interpret;
		this.playcount = 1;
		this.date = date;
	}

	public Track() {

	}

	public String toString() {
		return "";
	}

	public String getDate() {
		return date;
	}

	public void setDate(String value) {
		date = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String value) {
		name = value;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String value) {
		album = value;
	}

	public String getInterpret() {
		return interpret;
	}

	public void setInterpret(String value) {
		interpret = value;
	}

	public int getPlaycount() {
		return playcount;
	}

	public void setPlaycount(int value) {
		playcount = value;
	}

}
