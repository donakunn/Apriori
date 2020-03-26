
package server;

import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.ImageIcon;
import com.itextpdf.text.DocumentException;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import mining.AssociationRule;
import mining.AssociationRuleMiner;
import mining.AssociationRuleArchive;
import mining.FrequentPattern;
import mining.FrequentPatternMiner;
import mining.NoPatternException;
import mining.OneLevelPatternException;
import utility.ConnectionLog;
import data.Data;
import data.EmptySetException;
import database.DatabaseConnectionException;
import database.NoValueException;
import estensioni.Pdf;
import estensioni.PrecisionRecallCurve;

/**
 * Modella la comunicazione con un unico client.
 *
 *@author Gruppo Map formato da Donato Lucente, Antonio Ricchetti, Gianluca Colaianni
 */
class ServerOneClient extends Thread {

	/**
	 * Terminale lato server del canale tramite cui avviene lo scambio di oggetti client-server.
	 */
	private Socket socket;

	/**
	 * Flusso di oggetti dal client al server.
	 */
	private ObjectInputStream in; 

	/**
	 * Flusso di oggetti dal server al client.
	 */
	private ObjectOutputStream out;

	/**
	 * Buffer di memoria per l'invio di informazioni verso la socket client.
	 */
	private BufferedInputStream inBuf;

	/**
	 * Istanza della classe AssociationRuleArchive.
	 */
	private AssociationRuleArchive archive;

	/**
	 * Costruttore; inizializza il socket per la comunicazione con il client con il parametro in input al costruttore stesso. 
	 * Inizializza i canali di comunicazione per lo scambio di informazioni con il client stesso, avvia il thread invocando 
	 * il metodo start(), il quale &egrave; ereditato dalla superclasse Thread di cui la stessa classe ServeOneClient &egrave;
	 * sottoclasse.
	 * @param s Socket creato dal server per la comunicazione con un client
	 * @throws IOException errori di I/O inerenti lo/gli stream
	 */
	ServerOneClient(Socket s) throws IOException {
		this.socket=s;
		in=new ObjectInputStream(s.getInputStream());
		out= new ObjectOutputStream(s.getOutputStream());
		inBuf = new BufferedInputStream(s.getInputStream());
		start();
	}

	/**
	 * Ridefinisce il metodo run() della classe Thread per variazione funzionale. Gestisce le richieste del client, dal quale riceve un comando
	 * attraverso la connessione; a seconda del comando inviato, il server opera in uno dei modi seguenti:
	 * 
	 * Caso 1) Apprendimento di pattern frequenti e regole d'associazione confidenti associate a ciascun pattern. Un'istanza della classe
	 *    	   AssociationRuleArchive &egrave; poi popolata con i suddetti elementi. Le seguenti eccezioni potrebbero occorrere:
	 *    
	 *    	   - Se il set di pattern frequenti &egrave; vuoto, un'eccezione di tipo NoPatternException &egrave; catturata e gestita emettendo 
	 *           un messaggio che indica l'assenza di pattern nel set di pattern frequenti. 
	 *        
	 *         - Un'eccezione di tipo OneLevelPatternException &egrave; catturata qualora si tenti di estrarre una regola d'associazione da un 
	 *           pattern di lunghezza 1.
	 *         
	 *         - Se la connessione al database relazionale da cui si estraggono i pattern di partenza fallisce, un'eccezione di tipo
	 *           DatabaseConnectionException &egrave; catturata e gestita emettendo un messaggio indicante l'impossibilit&agrave; di stabilire
	 *           una connessione con il database stesso.
	 *         
	 *         - Se nessun valore &egrave; presente nel resultset estratto dalla tabella del database relazionale a cui si &egrave; connessi, 
	 *           un'eccezione di tipo NoValueException &egrave; catturata e gestita emettendo un messaggio che indica l'assenza di valori 
	 *           nel suddetto set.
	 *         
	 *         - Se occorre un errore all'interno del database, dovuti ad una non corretta esecuzione di una query o ad altri problemi
	 *           interni al database, un'eccezione di tipo SQLException &egrave; catturata e gestita emettendo un messaggio indicante
	 *           errori relativi ad operazioni sul DB.
	 *           
	 *         - Se l'archivio contenente l'insieme di transazioni estratto dalla tabella del database risulta vuoto, un'eccezione 
	 *           di tipo EmptySetException &egrave; catturata e gestita emettendo un messaggio indicante l'assenza di suddette transazioni
	 *           
	 * Caso 2) Il server si occupa del salvataggio dell'archivio di pattern frequenti/regole d'associazione attraverso il metodo  
	 *         AssociationRuleArchive.salva(), in un file memorizzato nel filesystem locale al server stesso. Si osservi che il 
	 *         nome del file &egrave; un nome standard, ed &egrave; passato al server dalla stessa interfaccia grafica client, e 
	 *         in modo nascosto al client stesso.
	 * 
	 * Caso 3) Caricamento da file di un archivio di pattern frequenti e regole d'associazione confidenti a ciascuno di essi associate, 
	 *         attraverso l'invocazione del metodo AssociationRuleArchive.carica().
	 *         
	 * Caso 4) Ricezione da parte del client del path relativo al file in cui salvare le informazioni, il minimo supporto e la minima
	 *         confidenza inserite dall'utente, il nome della tabella da cui sono stati estratte le informazioni richieste dall'utente, 
	 *         ed il grafico relativo alla distribuzione delle frequenze assolute dei pattern frequenti rispetto al supporto minimo
	 *         e delle regole d'associazione confidenti rispetto alla confidenza minima.
	 * 
	 * Caso 5) Costruisce un'istanza del grafico a curva di precisione ed estrae le frequenze assolute dei pattern frequenti e delle
	 *         regole d'associazione confidenti, gli uni rispetto al minimo supporto, gli altri rispetto alla minima confidenza 
	 *         impostati dall'utente; queste ultime due operazioni sono eseguite richiamando gli opportuni metodi 
	 *         PrecisionRecallCurve.suppCount() e PrecisionRecallCurve.confCount(). Le informazioni sono poi inviate dal server
	 *         verso il client in modo che quest'ultimo possa generare un grafico a curva di precisione. 
	 * 
	 * Si osservi inoltre che nel metodo run() possono occorrere le seguenti eccezioni:
	 * 
	 * 	- Se occorrono errori durante la connessione tra il client e il server, un'eccezione di tipo SocketException sar&agrave; catturata
	 *    e gestita attraverso l'emissione di un apposito messaggio;
	 *    
	 *  - se occorrono errori durante lo scambio di messaggi tra client e server, un'eccezione di tipo IOException sar&agrave; catturata
	 *    e gestita mediante l'emissione di un apposito messaggio;
	 *    
	 *  - se occorrono errori durante il caricamento di una classe da parte del ClassLoader, un'eccezione di tipo ClassNotFoundException
	 *    sar&agrave; catturata e gestita emettendo un apposito messaggio.
	 *   
	 * Il metodo si occupa anche di chiudere la socket di connessione dedicata al client, e nel caso fallisca cattura e gestisce
	 * un'eccezione di tipo IOException mediante un apposito messaggio. Infine, attraverso l'invocazione del metodo statico 
	 * ConnectionLog.writeLogText() del package utility, invia i messaggi relativi allo stato di esecuzione della richiesta client
	 * in un file testuale apposito.  
	 */
	public void run() {
		System.out.println("Nuovo client connesso");
		ConnectionLog.writeLogText("Nuovo client connesso");

		try{
			while (true) {
				int command=0;
				command = ((Integer)(in.readObject())).intValue();
				if(command<0 ){
					System.out.println("Client disconnesso"  );
					ConnectionLog.writeLogText("Client disconnesso"  );
					break;
				}
				switch(command)
				{
				case 1: 
					try{
						archive=new AssociationRuleArchive();
						String tableName=(String)in.readObject();
						Data trainingSet=new Data(tableName);
						Float minSup=(Float)in.readObject();
						Float minConf=(Float)in.readObject();
						LinkedList<FrequentPattern> outputFP=FrequentPatternMiner.frequentPatternDiscovery(trainingSet,minSup);
						Iterator<FrequentPattern> it=outputFP.iterator();

						while(it.hasNext()){
							FrequentPattern FP=it.next();
							archive.put(FP);
							LinkedList<AssociationRule> outputAR=null;
							try{
								outputAR = AssociationRuleMiner.confidentAssociationRuleDiscovery(trainingSet,FP,minConf);
								Iterator<AssociationRule> itRule=outputAR.iterator();
								while(itRule.hasNext()){
									archive.put(FP,itRule.next());
								}
							}
							catch(OneLevelPatternException e){}
							catch (NoPatternException e) {
								System.err.println("Nessun pattern presente nel set di pattern frequenti");
								ConnectionLog.writeLogText("Nessun pattern presente nel set di pattern frequenti");
							}
						}
					} 
					catch (DatabaseConnectionException e1) {
						System.err.println("Impossibile connettersi al Db");
						ConnectionLog.writeLogText("Impossibile connettersi al Db");
					} 
					catch (SQLException e1) {
						System.err.println("Errore durante operazioni SQL");
						ConnectionLog.writeLogText("Errore durante operazioni SQL");
					} 
					catch (NoValueException e1) {
						System.err.println("Nessun valore presente nel resultset estratto dalla relativa tabella del database");
						ConnectionLog.writeLogText("Nessun valore presente nel resultset estratto dalla relativa tabella del database");
					} 
					catch (EmptySetException e1) {
						System.err.println("L'insieme delle transazioni � vuoto"); 
						ConnectionLog.writeLogText("L'insieme delle transazioni � vuoto"); 
					}
					out.writeObject(archive.toString());
					break;

				case 2: 
					archive.salva((String)in.readObject());

					try {
						out.writeObject("\nPattern e regole salvate CON SUCCESSO");
						ConnectionLog.writeLogText("Pattern e regole salvate CON SUCCESSO");
					} 
					catch (IOException e) {
						out.writeObject("\nErrore durante il salvataggio"); 
						System.err.println("Errore durante il salvataggio");
						ConnectionLog.writeLogText("Errore durante il salvataggio");
					} 
					break;

				case 3: 
					out.writeObject(AssociationRuleArchive.carica((String)in.readObject()).toString());
					break;

				case 4:
					String path = (String) in.readObject();
					float minSup =(float) in.readObject();
					float minConf = (float) in.readObject();
					String tableName = (String) in.readObject();
					ImageIcon img = (ImageIcon) in.readObject();

					Image tmp = img.getImage();
					BufferedImage buffered = new BufferedImage(600,800,BufferedImage.TYPE_INT_RGB);
					buffered.getGraphics().drawImage(tmp, 0, 0, null);

					Pdf pdfInstance = new Pdf(path, minSup, minConf, tableName, buffered);
					break;

				case 5:
					PrecisionRecallCurve grafico = new PrecisionRecallCurve(archive.getArchive(),archive);
					int suppCount[]= grafico.suppCount();
					int confCount[] = grafico.confCount();
					out.writeObject(suppCount);
					out.writeObject(confCount);
					break;

				default:
					System.out.println("COMANDO INESISTENTE");
					ConnectionLog.writeLogText("COMANDO INESISTENTE");
				}
			}
		} 
		catch(SocketException e){
			System.err.println("Errore durante la connessione");
			ConnectionLog.writeLogText("Errore durante la connessione");
		} 
		catch (IOException e) {
			System.err.println("Errore durante lo scambio dei messaggi");
			ConnectionLog.writeLogText("Errore durante lo scambio dei messaggi");
		} 
		catch (ClassNotFoundException e) {
			System.err.println("Classe non Trovata");
			ConnectionLog.writeLogText("Classe non Trovata");
		} 
		catch (DocumentException e) {
			System.err.println("Salvataggio Pdf fallito");
			ConnectionLog.writeLogText("Salvataggio Pdf fallito" );
		} 
		catch (NoPatternException e1) {					
			System.err.println("Nessun pattern presente nel set");
			ConnectionLog.writeLogText("Nessun pattern presente nel set" );
		}   
		catch (SQLException e) {							
			System.err.println("Errore durante operazioni SQL");
			ConnectionLog.writeLogText("Errore durante operazioni SQL" );
		}
		finally {
			try {
				socket.close();
			} 
			catch (IOException e) {
				System.err.println("Socket non chiuso!");
				ConnectionLog.writeLogText("Socket non chiuso!");
			}
		}
	}
}



