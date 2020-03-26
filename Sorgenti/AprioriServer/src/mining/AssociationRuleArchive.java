
package mining;

import java.io.*;
import java.util.*;
import utility.ConnectionLog;

/**
 * Modella un contenitore di elementi del tipo &lt;pattern frequenti - {insieme di regole d'associazione confidenti}&gt;.
 * Implementa l'interfaccia Serializable in modo che le sue istanze siano serializzabili in flussi
 * di byte che le rappresentino.
 * 
 *@author Gruppo Map formato da Donato Lucente, Antonio Ricchetti, Gianluca Colaianni
 */
public class AssociationRuleArchive implements Serializable {

	/**
	 * Contenitore Map per le coppie del tipo &lt;Pattern Frequente, { Insieme di Regole d'Associazione estratte dal pattern chiave
	 * ed ordinate in maniera crescente rispetto alla confidenza}&gt;.
	 */
	private HashMap<FrequentPattern, TreeSet<AssociationRule>> archive = new HashMap<FrequentPattern, TreeSet<AssociationRule>>();

	/**
	 * Inserisce un pattern chiave all'archivio archive, se tale pattern non vi &egrave; gi&agrave; contenuto
	 * @param fp pattern chiave da aggiungere ad archive
	 */
	public void put(FrequentPattern fp) {
		if(!archive.containsKey(fp))
			archive.put(fp, new TreeSet<AssociationRule>());
	}

	/**
	 *Inserisce un pattern chiave fp e la relativa regola d'associazione estratta all'archivio archive, 
	 *se fp non vi &egrave; gi&agrave; contenuto come chiave. Altrimenti inserisce la regola d'associazione al 
	 *TreeSet delle regole indicizzato da fp in archive.
	 * @param fp pattern chiave da aggiungere ad archive
	 * @param rule oggetto AssociationRule da aggungere al Set di regole d'associazione associato ad fp
	 * @throws NoPatternException nessuna regola confidente è generata da un pattern frequente
	 */
	public void put(FrequentPattern fp,AssociationRule rule) throws NoPatternException {			
		if(!archive.containsKey(fp)) 
			archive.put(fp,new TreeSet<AssociationRule>());

		else  { TreeSet<AssociationRule> s = new TreeSet<AssociationRule>();
		s= getRules(fp);
		s.add(rule);
		archive.put(fp,s);
		}
	}

	/**
	 * Se fp compare come chiave in archive restituisce il set di regole d'associazione corrispondente, altrimenti solleva e propaga
	 * una eccezione NoPatternException, passando in input un messaggio apposito.
	 * @param fp pattern chiave per il quale ricercare le regole d'associazione
	 * @return Set di regole d'associazione indicizzato dal relativo pattern all'interno dell'archvio.
	 * @throws NoPatternException nessuna regola confidente è generata da un pattern frequente
	 */
	public TreeSet<AssociationRule> getRules(FrequentPattern fp) throws NoPatternException {
		if(archive.containsKey(fp))
			return archive.get(fp);
		else throw new NoPatternException("Pattern chiave non trovato");
	}

	/**
	 * Serializza l'oggetto riferito dalla parola chiave "this", e pone la rappresentazione serializzata
	 * nel file il cui nome &egrave; passato come parametro.
	 * Pu&ograve; occorrere un'eccezione di tipo IOException, che &egrave; catturata e gestita emettendo un
	 * apposito messaggio.
	 * @param nomefile nome del file in cui salvare la rappresentazone dell'oggetto "this"
	 * @throws FileNotFoundException File non trovato
	 * @throws IOException errori di I/O inerenti lo/gli stream
	 */
	public void salva(String nomefile) throws FileNotFoundException, IOException {		

		try {
			File savefile = new File(nomefile);
			FileOutputStream output = new FileOutputStream(savefile);
			ObjectOutputStream out = new ObjectOutputStream(output);
			out.writeObject(this);
			out.close();
			output.close();
		}
		catch (IOException e) {
			System.err.println("Errore durante il salvataggio del file");
			ConnectionLog.writeLogText("Errore durante il salvataggio del file");
			return;
		}
	}

	/**
	 * Si occupa di leggere e restituire l'archivio di  coppie del tipo &lt;pattern frequenti - {insieme di regole 
	 * d'associazione confidenti}&gt; cos&igrave; come &egrave; memorizzato nel file; il nome di quest'ultimo &egrave;
	 * passato come parametro.
	 * @param nomefile oggetto String contenente il nome del file da cui leggere l'oggetto serializzato
	 * @return Archivio contenente coppie del tipo &lt;pattern frequente - {insieme di regole d'associazione confidenti}&gt;
	 * @throws FileNotFoundException File non trovato
	 * @throws IOException errori di I/O sullo/sugli stream
	 * @throws ClassNotFoundException classe non trovata
	 */
	public static AssociationRuleArchive carica(String nomefile) throws FileNotFoundException, IOException,ClassNotFoundException {
		AssociationRuleArchive readArchive = new AssociationRuleArchive();
		try {
			FileInputStream input = new FileInputStream(nomefile);
			ObjectInputStream in = new ObjectInputStream(input);
			readArchive = (AssociationRuleArchive) in.readObject();
			in.close();
		}
		catch (FileNotFoundException f) {
			System.err.println("File non trovato");
			ConnectionLog.writeLogText("File non trovato");
		}
		catch (ClassNotFoundException c) {
			System.err.println("Classe AssociationRuleArchive non trovata");
			ConnectionLog.writeLogText("Classe AssociationRuleArchive non trovata");
		} 
		return readArchive;
	}

	/**
	 * Restituisce un insieme di coppie del tipo &lt;pattern frequente - {insieme di regole d'associazione confidenti}&gt;.
	 * @return Archivio di &lt;pattern frequente - {insieme di regole d'associazione confidenti}&gt;
	 */
	public HashMap<FrequentPattern, TreeSet<AssociationRule>> getArchive() {				
		return this.archive;
	}

	/**
	 * Si occupa di formattare una stringa di caratteri mostrante il pattern frequente, e subito sotto
	 * l'insieme delle regole d'associazione estratte dallo stesso pattern frequente, che soddisfano 
	 * la minima confidenza richiesta ed inserita dall'utente. Le regole suddette sono ordinate 
	 * in modo crescente rispetto alla loro confidenza.
	 * Ridefinisce il metodo toString() della superclasse Object.
	 * @return Stringa contente una linea di testo mostrante il pattern frequente, e subito sotto
	 *         l'insieme delle regole d'associazione confidenti ad esso associato.
	 */
	public String toString() {

		Iterator <Map.Entry<FrequentPattern, TreeSet<AssociationRule>>> it = archive.entrySet().iterator();
		String value ="";
		int i=1;
		while (it.hasNext()) {
			Map.Entry<FrequentPattern, TreeSet<AssociationRule>> entry = it.next();
			value += i + ":" + entry.getKey() +"\n " + entry.getValue() + "\n";
			i++;
		}
		return value;
	}
}