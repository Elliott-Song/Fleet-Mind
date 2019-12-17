package mygame;

import java.util.concurrent.ThreadLocalRandom;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.KeyStroke;

public class MyGame extends JFrame implements Methods {
	// FLEET MIND
	static boolean sim = true;
	JComponent obj1 = new JLabel();
	Image image, image1;
	Graphics2D g2d;

	static int run = 0;
	int[] loadAvail = {0,0,0};
	static ifBox[] boxes = new ifBox[8];
	static ifBox[] boxes2 = new ifBox[8];
	static double[] xy = new double[2];

	Color white = new Color(255, 255, 255);
	Color green = new Color(40, 80, 70);
	Color black = new Color(0, 0, 0);

	private Image dbImage;
	public static Graphics dbg;
	Font custo, Font2;
	int[] form = new int[2];
	int[] alive = new int[2];
	int win;
	int level;
	int mode;
	String[][] winWord = new String[2][2];
	static String[] formword = { "Arrow", "Circle", "Line", "Wall", "Box" };
	
	int scroll;
	ifBox[] demobox = new ifBox[2];
	

	double[] startpos = new double[] { 600, 400 };
	double[] startpos2 = new double[] { 500, 200 };
	// static Ship[] ships = new Ship[0];
	static ArrayList<Ship> ships = new ArrayList();
	static int selected;

	// funstuff stuff ---------------
	int[] sx = { 8, 18, 28 };
	int[] sy = { 15, 15, 15 };
	String[] s = { "1 Player", "2 Player", "What???" };
	int[] tx = { 3, 3, 3, 3, 3, 4, 4, 6, 6, 6, 6, 6, 7, 9, 9, 9, 9, 9, 10, 10, 10, 12, 12, 12, 12, 12, 13, 13, 13, 15,
			16, 16, 16, 16, 16, 17, 19, 19, 19, 19, 20, 21, 21, 21, 21, 22, 23, 23, 23, 23, 25, 25, 25, 25, 27, 27, 27,
			27, 28, 29, 29, 29, 29, 31, 31, 31, 31, 31, 32, 32, 33, 33, 33 };
	int[] ty = { 2, 3, 4, 5, 6, 2, 4, 2, 3, 4, 5, 6, 6, 2, 3, 4, 5, 6, 2, 4, 6, 2, 3, 4, 5, 6, 2, 4, 6, 2, 2, 3, 4, 5,
			6, 2, 3, 4, 5, 6, 2, 3, 4, 5, 6, 2, 3, 4, 5, 6, 2, 4, 5, 6, 3, 4, 5, 6, 2, 3, 4, 5, 6, 2, 3, 4, 5, 6, 2, 6,
			3, 4, 5 };
	double[] b1 = { 340, 640 };
	double[] b2 = { 740, 640 };
	double[] b3 = { 1140, 640 };

	// finds distance between two points
	public double dist(double[] a, double[] b) {
		double d = Math.sqrt((a[0] - b[0]) * (a[0] - b[0]) + (a[1] - b[1]) * (a[1] - b[1]));
		return d;
	}

	// finds angle from first point to second point
	public double ang(double[] a, double[] b) {
		double h = b[0] - a[0];
		double k = b[1] - a[1];
		double angle = Math.atan2(k, h);
		return angle;
	}

	// adds a new ship to the big Ship array with specified position, angle and team
	public ArrayList createShip(ArrayList o, double[] c, double a, int team) {
		o.add(new Ship(c, a, team, o.size()));
		return o;
	}

	public ArrayList createRanShip(ArrayList o, int team) {
		double[] c = new double[] { ThreadLocalRandom.current().nextDouble(100, 1400),
				ThreadLocalRandom.current().nextDouble(100, 650) };
		double a = ThreadLocalRandom.current().nextDouble(0, 6.3);
		if (c[0] > 750) {
			team = 0;
		} else {
			team = 1;
		}

		createShip(o, c, a, team);

		return o;
	}

	public void arrowForm(int team) {
		double d = team - 0.5;

		for (int i = 0; i < 5; i += 1) {
			double[] c = { 750 + 300 * d * (Math.abs(i - 2) + 2), 375 + 50 * (i - 3) };
			createShip(ships, c, Math.PI / 2 + Math.PI * d, team);
		}
		alive[team] += 5;
	}

	public void circleForm(int team) {
		double d = team - 0.5;

		for (int i = 0; i < 5; i += 1) {
			double[] c = { 750 + 1000 * d + Math.cos(2 * i * Math.PI / 5) * 50,
					375 + Math.sin(2 * i * Math.PI / 5) * 50 };
			createShip(ships, c, Math.PI / 2 + Math.PI * d, team);
		}
		alive[team] += 5;
	}

	public void lineForm(int team) {
		double d = team - 0.5;

		for (int i = 0; i < 5; i += 1) {
			double[] c = { 750 + 1000 * d + 80 * (i - 2), 375, };
			createShip(ships, c, Math.PI / 2 + Math.PI * d, team);
		}
		alive[team] += 5;
	}

	public void wallForm(int team) {
		double d = team - 0.5;

		for (int i = 0; i < 5; i += 1) {
			double[] c = { 750 + 1000 * d, 375 + 80 * (i - 2) };
			createShip(ships, c, Math.PI / 2 + Math.PI * d, team);
		}
		alive[team] += 5;
	}

	public void boxForm(int team) {
		double d = team - 0.5;

		for (int i = 0; i < 4; i += 1) {
			double[] c = { 750 + 1000 * d + Math.cos(2 * i * Math.PI / 4 + Math.PI / 4) * 200,
					375 + Math.sin(2 * i * Math.PI / 4 + Math.PI / 4) * 200 };
			createShip(ships, c, Math.PI / 2 + Math.PI * d, team);
		}
		double[] c = { 750 + 1000 * d, 375 };
		createShip(ships, c, Math.PI / 2 + Math.PI * d, team);
		alive[team] += 5;
	}

	public void makeForm(int team, int form) {
		switch (form) {
		case 0:
			arrowForm(team);
			break;
		case 1:
			circleForm(team);
			break;
		case 2:
			lineForm(team);
			break;
		case 3:
			wallForm(team);
			break;
		case 4:
			boxForm(team);
			break;
		default:
			// code block
		}
	}

	public void save(ifBox[] boxes, String f, int team) {
		System.out.println("save" + f);
		try {
			String verify, putData;
			File file = new File(getClass().getResource(f).getFile());
			file.createNewFile();
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			for (int i = 0; i < boxes.length; i += 1) {
				for (int j = 0; j < boxes[i].ifs.length; j += 1) {
					bw.write(Integer.toString(boxes[i].ifs[j]));
					bw.newLine();
				}
				bw.write(Integer.toString(boxes[i].then));
				bw.newLine();
			}
			bw.write(Integer.toString(form[team]));
			bw.flush();
			bw.close();
			fw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ifBox[] load(ifBox[] bs, String fName, int team) {
		System.out.println("load" + fName);
		try {
			String p  = getClass().getResource(fName).getFile();
			File f = new File(p);
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			for (int i = 0; i < bs.length; i += 1) {
				for (int j = 0; j < bs[i].ifs.length; j += 1) {
					bs[i].ifs[j] = Integer.parseInt(br.readLine());
				}
				bs[i].then = Integer.parseInt(br.readLine());
			}
			form[team] = Integer.parseInt(br.readLine());
			fr.close();
			br.close();
		} catch (IOException e) {
			InputStream input = getClass().getResourceAsStream(fName);
			try (BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
				for (int i = 0; i < bs.length; i += 1) {
					for (int j = 0; j < bs[i].ifs.length; j += 1) {
						bs[i].ifs[j] = Integer.parseInt(br.readLine());
					}
					bs[i].then = Integer.parseInt(br.readLine());
				}
				form[team] = Integer.parseInt(br.readLine());
				br.close();
				input.close();
			} catch (IOException o) {
				e.printStackTrace();
			}
		}
		return bs;
	}

	public ifBox[] clear(ifBox[] bs, int team) {
		for (int i = 0; i < bs.length; i += 1) {
			for (int j = 0; j < bs[i].ifs.length; j += 1) {
				bs[i].ifs[j] = 20;
			}
			bs[i].then = 4;
		}
		form[team] = 0;
		return bs;
	}

	public void funthing(Graphics g) {

		int size = 40;
		double[][][] poss = new double[1500 / size][750 / size][2];
		double mpos[] = new double[2];
		PointerInfo a = MouseInfo.getPointerInfo();
		Point b = a.getLocation();
		mpos[0] = b.getX();
		mpos[1] = b.getY();
		// System.out.println(mpos[0] + " " + mpos[1]);
		double[][] dis = new double[1500 / size][750 / size];
		double[][] ang = new double[1500 / size][750 / size];
		int[][] r = new int[1500 / size][750 / size];
		double dist;

		Color col;

		for (int i = 0; i < 1500 / size; i += 1) {
			for (int j = 0; j < 750 / size; j += 1) {

				poss[i][j][0] = i * size + 20;
				poss[i][j][1] = j * size + 40;
				dist = dist(poss[i][j], mpos);
				dis[i][j] = 80 * (dist + 10) / (dist / 5 * dist / 5 + 10) + 10;
				// System.out.println(dis);
				ang[i][j] = ang(poss[i][j], mpos);
				col = new Color((int) dis[i][j] * 2, (int) dis[i][j] * 2, (int) dis[i][j] * 2);
				r[i][j] = (int) (30 - dis[i][j] / 2);
				g.setColor(col);
				g.drawOval((int) (poss[i][j][0] - dis[i][j] * Math.cos(ang[i][j])),
						(int) (poss[i][j][1] - dis[i][j] * Math.sin(ang[i][j])), r[i][j], r[i][j]);
			}
		}

		g.setColor(Color.ORANGE);
		for (int i = 0; i < tx.length; i += 1) {
			g.fillOval((int) (poss[tx[i]][ty[i]][0] - dis[tx[i]][ty[i]] * Math.cos(ang[tx[i]][ty[i]])),
					(int) (poss[tx[i]][ty[i]][1] - dis[tx[i]][ty[i]] * Math.sin(ang[tx[i]][ty[i]])),
					(int) (30 - dis[tx[i]][ty[i]] / 2), (int) (30 - dis[tx[i]][ty[i]] / 2));
		}

		for (int i = 0; i < sx.length; i += 1) {
			g.setColor(Color.ORANGE);
			g.fillOval((int) (poss[sx[i]][sy[i]][0] - dis[sx[i]][sy[i]] * Math.cos(ang[sx[i]][sy[i]])),
					(int) (poss[sx[i]][sy[i]][1] - dis[sx[i]][sy[i]] * Math.sin(ang[sx[i]][sy[i]])), r[sx[i]][sy[i]],
					r[sx[i]][sy[i]]);
			r[sx[i]][sy[i]] *= 5;
			g.fillOval((int) (poss[sx[i]][sy[i]][0] + r[sx[i]][sy[i]] / 2),
					(int) (poss[sx[i]][sy[i]][1] + r[sx[i]][sy[i]] / 2), -r[sx[i]][sy[i]], -r[sx[i]][sy[i]]);
			// System.out.println(poss[sx[i]][sy[i]][0]+" " + poss[sx[i]][sy[i]][1]);
			if (-r[sx[i]][sy[i]] > 50) {
				g.setColor(Color.BLACK);
				g.drawString(s[i], (int) (poss[sx[i]][sy[i]][0]) - 30, (int) (poss[sx[i]][sy[i]][1]));
				if (i == 0) {
					g.drawString("[lvl " + Integer.toString(level) + "]", (int) (poss[sx[i]][sy[i]][0]) - 20, (int) (poss[sx[i]][sy[i]][1]) + 15);
				}
				mode = i;
			}

		}
		g.setColor(Color.WHITE);

	}
	
	public void drawText (String f, Graphics g) {
		ArrayList<String> lines = new ArrayList<String>();
		InputStream input = getClass().getResourceAsStream(f);
		try (BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       lines.add(line);
		    }
		    br.close();
		    input.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		for (int i = 0; i < lines.size(); i +=1) {
			g.drawString(lines.get(i), 750-(lines.get(i).length()*4), 50 + 25 * (i + scroll) );
		}
	}

	public MyGame() {
		try {
			// create the font to use. Specify the size!
			//custo = Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/Minecraft.ttf")).deriveFont(16f);
			custo = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/Minecraft.ttf")).deriveFont(16f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			// register the font
			ge.registerFont(custo);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		}
		//image = Toolkit.getDefaultToolkit().createImage("examples/space.gif");
		//image1 = image.getScaledInstance(1500, 750, Image.SCALE_DEFAULT);

		addMouseListener(new Mouse());
		addMouseWheelListener(new Mouse2());

		for (int i = 0; i < boxes.length; i += 1) {
			boxes[i] = new ifBox(i);
		}
		for (int i = 0; i < boxes2.length; i += 1) {
			boxes2[i] = new ifBox(i);
		}
		form[0] = 0;
		form[1] = 0;
		level = 0;
		winWord[0][0]= "Y O U   W O N !";
		winWord[0][1]= "Y O U   L O S T ...";
		winWord[1][0]= "P L A Y E R   1   W O N !";
		winWord[1][1]= "P L A Y E R   2   W O N !";
		scroll = 0;
		demobox[0] = new ifBox(0);
		demobox[1] = new ifBox(0);
		demobox[1].ifs[0] = 3;
		demobox[1].ifs[1] = 14;
		demobox[1].then = 0;

		if (sim) {
			selected = -1;
		} else {
			selected = 0;
		}

		obj1.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("UP"), "move up");
		obj1.getActionMap().put("move up", new MoveAction(0));
		obj1.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("LEFT"), "move left");
		obj1.getActionMap().put("move left", new MoveAction(-1));
		obj1.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("RIGHT"), "move right");
		obj1.getActionMap().put("move right", new MoveAction(1));
		obj1.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("SPACE"), "create");
		obj1.getActionMap().put("create", new CreateAction(1));
		add(obj1);
		init();
	}

	private class MoveAction extends AbstractAction {
		int f;

		MoveAction(int f) {
			this.f = f;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!sim) {
				switch (f) {
				case -1:
					ships.get(selected).turn(f);
					break;
				case 0:
					ships.get(selected).boost();
					// ships.get(selected).fire();
					break;
				case 1:
					ships.get(selected).turn(f);
					break;
				default:
					// code block
				}
			}

		}
	}

	private class CreateAction extends AbstractAction {
		int t;

		CreateAction(int t) {
			this.t = t;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			ships = createRanShip(ships, t);
			selected += 1;
		}
	}

	public void init() {
		windowManager();
	}

	public void windowManager() {
		setTitle("Engine");
		setVisible(true);
		setResizable(false);
		setSize(1500, 750);
		setBackground(black);
		setForeground(white);
		setFont(new Font("TimesRoman", Font.BOLD, 50));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public class Mouse extends MouseAdapter { //Clicking
		public void mousePressed(MouseEvent e) {

			MyGame.xy[0] = e.getX();
			MyGame.xy[1] = e.getY();
		}
	}
	public class Mouse2 implements MouseWheelListener{ // Scrolling
	    public void mouseWheelMoved(MouseWheelEvent e) {
	       int notches = e.getWheelRotation();
	       scroll -= notches;
	       //System.out.println(notches);
	    }
	}

	public void paintComponent(Graphics g) { // main Game Loop
		g.setFont(custo);
		// System.out.println(run);
		if (run == 0) { // main menu
						// -----------------------------------------------------------------------------------------------------
			// g.drawImage(image1, 0, 0, this);
			funthing(g);
			if (dist(b1, xy) < 50) {
				run = 1;
			}
			if (dist(b2, xy) < 50) {
				run = 2;
			}
			if (dist(b3, xy) < 50) {
				run = 4;
			}
			xy[0] = 0;
			xy[1] = 0;
		} else if (run == 1) { // strategy maker slash programming menu
								// -----------------------------------------------------------------------------------------------------
			g.drawString("~ S T R A T E G Y   B U I L D E R ~", 300, 60);
			g.drawString("Lvl: " + level, 1440, 60);
			for (int i = 0; i < boxes.length; i += 1) {
				boxes[i].dropdown(boxes[i].open);
			}
			for (int i = 0; i < boxes.length; i += 1) {
				boxes[i].drawBox(dbg);
			}
			for (int i = 0; i < boxes.length; i += 1) {
				boxes[i].drawdropdown(dbg, boxes[i].open);
			}
			// TEST Button
			g.drawRect(1405, 74, 91, 672);
			g.fillRect(1405 + 9, 74 + 9, 92 - 18, 672 - 18);
			g.setColor(Color.BLACK);
			g.drawString("TEST", 1405 + 20, 400);
			g.setColor(Color.WHITE);
			if (xy[0] > 1405 && xy[1] > 74) {
				run = 5;
				win = -1;
				boxes2 = load(boxes2, "/" + level + ".txt", 1);
				makeForm(0, form[0]);
				makeForm(1, form[1]);
			}
			// Form Selector Draw
			g.drawRect(1150, 74, 205, 672);
			for (int i = 0; i < 5; i += 1) {
				g.drawRect(1150, 74 + 672 / 5 * i, 205, 672 / 5);
				g.drawString(formword[i], 1150 + 80, 74 + 672 / 5 * i + 70);
				if (i == form[0]) {
					g.drawRect(1150 + 5, 74 + 672 / 5 * i + 5, 205 - 10, 672 / 5 - 10);
				}
			} // Form Selector logic
			if (xy[0] > 1150 && xy[0] < 1355) {
				for (int i = 0; i < 5; i += 1) {
					if (xy[1] > 74 + 672 / 5 * i && xy[1] < 74 + 672 / 5 * (i + 1)) {
						form[0] = i;
					}
				}
			}
			// BACK button
			g.drawRect(3, 35, 100, 30);
			g.drawString("HOME", 3 + 22, 35 + 20);
			if (xy[0] > 3 && xy[0] < 103 && xy[1] < 65) {
				run = 0;
			}

			// SAVE and LOAD buttons
			g.drawString("SAVE:", 800 + 22, 35 + 15);
			g.drawString("LOAD:", 800 + 22, 35 + 35);
			for (int i = 0; i < 3; i += 1) { // SAVE
				g.drawRect(875 + i * 40, 35, 25, 16);
				if (xy[0] > 875 + i * 40 && xy[0] < 875 + i * 40 + 25 && xy[1] < 35 + 16) {
					save(boxes, "/file" + i + ".txt", 0);
					loadAvail[i]=1;
				}
			}
			for (int i = 0; i < 3; i += 1) {
				if (loadAvail[i]==1) {
					g.drawRect(875 + i * 40, 55, 25, 16);
				}
				if (xy[0] > 875 + i * 40 && xy[0] < 875 + i * 40 + 25 && xy[1] > 55 && xy[1] < 55 + 16) {
					boxes = load(boxes, "/file" + i + ".txt", 0);
				}
			}

			// CLEAR button
			g.drawRect(1000, 35, 100, 30);
			g.drawString("CLEAR", 1000 + 22, 35 + 20);
			if (xy[0] > 1000 && xy[0] < 1000 + 100 && xy[1] < 65) {
				boxes = clear(boxes, 0);
			}
			xy[0] = 0;
			xy[1] = 0;
		} else if (run == 2) { // player 1 strategy maker if playing 2 player
								// -----------------------------------------------------------------------------------------------------
			g.drawString("P 1  S T R A T E G Y   B U I L D E R", 280, 60);
			for (int i = 0; i < boxes.length; i += 1) {
				boxes[i].dropdown(boxes[i].open);
			}
			for (int i = 0; i < boxes.length; i += 1) {
				boxes[i].drawBox(dbg);
			}
			for (int i = 0; i < boxes.length; i += 1) {
				boxes[i].drawdropdown(dbg, boxes[i].open);
			}
			// TEST Button
			g.drawRect(1405, 74, 91, 672);
			g.fillRect(1405 + 9, 74 + 9, 92 - 18, 672 - 18);
			g.setColor(Color.BLACK);
			g.drawString("NEXT", 1405 + 20, 400);
			g.setColor(Color.WHITE);
			if (xy[0] > 1405 && xy[1] > 74) {
				run = 3;
			}
			// Form Selector Draw
			g.drawRect(1150, 74, 205, 672);
			for (int i = 0; i < 5; i += 1) {
				g.drawRect(1150, 74 + 672 / 5 * i, 205, 672 / 5);
				g.drawString(formword[i], 1150 + 80, 74 + 672 / 5 * i + 70);
				if (i == form[0]) {
					g.drawRect(1150 + 5, 74 + 672 / 5 * i + 5, 205 - 10, 672 / 5 - 10);
				}
			} // Form Selector logic
			if (xy[0] > 1150 && xy[0] < 1355) {
				for (int i = 0; i < 5; i += 1) {
					if (xy[1] > 74 + 672 / 5 * i && xy[1] < 74 + 672 / 5 * (i + 1)) {
						form[0] = i;
					}
				}
			}
			// BACK button
			g.drawRect(3, 35, 100, 30);
			g.drawString("HOME", 3 + 22, 35 + 20);
			if (xy[0] > 3 && xy[0] < 103 && xy[1] < 65) {
				run = 0;
			}
			// SAVE and LOAD buttons
			g.drawString("SAVE:", 800 + 22, 35 + 15);
			g.drawString("LOAD:", 800 + 22, 35 + 35);
			for (int i = 0; i < 3; i += 1) { // SAVE
				g.drawRect(875 + i * 40, 35, 25, 16);
				if (xy[0] > 875 + i * 40 && xy[0] < 875 + i * 40 + 25 && xy[1] < 35 + 16) {
					save(boxes, "/file" + i + ".txt", 0);
					loadAvail[i]=1;
				}
			}
			for (int i = 0; i < 3; i += 1) {
				if (loadAvail[i]==1) {
					g.drawRect(875 + i * 40, 55, 25, 16);
				}
				if (xy[0] > 875 + i * 40 && xy[0] < 875 + i * 40 + 25 && xy[1] > 55 && xy[1] < 55 + 16) {
					boxes = load(boxes, "/file" + i + ".txt", 0);
				}
			}
			// CLEAR button
			g.drawRect(1000, 35, 100, 30);
			g.drawString("CLEAR", 1000 + 22, 35 + 20);
			if (xy[0] > 1000 && xy[0] < 1000 + 100 && xy[1] < 65) {
				boxes = clear(boxes, 0);
			}

			xy[0] = 0;
			xy[1] = 0;

		} else if (run == 3) { // player 2 strategy maker
								// -----------------------------------------------------------------------------------------------------
			g.setColor(Color.ORANGE);
			g.drawString("P 2  S T R A T E G Y   B U I L D E R", 280, 60);
			for (int i = 0; i < boxes2.length; i += 1) {
				boxes2[i].dropdown(boxes2[i].open);
			}
			for (int i = 0; i < boxes2.length; i += 1) {
				boxes2[i].drawBox(dbg);
			}
			for (int i = 0; i < boxes2.length; i += 1) {
				boxes2[i].drawdropdown(dbg, boxes2[i].open);
			}
			// TEST Button
			g.drawRect(1405, 74, 91, 672);
			g.fillRect(1405 + 9, 74 + 9, 92 - 18, 672 - 18);
			g.setColor(Color.BLACK);
			g.drawString("TEST", 1405 + 20, 400);
			g.setColor(Color.WHITE);
			if (xy[0] > 1405 && xy[1] > 74) {
				win = -1;
				run = 5;
				makeForm(0, form[0]);
				makeForm(1, form[1]);
			}
			// Form Selector Draw
			g.drawRect(1150, 74, 205, 672);
			for (int i = 0; i < 5; i += 1) {
				g.drawRect(1150, 74 + 672 / 5 * i, 205, 672 / 5);
				g.drawString(formword[i], 1150 + 80, 74 + 672 / 5 * i + 70);
				if (i == form[1]) {
					g.drawRect(1150 + 5, 74 + 672 / 5 * i + 5, 205 - 10, 672 / 5 - 10);
				}
			} // Form Selector logic
			if (xy[0] > 1150 && xy[0] < 1355) {
				for (int i = 0; i < 5; i += 1) {
					if (xy[1] > 74 + 672 / 5 * i && xy[1] < 74 + 672 / 5 * (i + 1)) {
						form[1] = i;
					}
				}
			}
			// BACK button
			g.drawRect(3, 35, 100, 30);
			g.drawString("BACK", 3 + 22, 35 + 20);
			if (xy[0] > 3 && xy[0] < 103 && xy[1] < 65) {
				run = 2;
			}
			// SAVE and LOAD buttons
			g.drawString("SAVE:", 800 + 22, 35 + 15);
			g.drawString("LOAD:", 800 + 22, 35 + 35);
			for (int i = 0; i < 3; i += 1) { // SAVE
				g.drawRect(875 + i * 40, 35, 25, 16);
				if (xy[0] > 875 + i * 40 && xy[0] < 875 + i * 40 + 25 && xy[1] < 35 + 16) {
					save(boxes2, "/file" + i + ".txt", 1);
					loadAvail[i]=1;
				}
			}
			for (int i = 0; i < 3; i += 1) {
				if (loadAvail[i]==1) {
					g.drawRect(875 + i * 40, 55, 25, 16);
				}
				if (xy[0] > 875 + i * 40 && xy[0] < 875 + i * 40 + 25 && xy[1] > 55 && xy[1] < 55 + 16) {
					boxes2 = load(boxes2, "/file" + i + ".txt", 1);
				}
			}
			// CLEAR button
			g.drawRect(1000, 35, 100, 30);
			g.drawString("CLEAR", 1000 + 22, 35 + 20);
			if (xy[0] > 1000 && xy[0] < 1000 + 100 && xy[1] < 65) {
				boxes2 = clear(boxes2, 1);
			}

			xy[0] = 0;
			xy[1] = 0;
		} else if (run == 4) { //-------------------------------------------------------------------------------
			drawText("/HTP.txt", g);
			
			demobox[0].y=25*scroll + 355;
			demobox[1].y=25*scroll + 630;
			for (int j = 0; j < 2; j +=1) {
				demobox[j].x=200;
				for (int i = 0; i < demobox[j].ifs.length; i += 1) {
					demobox[j].ifpos[i][0] = demobox[j].x + 100 + i * 100;
					demobox[j].ifpos[i][1] = demobox[j].y + 20;
					demobox[j].ifpos2[i][0] = demobox[j].ifpos[i][0] - 10;
					demobox[j].ifpos2[i][1] = demobox[j].ifpos[i][1] + 25;
					demobox[j].neg[i] = false;
				}
				demobox[j].thenpos[0] =  demobox[j].x + 900;
				demobox[j].thenpos[1] = demobox[j].y + 40;
				
				demobox[j].drawBox(g);
			}
			
			//Home Button
			g.drawRect(3, 35, 100, 30);
			g.drawString("HOME", 3 + 22, 35 + 20);
			if (xy[0] > 3 && xy[0] < 103 && xy[1] < 65) {
				run = 0;
			}
			xy[0] = 0;
			xy[1] = 0;
		} else if (run == 5) { // actual gameplay

			// System.out.println(ang(ships.get(0).c, ships.get(1).c) + " " + ships.get(0).a
			// + " " + ships.get(0).team); // prints the angle from a ship to another and
			// the direction of that ship

			//long time = System.currentTimeMillis();
			//g.drawImage(image1, 0, 0, this);
			
			if (win != -1) {
				g.drawString(winWord[mode][win], 750, 300);
				g.drawString("c l i c k   t o   e x i t", 720, 330);
			} else {
				g.drawString("|  |  |  |  |  |      B A T T L E   I N   P R O G R E S S     |  |  |  |  |  |", 570, 50);
				g.drawString("c l i c k   t o   e x i t", 720, 70);
				System.out.println(alive[0]+" " + alive[1]);
			}
			for (int i = 0; i < ships.size(); i += 1) {
				ships.get(i).place = i;
				if (ships.get(i).team == 0) {
					g.setColor(Color.ORANGE);
				} else if (ships.get(i).team == 1) {
					g.setColor(Color.WHITE);
				}
				ships.get(i).drawShip(dbg);
				ships.get(i).action();

				// deletes ships that go out of bounds
				// System.out.println(ships.get(i).c[0] + " " + ships.get(i).c[1]);
				if ((ships.get(i).c[0] < 0 || ships.get(i).c[0] > 1500 || ships.get(i).c[1] < 0
						|| ships.get(i).c[1] > 750) && selected != i || ships.get(i).health < 0) { // death
					alive[ships.get(i).team] -= 1;
					ships.remove(i);
					if (i <= selected && selected != 0) {
						selected -= 1;
					}
					if (alive[0] == 0 && win == -1) {
						win = 1;
					} else if (alive[1] == 0 && win == -1) {
						win = 0;
						if (level < 5) {
							level+=1;
						}
					}
					
				}
			}

			g.setColor(Color.CYAN);
			if (!sim) {
				// System.out.println(ships.get(selected).health);
				g.drawOval((int) ships.get(selected).c[0] - 10, (int) ships.get(selected).c[1] - 10, 20, 20);
			}

			// System.out.println(dist(good.c, good2.rw));
			// System.out.println(ang(good.lw, good2.rw));
			// System.out.println(ships.size());

			if (xy[0] > 0) { // back to editor
				alive[0]=0;
				alive[1]=0;
				ships.clear();
				if (mode == 0) {
					run = 1;
				} else {
					run = 2;
				}
				
			}
			xy[0] = 0;
			xy[1] = 0;
		}
		repaint();

	}

	public void paint(Graphics g) {
		dbImage = createImage(getWidth(), getHeight());
		dbg = dbImage.getGraphics();
		paintComponent(dbg);

		g.drawImage(dbImage, 0, 0, this);
	}

	public static void main(String[] args) {
		new MyGame();
	}
}
