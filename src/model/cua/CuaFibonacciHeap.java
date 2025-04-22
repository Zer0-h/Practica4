package model.cua;

import java.util.*;
import model.huffman.NodeHuffman;

/**
 * Implementació d'una cua de prioritat mitjançant un Heap de Fibonacci mínim.
 * Aquesta estructura és eficient per a operacions repetides d'inserció i
 * extreure mínim.
 *
 * Aquesta implementació té un cost:
 * - afegir: O(1) amortitzat
 * - extreure (mínim): O(log n) amortitzat
 * - mida, esBuida: O(1)
 *
 * @author tonitorres
 */
public class CuaFibonacciHeap implements CuaPrioritat {

    private FibonacciNode minim;       // Referència al node amb la freqüència mínima
    private int nombreNodes;           // Nombre total de nodes a la cua

    /**
     * Crea una nova cua de tipus Heap de Fibonacci buida.
     */
    public CuaFibonacciHeap() {
        minim = null;
        nombreNodes = 0;
    }

    /**
     * Afegeix un nou node Huffman a la cua.
     *
     * @param node node a afegir
     */
    @Override
    public void afegir(NodeHuffman node) {
        FibonacciNode nou = new FibonacciNode(node);
        if (minim != null) {
            afegirALlistaArrel(nou);
            if (node.getFrequencia() < minim.getFrequencia()) {
                minim = nou;
            }
        } else {
            minim = nou;
        }
        nombreNodes++;
    }

    /**
     * Extreu el node amb la freqüència mínima i reorganitza el heap.
     *
     * @return node Huffman mínim
     */
    @Override
    public NodeHuffman extreure() {
        if (minim == null) {
            return null;
        }

        FibonacciNode minActual = minim;

        // Afegim els fills del mínim a la llista d’arrels
        if (minActual.getFill() != null) {
            FibonacciNode fill = minActual.getFill();
            List<FibonacciNode> fills = new ArrayList<>();
            FibonacciNode actual = fill;
            do {
                fills.add(actual);
                actual = actual.getDreta();
            } while (actual != fill);

            for (FibonacciNode f : fills) {
                f.setPare(null);
                afegirALlistaArrel(f);
            }
        }

        eliminarDeLlistaArrel(minActual);

        if (minActual == minActual.getDreta()) {
            minim = null;
        } else {
            minim = minActual.getDreta();
            consolidar();
        }

        nombreNodes--;
        return minActual.getValor();
    }

    /**
     * Afegeix un node a la llista d’arrels del Heap.
     */
    private void afegirALlistaArrel(FibonacciNode node) {
        node.setEsquerra(minim);
        node.setDreta(minim.getDreta());
        minim.getDreta().setEsquerra(node);
        minim.setDreta(node);
    }

    /**
     * Elimina un node de la llista d’arrels.
     */
    private void eliminarDeLlistaArrel(FibonacciNode node) {
        node.getEsquerra().setDreta(node.getDreta());
        node.getDreta().setEsquerra(node.getEsquerra());
    }

    /**
     * Consolida la llista d’arrels agrupant arbres amb el mateix grau.
     */
    private void consolidar() {
        int midaArray = ((int) Math.floor(Math.log(nombreNodes) / Math.log(2))) + 1;
        List<FibonacciNode> aux = new ArrayList<>(Collections.nCopies(midaArray, null));
        List<FibonacciNode> llistaArrel = obtenirLlistaArrel();

        for (FibonacciNode node : llistaArrel) {
            int grau = node.getGrau();
            while (grau >= aux.size()) {
                aux.add(null);
            }

            while (aux.get(grau) != null) {
                FibonacciNode altre = aux.get(grau);
                if (node.getFrequencia() > altre.getFrequencia()) {
                    FibonacciNode temp = node;
                    node = altre;
                    altre = temp;
                }
                enllaçar(altre, node);
                aux.set(grau, null);
                grau++;
                while (grau >= aux.size()) {
                    aux.add(null);
                }
            }

            aux.set(grau, node);
        }

        // Recalcular el nou mínim
        minim = null;
        for (FibonacciNode node : aux) {
            if (node != null) {
                if (minim == null) {
                    node.setEsquerra(node);
                    node.setDreta(node);
                    minim = node;
                } else {
                    afegirALlistaArrel(node);
                    if (node.getFrequencia() < minim.getFrequencia()) {
                        minim = node;
                    }
                }
            }
        }
    }

    /**
     * Obté tots els nodes de la llista d’arrels.
     */
    private List<FibonacciNode> obtenirLlistaArrel() {
        List<FibonacciNode> llista = new ArrayList<>();
        if (minim != null) {
            FibonacciNode actual = minim;
            do {
                llista.add(actual);
                actual = actual.getDreta();
            } while (actual != minim);
        }
        return llista;
    }

    /**
     * Enllaça dos arbres del mateix grau, fent que y sigui fill de x.
     */
    private void enllaçar(FibonacciNode y, FibonacciNode x) {
        eliminarDeLlistaArrel(y);
        y.setPare(x);

        if (x.getFill() == null) {
            y.setDreta(y);
            y.setEsquerra(y);
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

    /**
     * Retorna la mida actual de la cua.
     */
    @Override
    public int mida() {
        return nombreNodes;
    }

    /**
     * Indica si la cua és buida.
     */
    @Override
    public boolean esBuida() {
        return nombreNodes == 0;
    }
}
