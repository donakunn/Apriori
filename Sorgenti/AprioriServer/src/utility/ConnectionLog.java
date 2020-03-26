
package utility;

import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Crea un file di log dove immettere lo stato operazionale del server e le informazioni relative
 * ad una connessione con un client.
 *    
 *@author Gruppo Map formato da Donato Lucente, Antonio Ricchetti, Gianluca Colaianni
 */
public class ConnectionLog {

	/**
	 * Stream di output per l'invio di informazioni verso un file.
	 */
	private static FileOutputStream outFile;

	/**
	 * Stream per la stampa di stringhe su uno stream di output.
	 */
	private static PrintStream outWriter;

	/**
	 * Rappresenta	data e ora locali formattate opportunamente.
	 */
	private SimpleDateFormat dateFormatter;

	/**
	 * Costruttore di un'istanza di ConnectionLog, che si occupa di scrivere un messaggio di log
	 * descrivente un evento relativo all'interazione tra il server e il client; riporta
	 * la data e l'ora di creazione del log testuale.	
	 * Tale costruttore cattura un'eccezone di tipo IOException qualora si verifichino problemi con l'invio
	 * delle informazioni sugli stream di output, e la gestisce emettendo un'apposito messaggio apposito.
	 * @param logText messaggio da stampare su un file testuale di log.
	 */
	public ConnectionLog(String logText){
		File dir = new File("ServerLog");
		Date data= new Date();
		dateFormatter = new SimpleDateFormat("E yyyy.MM.dd 'at' hh-mm-ss a zzz");
		try{
			File outF= new File(dir,logText+" " + dateFormatter.format(data) + ".txt");
			outFile = new FileOutputStream(outF, true);
			outWriter = new PrintStream(outFile);			
		}
		catch(IOException e){
			System.err.println("Errore: impossibile scrivere il messaggio di log nel file di destinazione");
		}
	};

	/**
	 * Si occupa di formattare il messaggio testuale di log che derscrive un evento, indicando l'ora esatta
	 * dell'evento stesso e una sua descrizione. Il formato del messaggio &egrave; qui di seguito riportato:
	 * 	
	 * 				[&lt;hh:mm:ss&gt;] &lt;messaggio testuale descrivente un evento&gt;	
	 * 
	 * Per esempio:
	 * 	
	 * 				[11:46:50] Server started	
	 * 	N.B. A titolo informativo, le parentesi angolari ("&lt;", "&gt;") indicano la sintassi di un elemento 
	 *       di quel tipo; la notazione &egrave; "presa in prestito" dalla Backus Naur Form (BNF), formalismo 
	 *       notazionale generalmente usato per la descrizione della sintassi di linguaggi formali.
	 *        
	 * @param logText messaggio testuale di log da stampare secondo il formato descritto
	 */
	public static  void writeLogText(String logText) {
		Date data= new Date();
		SimpleDateFormat dateFormatter2 = new SimpleDateFormat("hh:mm:ss");
		outWriter.println("["+ dateFormatter2.format(data)+"] " + logText + "\n");
	}

	/**
	 * Si occupa della chiusura dello stream di output verso file e dello stream per la stampa di strighe
	 * su uno stream di output.
	 * Il metodo cattura un'eccezione di tipo IOException qualora si verifichino errori nella chiusura 
	 * degli stream di output, e la gestisce emettendo un messaggio apposito.
	 */
	public static void logClosing(){
		try{
			outWriter.close();
			outFile.close();
		}
		catch(IOException e){
			System.err.println("Errore: impossibile terminare il processo di scrittura del log file\n");
		}
	}
}