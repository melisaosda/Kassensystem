package datenspeicherung.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import fachkonzept.Produkt;

public class ProduktSpeicher
{
	private static final String KONFIG_DATEI = "db.properties";

	private final String url;
	private final String benutzer;
	private final String passwort;

	public ProduktSpeicher() throws IOException
	{
		Properties props = new Properties();
		try (FileInputStream in = new FileInputStream(KONFIG_DATEI))
		{
			props.load(in);
		}
		url = props.getProperty("url");
		benutzer = props.getProperty("user");
		passwort = props.getProperty("password", "");
	}

	private Connection verbinden() throws SQLException
	{
		return DriverManager.getConnection(url, benutzer, passwort);
	}

	public void speichern(Produkt p) throws SQLException
	{
		String sql = "INSERT INTO produkt (EAN, bezeichnung, nettopreis, basiseinheit, umsatzsteuersatz) VALUES (?, ?, ?, ?, ?)";
		try (Connection con = verbinden(); PreparedStatement ps = con.prepareStatement(sql))
		{
			ps.setString(1, p.getEAN());
			ps.setString(2, p.getBezeichnung());
			ps.setDouble(3, p.getNettoPreis());
			ps.setString(4, p.getBasiseinheit());
			ps.setInt(5, p.getUmsatzsteuersatz());
			ps.executeUpdate();
		}
	}

	public Produkt laden(String ean) throws SQLException
	{
		String sql = "SELECT EAN, bezeichnung, nettopreis, basiseinheit, umsatzsteuersatz FROM produkt WHERE EAN = ?";
		try (Connection con = verbinden(); PreparedStatement ps = con.prepareStatement(sql))
		{
			ps.setString(1, ean);
			try (ResultSet rs = ps.executeQuery())
			{
				return rs.next() ? zeile(rs) : null;
			}
		}
	}

	public List<Produkt> alleLaden() throws SQLException
	{
		String sql = "SELECT EAN, bezeichnung, nettopreis, basiseinheit, umsatzsteuersatz FROM produkt";
		List<Produkt> liste = new ArrayList<>();
		try (Connection con = verbinden(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery())
		{
			while (rs.next())
			{
				liste.add(zeile(rs));
			}
		}
		return liste;
	}

	public boolean aendern(Produkt p) throws SQLException
	{
		String sql = "UPDATE produkt SET bezeichnung = ?, nettopreis = ?, basiseinheit = ?, umsatzsteuersatz = ? WHERE EAN = ?";
		try (Connection con = verbinden(); PreparedStatement ps = con.prepareStatement(sql))
		{
			ps.setString(1, p.getBezeichnung());
			ps.setDouble(2, p.getNettoPreis());
			ps.setString(3, p.getBasiseinheit());
			ps.setInt(4, p.getUmsatzsteuersatz());
			ps.setString(5, p.getEAN());
			return ps.executeUpdate() > 0;
		}
	}

	public boolean loeschen(String ean) throws SQLException
	{
		String sql = "DELETE FROM produkt WHERE EAN = ?";
		try (Connection con = verbinden(); PreparedStatement ps = con.prepareStatement(sql))
		{
			ps.setString(1, ean);
			return ps.executeUpdate() > 0;
		}
	}

	private Produkt zeile(ResultSet rs) throws SQLException
	{
		return new Produkt(rs.getString("EAN"), rs.getString("bezeichnung"), rs.getDouble("nettopreis"),
				rs.getString("basiseinheit"), rs.getInt("umsatzsteuersatz"));
	}
}
