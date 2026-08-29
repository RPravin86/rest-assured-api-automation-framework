package io.github.rpravin86.api.retry;

import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.regex.Pattern;
import javax.net.ssl.SSLException;

/** Classifies transient transport and upstream HTTP failures. */
public final class InfrastructureFailureClassifier {

    private static final Pattern REST_ASSURED_ACTUAL_STATUS = Pattern.compile(
            "(?is).*but was\\s*<(?:429|502|503|504)>.*");
    private static final Pattern DIRECT_HTTP_STATUS = Pattern.compile(
            "(?is).*(?:HTTP|returned status(?: code)?|response status(?: code)?)"
                    + "\\s*[:=]?\\s*<?(?:429|502|503|504)>?.*");

    private InfrastructureFailureClassifier() {
    }

    public static boolean isRetryable(Throwable failure) {
        Throwable current = failure;

        while (current != null) {
            if (isTransientTransportFailure(current)
                    || hasTransientHttpStatus(current.getMessage())) {
                return true;
            }

            Throwable cause = current.getCause();
            current = cause == current ? null : cause;
        }

        return false;
    }

    private static boolean isTransientTransportFailure(Throwable failure) {
        return failure instanceof SocketTimeoutException
                || failure instanceof ConnectException
                || failure instanceof UnknownHostException
                || failure instanceof NoRouteToHostException
                || failure instanceof SSLException
                || failure instanceof SocketException;
    }

    private static boolean hasTransientHttpStatus(String message) {
        return message != null
                && (REST_ASSURED_ACTUAL_STATUS.matcher(message).matches()
                || DIRECT_HTTP_STATUS.matcher(message).matches());
    }
}
