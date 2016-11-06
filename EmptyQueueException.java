public class EmptyQueueException extends RuntimeException 
{

    public EmptyQueueException() 
    {
        this(null);
    }

    public EmptyQueueException(String message) 
    {
        super(message);
    }

}
