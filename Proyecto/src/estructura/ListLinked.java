package estructura;

public class ListLinked<T> {
    private Node<T> head;

    public ListLinked() {
        this.head = null;
    }

    public void addLast(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
            return;
        }
        Node<T> temp = head;
        while (temp.next != null) temp = temp.next;
        temp.next = newNode;
    }

    public void addFirst(T data) {
        Node<T> newNode = new Node<>(data);
        newNode.next = head;
        head = newNode;
    }

    public boolean contains(T data) {
        Node<T> temp = head;
        while (temp != null) {
            if (temp.data.equals(data)) return true;
            temp = temp.next;
        }
        return false;
    }

    public void remove(T data) {
        if (head == null) return;
        if (head.data.equals(data)) {
            head = head.next;
            return;
        }
        Node<T> prev = head;
        Node<T> curr = head.next;
        while (curr != null) {
            if (curr.data.equals(data)) {
                prev.next = curr.next;
                return;
            }
            prev = curr;
            curr = curr.next;
        }
    }

    public Node<T> getHead() {
        return head;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public void printList() {
        Node<T> temp = head;
        while (temp != null) {
            System.out.print(temp.data + " ");
            temp = temp.next;
        }
        System.out.println();
    }
    
    public int size() {
        int count = 0;
        Node<T> current = head;
        while (current != null) {
            count++;
            current = current.next;
        }
        return count;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node<T> current = head;
        while (current != null) {
            sb.append(current.data.toString());
            if (current.next != null) {
                sb.append(" -> ");
            }
            current = current.next;
        }
        return sb.toString();
    }
}