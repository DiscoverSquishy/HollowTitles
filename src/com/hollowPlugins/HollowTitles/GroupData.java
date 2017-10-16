package com.hollowPlugins.HollowTitles;

import java.util.List;

public class GroupData {

	private List<String> titles;
	private String format;
	private int rank;
	private String name;

	public GroupData(String groupName, int rank, String format, List<String> titles) {
		this.setName(groupName);
		this.setRank(rank);
		this.setFormat(format);
		this.setTitles(titles);
	}

	public List<String> getTitles() {
		return titles;
	}

	private void setTitles(List<String> titles) {
		this.titles = titles;
	}

	public String getFormat() {
		return format;
	}

	private void setFormat(String format) {
		this.format = format;
	}

	public int getRank() {
		return rank;
	}

	private void setRank(int rank) {
		this.rank = rank;
	}

	public String getName() {
		return name;
	}

	private void setName(String groupName) {
		this.name = groupName;
	}

	public boolean containsTitle(String playerTitle) {
		for (String title : titles) {
			if (title.equalsIgnoreCase(playerTitle)) {
				return true;
			}
		}
		return false;
	}

	public void addTitle(String newTitle) {
		titles.add(newTitle);
	}

	public void removeTitle(String oldTitle) {
		titles.remove(oldTitle);
	}

}
