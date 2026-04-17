package by.alexeysavchic.beer_pet_project.exception;

public final class ErrorMessages {
    public final static String expiredToken = "duration of action JWT token expired";

    public final static String unsupportedToken = "The received token does not match the correct format";

    public final static String malformedToken = "The received data does not match the correct token format";

    public final static String securityException = "You have not permit to access this resource";

    public final static String invalidToken = "Invalid token";

    public final static String userNotFound = "User with id=%d not found";

    public final static String wrongPassword = "User with email %s have entered invalid password";

    public final static String absentRefreshToken = "Refresh cookie is absent";

    public final static String wrongTokenType = "Wrong token type. Type %s received but %s type expected";

    public final static String usernameAlreadyExists = "User with username %s already exists";

    public final static String emailAlreadyExists = "User with email %s already exists";

    public final static String unknownBeerBrandException = "Unknown beer brand: %s before adding beer you must add beer brand";

    public final static String beerNotFound = "Cannot find beer with received characteristics";

    public final static String beerBrandNotFound = "Cannot find beer brand with received characteristics";

    public final static String beerIsAbsentInWarehouse = "Beer with ids: %s is absent in require quantity";

    public final static String transactionDidNotPass = "Beers with ids: %s is absent in require quantity";

    public final static String beersNotFoundException = "Beers with ids: %s doesn't exist";

    public final static String updateWarehouseXmlException = "Beers is absent in require quantity in warehouse";
}
