package model.cua;

import model.NodeHuffman;
import java.util.*;

public class CuaFibonacciHeap implements CuaPrioritat {

    private FibonacciNode minNode;
    private int nodeCount;

    public CuaFibonacciHeap() {
        minNode = null;
        nodeCount = 0;
    }

    @Override
    public void afegir(NodeHuffman node) {
        FibonacciNode nouNode = new FibonacciNode(node);

        if (minNode != null) {
            addToRootList(nouNode);
            if (node.getFrequencia() < minNode.getFrequencia()) {
                minNode = nouNode;
            }
        } else {
            minNode = nouNode;
        }
        nodeCount++;
    }

    @Override
    public NodeHuffman extreure() {


        if (minNode == null) return null;

        FibonacciNode min = minNode;

        if (min.getFill() != null) {
            // Add the children of the minNode to the root list
            addChildrenToRootList(min);
        }

        // Remove the minNode from the root list
        removeNodeFromRootList(min);
        if (min == min.getDreta()) {
            // Set minNode to null if it was the only node in the root list
            minNode = null;
        }
            else {
            minNode = min.getDreta();
            // Consolidate the trees in the root list
            consolidate();
        }
        nodeCount--;

        return min.getValor();
    }

    private void addChildrenToRootList(FibonacciNode min) {
        FibonacciNode child = min.getFill();
        do {
            FibonacciNode nextChild = child.getDreta();
            child.setEsquerra(minNode);
            child.setDreta(minNode.getDreta());
            minNode.getDreta().setEsquerra(child);
            minNode.setDreta(child);
            child.setPare(null);
            child = nextChild;
        } while (child != min.getFill());
    }

    private void removeNodeFromRootList(FibonacciNode node) {
        node.getEsquerra().setDreta(node.getDreta());
        node.getDreta().setEsquerra(node.getEsquerra());
    }

        // Consolidate the trees in the root list
    private void consolidate() {
        int arraySize = ((int) Math.floor(Math.log(nodeCount) / Math.log(2.0))) + 1;
        List<FibonacciNode> array = new ArrayList<>(Collections.nCopies(arraySize, null));
        List<FibonacciNode> rootList = getRootList();

        for (FibonacciNode node : rootList) {
            int degree = node.getGrau();
            while (array.get(degree) != null) {
                FibonacciNode other = array.get(degree);
                if (node.getFrequencia() > other.getFrequencia()) {
                    FibonacciNode temp = node;
                    node = other;
                    other = temp;
                }

              	// Link two trees of the same degree
                link(other, node);
                array.set(degree, null);
                degree++;
            }
            array.set(degree, node);
        }

        minNode = null;
        for (FibonacciNode node : array) {
            if (node != null) {
                if (minNode == null) {
                    minNode = node;
                } else {

                    // Add the node back to the root list
                    addToRootList(node);
                    if (node.getFrequencia() < minNode.getFrequencia()) {
                        minNode = node;
                    }
                }
            }
        }
    }

    private List<FibonacciNode> getRootList() {
        List<FibonacciNode> root = new ArrayList<>();
        if (minNode != null) {
            FibonacciNode actual = minNode;
            do {
                root.add(actual);
                actual = actual.getDreta();
            } while (actual != minNode);
        }
        return root;
    }

    private void addToRootList(FibonacciNode node) {
        node.setEsquerra(minNode);
        node.setDreta(minNode.getDreta());

        node.getDreta().setEsquerra(node);
        minNode.setDreta(node);
    }

        // Link two trees of the same degree
    private void link(FibonacciNode y, FibonacciNode x) {
      	// Remove y from the root list
        removeNodeFromRootList(y);
        y.setEsquerra(y);
        y.setDreta(y);
        y.setPare(x);

        if (x.getFill() == null) {
            // Make y a child of x
            x.setFill(y);
        } else {
            y.setDreta(x.getFill());
            y.setEsquerra(x.getFill().getEsquerra());
            x.getFill().getEsquerra().setDreta(y);
            x.getFill().setEsquerra(y);
        }

        x.setGrau(x.getGrau() + 1);
        y.setMarca(false);
    }


    @Override
    public int mida() {
        return nodeCount;
    }

    @Override
    public boolean esBuida() {
        return nodeCount == 0;
    }
}
