package mygame;

import java.util.ArrayList;

import mygame.Ship.bullet;

public class IFs {
	public static double dist (double[] a, double[] b) {
		double d = Math.sqrt((a[0]-b[0])*(a[0]-b[0])+(a[1]-b[1])*(a[1]-b[1]));
		return d;
	}
	
	//finds angle from first point to second point
	public static double ang (double[] a, double[] b) {
		double h = b[0]-a[0];
		double k = b[1]-a[1];
		double angle = Math.atan2(k,h);
		return angle;
	}
	//finds difference between angles
	public static double angDiff (double a, double b) {
		double angle = Math.abs(a-b);
		if (angle > Math.PI) {
			angle = 2*Math.PI - angle;
		}
		return angle;
	}
	public static double angDir (double a, double b) {
		double angle = (a-b);
		if (angle > Math.PI) {
			angle = -2*Math.PI + angle;
		} else if (angle < -Math.PI) {
			angle = 2*Math.PI + angle;
		}
		return angle;
	}
	
	public static boolean rightSide (double[] c, int team) {
		if (team == 0 && c[0] < 750) {
			return true;
		} else if (team == 1 && c[0] > 750){
			return true;
		}
		return false;
	}
	public static boolean justDo () {
		return true;
	}
	public static boolean anear (double[] c, ArrayList<Ship> s, int d, int t, int place) {
		for (int i = 0; i< s.size(); i+=1) {
			if (dist(c,s.get(i).c)<d && t + s.get(i).team != 1 && i != place) {
				return true;
			}
		}
		return false;
	}
	public static boolean enear (double[] c, ArrayList<Ship> s, int d, int t) {
		for (int i = 0; i< s.size(); i+=1) {
			if (dist(c,s.get(i).c)<d && t + s.get(i).team == 1) {
				return true;
			}
		}
		return false;
	}
	public static boolean asight (double[] c, double a, int t, ArrayList<Ship> s, int range, int place) {
		// if direction angle is close to angle between both ships
		//System.out.println(Math.abs(a-(ang(c,s[1].c))));
		for (int i = 0; i < s.size(); i += 1) {
			if (angDiff(a,ang(c,s.get(i).c))<(Math.PI/range) && t + s.get(i).team != 1 && i !=place) {
				//System.out.println("their team is: " + t + " my angle is " + a + " their angle is " + ang(c,s.get(i).c) + " the diff is: " + angDiff(a,ang(c,s.get(i).c)));
				//System.out.println("their team is: " + t + " " + Math.abs(a-(ang(c,s.get(i).c))) + " " + Math.PI/range);
				//System.out.println("their team is: " + s.get(i).team + " my team is: " + t);
				return true;
			}
		}
		
		return false;
	}
	public static boolean esight (double[] c, double a, int t, ArrayList<Ship> s, int range) {
		// if direction angle is close to angle between both ships
		//System.out.println(Math.abs(a-(ang(c,s[1].c))));
		for (int i = 0; i < s.size(); i += 1) {
			if (angDiff(a,ang(c,s.get(i).c))<(Math.PI/range) && t + s.get(i).team == 1) {
				//System.out.println("A " +i);
				return true;
			}
		}
		
		return false;
	}
	public static boolean spincw (double r) {
		if (r>0) {
			return true;
		}
		return false;
	}
	public static boolean nearedge (double[] c, int d) {
		if (c[0] < d || c[0] > 1500 - d || c[1] < d || c[1] > 750-d) {
			//System.out.println(dist(centre, c));
			return true;
		}
		return false;
	}
	public static boolean hurt (int h) {
		if (h<=80) {
			return true;
		}
		return false;
	}
	public static boolean senseL (double[] c, double a, int t, ArrayList<Ship> s, int range, int place) {
		// if direction angle is close to angle between both ships
		//System.out.println(Math.abs(a-(ang(c,s[1].c))));
		for (int i = 0; i < s.size(); i += 1) {
			if (angDiff(a,ang(c,s.get(i).c))<(Math.PI/range) && angDir(a,ang(c,s.get(i).c)) > 0 && i != place) {
				//System.out.println("B " + i);
				return true;
			}
		}
		
		return false;
	}
	public static boolean senseR (double[] c, double a, int t, ArrayList<Ship> s, int range, int place) {
		// if direction angle is close to angle between both ships
		//System.out.println(Math.abs(a-(ang(c,s[1].c))));
		for (int i = 0; i < s.size(); i += 1) {
			if (angDiff(a,ang(c,s.get(i).c))<(Math.PI/range) && angDir(a,ang(c,s.get(i).c)) < 0 && i != place) {
				//System.out.println("B " + i);
				return true;
			}
		}
		
		return false;
	}
	
	
	public static void hit (bullet[] b, ArrayList<Ship> s, int p) {
		int num = 0;
		for (int j = 0; j < b.length; j+=1) {
			for (int i = 0; i < s.size(); i+=1) {
				if (dist(b[j].pos, s.get(i).c) < 10 && i != p/*&& t + s.get(i).team == 1*/) {
					s.get(i).hit = 20;
					b[j].shot=false;
				}
			}
		}
	}
	
}
