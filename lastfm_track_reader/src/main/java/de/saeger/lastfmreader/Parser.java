package de.saeger.lastfmreader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class Parser {

	private String url;
	private String url_in;
	private int page;
	private String user;
	private int limit;
	private String key;
	private int totalPages;
	private Track tmpTrack = new Track();
	private final Lock lock = new ReentrantLock();

	private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory
			.newInstance();
	private static final XPathFactory XPATH_FACTORY = XPathFactory
			.newInstance();

	public int loadTotalPages() {
		URLConnection connection = connect(1);
		StringBuilder sb = new StringBuilder();
		try {
			InputStream is = connection.getInputStream();
			is.skip(100);
			for (int i = 0; i < 50; i++) {
				sb.append((char) is.read());
			}

		} catch (IOException e) {
			LastfmReaderGUI.changeStatus("Can not connect to the URL.\n");
		}

		sb.delete(0, sb.indexOf("totalPages="));
		sb.setLength(sb.indexOf("total=") - 1);
		sb.trimToSize();

		return Integer.parseInt((String) sb.subSequence(12, sb.capacity() - 1));
	}

	public URLConnection connect(int y) {
		URLConnection connection = null;
		try {
			URL url = new URL(getUrl_in() + "&page=" + y);
			connection = url.openConnection();
		} catch (IOException e) {
			LastfmReaderGUI.changeStatus("Can not connect to the URL.");
		}

		return connection;
	}
	
	public void parseInterpret(XPath xPathEvaluator, Document document, TrackContainer aTrackContainer) {
		try {
			XPathExpression nameExpr = xPathEvaluator.compile("lfm/recenttracks/track/artist");
			NodeList nameNodes = (NodeList) nameExpr.evaluate(document,
					XPathConstants.NODESET);
		
			for (int i = 0; i < nameNodes.getLength(); i++) {
				Node nameNode = nameNodes.item(i);
				getTmpTrack().setInterpret(
						String.format(nameNode.getTextContent()));
				parseTrack(xPathEvaluator, nameNode);
				parseAlbum(xPathEvaluator, nameNode);
				parseDate(xPathEvaluator, nameNode);
				addTmpTrack(aTrackContainer);
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void parseTrack(XPath xPathEvaluator, Node nameNode) {
		try {
			XPathExpression nameExpr = xPathEvaluator.compile("following-sibling::name");
			NodeList trackNameNodes = (NodeList) nameExpr.evaluate(nameNode,
					XPathConstants.NODESET);
		
			for (int j = 0; j < trackNameNodes.getLength(); j++) {
				getTmpTrack().setName(
						String.format(trackNameNodes.item(j)
								.getTextContent()));
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void parseAlbum(XPath xPathEvaluator, Node nameNode) {
		try {
			XPathExpression albumNameExpr = xPathEvaluator
					.compile("following-sibling::album");
		
			NodeList albumNameNodes = (NodeList) albumNameExpr.evaluate(nameNode,
					XPathConstants.NODESET);
			for (int j = 0; j < albumNameNodes.getLength(); j++) {
				getTmpTrack().setAlbum(
						String.format(albumNameNodes.item(j)
								.getTextContent().replaceAll("%", "")));
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void parseDate(XPath xPathEvaluator, Node nameNode) {
		try {
			XPathExpression trackDateExpr = xPathEvaluator
					.compile("following-sibling::date");
		
			NodeList trackDateNodes = (NodeList) trackDateExpr.evaluate(nameNode,
					XPathConstants.NODESET);
			for (int j = 0; j < trackDateNodes.getLength(); j++) {
				String s = String.format(trackDateNodes.item(j).getTextContent())
						.replace(" Jan ", ".01.").replace(" Feb ", ".02.")
						.replace(" Mar ", ".03.").replace(" Apr ", ".04.")
						.replace(" May ", ".05.").replace(" Jun ", ".06.")
						.replace(" Jul ", ".07.").replace(" Aug ", ".08.")
						.replace(" Sep ", ".09.").replace(" Oct ", ".10.")
						.replace(" Nov ", ".11.").replace(" Dec ", ".12.")
						.replace(",", "");
	
				StringBuffer sb = new StringBuffer();
				if (s.length() == 15)
					sb.append("0");
				for (int index = 0; index < s.length(); index++)
					sb.append(s.charAt(index));
				s = sb.toString();
				sb.delete(0, sb.length());
				sb.append(s.substring(6, 10));
				sb.append(s.substring(2, 5));
				sb.append('.');
				sb.append(s.substring(0, 2));
				sb.append(s.substring(10, 16));
	
				s = sb.toString();
			
				getTmpTrack().setDate(s);
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addTmpTrack(TrackContainer aTrackContainer) {
		// Binäre Suche um Album zusuchen
		int x = aTrackContainer.binarySearch(getTmpTrack().getName(),
				getTmpTrack().getAlbum(), getTmpTrack().getInterpret(),
				getTmpTrack().getDate());
		// Track auf Album suchen (Linear)
		if (x >= 0)
			x = aTrackContainer.linearSearch(x,
					getTmpTrack().getName(), getTmpTrack().getAlbum(),
					getTmpTrack().getInterpret(), getTmpTrack()
							.getDate());
	}

	public void parseData(TrackContainer aTrackContainer,
			URLConnection connection) throws Exception {
		DocumentBuilder db;
		XPath xPathEvaluator;
		try {
			db = DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();

			final Document document = db.parse(connection.getInputStream());
			xPathEvaluator = XPATH_FACTORY.newXPath();
			lock.lock();
			parseInterpret(xPathEvaluator, document, aTrackContainer);
			lock.unlock();
		} catch (Exception e) {
			LastfmReaderGUI.changeStatus(e.toString());
		}
	}

	private String buildUrl() {
		return this.url + "&user=" + this.user + "&api_key=" + this.key
				+ "&limit=" + this.limit;
	}

	public Parser(String user, int limit, String key, String url,
			String totalPages) {
		this.user = user;
		this.limit = limit;
		this.key = key;
		this.url = url;
		this.url_in = buildUrl();
		if (totalPages.equalsIgnoreCase("all"))
			this.totalPages = loadTotalPages();
		else
			this.totalPages = Integer.parseInt(totalPages);
	}

	public Parser() {
		this.user = "zaega";
		this.limit = 200;
		this.key = "30d3d4877f08d37cdbba1a8ac3ebf982";
		this.url = "http://ws.audioscrobbler.com/2.0/?method=user.getrecenttracks";
		this.url_in = buildUrl();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String value) {
		url = value;
	}

	public int getPage() {
		return page;
	}

	public void setPage(short value) {
		page = value;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String value) {
		user = value;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(short value) {
		limit = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String value) {
		key = value;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int value) {
		totalPages = value;
	}

	public String getUrl_in() {
		return url_in;
	}

	public void setUrl_in(String value) {
		url_in = value;
	}

	public Track getTmpTrack() {
		return tmpTrack;
	}

	public void setTmpTrack(Track value) {
		tmpTrack = value;
	}

}
