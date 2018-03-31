package fr.meconnu.calc;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Geo {
	
	static double toRadian(float x) {
		return x * Math.PI / 180.0;
	}
	
/*	public static float Distance2(Vector2 pos1, Vector2 pos2) {
		  Double e = Math.toRadians(pos1.x);
		  Double f = Math.toRadians(pos1.y);
		  Double g = Math.toRadians(pos2.x);
		  Double h = Math.toRadians(pos2.y);
		  Double angle=(Math.cos(e)*Math.cos(g)*Math.cos(f)*Math.cos(h)+Math.cos(e)*Math.sin(f)*Math.cos(g)*Math.sin(h)+Math.sin(e)*Math.sin(g));
		  return (float) (6371000f*Math.acos(angle));
		 }
	
	public static float Distance1(Vector2 pos1, Vector2 pos2) {
		  Double e = Math.toRadians(pos1.x);
		  Double f = Math.toRadians(pos2.x);
		  Double g = Math.toRadians(pos1.y);
		  Double h = Math.toRadians(pos2.y);
		  Double angle=(Math.cos(e)*Math.cos(f)*Math.cos(h - g)+Math.sin(e)*Math.sin(f));
		  return (float) (6371000f*Math.acos(angle));
		 }
*/
	public static float Distance(Vector3 position, Vector3 oldposition) {
		/*  return Math.sqrt((pos2.x-pos1.x)*(pos2.x-pos1.x)+(pos2.y-pos1.y)*(pos2.y-pos1.y))* 111160;*/
		return position.dst(oldposition)*111160f;
		 }
	
	public static float Distance2D(Vector2 position, Vector2 oldposition) {
		/*  return Math.sqrt((pos2.x-pos1.x)*(pos2.x-pos1.x)+(pos2.y-pos1.y)*(pos2.y-pos1.y))* 111160;*/
		return position.dst(oldposition)*111160f;
		 }
	
	public static float Distance2D(Vector3 position, Vector3 oldposition) {
		/*  return Math.sqrt((pos2.x-pos1.x)*(pos2.x-pos1.x)+(pos2.y-pos1.y)*(pos2.y-pos1.y))* 111160;*/
		Vector2 position2D=new Vector2(position.x,position.y);
		Vector2 oldposition2D=new Vector2(position.x,position.y);
		return position2D.dst(oldposition2D)*111160f;
		 }
	
	public static float Angle(Vector2 pos1, Vector2 pos2) {
		Vector2 temp=new Vector2(pos2.x,pos2.y);
		return temp.sub(pos1).angle();
	}
	

}
