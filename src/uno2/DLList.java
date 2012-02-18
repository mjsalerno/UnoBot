package uno2;

import java.util.Iterator;
import java.util.LinkedList;


public class DLList<T> implements IList<T>, Iterable<T> {

    private int count;
//  Declare instance fields/variables.
    private Node<T> head;
    private Node<T> at=head;

//  Define default constructor.
    public DLList() {
        this.clear();
    }
    
    public T next(){
        at = at.next;
        return at.data;
    }
    public T prev(){
        at = at.prev;
        return at.data;
    }
    
    public T at(){
        return at.data;
    }
    
    private Node<T> getNodeAt(int index) {
        Node curr = this.head;
        int mid = this.count / 2;
        int fromBack = this.count - index;

        if (mid >= index) {
            for (int i = 0; i < index; i++) {
                curr = curr.next;
            }
        }

        if (mid < index) {
            for (int i = 0; i < fromBack; i++) {
                curr = curr.prev;
            }
        }

        return curr;
    }

//  Define methods declared in interface IList<T>.
    @Override
    public int size() {
        return this.count;
    }

    @Override
    public void clear() {
        this.head = null;
        this.count = 0;
    }

    @Override
    public T get(int index) {
        T result = null;

        if ((0 <= index) && (index < this.count)) {
            result = this.getNodeAt(index).data;
        }

        return result;
    }

    @Override
    public T set(int index, T data) {
        T result = null;

        if ((0 <= index) && (index < this.count)) {
            Node<T> curr = this.getNodeAt(index);

            result = curr.data;
            curr.data = data;
        }

        return result;
    }

    @Override
    public boolean add(T data) {

        if (this.count == 0) {
            Node newNode = new Node(data);
            newNode.next = newNode;
            newNode.prev = newNode;
            this.head = newNode;
            at = this.head;
        } else {
            Node prev = this.head.prev;
            Node newNode = new Node(data, this.head, prev);
            prev.next = newNode;
            head.prev = newNode;
        }

        this.count++;

        return true;
    }

    @Override
    public boolean add(int index, T data) {
        boolean result = false;

        if ((0 <= index) && (index <= this.count)) {
            if (count == 0) {
                Node newNode = new Node(data);

                newNode.next = newNode;
                newNode.prev = newNode;
                this.head = newNode;
            } else {
                if (index == 0) {
                    Node prev = head.prev;
                    Node next = head;
                    Node newNode = new Node(data, next, prev);

                    next.prev = newNode;
                    prev.next = newNode;
                    this.head = newNode;
                } else {
                    Node prev = this.getNodeAt(index - 1);
                    Node next = prev.next;
                    Node newNode = new Node(data, next, prev);

                    prev.next = newNode;
                    next.prev = newNode;
                }
            }

            this.count++;
            result = true;
        }

        return result;
    }

    @Override
    public T remove(int index) {
        T result = null;

        if ((0 <= index) && (index < this.count)) {
            Node<T> curr = getNodeAt(index);

            if (index == 0) {
                //curr = this.head;
                Node last = this.head.prev;
                this.head = curr.next;
                this.head.prev = curr.prev;
                last.next = this.head;
            } else {
                Node prev = curr.prev;
                Node next = curr.next;

                curr = prev.next;
                prev.next = next;
                next.prev = prev;
            }

            this.count--;
            result = curr.data;
        }

        return result;
    }

    @Override
    public int indexOf(T that) {
        int result = -1;
        Node curr = this.head;

        for (int i = 0; (result == -1) && (i < this.count); i++) {
            if (that.equals(curr.data)) {
                result = i;
            }

            curr = curr.next;
        }

        return result;
    }

    @Override
    public boolean contains(T that) {
        return this.indexOf(that) >= 0;
    }

//  Override methods defined in Object.
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node curr = this.head;

        for (int i = 0; i < this.count; i++) {
            if (i > 0) {
                sb.append(", ");
            }

            sb.append(curr.data);
            curr = curr.next;
        }

        sb.append(']');

        return sb.toString();
    }
    
    public LinkedList<T> toLinkedList() {
        LinkedList<T> list = new LinkedList<T>();
        Node<T> node = this.head;
        for (int i = 0; i < this.count; i++) {
           list.add(node.data);  
           node = node.next;
        }
        return list;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object other) {
        boolean result = false;

        if (other != null) {
            if (this.getClass() == other.getClass()) {
                DLList<T> that = (DLList<T>) other;

                if (this.size() == that.size()) {
                    Node thisCurr = this.head;
                    Node thatCurr = that.head;

                    for (int i = 0; i < this.count; i++) {
                        if (!thisCurr.data.equals(thatCurr.data)) {
                            break;
                        }

                        thisCurr = thisCurr.next;
                        thatCurr = thatCurr.next;
                    }

                    if (thisCurr == null) {
                        result = true;
                    }
                }
            }
        }

        return result;
    }

    @Override
    public Iterator<T> iterator() {
        return new DlListIterator<T>(this.head);
    }

    private class DlListIterator<T> implements Iterator{
        Node<T> at;
        
        public DlListIterator(Node head){
            this.at = head;
        }

        @Override
        public boolean hasNext() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Object next() {
            this.at = at.next;
            T data = at.data;
            return at.data;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

//  Define private inner class Node.
    private class Node <T>{

        T data;
        Node<T> next;
        Node<T> prev;

        Node(T data) {
            this(data, null, null);
        }

        Node(T data, Node<T> next, Node<T> prev) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    public static void main(String[] args) {
        DLList<String> list = new DLList<String>();

        System.out.println("TEST ADD WITH INDEX;");
        list.add(0, "one");
        list.add(1, "two");
        list.add(2, "three");
        list.add(3, "four");
        list.add(4, "five");

        System.out.println("");
        System.out.println("TEST GET:");
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i) + " ");
        }

        System.out.println("");
        System.out.println("TEST TOsTRING:");
        System.out.println(list.toString());

        System.out.println("");
        System.out.println("TEST INDEXOF:");
        System.out.println("indexOf(three) " + list.indexOf("three"));
        System.out.println("indexOf(one) " + list.indexOf("one"));
        System.out.println("indexOf(five) " + list.indexOf("five"));
        System.out.println("indexOf(99) " + list.indexOf("99"));

        System.out.println("");
        System.out.println("TEST SET:");
        System.out.println("set(0,1) " + list.set(0, "1"));
        System.out.println(list.toString());
        System.out.println("set(1,2) " + list.set(1, "2"));
        System.out.println(list.toString());
        System.out.println("set(2,3) " + list.set(2, "3"));
        System.out.println(list.toString());
        System.out.println("set(3,4) " + list.set(3, "4"));
        System.out.println(list.toString());
        System.out.println("set(4,5) " + list.set(4, "5"));
        System.out.println(list.toString());
        System.out.println("set(99,99) " + list.set(99, "99"));
        System.out.println(list.toString());

        System.out.println("");
        System.out.println("TEST SIZE/CLEAR:");
        System.out.println("size() " + list.size());
        list.clear();
        System.out.println("clear " + list.toString());

        System.out.println("");
        System.out.println("TEST ADD AT END");
        list.add("one");
        list.add("two");
        list.add("three");
        list.add("four");
        list.add("five");

        System.out.println("");
        System.out.println("TEST REMOVE:");
        System.out.println(list.toString());
        System.out.println("remove(0) " + list.remove(0));
        System.out.println(list.toString());
        System.out.println("remove(1) " + list.remove(1));
        System.out.println(list.toString());
        System.out.println("remove(2) " + list.remove(2));
        System.out.println(list.toString());
        System.out.println("remove(3) " + list.remove(3));
        System.out.println(list.toString());



    }
}


