
package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.Iterator;
import java.util.List;
import database.TableSchema.Column;
import utility.ConnectionLog;

/**
 * Modella l'insieme delle tuple collezionate all'interno di una tabella di un database relazionale.
 * Una singola tupla di tale insieme è modellata attraverso l'inner class TupleData di TableData.
 * 
 *@author Gruppo Map formato da Donato Lucente, Antonio Ricchetti, Gianluca Colaianni
 */
public class TableData  {

	/**
	 * Attributo di tipo DbAccess, usato per l'accesso al database relazionale d'interesse.
	 */
	private DbAccess db;

	/**
	 * Avvalora l'attributo utile all'accesso al database relazionale di interesse.
	 * @param db oggetto DbAccess per l'accesso al database
	 */
	public void DbAccess(DbAccess db){
		this.db=db;
	}

	/**
	 *Modella una singola tupla dell'insieme di tuple delineato dalla top level class TableData. 
	 *
	 */
	public class TupleData{

		/**
		 * Lista di oggetti Object, nonch&eacute; di una tupla; infatti quest'ultima &egrave; considerabile 
		 * come una lista di valori di attributi posti in un dato ordine, del tipo:
		 * 		
		 * 			attr_val_1 ..... attr_val_n
		 * 
		 * NOTA BENE: l'uso di una List&lt;Object&gt; permette di mantenere l'ordine dei valori degli attributi interni alla tupla
		 *            secondo lo schema che caratterizza la tabella del database.
		 */
		public List<Object> tuple=new ArrayList<Object>();

		/**
		 * Si occupa di formattare una stringa di caratteri mostrante una tupla estratta dall'insieme delle tuple
		 * della tabella del database.
		 * Ridefinisce il metodo toString() della superclasse Object.
		 * @return Stringa che rappresenta una tupla
		 */
		public String toString(){
			String value="";
			Iterator<Object> it= tuple.iterator();
			while(it.hasNext())
				value+= (it.next().toString() +" ");

			return value;
		}
	}

	/**
	 * Ricava lo schema della tabella del database relazionale, con nome table. Esegue un'interrogazione
	 * per estrarre le tuple da tale tabella, e per ogni tupla del resultset crea un oggetto istanza
	 * della classe TupleData, il cui riferimento va incluso nella lista da restituire. In particolare, 
	 * per la tupla corrente nel resultset, si estraggono i valori dei singoli campi usando i metodi
	 * getFloat() e/o getString(), e si aggiungono all'oggetto istanza della classe tupla che si sta costruendo.
	 * @param table nome della tabella del database relazionale
	 * @return Insieme di istanze di TupleData, nonch&eacute; di tuple della tabella del database relazionale
	 * @throws SQLException errore nell'esecuzione delle operazioni SQL
	 */
	public List<TupleData> getTransazioni(String table) throws SQLException{

		LinkedList<TupleData> transSet = new LinkedList<TupleData>();
		Statement statement;
		DbAccess db = new DbAccess();		
		try {
			db.initConnection();
		}
		catch (DatabaseConnectionException e) {
			System.err.println("Errore durante la connessione al Db");
			ConnectionLog.writeLogText("Errore durante la connessione al Db");
		}

		TableSchema tSchema=new TableSchema(db ,table);
		String query="select ";

		for(int i=0;i<tSchema.getNumberOfAttributes();i++){
			Column c=tSchema.getColumn(i);
			if(i>0)
				query+=",";
			query += c.getColumnName();
		}
		if(tSchema.getNumberOfAttributes()==0)
			throw new SQLException();

		query += (" FROM "+table);

		statement = db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery(query);
		while (rs.next()) {
			TupleData currentTuple=new TupleData();
			for(int i=0;i<tSchema.getNumberOfAttributes();i++)
				if(tSchema.getColumn(i).isNumber())
					currentTuple.tuple.add(rs.getFloat(i+1));
				else
					currentTuple.tuple.add(rs.getString(i+1));
			transSet.add(currentTuple);
		}
		rs.close();
		statement.close();

		try {
			db.closeConnection();
		}
		catch (DatabaseConnectionException e) {
			System.err.println("Errore durante la chiusura della connessione al Db");
			ConnectionLog.writeLogText("Errore durante la chiusura della connessione al Db");
		}
		return transSet;
	}

	/**
	 * Si occupa di restituire una lista contenente tutti i differenti tipi di valori
	 * presenti nella colonna, considerando un'unica occorrenza di ciascun e non considerando 
	 * i duplucati.
	 * @param table nome della tabella del database relazionale
	 * @param column colonna della tabella identificata da table
	 * @return Lista di valori distinti ordinati in modalità ascendente
	 *         che l'attributo identificato da column assume nella tabella
	 *         identificata da table.               
	 * @throws SQLException errore nell'esecuzione delle operazioni SQL
	 */
	public List<Object> getDistinctColumnValue(String table, Column column) throws SQLException{

		DbAccess db = new DbAccess();
		Statement statement;
		List<Object> distValues = new ArrayList<Object>();

		try {					
			db.initConnection();
		}
		catch (DatabaseConnectionException e) {
			System.err.println("Errore durante la connessione al Db");
			ConnectionLog.writeLogText("Errore durante la connessione al Db");
		}
		TableSchema tSchema = new TableSchema(db,table);
		String query = "select distinct " + column.getColumnName() + " from " + table + " order by " + column.getColumnName() + " asc";

		if (tSchema.getNumberOfAttributes() == 0)
			throw new SQLException();

		statement = db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery(query);

		while(rs.next()) {
			if (column.isNumber())
				distValues.add(rs.getFloat(1));
			else distValues.add(rs.getString(1));
		}
		rs.close();
		statement.close();

		try {							
			db.closeConnection();
		}
		catch (DatabaseConnectionException e) {
			System.err.println("Errore durante la chiusura della connessione al Db");
			ConnectionLog.writeLogText("Errore durante la chiusura della connessione al Db");
		}
		return distValues;
	}

	/**
	 * Formula ed esegue un'interrogazione SQL per estrarre il valore aggregato (valore minimo o valore massimo)
	 * cercato nella colonna di nome column nella tabella table. Il metodo solleva e propaga un'eccezione di tipo
	 * NoValueException se il resultset &egrave; vuoto oppure il valore calcolato &egrave; pari a null, passando
	 * un opportuno messaggio.
	 * @param table nome della tabella del database relazionale
	 * @param column nome della colonna della tabella di nome table
	 * @param aggregate operatore SQL di aggregazione (min, max)
	 * @return Valore aggregato cercato
	 * @throws SQLException errore nell'esecuzione delle operazioni SQL
	 * @throws NoValueException alcun valore &egrave; presente all'interno del resultset
	 */
	public  Object getAggregateColumnValue(String table,Column column,QUERY_TYPE aggregate) throws SQLException,NoValueException{
		Statement statement;
		DbAccess db = new DbAccess();	
		Object value=null;
		String aggregateOp="";

		String query="select ";
		if(aggregate==QUERY_TYPE.MAX)
			aggregateOp+="max";
		else
			aggregateOp+="min";
		query+=aggregateOp+"("+column.getColumnName()+ ") FROM "+table;

		statement = db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery(query);
		if (rs.next()) {
			if(column.isNumber())
				value=rs.getFloat(1);
			else
				value=rs.getString(1);
		}
		rs.close();
		statement.close();
		if(value==null)
			throw new NoValueException("Nessun valore aggregato " + aggregateOp+ " sulla colonna "+ column.getColumnName());
		return value;
	}
}
