package net.bitcoinguard.sheriff.rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Jiri on 11. 7. 2014.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST,reason = "No keys provided in request")
public class NoKeysProvidedException extends RuntimeException {
}
