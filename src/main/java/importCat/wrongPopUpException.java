package importCat;

public class wrongPopUpException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public wrongPopUpException(String message) {
		super("Il testo dell'allert non corrisponde a quello atteso: "+message);
		// TODO Auto-generated constructor stub
	}
	
}
