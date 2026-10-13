package fachkonzept;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Bon
{
	private final Date zeitpunkt;
	private final List<Position> positionen = new ArrayList<>();
	private final double gezahlt;

	public Bon(List<Position> pPositionen, double pGezahlt)
	{
		for (Position p : pPositionen)
		{
			if (!p.istGeloescht())
			{
				positionen.add(p);
			}
		}
		if (positionen.isEmpty())
		{
			throw new IllegalArgumentException("Ein Bon braucht mindestens eine Position.");
		}
		zeitpunkt = new Date();
		gezahlt = pGezahlt;
		if (gezahlt < getGesamtbetrag())
		{
			throw new IllegalArgumentException("Der gezahlte Betrag reicht nicht aus.");
		}
	}

	public Date getZeitpunkt()
	{
		return zeitpunkt;
	}

	public List<Position> getPositionen()
	{
		return new ArrayList<>(positionen);
	}

	public double getNetto()
	{
		double summe = 0;
		for (Position p : positionen)
		{
			summe += p.getNetto();
		}
		return runde(summe);
	}

	public double getSteuer()
	{
		double summe = 0;
		for (Position p : positionen)
		{
			summe += p.getSteuer();
		}
		return runde(summe);
	}

	public double getGesamtbetrag()
	{
		double summe = 0;
		for (Position p : positionen)
		{
			summe += p.getBrutto();
		}
		return runde(summe);
	}

	public double getGezahlt()
	{
		return gezahlt;
	}

	public double getRueckgeld()
	{
		return runde(gezahlt - getGesamtbetrag());
	}

	public String getDateiname()
	{
		return "bon_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(zeitpunkt) + ".txt";
	}

	public String erstelleText()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("KASSENBON\n");
		sb.append(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(zeitpunkt)).append("\n");
		sb.append("------------------------------------------\n");
		for (Position p : positionen)
		{
			Produkt produkt = p.getProdukt();
			sb.append(produkt.getBezeichnung()).append("\n");
			sb.append(String.format(Locale.GERMANY, "  %d x %.2f EUR (netto, %d%% MwSt) %18.2f EUR\n", p.getMenge(),
					produkt.getNettoPreis(), produkt.getUmsatzsteuersatz(), p.getBrutto()));
		}
		sb.append("------------------------------------------\n");
		sb.append(String.format(Locale.GERMANY, "Netto:     %10.2f EUR\n", getNetto()));
		sb.append(String.format(Locale.GERMANY, "MwSt:      %10.2f EUR\n", getSteuer()));
		sb.append(String.format(Locale.GERMANY, "Gesamt:    %10.2f EUR\n", getGesamtbetrag()));
		sb.append(String.format(Locale.GERMANY, "Gegeben:   %10.2f EUR\n", gezahlt));
		sb.append(String.format(Locale.GERMANY, "Rueckgeld: %10.2f EUR\n", getRueckgeld()));
		sb.append("\nVielen Dank fuer Ihren Einkauf!\n");
		return sb.toString();
	}

	private double runde(double wert)
	{
		return Math.round(wert * 100.0) / 100.0;
	}
}
