package org.zezutom.wordcloud.domain;

public class FilterTag {

	private String text;

	public FilterTag(String text) {
		super();
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public boolean textEquals(String text) {
		return normalize(getText()).equals(normalize(text));
	}
	
	public boolean textContains(String text) {
		return normalize(getText()).contains(normalize(text));
	}
	
	private String normalize(String value) {
		return (value == null) ? "" : value.trim().toLowerCase();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FilterTag other = (FilterTag) obj;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}	
		
}
