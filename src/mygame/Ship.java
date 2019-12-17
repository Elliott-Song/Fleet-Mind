package mygame;

import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.ThreadLocalRandom;

public class Ship {
	
	public static int count;
	
	double slip = 150;
	public double[] rw = new double[2];
	public double[] lw = new double[2];
	public double[] n = new double[2];
	public double[] c = new double[2];
	public double[] v = new double[2];
	public double a, p, r, rp;
	
	int health;
	int place;
	int team;
	boolean bool;
	boolean[] bools = new boolean[21];
	
	int actime;
	
	int smo = 10;
	int delay;
	public double[][] s = new double[smo][4];
	public double sa[]= new double [smo];
	
	int bcount = 0;
	int btime = 0;
	bullet[] b = new bullet[5];
	int hit;
	
	public double dist(double[] a, double[] b) {
		double d = Math.sqrt((a[0] - b[0]) * (a[0] - b[0]) + (a[1] - b[1]) * (a[1] - b[1]));
		return d;
	}
	
	public class bullet {
		public double[] pos = new double[2];
		public double[] vel = new double[2];
		public double ang;
		boolean shot;
		
		bullet(double[] c, double an) {
			pos[0]=c[0];
			pos[1]=c[1];
			vel[0]=v[0]/1000;
			vel[1]=v[1]/1000;
			//System.out.println(vel[0] + " " +  v[0]);
			ang=an;
			shot = false;
		}
		private void drawbullet(Graphics g) {
			g.drawLine((int)pos[0], (int)pos[1], (int) (pos[0]+ 10 * Math.cos(ang)), (int) (pos[1]+ 10 * Math.sin(ang)));
			move();
		}
		private void move() {
			if (shot) {
				pos[0] += vel[0] + 2 * Math.cos(ang);
				pos[1] += vel[1] + 2 * Math.sin(ang);
				if (c[0]<0 || c[0] > 1400 || c[1] < 0 || c[1] > 750) {
					shot=false;
				}
			} else {
				pos[0] = c[0];
				pos[1] = c[1];
				ang = a;
			}
			
		}
	}

	// two things:
	// c for centre- and array of x and y coordinates
	// a for angle- the ship's direction
	public Ship(double[] centrePos, double angle, int team, int place) {
		health = 100;
		c = centrePos;
		a = angle;
		this.team=team;
		this.place = place;
		delay= 100;
		for (int i = 0; i < smo; i+=1) {
			sa[i]=(a + Math.PI);
			s[i][0]=(c[0] + 5 * Math.cos(a + Math.PI));
			s[i][1]=(c[1] + 5 * Math.sin(a + Math.PI));
			s[i][2]=sa[i];
			s[i][3]=0;
		}
		count +=1;
		for (int i = 0; i<b.length; i+=1) {
			b[i] = new bullet(c, a);
		}
		hit = 0;
		// p=2000;
		// rp=0.01;
	}
	
	private void smoke(Graphics g) {
		for (int i =  0; i < 10; i +=1) {
			sa[i]=(a + Math.PI + ThreadLocalRandom.current().nextDouble(-0.4, +0.4));
			s[i][3]+=1;
			if (s[i][3]>i*(i+2) || delay < 2) {
				s[i][0]=(c[0] + 10 * Math.cos(sa[i]));
				s[i][1]=(c[1] + 10 * Math.sin(sa[i]));
				s[i][2]=sa[i];
				s[i][3]=0;
			}
			if (delay <20) {
				s[i][0] += 1 * Math.cos(s[i][2]);
				s[i][1] += 1 * Math.sin(s[i][2]);
			}
			
			g.drawOval((int)s[i][0],(int)s[i][1],2,2);
		}
	}

	// is responsible for the movement and graphical representation of each ship
	public void drawShip(Graphics g) {
		// set right colour
		if (team == 0) {
			g.setColor(Color.WHITE);
		} else if (team == 1) {
			g.setColor(Color.ORANGE);
		}
		
		//bullets
		for (int i=0; i<5; i +=1) {
			if (b[i]!=null) {
				b[i].drawbullet(g);
			}
		}
		
		//smokey trail
		if (p==0) {
			delay+=1;
		} else {
			delay=0;
		}
		if (delay<100) {
			smoke(g);
		}

		rw[0] = (c[0] + 20 * Math.cos(a + Math.PI / 2));// right wing
		rw[1] = (c[1] + 20 * Math.sin(a + Math.PI / 2));
		lw[0] = (c[0] + 20 * Math.cos(a - Math.PI / 2));// left wing
		lw[1] = (c[1] + 20 * Math.sin(a - Math.PI / 2));
		n[0] = (c[0] + 10 * Math.cos(a));// nose
		n[1] = (c[1] + 10 * Math.sin(a));

		// draw ship out of lines
		
		g.drawLine((int) c[0], (int) c[1], (int) rw[0], (int) rw[1]);
		g.drawLine((int) c[0], (int) c[1], (int) lw[0], (int) lw[1]);
		g.drawLine((int) c[0], (int) c[1], (int) n[0], (int) n[1]);
		g.drawLine((int) rw[0], (int) rw[1], (int) n[0], (int) n[1]);
		g.drawLine((int) lw[0], (int) lw[1], (int) n[0], (int) n[1]);
		movement();
		IFs.hit(b, MyGame.ships, place);
		if (hit == 20) {
			health -= 10;
		}
		if (hit > 0) {
			hit -=1;
			g.fillOval((int) c[0]-hit/2, (int) c[1]-hit/2, hit, hit);
		}
		
	}

	private void movement() {
		// Velocity
		v[0] *= 1 - 1 / slip;
		v[1] *= 1 - 1 / slip;
		v[0] += p * Math.cos(a);
		v[1] += p * Math.sin(a);
		c[0] += v[0] / 1000;
		c[1] += v[1] / 1000;
		p=0;

		// rotation
		r *= 1 - 1 / slip;
		r += rp;
		a += r;
		rp = 0;
		
		if (a> Math.PI) {
			a-= 2*Math.PI;
		} else if ( a< -Math.PI) {
			a+= 2*Math.PI;
		}
	}
	
	public void boost() {
		// Velocity
		p+=125;
	}
	
	public void turn(int f) {
		// Velocity
		rp+=0.003*f;
	}
	
	// the fire function creates a new bullet out of five possible bullet spots. Once all five have been shot, there is a 500 tick delay until they can be shot again
	public void fire() {
		// Velocity
		b[bcount] = new bullet(c, a);
		b[bcount].shot = true;
		//System.out.println(bcount);
		bcount+=1;
		
		if (bcount>4) { //if all bullets have been shot
			bcount=0;
		}
		p-=50;
	}
	public void action(int then) {
		switch (then) {
		case 0:
			fire();
			break;
		case 1:
			boost();
			break;
		case 2:
			turn(-1);
			break;
		case 3:
			turn(1);
			break;
		default:
			// code block
		}
	}
	
	public void action() {
		
		bools[20] = IFs.justDo(); // always on
		bools[10] = IFs.rightSide(c, team); // if ship is team's home side
		bools[11] = IFs.anear(c,  MyGame.ships, 80, team, place); // if an ally is near
		bools[12] = IFs.enear(c,  MyGame.ships, 80, team); // if an enemy is near
		bools[13] = IFs.asight(c, a, team, MyGame.ships, 20, place); // if an ally is in sight
		bools[14] = IFs.esight(c, a, team, MyGame.ships, 20); // if an enemy is in sight
		bools[15] = IFs.spincw(r); // if ship is spinning clockwise
		bools[16] = IFs.nearedge(c, 200); //if ship is near the edge of the screen
		bools[17]= IFs.hurt(health); // if ship health is low
		bools[18]= IFs.senseL(c, a, team, MyGame.ships, 4, place); // if any ship is on left/front of ship
		bools[19]= IFs.senseR(c, a, team, MyGame.ships, 4, place); // if any ship is on right/front of ship
		
		for (int i = 0; i < 10; i+=1) { // negs
			bools[i]= !bools[i+10];
		}
		
		if (actime ==1 && MyGame.selected != place || actime == 1 && MyGame.sim) {
			if (team == 0) { // team 1 strategy -----------------------------------------------------
				
				for (int i = 0; i < MyGame.boxes.length; i+=1) { // my genius way of checking different input
					if (bools[MyGame.boxes[i].ifs[0]]
					&&bools[MyGame.boxes[i].ifs[1]]
					&&bools[MyGame.boxes[i].ifs[2]]
					&&bools[MyGame.boxes[i].ifs[3]]
					&&bools[MyGame.boxes[i].ifs[4]]
					&&bools[MyGame.boxes[i].ifs[5]]
					&&bools[MyGame.boxes[i].ifs[6]]
					&&bools[MyGame.boxes[i].ifs[7]]) {
						action(MyGame.boxes[i].then);
					}
				}
//				if (bools [5] && bools[14]) {
//					fire();
//				}
//				if (bools[9] && !bools[4] && !bools[2]) {
//					turn(-1);
//				} else {
//					turn(1);
//				}
//				if (!bools[4] || bools[5]) {
//					boost();
//				}
				
			
			}else if (team == 1) { // team 2 strategy -----------------------------------------------
				for (int i = 0; i < MyGame.boxes2.length; i+=1) { // my genius way of checking different input
					if (bools[MyGame.boxes2[i].ifs[0]]
					&&bools[MyGame.boxes2[i].ifs[1]]
					&&bools[MyGame.boxes2[i].ifs[2]]
					&&bools[MyGame.boxes2[i].ifs[3]]
					&&bools[MyGame.boxes2[i].ifs[4]]
					&&bools[MyGame.boxes2[i].ifs[5]]
					&&bools[MyGame.boxes2[i].ifs[6]]
					&&bools[MyGame.boxes2[i].ifs[7]]) {
						action(MyGame.boxes2[i].then);
					}
				}
				
			}
			
		}
		
		
		
		if (actime > 30) {
			actime =0;
		}
		actime +=1;
	}
}
