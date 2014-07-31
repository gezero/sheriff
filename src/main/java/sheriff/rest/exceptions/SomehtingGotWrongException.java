package sheriff.rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Jiri on 9. 7. 2014.
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class SomehtingGotWrongException extends RuntimeException {
}
