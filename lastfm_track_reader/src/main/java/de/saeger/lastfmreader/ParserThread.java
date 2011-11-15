package de.saeger.lastfmreader;

public class ParserThread extends Thread {

	private TrackContainer aTrackContainer;
	private int threads;
	private int start;
	private Parser aParser;

	public ParserThread() {
	}

	public ParserThread(ThreadGroup group, String name,
			TrackContainer aTrackContainer, int threads, int start,
			Parser aParser) {
		super(group, name);
		setaTrackContainer(aTrackContainer);
		setThreads(threads);
		setStart(start);
		setaParser(aParser);
	}

	public void run() {
		try {
			for (int y = start; y <= aParser.getTotalPages(); y = y + threads) {
				aParser.parseData(aTrackContainer, aParser.connect(y));
				LastfmReaderGUI.changeStatus("Page " + y + " of "
						+ aParser.getTotalPages());
			}
		} catch (Exception e) {
		}
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public Parser getaParser() {
		return aParser;
	}

	public void setaParser(Parser aParser) {
		this.aParser = aParser;
	}

	public int getThreads() {
		return threads;
	}

	public void setThreads(int threads) {
		this.threads = threads;
	}

	public TrackContainer getaTrackContainer() {
		return aTrackContainer;
	}

	public void setaTrackContainer(TrackContainer aTrackContainer) {
		this.aTrackContainer = aTrackContainer;
	}

}
