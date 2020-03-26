
package database;

/**
 * Modella un'eccezione personalizzata che occorre quando alcuna regola confidente
 * &egrave; generata da un pattern frequente di lunghezza 1.
 *                 
 *@author Gruppo Map formato da Donato Lucente, Antonio Ricchetti, Gianluca Colaianni
 */
public class DatabaseConnectionException extends Exception{

	/**
	 * Costruttore dell'oggetto eccezione DatabaseConnectionException.
	 * Richiama il costruttore della superclasse Exception passandogli un riferimento ad un oggetto String contenente un messaggio di errore.
	 * @param mes oggetto String delineante un messaggio d'errore
	 */
	public DatabaseConnectionException(String mes){
		super(mes);
	};
}
