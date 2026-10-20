package fachkonzept;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

public class Geldschublade
{
	// ESC/POS "Schublade oeffnen": ESC p Pin(0) Impuls-An(25 = 50 ms) Impuls-Aus(250 = 500 ms)
	public static final byte[] STANDARD_BEFEHL = { 0x1B, 0x70, 0x00, 0x19, (byte) 0xFA };

	private final String comPort;
	private final int baudrate;
	private final byte[] befehl;
	private boolean offen;

	// Ohne COM-Port laeuft die Schublade im Simulationsmodus (es wird nichts an Hardware gesendet)
	public Geldschublade(String pComPort, int pBaudrate, byte[] pBefehl)
	{
		comPort = (pComPort == null || pComPort.trim().isEmpty()) ? null : pComPort.trim().toUpperCase();
		if (comPort != null && !comPort.matches("COM\\d{1,3}"))
		{
			throw new IllegalArgumentException("Ungueltiger COM-Port: " + pComPort + " (erwartet z. B. COM3)");
		}
		baudrate = pBaudrate;
		befehl = pBefehl.clone();
	}

	public boolean istSimulation()
	{
		return comPort == null;
	}

	public void oeffne() throws IOException
	{
		if (comPort != null)
		{
			sendeBefehl();
		}
		offen = true;
	}

	public void schliesse()
	{
		offen = false;
	}

	public boolean istOffen()
	{
		return offen;
	}

	private void sendeBefehl() throws IOException
	{
		stelleSchnittstelleEin();
		try (OutputStream out = new FileOutputStream("\\\\.\\" + comPort))
		{
			out.write(befehl);
			out.flush();
		}
		catch (IOException e)
		{
			throw new IOException("COM-Port " + comPort + " ist nicht erreichbar (Schublade angesteckt? Anderes Programm belegt den Port?): "
					+ e.getMessage(), e);
		}
	}

	// Setzt die Baudrate mit dem Windows-Bordmittel "mode"
	private void stelleSchnittstelleEin() throws IOException
	{
		Process prozess = new ProcessBuilder("cmd", "/c", "mode", comPort, "BAUD=" + baudrate, "PARITY=n", "DATA=8", "STOP=1")
				.redirectErrorStream(true).start();
		try
		{
			if (!prozess.waitFor(5, TimeUnit.SECONDS))
			{
				prozess.destroyForcibly();
				throw new IOException("Einstellung von " + comPort + " dauerte zu lange.");
			}
		}
		catch (InterruptedException e)
		{
			Thread.currentThread().interrupt();
			throw new IOException("Einstellung von " + comPort + " wurde unterbrochen.", e);
		}
		if (prozess.exitValue() != 0)
		{
			throw new IOException("COM-Port " + comPort + " konnte nicht eingestellt werden (Port vorhanden?).");
		}
	}
}
