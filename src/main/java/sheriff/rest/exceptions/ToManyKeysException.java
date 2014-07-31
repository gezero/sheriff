package sheriff.rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Jiri on 9. 7. 2014.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "To many keys provided.")
public class ToManyKeysException extends RuntimeException {
}
