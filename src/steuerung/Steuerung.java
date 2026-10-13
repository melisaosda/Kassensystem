package steuerung;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import datenspeicherung.db.ProduktSpeicher;
import fachkonzept.Bon;
import fachkonzept.Position;
import fachkonzept.Produkt;

public class Steuerung
{
	private static final String BON_ORDNER = "bons";

	private final ProduktSpeicher speicher;
	private final List<Position> positionen = new ArrayList<>();
	private double gegeben;
	private boolean bezahlt;

	public Steuerung() throws IOException
	{
		speicher = new ProduktSpeicher();
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
		bezahlt = false;
		return position;
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
