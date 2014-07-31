package sheriff.rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Jiri on 14. 7. 2014.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Address was not found")
public class AddressNotFoundException extends RuntimeException {
}
