package model.cua;

import java.util.Arrays;
import model.NodeHuffman;
import java.util.PriorityQueue;

public class CuaBinaryHeap implements CuaPrioritat {
     // Array representation of the heap
    private NodeHuffman[] heap;

      // Number of elements currently in the heap
    private int size;

      // Maximum number of elements the heap can hold
    private int capacity;

    // Constructor to initialize the heap with a given capacity
    public CuaBinaryHeap(int c) {
        capacity = c;
        heap = new NodeHuffman[capacity];
        size = 0;
    }


    @Override
    public void afegir(NodeHuffman node) {
        if (size >= capacity) {
              // Resize the heap if capacity is reached
            resize();
        }

          // Place the new value at the end of the heap
        heap[size] = node;

          // Index of the newly added element
        int current = size;

          // Increase the size of the heap
        size++;

        // Restore the heap property by "heapifying up"
        while (current > 0 && heap[current].getFrequencia() < heap[parent(current)].getFrequencia()) {
              // Swap with the parent if smaller
            swap(current, parent(current));

              // Move up to the parent's position
            current = parent(current);
        }
    }

    @Override
    public NodeHuffman extreure() {
        if (size == 0) {
            // Check if the heap is empty
            throw new RuntimeException("Heap is empty!");
        }

        // The root of the heap is the minimum element
        NodeHuffman min = heap[0];

        // Replace the root with the last element in the heap
        heap[0] = heap[size - 1];
        size--;

          // Restore the heap property by "heapifying down" from the root
        heapifyDown(0);

          // Return the extracted minimum element
        return min;
    }

    @Override
    public int mida() {
        return size;
    }

    @Override
    public boolean esBuida() {
        return size == 0;
    }

        // Method to get the index of the
      // parent of a given node
    private int parent(int i) {
        return (i - 1) / 2;
    }

    // Method to get the index of the
      // left child of a given node
    private int leftChild(int i) {
        return 2 * i + 1;
    }

    // Method to get the index of the
      // right child of a given node
    private int rightChild(int i) {
        return 2 * i + 2;
    }

    // Method to swap two elements in the heap array
    private void swap(int i, int j) {
        NodeHuffman temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    // Method to resize the heap array when capacity is reached
    private void resize() {
        capacity *= 2;
        heap = Arrays.copyOf(heap, capacity);
    }

        // Method to restore the heap property by
       // "heapifying down" from a given index
    private void heapifyDown(int i) {
           // Assume the current index is the smallest
        int smallest = i;

          // Get the left child index
        int left = leftChild(i);

          // Get the right child index
        int right = rightChild(i);

        // If the left child is smaller than the current
          // smallest element, update smallest
        if (left < size && heap[left].getFrequencia() < heap[smallest].getFrequencia()) {
            smallest = left;
        }

        // If the right child is smaller than the current
          // smallest element, update smallest
        if (right < size && heap[right].getFrequencia() < heap[smallest].getFrequencia()) {
            smallest = right;
        }

        // If the smallest element is not the current
          // element, swap and continue heapifying down
        if (smallest != i) {
            swap(i, smallest);
            heapifyDown(smallest);
        }
    }
}
