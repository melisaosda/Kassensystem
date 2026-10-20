package steuerung;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import datenspeicherung.db.ProduktSpeicher;
import fachkonzept.Bon;
import fachkonzept.Geldschublade;
import fachkonzept.Position;
import fachkonzept.Produkt;

public class Steuerung
{
	private static final String BON_ORDNER = "bons";
	private static final String KASSEN_KONFIG = "kasse.properties";

	private final ProduktSpeicher speicher;
	private final Geldschublade geldschublade;
	private final List<Position> positionen = new ArrayList<>();
	private double gegeben;
	private boolean bezahlt;

	public Steuerung() throws IOException
	{
		speicher = new ProduktSpeicher();
		geldschublade = erzeugeGeldschublade();
	}

	// Die Datei ist optional; ohne sie laeuft die Geldschublade im Simulationsmodus
	private static Geldschublade erzeugeGeldschublade() throws IOException
	{
		if (!Files.exists(Paths.get(KASSEN_KONFIG)))
		{
			return new Geldschublade(null, 9600, Geldschublade.STANDARD_BEFEHL);
		}
		Properties props = new Properties();
		try (FileInputStream in = new FileInputStream(KASSEN_KONFIG))
		{
			props.load(in);
		}
		try
		{
			int baud = Integer.parseInt(props.getProperty("schublade.baud", "9600").trim());
			return new Geldschublade(props.getProperty("schublade.com"), baud,
					leseBefehl(props.getProperty("schublade.befehl")));
		}
		catch (IllegalArgumentException e)
		{
			throw new IOException("Fehler in " + KASSEN_KONFIG + ": " + e.getMessage(), e);
		}
	}

	// Befehl als Hex-Bytes, z. B. "1B 70 00 19 FA"; leer = ESC/POS-Standardbefehl
	private static byte[] leseBefehl(String hex)
	{
		if (hex == null || hex.trim().isEmpty())
		{
			return Geldschublade.STANDARD_BEFEHL;
		}
		String[] teile = hex.trim().split("[\\s,]+");
		byte[] befehl = new byte[teile.length];
		for (int i = 0; i < teile.length; i++)
		{
			befehl[i] = (byte) Integer.parseInt(teile[i], 16);
		}
		return befehl;
	}

	public Produkt rufeProduktdaten(String ean) throws SQLException
	{
		return speicher.laden(ean);
	}

	public Position leseBarcode(String ean) throws SQLException
	{
		return leseBarcode(ean, 1);
	}

	public Position leseBarcode(String ean, int menge) throws SQLException
	{
		Produkt produkt = rufeProduktdaten(ean);
		if (produkt == null)
		{
			throw new IllegalArgumentException("Kein Produkt mit der EAN " + ean + " gefunden.");
		}
		Position position = new Position();
		position.erfassePosition(produkt, menge);
		position.bestaetigePosition();
		positionen.add(position);
		zahlungVerwerfen();
		return position;
	}

	public List<Position> getPositionen()
	{
		List<Position> aktive = new ArrayList<>();
		for (Position p : positionen)
		{
			if (!p.istGeloescht())
			{
				aktive.add(p);
			}
		}
		return aktive;
	}

	public void storniere(Position position)
	{
		position.loeschePosition();
		zahlungVerwerfen();
	}

	public void zuruecksetzen()
	{
		positionen.clear();
		gegeben = 0;
		bezahlt = false;
		geldschublade.schliesse();
	}

	public Path schliesseTransaktionAb() throws IOException
	{
		Path bon = druckeBon();
		zuruecksetzen();
		return bon;
	}

	public double getGezahlt()
	{
		return bezahlt ? gegeben : 0;
	}

	public double getRueckgeld()
	{
		return bezahlt ? Math.round((gegeben - getGesamtbetrag()) * 100.0) / 100.0 : 0;
	}

	public boolean istGeldschubladeOffen()
	{
		return geldschublade.istOffen();
	}

	public boolean istGeldschubladeSimulation()
	{
		return geldschublade.istSimulation();
	}

	private void zahlungVerwerfen()
	{
		bezahlt = false;
		geldschublade.schliesse();
	}

	public double getGesamtbetrag()
	{
		double summe = 0;
		for (Position p : positionen)
		{
			if (!p.istGeloescht())
			{
				summe += p.getBrutto();
			}
		}
		return Math.round(summe * 100.0) / 100.0;
	}

	public double bekommeZahlung(double betrag)
	{
		double gesamt = getGesamtbetrag();
		if (gesamt <= 0)
		{
			throw new IllegalStateException("Es gibt nichts zu bezahlen.");
		}
		if (betrag < gesamt)
		{
			throw new IllegalArgumentException("Zahlung reicht nicht aus. Noch offen: "
					+ String.format(Locale.GERMANY, "%.2f", gesamt - betrag) + " EUR");
		}
		gegeben = betrag;
		bezahlt = true;
		try
		{
			geldschublade.oeffne();
		}
		catch (IOException e)
		{
			throw new IllegalStateException("Die Zahlung wurde erfasst, aber die Geldschublade konnte nicht geoeffnet werden: "
					+ e.getMessage());
		}
		return Math.round((betrag - gesamt) * 100.0) / 100.0;
	}

	public Path druckeBon() throws IOException
	{
		if (!bezahlt)
		{
			throw new IllegalStateException("Der Bon kann erst nach der Zahlung gedruckt werden.");
		}
		Bon bon = new Bon(positionen, gegeben);
		Files.createDirectories(Paths.get(BON_ORDNER));
		Path datei = Paths.get(BON_ORDNER, bon.getDateiname());
		Files.write(datei, bon.erstelleText().getBytes(StandardCharsets.UTF_8));
		return datei;
	}
}
