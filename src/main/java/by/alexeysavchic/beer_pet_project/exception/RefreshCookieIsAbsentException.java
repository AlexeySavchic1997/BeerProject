package by.alexeysavchic.beer_pet_project.exception;

public class RefreshCookieIsAbsentException extends RuntimeException
{
    public RefreshCookieIsAbsentException() {
        super(ErrorMessages.absentRefreshCookie);
    }
}
