package model.cua;

import model.NodeHuffman;
import java.util.PriorityQueue;

public class CuaBinaryHeap implements CuaPrioritat {
    private final PriorityQueue<NodeHuffman> heap = new PriorityQueue<>();

    @Override
    public void afegir(NodeHuffman node) {
        heap.add(node);
    }

    @Override
    public NodeHuffman extreure() {
        return heap.poll();
    }

    @Override
    public int mida() {
        return heap.size();
    }

    @Override
    public boolean esBuida() {
        return heap.isEmpty();
    }
}
