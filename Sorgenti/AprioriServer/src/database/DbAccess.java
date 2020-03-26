
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import utility.ConnectionLog;


/**
 * Gestisce l'accesso al DB per la lettura dei dati di training.
 * 
 * @author Gruppo Map formato da Donato Lucente, Antonio Ricchetti, Gianluca Colaianni
 */
public class DbAccess {

	/**
	 * Attributo di classe che rappresenta il Driver manager presso il quale si registreranno 
	 * gli oggetti driver utili alla connessione con il database e all'interazine con quest'ultimo.
	 * L'attributo non &egrave; modificabile dopo essere stato inizializzato.
	 */
	private static final String DRIVER_CLASS_NAME = "org.gjt.mm.mysql.Driver";

	/**
	 * Attributo di classe, nonch&eacute; stringa, indicante lo standard JDBC di Java per l'interfacciamento
	 * con il database, seguito dal nome del meccanismo di connessione al database MySQL.
	 * L'attributo non &egrave; modificabile dopo essere stato inizializzato.
	 */
	private static final String DBMS = "jdbc:mysql";

	/**
	 * Nome dell'host su cui il database risiede e a cui la macchina locale si connetter&agrave;
	 * per ottenere l'accesso al database specificato.
	 * L'attributo non &egrave; modificabile dopo essere stato inizializzato.
	 */
	private  final String SERVER = "localhost";

	/**
	 * Nome della tabella all'interno del database localizzato sul sever "localhost".
	 */
	private String DATABASE = "AprioriDB";

	/**
	 * Numero di porta sulla quale il processo server dell'host server &egrave; registrata
	 * per mettere a disposizione i propri servizi.
	 */
	private  int PORT = 3306;

	/**
	 * Nome utente per l'accesso al database localizzato sul sever "localhost".
	 */
	private  String USER_ID = "AprioriUser";

	/**
	 * Password per l'accesso al database localizzato sul sever "localhost".
	 */
	private  String PASSWORD = "apriori";				

	/**
	 * Istanza della classe Connection, che astrae la connessione con il database 
	 * localizzato sul sever "localhost", e che consente al programma di effettuare
	 * interrogazioni e operazioni di modifica ed aggiornamento del database stesso
	 * mediante appositi metodi, quali Statement.executeQuery() e Statement.executeUpdate(),
	 * rispettivamente.
	 */
	private Connection conn;

	/**
	 * Inizializza l'URL relativo al database, ottiene il driver di interfacciamento con il database stesso
	 * e inizializza la connessione a quest'ultimo.
	 * Le seguenti eccezioni potrebbero occorrere:
	 * 
	 * 1) Se il riferimento al Class object ottenuto attraverso il metodo statico Class.forName()
	 * 	  e relativo al DriverManager per l'interfacciamento con il database non pu&ograve; essere rilevato,
	 * 	  allora il metodo catturer&agrave; un'eccezione di tipo ClassNotFoundException e la gestir&agrave;
	 * 	  sollevando e propagando un'eccezione di tipo DatabaseConnectionException;
	 * 
	 * 2) Se il metodo non ha accesso a taluni membri della classe specificata dal Class object
	 * 	  ottenuto via Class.forName(), allora catturer&agrave; un'eccezione di tipo IllegalAccesException
	 *    e la gestir&agrave; sollevando e propagando un'eccezione di tipo DatabaseConnectionException;
	 * 
	 * 3) Se non &egrave; possibile ottenere un'istanza della classe descritta dal metodo statico Class.forName(), allora
	 * 	  il metodo catturer&agrave; un'eccezione di tipo InstatiationException  e la gestir&agrave; sollevando e propagando 
	 * 	  un'eccezione di tipo DatabaseConnectionException.
	 * 
	 * 4) Se non &egrave; possibile ottenere l'oggetto Connection per eseguire query e modificare ed aggiornare il database,
	 * 	  il metodo catturer&agrave; un'eccezione di tipo SQLException e la gestir&agrave; sollevando e propagando un'eccezione 
	 * 	  di tipo DatabaseConnectionException.
	 * 
	 * @throws DatabaseConnectionException  errore nella connessione od interazione con il database
	 */
	public  void initConnection() throws DatabaseConnectionException{
		String connectionString = DBMS+"://" + SERVER + ":" + PORT + "/" + DATABASE + "?useSSL=false";	

		try {
			Class.forName(DRIVER_CLASS_NAME).newInstance();
		} 
		catch (IllegalAccessException e) {
			System.err.println("IllegalAccessException");
			ConnectionLog.writeLogText("IllegalAccessException");
			throw new DatabaseConnectionException(e.toString());
		}
		catch (InstantiationException e) {
			System.err.println("InstantiationException");
			ConnectionLog.writeLogText("InstantiationException");
			throw new DatabaseConnectionException(e.toString());
		} 
		catch (ClassNotFoundException e) {
			System.err.println("Impossibile trovare il Driver: " + DRIVER_CLASS_NAME);
			ConnectionLog.writeLogText("Impossibile trovare il Driver: " + DRIVER_CLASS_NAME);
			throw new DatabaseConnectionException(e.toString());
		}

		try {
			conn = DriverManager.getConnection(connectionString, USER_ID, PASSWORD);
		} 
		catch (SQLException e) {
			System.err.println("Impossibile connettersi al DB");
			ConnectionLog.writeLogText("Impossibile connettersi al DB");
			throw new DatabaseConnectionException(e.toString());
		}
	}

	/**
	 * Restituisce la connessione attraverso la quale interrogare, modificare ed aggiornare il database.
	 * @return Connessione al database
	 */
	public Connection getConnection(){
		return conn;
	}

	/**
	 * Si occupa di chiudere la connessione con il database; se la chiusura fallisce, il metodo cattura un'eccezione
	 * di tipo SQLException e la gestisce sollevando e propagando un'eccezione di tipo DatabaseConnectionException,
	 * passando in input un apposito messaggio descrivente l'eccezione occorsa.
	 * @throws DatabaseConnectionException errore nella connessione od interazione con il database
	 */
	public void closeConnection() throws DatabaseConnectionException {
		try {
			conn.close();
		} 
		catch(SQLException e) {

			ConnectionLog.writeLogText("Errore nella chiusura della connessione: " + e.getMessage());
			throw new DatabaseConnectionException("Errore nella chiusura della connessione: " + e.getMessage());
		}
	}
}
