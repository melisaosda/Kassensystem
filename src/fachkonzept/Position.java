package fachkonzept;

public class Position
{
	private Produkt produkt;
	private int menge;
	private boolean bestaetigt;
	private boolean geloescht;

	public void erfassePosition(Produkt pProdukt, int pMenge)
	{
		if (pProdukt == null)
		{
			throw new IllegalArgumentException("Produkt darf nicht leer sein.");
		}
		pruefeMenge(pMenge);
		produkt = pProdukt;
		menge = pMenge;
		bestaetigt = false;
		geloescht = false;
	}

	public void bearbeitePosition(int pMenge)
	{
		if (bestaetigt || geloescht)
		{
			throw new IllegalStateException("Bestaetigte oder geloeschte Positionen koennen nicht bearbeitet werden.");
		}
		pruefeMenge(pMenge);
		menge = pMenge;
	}

	public void loeschePosition()
	{
		geloescht = true;
	}

	public void bestaetigePosition()
	{
		if (produkt == null || geloescht)
		{
			throw new IllegalStateException("Position kann nicht bestaetigt werden.");
		}
		bestaetigt = true;
	}

	public Produkt getProdukt()
	{
		return produkt;
	}

	public int getMenge()
	{
		return menge;
	}

	public boolean istBestaetigt()
	{
		return bestaetigt;
	}

	public boolean istGeloescht()
	{
		return geloescht;
	}

	public double getNetto()
	{
		return runde(produkt.getNettoPreis() * menge);
	}

	public double getSteuer()
	{
		return runde(getNetto() * produkt.getUmsatzsteuersatz() / 100.0);
	}

	public double getBrutto()
	{
		return runde(getNetto() + getSteuer());
	}

	private void pruefeMenge(int pMenge)
	{
		if (pMenge < 1)
		{
			throw new IllegalArgumentException("Menge muss mindestens 1 sein.");
		}
	}

	private double runde(double wert)
	{
		return Math.round(wert * 100.0) / 100.0;
	}
}
