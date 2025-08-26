package app.exceptions;

/**
 * Exception personnalisée pour les cas où une ressource (ville, département, etc.)
 * n'est pas trouvée ou invalide.
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}