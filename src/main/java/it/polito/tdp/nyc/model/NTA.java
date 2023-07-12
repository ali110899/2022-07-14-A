package it.polito.tdp.nyc.model;

import java.util.Objects;
import java.util.Set;

public class NTA {

	private String NTACode;
	private Set<String> SSID;
	
	public NTA(String nTACode, Set<String> sSID) {
		super();
		NTACode = nTACode;
		SSID = sSID;
	}

	public String getNTACode() {
		return NTACode;
	}

	public Set<String> getSSID() {
		return SSID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(NTACode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NTA other = (NTA) obj;
		return Objects.equals(NTACode, other.NTACode);
	}

	@Override
	public String toString() {
		return "NTA [NTACode=" + NTACode + ", SSID=" + SSID + "]";
	}

	
	
}
