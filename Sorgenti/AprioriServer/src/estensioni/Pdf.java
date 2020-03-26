
package estensioni;

import java.awt.image.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;


/**
 * Si occupa di creare un file pdf e di salvarvi l'archivio di coppie el tipo &lt;pattern frequenti - {insieme 
 * di regole d'associazione confidenti}&gt; precedentemente estratto dal database e salvato preventivamente 
 * dal server in un file dati apposito.
 *
 *@author Gruppo Map formato da Donato Lucente, Antonio Ricchetti, Gianluca Colaianni
 */
public class Pdf {

	/**
	 * Attributo che delinea il nome di percorso ( es. C:\Users\......) del file .pdf in cui salvare l'archivio
	 * di &lt;pattern frequenti - regole d'associazione confidenti&gt;.
	 */
	private String path;

	/**
	 * Minimo supporto inserito dal client e da questo inviato verso il server.
	 */
	private float minSup;

	/**
	 * Minima confidenza inserita dal client e da questo inviato verso il server.
	 */
	private float minConf;

	/**
	 * Nome della tabella contenente i dati estratti dal database relazionale. 
	 */
	private String tableName;

	/**
	 * Immagine inviata dal client e contenente il grafico a curva di precisione.
	 */
	private BufferedImage image;

	/**
	 * Costruttore; si occupa di creare il file con estensione .pdf e di salvarvi l'archivio di &lt;pattern frequenti - {insieme 
	 *di regole d'associazione confidenti}&gt; richiamando il metodo SalvaPdf() della classe corrente.
	 * @param path percorso relativo al file pdf creato
	 * @param minSup minimo supporto inserito dall'utente
	 * @param minConf minima confidenza inserita dall'utente
	 * @param tableName nome della tabella del database da cui estrarre le informazioni richieste
	 * @param image immagine da salvare sul file pdf
	 * @throws FileNotFoundException file non trovato
	 * @throws IOException errori di I/O inerenti lo/gli stream
	 * @throws DocumentException errore nella creazione e aggiornamento/modifica del documento pdf
	 * @throws ClassNotFoundException classe non trovata
	 * @throws SQLException errore nell'esecuzione delle operazioni SQL
	 */	
	public Pdf(String path, float minSup,float minConf, String tableName,BufferedImage image) throws FileNotFoundException, IOException,DocumentException, ClassNotFoundException, SQLException{
		this.path = path;
		this.minSup = minSup;
		this.minConf= minConf;
		this.tableName= tableName;
		this.image = image;

		SalvaPdf();

	}

	/**
	 * Si occupa di leggere l'archivio di &lt;pattern frequenti - {insieme di regole d'associazione confidenti}&gt;
	 * da un file di dati preventivamente salvato dal server nella propria memoria di massa locale; inoltre, 
	 * il metodo ottiene lo schema e la lista di tuple che costituiscono la tabella del database relazionale, 
	 * e successivamente l'archivio e la lista suddetti sono salvati nella memoria di massa del server stesso 
	 * in un file con estensione pdf opportunamente denominato dall'utente.
	 * Possono occorrerele seguenti eccezioni:
	 * 
	 * 	- Se occorrono errori nella creazione del documento con estensione .pdf o nel salvataggio dei dati
	 *    in quest'ultimo, un'eccezione di tipo DocumentException &egrave; catturata e gestita emettendo
	 *    un apposito messaggio;
	 *    
	 *  - Se occorrono errori durante il caricamento della tabella dal database relazionale, un'eccezione
	 *    tipo SQLException &egrave; catturata e gestita emettendo un apposito messaggio;
	 *    
	 *  - Se il file contenente l'archivio di &lt;pattern frequenti - {insieme di regole d'associazione confidenti}&gt;
	 *    non &egrave; presente nella memoria locale del server, un'eccezione di tipo FileNotFoundException &egrave; catturata
	 *    e gestita emettendo un apposito messaggio;
	 *    
	 *  - Se il ClassLoader non rileva la presenza di una classe nella memoria principale, un'eccezione di tipo
	 *    ClassNotFoundException &egrave; catturata e gestita emettendo un apposito messaggio;
	 *    
	 *  - Se occorrono errori nella lettura di dati da uno stream di input oppure nella scrittura dei dati su 
	 *    uno stream di output, un'eccezione di tipo IOException &egrave; catturata e gestita emettendo un apposito
	 *    messaggio.
	 * 
	 * @throws FileNotFoundException file non trovato
	 * @throws IOException errori di I/O inerenti lo/gli stream
	 * @throws DocumentException errore nell creazione o aggiornamento/modifica del documento pdf
	 * @throws ClassNotFoundException classe non trovata
	 * @throws SQLException errore nell'esecuzione delle operazioni SQL
	 */
	public void SalvaPdf() throws FileNotFoundException, IOException,DocumentException, ClassNotFoundException, SQLException{		

		FileInputStream input = new FileInputStream("Rules.dat");
		ObjectInputStream in = new ObjectInputStream(input);
		String Archive = (in.readObject()).toString();
		in.close();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "png", baos);
		Image iTextImage = Image.getInstance(baos.toByteArray());
		iTextImage.scalePercent(80f);

		Document document = new Document();
		File outF= new File(path);
		PdfWriter.getInstance(document,new FileOutputStream(outF));
		document.open();
		document.addAuthor("GruppoMap: Donato Lucente, Gianluca Colaianni, Antonio Ricchetti\n");
		document.addCreationDate();
		document.add(new Paragraph("APRIORI RESULT SET\n\n" + "Nome tabella: " +tableName +
				"\nSupporto minimo: " + minSup + "\nConfidenza minima: "+ minConf + "\n" ));
		document.add(new Paragraph("_______________________________________________________________________\n\n"));
		document.add(new Paragraph(Archive));
		document.add(iTextImage);
		document.close();
	}
}
