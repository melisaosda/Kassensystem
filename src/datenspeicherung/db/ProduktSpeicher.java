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

	private Produkt zeile(ResultSet rs) throws SQLException
	{
		return new Produkt(rs.getString("EAN"), rs.getString("bezeichnung"), rs.getDouble("nettopreis"),
				rs.getString("basiseinheit"), rs.getInt("umsatzsteuersatz"));
	}
}
