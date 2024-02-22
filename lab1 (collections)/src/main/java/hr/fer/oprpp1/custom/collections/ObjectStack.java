package hr.fer.oprpp1.custom.collections;


/**
 * Adapter class that adapts the ArrayIndexedCollection to a stack.
 *
 * @Author Danijel Barišić
 */
public class ObjectStack {

    /**
     * Internal data structure the stack uses for element storage.
     */
    private ArrayIndexedCollection elements;

    /**
     * Allocates initial space for the internal data structure.
     */
    public ObjectStack() {
        elements = new ArrayIndexedCollection();
    }

    /**<
     * Checks if the stack has no elements.
     *
     * @return true if number of elements in the stack is 0, false otherwise
     */
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    /**
     * @return number of elements in the stack
     */
    public int size() {
        return elements.size();
    }

    /**
     * Pushes given value on the stack.
     *
     * @param value
     * @throws NullPointerException if the provided value is null
     */
    public void push(Object value) {

        if (value == null) {
            throw new NullPointerException("New value cannot be null");
        }

        elements.add(value);
    }

    /**
     * Removes last value pushed on stack from stack and returns it.
     *
     * @return the last pushed value in stack (i.e. the top of the stack)
     * @throws EmptyStackException when trying to pop elements from empty stack
     */
    public Object pop() {

        if (isEmpty()) {
            throw new EmptyStackException("Cannot pop elements from empty stack");
        }

        Object topObject = elements.get(size() - 1);
        elements.remove(size() - 1);

        return topObject;
    }

    /**
     * Returns last element placed on stack but does not delete it from stack.
     *
     * @return last element placed on stack
     * @throws EmptyStackException when trying to peek at elements from empty stack
     */
    public Object peek() {

        if (isEmpty()) {
            throw new EmptyStackException("Cannot peek at elements from empty stack");
        }

        Object topObject = elements.get(size() - 1);
        return topObject;
    }

    /**
     * Removes all elements from stack.
     */
    public void clear() {
        elements.clear();
    }

}
