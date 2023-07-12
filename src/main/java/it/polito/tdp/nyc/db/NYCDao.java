package it.polito.tdp.nyc.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nyc.model.Hotspot;
import it.polito.tdp.nyc.model.NTA;

public class NYCDao {
	
	public List<Hotspot> getAllHotspot(){
		String sql = "SELECT * FROM nyc_wifi_hotspot_locations";
		List<Hotspot> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Hotspot(res.getInt("OBJECTID"), res.getString("Borough"),
						res.getString("Type"), res.getString("Provider"), res.getString("Name"),
						res.getString("Location"),res.getDouble("Latitude"),res.getDouble("Longitude"),
						res.getString("Location_T"),res.getString("City"),res.getString("SSID"),
						res.getString("SourceID"),res.getInt("BoroCode"),res.getString("BoroName"),
						res.getString("NTACode"), res.getString("NTAName"), res.getInt("Postcode")));
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

		return result;
	}
	
	public List<String> getHotspotBorough() {
		
		String sql = "SELECT DISTINCT Borough "
				+ "FROM nyc_wifi_hotspot_locations "
				+ "ORDER BY Borough ASC";
		
		List<String> result = new ArrayList<String>();
		
		try {
			
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				
				result.add(res.getString("Borough"));
			
			}
			
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
	}
	
	public List<NTA> getNTAbyBorough(String borough) {
		
		String sql = "SELECT DISTINCT n.NTACode, n.SSID "
				+ "FROM nyc_wifi_hotspot_locations AS n "
				+ "WHERE n.Borough =? "
				+ "ORDER BY n.NTACode";
		
		List<NTA> result = new ArrayList<NTA>();
		
		try {
			
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, borough);
			ResultSet res = st.executeQuery();

			String lastNtaCode = "";
			
			while (res.next()) {
				
				//ho 2 casi:
				if(res.getString("NTACode").equals(lastNtaCode)==false) {
					//1. mi da un nuovo NTA --> creo vertice
					Set<String> listaSSID = new HashSet<String>();
					listaSSID.add(res.getString("SSID"));
					NTA nta = new NTA(res.getString("NTACode"), listaSSID);
					result.add(nta);
					lastNtaCode=res.getString("NTACode"); /////////
				} else {
					//2. uguale a quello precedente-->aggiungo solo SSID al Set<>
					result.get(result.size()-1).getSSID().add(res.getString("SSID"));
				}
			
			}
			
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
}
