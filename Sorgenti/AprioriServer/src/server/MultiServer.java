
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import utility.ConnectionLog;

/**
 * Modella un server in grado di accettare la richiesta trasmessa da un generico Client e istanzia un oggetto 
 * della classe ServerOneClient che si occuper&agrave; di servire le richieste del Client stesso, affidando 
 * l'esecuzione di queste ultime ad un thread dedicato. Il server sar&agrave; registrato su una porta predefinita
 * ( al di fuori del range 1-1024, poich&egrave; associate a servizi web predefiniti e noti ), per esempio 8080. 
 * @see <a href="https://www.iana.org/assignments/service-names-port-numbers/service-names-port-numbers.xhtml">
 *       https://www.iana.org/assignments/service-names-port-numbers/service-names-port-numbers.xhtml</a>
 *@author Gruppo Map formato da Donato Lucente, Antonio Ricchetti, Gianluca Colaianni
 */
public class MultiServer extends Thread{

	/**
	 * Attributo di classe indicante il numero di porta a cui il server &egrave; registrato. L'attributo non &egrave; modificabile
	 * dopo essere stato inizializzato.
	 */
	private static final int PORT = 8080;

	/**
	 * Costruttore; si occupa di invocare il metodo run per avviare il thread principale su cui il server &egrave; eseguito.
	 */
	public MultiServer() {
		run();
	}

	/**
	 * Assegna ad una variabile locale il riferimento ad una istanza della classe ServerSocket creata usando la porta PORT.
	 * L'istanza suddetta si pone in attesa di richieste di connessione da parte di client, in risposta alle quali viene restituito
	 * l'oggetto Socket da passare come argomento al costruttore della classe ServeOneClient. Inoltre, attraverso l'invocazione del metodo statico 
	 * ConnectionLog.writeLogText() del package utility, invia i messaggi relativi allo stato di esecuzione della richiesta client
	 * in un file testuale apposito.
	 * Ridefinisce il metodo run() della superclasse Thread della classe corrente.
	 * Possono occorrere le seguenti eccezioni:
	 * 
	 * 1) Se la creazione del socket lato server mediante il costruttore java.net.ServerSocket.ServerSocket() fallisce, un'eccezione
	 *    di tipo IOException &egrave; catturata e gestita emettendo un messaggio d'errore indicante l'impossibilit&agrave; di attendere
	 *    richieste client sulla socket server registrata al numero di porta PORT;
	 *    
	 * 2) In caso di fallimento della connessione del client alla socket ad esso dedicata, un'eccezione di tipo IOException &egrave; catturata
	 *    e gestita emettendo un messaggio d'errore indicante lo stato di fallimento della connessione, l'indirizzo IP e il numero di porta
	 *    che identificano il server.
	 *    
	 * 3) Nel caso in cui il thread a cui &egrave; affidata l'esecuzione della richiesta del client non dovesse riprendere la sua esecuzione
	 *    a seguito della messa in attesa, un'eccezione di tipo InterruptedException &egrave; catturata e gestita emettendo un messaggio
	 *    d'errore;
	 *    
	 */
	public void run() {
		ConnectionLog conlog = new ConnectionLog("ServerLog");

		try {
			ServerSocket s = new ServerSocket(PORT);
			System.out.println("Server started");
			ConnectionLog.writeLogText("Server started");

			try {
				while(true) {
					System.out.println("In attesa di connessione...");
					Socket socket = s.accept();
					ConnectionLog.writeLogText("In attesa di connessione...");

					try {
						ConnectionLog.writeLogText("Connessione accettata all'indirizzo " + socket.getInetAddress() + " su numero di porta " + socket.getLocalPort());
						System.out.println("Connessione accettata all'indirizzo " + socket.getInetAddress() + " su numero di porta " + socket.getLocalPort());
						new ServerOneClient(socket);

						try {
							sleep(500);
						} 
						catch(InterruptedException e) {
							System.err.println("Interrupted");
							ConnectionLog.writeLogText("Interrupted");
						} 
					} 
					catch (IOException e) {	
						System.err.println("Connessione fallita all'indirizzo " + socket.getInetAddress() + " su numero di porta " + socket.getLocalPort());
						ConnectionLog.writeLogText("Connessione fallita all'indirizzo " + socket.getInetAddress() + " su numero di porta " + socket.getLocalPort());
						socket.close();
					}
				}
			} 
			finally {
				System.out.println("Server Chiuso...");
				ConnectionLog.writeLogText("Server Chiuso...");
				s.close();
				ConnectionLog.logClosing();
			}
		} 
		catch (IOException e) {
			System.err.println("Errore: impossibile attendere richieste client sulla porta " + PORT);
			ConnectionLog.writeLogText("Errore: impossibile attendere richieste client sulla porta " + PORT);
		}
		System.out.println("Server Chiuso");
		ConnectionLog.writeLogText("Server Chiuso");
	}

	/**
	 * Funzione main della classe corrente; si occupa di creare un nuovo oggetto Multiserver.
	 * @param args argomenti inseriti dalla linea di comando della console
	 */
	public static void main(String[] args) {
		new MultiServer();
	}
}
