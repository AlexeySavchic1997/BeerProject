package by.alexeysavchic.beer_pet_project.exception;

public class UpdatingWarehouseXmlError extends RuntimeException
{
    public UpdatingWarehouseXmlError() {
        super(ErrorMessages.updateWarehouseXmlException);
    }
}
