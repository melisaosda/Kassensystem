package steuerung;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import datenspeicherung.db.ProduktSpeicher;
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
		Files.createDirectories(Paths.get(BON_ORDNER));
		Date jetzt = new Date();
		Path datei = Paths.get(BON_ORDNER, "bon_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(jetzt) + ".txt");
		Files.write(datei, erstelleBon(jetzt).getBytes(StandardCharsets.UTF_8));
		return datei;
	}

	private String erstelleBon(Date zeit)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("KASSENBON\n");
		sb.append(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(zeit)).append("\n");
		sb.append("------------------------------------------\n");
		double netto = 0;
		double steuer = 0;
		for (Position p : positionen)
		{
			if (p.istGeloescht())
			{
				continue;
			}
			Produkt produkt = p.getProdukt();
			sb.append(String.format(Locale.GERMANY, "%s\n", produkt.getBezeichnung()));
			sb.append(String.format(Locale.GERMANY, "  %d x %.2f EUR (netto, %d%% MwSt) %18.2f EUR\n", p.getMenge(),
					produkt.getNettoPreis(), produkt.getUmsatzsteuersatz(), p.getBrutto()));
			netto += p.getNetto();
			steuer += p.getSteuer();
		}
		sb.append("------------------------------------------\n");
		sb.append(String.format(Locale.GERMANY, "Netto:     %10.2f EUR\n", netto));
		sb.append(String.format(Locale.GERMANY, "MwSt:      %10.2f EUR\n", steuer));
		sb.append(String.format(Locale.GERMANY, "Gesamt:    %10.2f EUR\n", getGesamtbetrag()));
		sb.append(String.format(Locale.GERMANY, "Gegeben:   %10.2f EUR\n", gegeben));
		sb.append(String.format(Locale.GERMANY, "Rueckgeld: %10.2f EUR\n", gegeben - getGesamtbetrag()));
		sb.append("\nVielen Dank fuer Ihren Einkauf!\n");
		return sb.toString();
	}
}
