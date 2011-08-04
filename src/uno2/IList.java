package uno2;

public interface IList<T>
{
	int size();
	void clear();

	T get(int index);
	T set(int index, T data);

	boolean add(T data);
	boolean add(int index, T data);
	T remove(int index);

	boolean contains(T that);
	int indexOf(T that);
}
