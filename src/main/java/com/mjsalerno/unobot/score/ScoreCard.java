package com.mjsalerno.unobot.score;

import java.io.Serializable;

/**
 * 
 * Tracks the scores for a single player
 *
 */
public class ScoreCard implements Comparable<ScoreCard>, Serializable{
	
	private static final long serialVersionUID = 1L;

	
	private String name;
	private int score;
	private int wins;
	private int losses;
	
	public ScoreCard() {		
	}
	
	public ScoreCard(String name) {
		this.name = name;
	}
	
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getScore() {
		return score;
	}


	public void addScore(int score) {
		this.score += score;
	}


	public int getWins() {
		return wins;
	}


	public void incrementWins() {
		this.wins++;
	}


	public int getLosses() {
		return losses;
	}


	public void incrementLosses() {
		this.losses++;
	}
	
	

	////////////////////////////

	@Override
	public int compareTo(ScoreCard o) {
		//by comparing with o.score first then we will get in descending order
		return Integer.compare(o.score, this.score);
	}


	@Override
	public int hashCode() {
		return name.hashCode();
	}


	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof ScoreCard))
			return false;
		ScoreCard other = (ScoreCard) obj;
		
		return name.equals(other.name);
	}

	@Override
	public String toString() {
		return name + ": " + score;
	}
	
	public String toRankString() {
		return name + ": " + score + ",  " + wins+" wins, " + losses + " losses";
	}
	
	
	
	
}
