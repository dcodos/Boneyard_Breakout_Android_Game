package com.virginia.edu.cs2110_group22;

import java.util.ArrayList;

import android.graphics.Rect;

public class CollisionBox {

	private double x;
	private double y;
	private double w;
	private double h;
	Rect box;
	
	
	public CollisionBox(double x2, double y2, int w, int h){
		this.x = x2;
		this.y = y2;
		this.w = w;
		this.h = h;
		box = new Rect((int) this.x, (int) this.y,  (int) (this.x + this.w),  (int) (this.y + this.h));
		
	}
	
	public boolean Intersects(CollisionBox other){
		if(box.intersect(other.getBox())){
			return true;
		}
		return false;
	}
	
	public Rect getBox(){
		return this.box;
	}
	
	public double getx(){
		return this.x;
	}
	
	public double gety(){
		return this.y;
	}
	
	public double getw(){
		return this.w;
	}
	
	public double geth(){
		return this.h;
	}
	
	public void setx(double x){
		this.x = x;
		box.left = (int) x;
		box.right = (int) (x + this.w);
		
	}
	
	public void sety(double y){
		this.y = y;
		box.top = (int) y;
		box.bottom = (int) (y + this.h);
	}
	
	public boolean topIntersects(CollisionBox other){
		ArrayList<Integer> topLine = new ArrayList<Integer>();
		for(int i = 0; i < this.w; ++i){
			topLine.add((int)(this.x + i));
		}
		ArrayList<Integer> otherBot = new ArrayList<Integer>();
		for(int j = 0; j < other.getw(); ++j){
			otherBot.add((int)(other.getx() + j));
		}
		int count = 0;
		if(this.y < (other.gety() + other.geth()) && (this.y + this.h) > (other.gety() + other.geth())){
			for(int k = 0; k < topLine.size(); ++k){
				if(otherBot.contains(topLine.get(k))){
					++count;
				}
			}
		}
		if(count > 10){
			if(this.rightIntersects(other) || this.leftIntersects(other)){
				return false;
			}
			return true;
		}
		return false;
	}
	
	public boolean bottomIntersects(CollisionBox other){
		ArrayList<Integer> topLine = new ArrayList<Integer>();
		for(int i = 0; i < this.w; ++i){
			topLine.add((int)(this.x + i));
		}
		ArrayList<Integer> otherBot = new ArrayList<Integer>();
		for(int j = 0; j < other.getw(); ++j){
			otherBot.add((int)(other.getx() + j));
		}
		int count = 0;
		if((this.y + this.h) > other.gety() && this.y < other.gety()){
			for(int k = 0; k < topLine.size(); ++k){
				if(otherBot.contains(topLine.get(k))){
					++count;
				}
			}
		}
		if(count > 10){
			if(this.rightIntersects(other) || this.leftIntersects(other)){
				return false;
			}
			return true;
		}
		return false;
	}
	public boolean leftIntersects(CollisionBox other){
		ArrayList<Integer> leftLine = new ArrayList<Integer>();
		for(int i = 0; i < this.h; ++i){
			leftLine.add((int)(this.y + i));
		}
		ArrayList<Integer> otherRight = new ArrayList<Integer>();
		for(int j = 0; j < other.geth(); ++j){
			otherRight.add((int)(other.gety() + j));
		}
		int count = 0;
		if(this.x < (other.getx() + other.getw()) && (this.x + this.w) > (other.getx() + other.getw())){
			for(int k = 0; k < leftLine.size(); ++k){
				if(otherRight.contains(leftLine.get(k))){
					++count;
				}
			}
		}
		if(count > 10){
			return true;
		}
		return false;
	}
	public boolean rightIntersects(CollisionBox other){
		ArrayList<Integer> leftLine = new ArrayList<Integer>();
		for(int i = 0; i < this.h; ++i){
			leftLine.add((int)(this.y + i));
		}
		ArrayList<Integer> otherRight = new ArrayList<Integer>();
		for(int j = 0; j < other.geth(); ++j){
			otherRight.add((int)(other.gety() + j));
		}
		int count = 0;
		if((this.x + this.w) > other.getx() && this.x < other.getx()){
			for(int k = 0; k < leftLine.size(); ++k){
				if(otherRight.contains(leftLine.get(k))){
					++count;
				}
			}
		}
		if(count > 10){
			return true;
		}
		return false;
	}
}
