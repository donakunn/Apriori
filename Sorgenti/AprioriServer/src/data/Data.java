
package data;

import java.sql.SQLException;
import java.util.*;
import database.DatabaseConnectionException;
import database.DbAccess;
import database.NoValueException;
import database.TableData;
import database.TableData.*;
import utility.ConnectionLog;
import database.TableSchema;

/**
 * Modella un insieme di transazioni, nonch&eacute; di vettori di coppie del tipo  &lt;attributo-valore&gt;.
 *
 *@author Gruppo Map formato da Donato Lucente, Antonio Ricchetti, Gianluca Colaianni
 */
public class Data {

	/**
	 * Crea una matrice di Object che ha un numero di righe pari alla cardinalit&agrave; dell'insieme di transazioni contenute 
	 * in un'istanza di Data, e un numero di colonne pari al numero di attributi che formano lo schema della corrente 
	 * istanza di Data.
	 */
	private Object data [][];

	/**
	 * Cardinalit&agrave; dell'insieme di transazioni contenute nella corrente istanza di Data.
	 */
	private int numberOfExamples;

	/**
	 * Una lista di attributi, avvalorati in ciascuna transazione.
	 */
	private List<Attribute> attributeSet;

	/**
	 * Costruttore di una tabella Data; ottiene la connessione al database di interesse e riproduce esattamente lo schema 
	 * che caratterizza la tabella sorgente del database "table" in un'istanza della classe TableSchema; ottiene inoltre l'insieme delle tuple
	 * che popolano la tabella sorgente stessa e lo riporta in un'istanza della classe TableData disponendo i valori degli attributi 
	 * secondo lo schema precedentemente riprodotto. Infine, attraverso l'invocazione del metodo statico 
	 * ConnectionLog.writeLogText() del package utility, invia i messaggi relativi allo stato di esecuzione della richiesta client
	 * in un file testuale apposito.
	 * Il metodo pu&ograve; generare le seguenti eccezioni:
	 * 
	 * 1) Nel caso di errori di connessione al database relazionale da cui estrarre i dati richiesti dal client,
	 *    un'eccezione di tipo DatabaseConnectionException &egrave; catturata e gestita emettendo un apposito messaggio;
	 *    
	 * 2) Nel caso di errori durante il caricamento di una tabella dal database relaizonale a cui si &egrave; connessi, 
	 *    un'eccezione di tipo SQLException &egrave; catturata e gestita emettendo un apposito messaggio.
	 *    
	 * @param table: oggetto della classe String rappresentante il nome della tabella sorgente del database.
	 * @throws SQLException errore nell'esecuzione delle operazioni SQL
	 * @throws DatabaseConnectionException errore nell'interazione con il database
	 * @throws NoValueException alcun valore &egrave; presente all'interno del resultset
	 */
	public Data(String table) throws SQLException,DatabaseConnectionException,NoValueException{	

		DbAccess db = new DbAccess();
		try {
			db.initConnection();
		}
		catch (DatabaseConnectionException e) {
			System.err.println("Errore durante connessione al Db");
			ConnectionLog.writeLogText("Errore durante connessione al Db");
		}

		TableSchema tSchema = new TableSchema(db, table);  		
		TableData tableData = new TableData();
		List<TupleData> list = new ArrayList<TupleData>();			

		try {
			list = tableData.getTransazioni(table);
		} 
		catch (SQLException e) {
			System.err.println("Errore durante il caricamento della tabella " + table);
			ConnectionLog.writeLogText("Errore durante il caricamento della tabella " + table);
		}

		numberOfExamples = list.size();
		attributeSet = new LinkedList<Attribute>();			

		for (int i=0; i<tSchema.getNumberOfAttributes(); i++) {
			List<Object> distinctValues = tableData.getDistinctColumnValue(table, tSchema.getColumn(i));

			if(tSchema.getColumn(i).isNumber()){ 
				attributeSet.add(new ContinuousAttribute(tSchema.getColumn(i).getColumnName(), i, 0f, 30.3f));
			}
			else {
				String[] fromList = new String[distinctValues.size()];
				int j = 0;
				for (Object value : distinctValues) {
					fromList[j] = String.valueOf(value);
					j++;
				}
				attributeSet.add(new DiscreteAttribute(tSchema.getColumn(i).getColumnName(), i,fromList));
			}
		}

		data = new Object [numberOfExamples][attributeSet.size()];
		for(int i=0; i<numberOfExamples; i++) {
			List<Object> currentTuple =  list.get(i).tuple;

			for(int j=0; j<attributeSet.size();j++) {
				data[i][j]=currentTuple.get(j); 
			}
		}

		try {
			db.closeConnection();
		}
		catch (DatabaseConnectionException e) {
			System.err.println("Errore durante la chiusura della connessione al Db");
			ConnectionLog.writeLogText("Errore durante la chiusura della connessione al Db");
		}
	}

	/**
	 * Restituisce il valore del membro numberOfExamples dell'istanza corrente di Data. 
	 * @return Cardinalit&agrave; dell'istanza di Data corrente.
	 */
	public int getNumberOfExamples(){
		return numberOfExamples;
	}

	/**
	 * Restituisce la cardinalit&agrave; dell'insieme di attributi costituenti lo schema della tabella corrente, quest'ultima istanza di Data.
	 * @return Cardinalit&agrave; del membro attributeSet.
	 */
	public int getNumberOfAttributes(){
		return attributeSet.size() ;
	}

	/**
	 * Restituisce il valore dell' attributo in psozizione attributeIndex per la transazione memomorizzata 
	 * nella posizione exampleIndex della corrente tabella, quest'ultima istanza di Data. 
	 * @param exampleIndex indice di riga per la matrice data che corrisponde ad una specifica transazione
	 * @param attributeIndex indice di colonna per un attributo
	 * @return Valore assunto dall'attributo identificato da attributeIndex nella transazione
	 */
	public Object getAttributeValue(int exampleIndex, int attributeIndex){
		return data[exampleIndex][attributeIndex];
	}

	/**
	 * Restituisce l'attributo in posizione attributeIndex di attributeSet.
	 * @param attributeIndex indice di colonna per un attributo.
	 * @return Valore assunto dall'attributo identificato da attributeIndex nella transazione
	 * 		   identificata da exampleIndex nel membro data.
	 */
	public Attribute getAttribute(int attributeIndex){
		return attributeSet.get(attributeIndex);
	}

	/**
	 * Restituisce un oggetto String contenente una stringa di testo ottenuta concatenando un identificativo di riga ( per esempio: "D1")
	 * con l'insieme dei valori contenuti all'interno della tupla i-esima della corrente istanza di Data. Per esempio:
	 * 		
	 * 		D1: value_Att1, value_Att2, .... , value_Attn
	 * 
	 * dove value_Att1, ..., value_Attn sono i valori dei corrispondenti attributi, distrbuiti secondo lo schema dell'istanza di Data corrente.
	 * Si noti che i valori suddetti sono separati da una virgola.
	 * Ridefinisce il metodo toString() della superclasse Object.
	 * @return stringa contenente un identificativo alfanumerico del tipo Di, con i compreso tra 0 e il numero di attributi
	 *         dello schema della tabella.
	 */
	public String toString(){
		String value = "";
		for(int i=0; i<getNumberOfExamples();i++){
			value += "D"+(i+1)+": ";
			for(int j=0; j<getNumberOfAttributes();j++){
				value+= data[i][j] + ", ";				
			}
			value +="\n";
		}
		return value;		
	}
}
