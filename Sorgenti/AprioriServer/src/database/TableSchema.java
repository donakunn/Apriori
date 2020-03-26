
package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Modella lo schema di una tabella di un database relazionale, dove per schema si intende un'entit&agrave; del tipo:
 * 							
 * 							&lt;nome_tabella&gt;( &lt;{attr_1, ...., attr_n }&gt; )
 * 
 * dove nome_tabella è il nome della tabella del database, mentre &lt;{attr_1, ...., attr_n }&gt; è l'insieme 
 * dei nomi degli attributi costituenti lo schema della stessa tabella.
 *
 *@author Gruppo Map formato da Donato Lucente, Antonio Ricchetti, Gianluca Colaianni
 */
public class TableSchema {

	/**
	 *Classe interna ( o inner class ) della classe TableData; modella una colonna di una tabella di un database relazionale.
	 */
	public class Column{

		/**
		 * Delinea il nome della colonna.
		 */
		private String name;

		/**
		 * Delinea il dominio dei valori contenuti all'interno della colonna
		 */
		private String type;

		/**
		 * Costruttore di un oggetto Column; avvalora i membri name e type con i riferimenti ad oggetti String
		 * passati in input.
		 * @param name riferimento ad un oggetto String contenente il nome della colonna
		 * @param type Riferimento ad un oggetto String contenente il nome del dominio dei valori della colonna	
		 */
		Column(String name,String type){
			this.name=name;
			this.type=type;
		}

		/**
		 * Restituisce il nome della colonna corrente.
		 * @return Stringa contenente il nome della colonna
		 */
		public String getColumnName(){
			return name;
		}

		/**
		 * Controlla se il valore di un elemento della colonna corrente sia numerico oppure no.
		 * @return true se il valore della colonna corrente è numerico, false altrimenti
		 */
		public boolean isNumber(){
			return type.equals("number");
		}

		/**
		 * Si occupa di formattare una stringa di caratteri mostrante informazioni sulla colonna, quali il nome e il tipo dei suoi valori.
		 * Ridefinisce il metodo toString() della superclasse Object.
		 * @return Stringa contente il nome della colonna e il dominio dei suoi valori.
		 */
		public String toString(){
			return name+":"+type;
		}
	}

	/**
	 * Lista di oggetti Column, nonchè schema di una tabella del database;
	 * 
	 * NOTA BENE: l'uso di una List<Column> permette di mantenere l'ordine degli attributi che caratterizzano lo schema
	 * 			  della tabella del database relazionale.
	 */
	List<Column> tableSchema=new ArrayList<Column>();

	/**
	 * Costruttore di un oggetto TableSchema che rappresenta lo schema della tabella di un database relazionale
	 * il cui nome &egrave; contenuto nell'oggetto Stringa tableName; il costruttore mappa i tipi MySQL con i tipi Java
	 * mediante un'apposita tabella di conversione, e inoltre ottiene la connessione al database relazionale, 
	 * ricavando informazioni sul Database Managemente System ad esso relativo.
	 * Sulla base della tabella di corrispondenza dei tipi prima descritta, il costruttore ottiene il nome e il dominio
	 * dei valori di ciascuna colonna della tabella a cui accede, riproducendone così lo schema. 
	 * Il metodo pu&ograve; sollevare e propagare un'eccezione di tipo SQLException a causa di errori di accesso al database.
	 * Altri tipi di errore sono identificati da un codice errore, fornito dal produttore del database, oppure
	 * &egrave; fornita una stringa che spiega il tipo di errore.
	 * @param db riferimento ad un oggetto DbAccess che consente di ottenere l'accesso al database relazionale
	 * @param tableName Nome della tabella locata nel database relazionale
	 * @throws SQLException errore nell'esecuzione delle operazioni SQL
	 */
	public TableSchema(DbAccess db, String tableName) throws SQLException{

		/**
		 * Contenitore di tipo HashMap<K, V>, con K e V parametri di tipo, a cui associamo in tal caso
		 * i rispettivi tipi :
		 * 			
		 * 			K = String
		 * 			V = String
		 * 
		 * Dunque, mapSQL_JAVAT permette di mappare le corrispondenze tra i differenti tipi di dato 
		 * del linguaggio MySQL con i tipi in Java, stabilendo se un dato tipo MySQL corrisponde, 
		 * per esempio, ad un tipo java numerico o stringa.
		 */
		HashMap<String,String> mapSQL_JAVATypes=new HashMap<String, String>();

		mapSQL_JAVATypes.put("CHAR","string");
		mapSQL_JAVATypes.put("VARCHAR","string");
		mapSQL_JAVATypes.put("LONGVARCHAR","string");
		mapSQL_JAVATypes.put("BIT","string");
		mapSQL_JAVATypes.put("SHORT","number");
		mapSQL_JAVATypes.put("INT","number");
		mapSQL_JAVATypes.put("LONG","number");
		mapSQL_JAVATypes.put("FLOAT","number");
		mapSQL_JAVATypes.put("DOUBLE","number");



		Connection con=db.getConnection();
		DatabaseMetaData meta = con.getMetaData();
		ResultSet res = meta.getColumns(null, null, tableName, null);

		while (res.next()) {

			if(mapSQL_JAVATypes.containsKey(res.getString("TYPE_NAME")))
				tableSchema.add(new Column(
						res.getString("COLUMN_NAME"),
						mapSQL_JAVATypes.get(res.getString("TYPE_NAME")))); 
		}
		res.close();	    
	}

	/**
	 * Permette di ottenere la cardinalit&agrave; dell'insieme di attributi costituenti lo schema
	 * della tabella del database relazionale.
	 * @return Cardinalit&agrave; dell'insieme di attributi dello schema della tabella
	 *         estratta dal database relazionale
	 */
	public int getNumberOfAttributes(){
		return tableSchema.size();
	}

	/**
	 * Permette di ottenere informazioni sulla colonna in posizione i-esima nello schema della tabella. 
	 * della tabella del database relazionale.
	 * @param index indice di colonna
	 * @return Colonna i-esima dello schema della tabella.
	 */
	public Column getColumn(int index){
		return tableSchema.get(index);
	}
}




