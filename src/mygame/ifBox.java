package mygame;

import java.awt.Color;
import java.awt.Graphics;

public class ifBox {
	public static double dist(double[] a, double[] b) {
		double d = Math.sqrt((a[0] - b[0]) * (a[0] - b[0]) + (a[1] - b[1]) * (a[1] - b[1]));
		return d;
	}

	public int open; // if there are any open selection window things
	int[] ifs = new int[8];
	boolean[] neg = new boolean[ifs.length];
	double[][] ifpos = new double[ifs.length][2];
	double[][] ifpos2 = new double[ifs.length][2];
	int then;
	double[] thenpos = new double[2];
	int flag;
	public static int no = 0;
	static String[] thenword = { "Fire", "Boost", "Turn Left", "Turn Right", "Do Nothing" };
	static String[] ifword = { "On initial side", "An ally is near", "An enemy is near", "An ally is in sight", "An enemy is in sight", 
			"Spinning clockwise", "Near edge", "Health is low", "A ship is sensed on Left", "A ship is sensed on Right", "True"};

	int place;
	int x, y;
	
	public static Color grey = new Color(30, 30, 30);
	public static Color russet = new Color(40, 25, 20);
	

	public ifBox(int place) {
		
		open = -1;
		then = 4;
		this.place = place;
		x = 4;
		y = 80 + (660) / 8 * place;
		thenpos[0] = 900;
		thenpos[1] = y + 40;

		for (int i = 0; i < ifs.length; i += 1) {
			ifs[i] = 20;
			ifpos[i][0] = 100 + i * 100;
			ifpos[i][1] = y + 20;
			ifpos2[i][0] = ifpos[i][0] - 10;
			ifpos2[i][1] = ifpos[i][1] + 25;
			neg[i] = false;
		}
	}

	public void dropdown(int open) { // drop down menu function
		
		int bsize = 40;
		if (open == 8) {
			int midx = (int) thenpos[0] + 50;
			int midy = (int) thenpos[1] + place * -bsize * 2 / 3;
			// clickboxes
			if (MyGame.xy[0] > midx && MyGame.xy[0] < midx + 200 && MyGame.xy[1] > midy && MyGame.xy[1] < midy + bsize * 5) {
				//this.flag = -1;
				no = -8;
				for (int i = 0; i < 5; i += 1) {
					if (MyGame.xy[1] > (int) i * bsize + 2 + midy && MyGame.xy[1] < (int) (i + 1) * bsize + 2 + midy) {
						then = i;
						
					}
				}
			}
		} else if (open > -1) {
			int midx = 150 + open * 100;
			int midy = (int) thenpos[1] + place * -bsize * 3 / 2;
			// clickboxes
			if (MyGame.xy[0] > midx && MyGame.xy[0] < midx + 210 && MyGame.xy[1] > midy && MyGame.xy[1] < midy + bsize * 11) {
				//this.flag = -1;
				no = -9;
				for (int i = 0; i < 11; i += 1) {
					if (MyGame.xy[1] > (int) i * bsize + 2 + midy && MyGame.xy[1] < (int) (i + 1) * bsize + 2 + midy) {
						ifs[open] = 10+i;
						
					}
				}
			}
		}

	}
public void drawdropdown(Graphics g, int open) { // drop down menu function
		
		int bsize = 40;
		if (open == 8) {
			int midx = (int) thenpos[0] + 50;
			int midy = (int) thenpos[1] + place * -bsize * 2 / 3;
			g.drawRect(midx, midy, 200, bsize * 5);
			for (int i = 0; i < 5; i += 1) {
				if (i == then) {
					g.setColor(Color.lightGray);
				}
				g.fillRect(midx + 3, (int) i * bsize + 2 + midy, 200 - 6, bsize - 6);
				g.setColor(Color.BLACK);
				g.drawString(thenword[i], midx + 5, (int) i * bsize + 20 + midy);
				g.setColor(Color.WHITE);
			}
			g.drawLine(midx + 205, (int) then * bsize + 2 + midy, midx + 205, (int) (then +1) * bsize - 4 + midy);
			g.drawLine((int) thenpos[0], (int) thenpos[1], midx, midy);
			g.drawLine((int) thenpos[0], (int) thenpos[1], midx, midy + 5 * bsize);

		} else if (open > -1) {
			int midx = 150 + open * 100;
			int midy = (int) thenpos[1] + place * -bsize * 3 / 2;
			g.drawRect(midx, midy, 210, bsize * 11);
			for (int i = 0; i < 11; i += 1) {
				if (i == ifs[open]-10) {
					g.setColor(Color.lightGray);
				}
				g.fillRect(midx + 3, (int) i * bsize + 2 + midy, 210 - 6, bsize - 6);
				g.setColor(Color.BLACK);
				g.drawString(i + " " + ifword[i], midx + 5, (int) i * bsize + 20 + midy);
				g.setColor(Color.WHITE);
			}
			g.drawLine(midx + 215, (int) (ifs[open]-10) * bsize + 2 + midy, midx + 215, (int) (ifs[open]-10 +1) * bsize - 4 + midy);
			g.drawLine((int) ifpos[open][0], (int) ifpos[open][1], midx, midy);
			g.drawLine((int) ifpos[open][0], (int) ifpos[open][1], midx, midy + 11 * bsize);
			
		}

	}

	public void drawBox(Graphics g) {
		
		// System.out.println(open);
		if (MyGame.run == 1 || MyGame.run == 2) {
			g.setColor(grey);
		} else if (MyGame.run == 3 || MyGame.run == 4) {
			g.setColor(russet);
		}
		g.fillRect(x, y, 1100, 660 / 8);
		g.setColor(Color.WHITE);
		g.drawRect(x, y, 1100, 660 / 8);
		g.drawString(Integer.toString(place), x+10, y + 50);
		// Big Boy Circle
		g.drawOval((int) thenpos[0] - 50, (int) thenpos[1] - 30, 100, 60);
		g.drawString(thenword[then], (int) thenpos[0] - 40, (int) thenpos[1] + 10);
		g.drawLine((int) thenpos[0] - 60, y+10, (int) thenpos[0] - 60, y+ 675 / 8 - 10);

		flag = 0;
		if (no < 0) {
			no +=1;
		}
		if (dist(MyGame.xy, thenpos) < 42 && open != 8 && no ==0) {
			open = 8;
			flag = 1;
		} else {
			for (int i = 0; i < ifpos.length; i += 1) {
				if (dist(MyGame.xy, ifpos[i]) < 18 && open != i && no == 0) {
					open = i;
					flag = 1;
				}
			}
		}
		
		//dropdown(g, open);
		if (flag < 1 && MyGame.xy[0] > 0) {
			open = -1;
		}

		for (int i = 0; i < ifpos.length; i += 1) {
			// Big Circles
			g.drawOval((int) ifpos[i][0] - 20, (int) ifpos[i][1] - 15, 40, 30);
			if (ifs[i] < 20) {
				g.drawString(Integer.toString(ifs[i] % 10), (int) ifpos[i][0] - 10, (int) ifpos[i][1]);
			} else {
				g.drawString("- -", (int) ifpos[i][0] - 10, (int) ifpos[i][1]);
			}
			// Little Circles
			if (ifs[i] > 9) {
				neg[i] = false;
			} else {
				neg[i] = true;
			}
			g.drawOval((int) ifpos2[i][0] - 10, (int) ifpos2[i][1] - 8, 20, 16);
			if (neg[i]) {
				g.fillOval((int) ifpos2[i][0] - 10, (int) ifpos2[i][1] - 8, 20, 16);
			}
			if (dist(MyGame.xy, ifpos2[i]) < 10 && no ==0) {
				if (neg[i]) {
					ifs[i] += 10;
				} else if (!neg[i] && ifs[i] < 20 && no ==0) {
					ifs[i] -= 10;
				}
				System.out.println(ifs[i]);
			}

		}
		
	}
}
