package fachkonzept;

public class Produkt
{
	private String EAN;
	private String bezeichnung;
	private double nettopreis;
	private String basiseinheit;
	private int umsatzsteuersatz;
	
	public Produkt(String pEAN)
	{
		pEAN = EAN;
	}
	
	public String getEAN()
	{
		return EAN;
	}
	
	public String getBezeichnung()
	{
		return bezeichnung;
	}
	
	public double getNettoPreis()
	{
		return nettopreis;
	}
	
	public String getBasiseinheit()
	{
		return basiseinheit;
	}
	
	public int getUmsatzsteuersatz()
	{
		return umsatzsteuersatz;
	}
}
