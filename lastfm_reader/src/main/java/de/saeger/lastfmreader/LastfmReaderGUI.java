package de.saeger.lastfmreader;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.EventQueue;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
/**
 * Draws the GUI.
 * @author Saeger
 *
 */
public class LastfmReaderGUI extends JFrame {
	/**Contains all other GUI parts. */
	private JPanel contentPanel;
	/**Is used for:
	 * The last.fm user name to fetch the recent tracks of.*/
	private JTextField textUser;
	/**Is used for:
	 * A Last.fm API key.*/
	private JTextField textKey;
	/**Is used for:
	 * The number of results to fetch per page. Maximum is 200.*/
	private JTextField textLimit;
	/**Is used for:
	 * The last.fm URL for this API service.*/
	private JTextField textUrl;
	/**Contains the Tabs.*/
	private JTabbedPane tabbedPane;
	/**Contains the table for the data.*/
	private JPanel data;
	/**Presents the data.*/
	private JTable table;
	/**Is used to scroll through the data in the table.*/
	private JScrollPane scrollPane;
	/**Is used for:
	 * The Number of pages to fetch. "all" = fetch all pages.*/
	private JTextField textPages;
	/**Contains the data.*/
	private TrackContainer aTrackContainer;
	/**Is used to set he number of threads.*/
	private JSlider slider;
	/**Displays the current status of the program.
	 * For example: Exceptions, Progress, ...*/
	private static JTextArea textArea;
	/**Is used to scroll through the status information in the textArea.*/
	private JScrollPane scrollPane2;
	/**<b>Optional</b>:
	 * Is used for:
	 * The number of different Tracks.
	 * This information can increase the performance of the program
	 * because it do not have to resize the data structure and 
	 * copy the data whenever the old one is full.*/
	private JTextField textTracks;

	/**
	 * 
	 * @param args Command line parameter
	 */
	public static void main(final String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LastfmReaderGUI frame = new LastfmReaderGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * Standard constructor: Initialize the GUI.
	 */
	public LastfmReaderGUI() {
		setTitle("Last.fm Reader");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 640, 480);
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPanel);

		JButton btnLoad = new JButton("Load Data");
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnLoadActionPerformed();
			}
		});
		btnLoad.setBounds(299, 408, 131, 23);
		contentPanel.setLayout(null);
		contentPanel.add(btnLoad);

		JButton btnShowData = new JButton("Show Data");
		btnShowData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnShowDataActionPerformed();
			}
		});
		btnShowData.setBounds(462, 408, 120, 23);
		contentPanel.add(btnShowData);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 604, 386);
		contentPanel.add(tabbedPane);

		JPanel settings = new JPanel();
		tabbedPane.addTab("Settings", null, settings, null);
		settings.setLayout(null);

		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(11, 8, 70, 14);
		settings.add(lblUsername);

		textUser = new JTextField();
		textUser.setBounds(91, 5, 86, 20);
		settings.add(textUser);
		textUser.setText("zaega");
		textUser.setHorizontalAlignment(SwingConstants.CENTER);
		textUser.setColumns(10);

		JLabel lblPagelimit = new JLabel("Page-Limit:");
		lblPagelimit.setBounds(11, 33, 70, 14);
		settings.add(lblPagelimit);

		textLimit = new JTextField();
		textLimit.setBounds(91, 30, 86, 20);
		settings.add(textLimit);
		textLimit.setHorizontalAlignment(SwingConstants.CENTER);
		textLimit.setText("200");
		textLimit.setColumns(10);

		JLabel lblApikey = new JLabel("API-Key:");
		lblApikey.setBounds(16, 58, 65, 14);
		settings.add(lblApikey);

		textKey = new JTextField();
		textKey.setBounds(91, 55, 365, 20);
		settings.add(textKey);
		textKey.setText("30d3d4877f08d37cdbba1a8ac3ebf982");
		textKey.setHorizontalAlignment(SwingConstants.CENTER);
		textKey.setColumns(10);

		JLabel lblUrl = new JLabel("URL:");
		lblUrl.setBounds(26, 83, 55, 14);
		settings.add(lblUrl);

		textUrl = new JTextField();
		textUrl.setBounds(91, 80, 498, 20);
		settings.add(textUrl);
		textUrl.setText("http://ws.audioscrobbler.com/2.0/?method=user.getrecenttracks");
		textUrl.setHorizontalAlignment(SwingConstants.CENTER);
		textUrl.setColumns(10);

		JLabel lblTotalpages = new JLabel("Total-Pages:");
		lblTotalpages.setBounds(260, 8, 78, 14);
		settings.add(lblTotalpages);

		textPages = new JTextField();
		textPages.setHorizontalAlignment(SwingConstants.CENTER);
		textPages.setText("all");
		textPages.setBounds(348, 5, 108, 20);
		settings.add(textPages);
		textPages.setColumns(10);

		slider = new JSlider();
		slider.setName("");
		slider.setMajorTickSpacing(1);
		slider.setSnapToTicks(true);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setValue(2);
		slider.setMinimum(1);
		slider.setMaximum(4);
		slider.setBounds(201, 140, 200, 39);
		settings.add(slider);

		JLabel lblThreads = new JLabel("Threads:");
		lblThreads.setBounds(201, 115, 65, 14);
		settings.add(lblThreads);

		scrollPane2 = new JScrollPane();
		scrollPane2.setBounds(66, 190, 465, 157);
		settings.add(scrollPane2);

		textArea = new JTextArea();
		textArea.setAutoscrolls(false);
		textArea.setSelectedTextColor(Color.WHITE);
		textArea.setForeground(Color.WHITE);
		textArea.setCaretColor(Color.WHITE);
		textArea.setBackground(Color.BLACK);
		scrollPane2.setViewportView(textArea);
		textArea.setEditable(false);

		JLabel lblTracks = new JLabel("Differend Tracks (nearly):");
		lblTracks.setToolTipText("optional, maybe improves the performance");
		lblTracks.setBounds(187, 33, 151, 14);
		settings.add(lblTracks);

		textTracks = new JTextField();
		textTracks.setHorizontalAlignment(SwingConstants.CENTER);
		textTracks.setBounds(348, 30, 108, 20);
		settings.add(textTracks);
		textTracks.setColumns(10);

		data = new JPanel();
		tabbedPane.addTab("Data", null, data, null);
		data.setLayout(null);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 579, 336);
		data.add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] {
				"Title", "Interpret", "Album", "Playcount", "Last Played" }));
	}
	
	private void btnShowDataActionPerformed() {
		try {
			aTrackContainer.showData(table);
		} catch (NullPointerException e) {
			changeStatus("No Data.\nPlease first load Data.");
		}
	}

	private void btnLoadActionPerformed() {

		try {
			changeStatus("Prepare for loading data...");
			Parser aParser = new Parser(textUser.getText(),
					Integer.parseInt(textLimit.getText()), textKey.getText(),
					textUrl.getText(), textPages.getText());
			final long start = System.currentTimeMillis();
			if ((textPages.getText().equalsIgnoreCase("all"))
					&& !textTracks.getText().equalsIgnoreCase("")) {
				aTrackContainer = TrackContainer.getObjectreferenz(Integer
						.parseInt(textTracks.getText()));
			} else
				aTrackContainer = TrackContainer.getObjectreferenz();
			int i = slider.getValue();
			int y = 1;
			String s = "thread-";
			final ThreadGroup group = new ThreadGroup("aGroup");
			for (int x = 0; x < i; x++) {
				s = s + x;
				new ParserThread(group, s, aTrackContainer, i, y++, aParser)
						.start();
				changeStatus("Thread-" + (x + 1) + " started");
			}
			new Thread() {
				public void run() {
					while (group.activeCount() != 0) {
					}
					changeStatus("Finish after: "
							+ (System.currentTimeMillis() - start + " ms"));
				}
			}.start();

		} catch (NumberFormatException e) {
			changeStatus("Wrong Format.\nPlease check your input data.");
		}
	}

	public static void changeStatus(String s) {
		textArea.setText(s + "\n" + textArea.getText());
		textArea.update(textArea.getGraphics());
	}
}
